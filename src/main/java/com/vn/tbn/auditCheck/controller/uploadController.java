package com.vn.tbn.auditCheck.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.ibm.watson.discovery.v1.model.CreateCollectionOptions;
import com.ibm.watson.discovery.v1.model.DeleteCollectionOptions;
import com.ibm.watson.discovery.v1.model.DeleteCollectionResponse;
import com.vn.tbn.auditCheck.dto.UploadDTO;
import com.vn.tbn.auditCheck.model.BaseData;
import com.vn.tbn.auditCheck.model.Collection;
import com.vn.tbn.auditCheck.model.CollectionData;
import com.vn.tbn.auditCheck.model.Corpus;
import com.vn.tbn.auditCheck.model.FeedbackCollectionData;
import com.vn.tbn.auditCheck.model.RelevanceCollectionData;
import com.vn.tbn.auditCheck.repository.CollectionDataRepository;
import com.vn.tbn.auditCheck.repository.CollectionRepository;
import com.vn.tbn.auditCheck.repository.CorpusRepository;
import com.vn.tbn.auditCheck.repository.FeedbackDataRepository;
import com.vn.tbn.auditCheck.service.CollectionDataService;
import com.vn.tbn.auditCheck.service.CorpusService;
import com.vn.tbn.auditCheck.service.DiscoveryService;
import com.vn.tbn.auditCheck.service.RelevanceCollectionDataService;

@RestController
@RequestMapping("study")
public class uploadController {
	private static final int ArrayList = 0;

	@Autowired
	CorpusService corpusService;
	
	@Autowired
	CorpusRepository corpusRepository;
	
	@Autowired
	CollectionRepository collectionRepository;
	
	@Autowired
	DiscoveryService discoveryService;
	
	@Autowired
	CollectionDataRepository collectionDataRepository;
	
	@Autowired
	FeedbackDataRepository feedbackDataRepository;
	
	@Autowired
	CollectionDataService collectionDataService;
	
	@Autowired
	RelevanceCollectionDataService relevanceCollectionDataService;
	
	Gson gson;
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index(ModelAndView modelAndView, HttpServletRequest request) {
		List<Corpus> listCorpus = corpusService.getAllCorpus();
		modelAndView.addObject("listCorpus", listCorpus);
		return modelAndView;
	}
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ResponseEntity<?> uploadData(@RequestBody UploadDTO upload, HttpServletRequest request) {
		Corpus corpus = corpusRepository.findByCorpusId(upload.getCorpusId());
		Collection oldCollection = collectionRepository.findByCorpusId(upload.getCorpusId());
		
		String environmentId = corpus.getEnviromentId();
		
		if (oldCollection != null) {
			String collectionId = oldCollection.getCollectionId();
			DeleteCollectionOptions deleteRequest = new DeleteCollectionOptions.
					Builder(environmentId, collectionId).build();
			DeleteCollectionResponse deleteResponse = discoveryService.getDiscovery().
					deleteCollection(deleteRequest).execute().getResult();
			
			System.out.println("====> deleteResponse : " + deleteResponse);
			
			collectionRepository.delete(oldCollection);
		}
		
		String language = corpus.getLanguageCode();
		String collectionName = corpus.getCorpusId() + "_" + System.currentTimeMillis();
		
		CreateCollectionOptions.Builder createCollectionBuilder = new CreateCollectionOptions.
				Builder(environmentId, collectionName).language(language);
		com.ibm.watson.discovery.v1.model.Collection createResponse = discoveryService.getDiscovery().
				createCollection(createCollectionBuilder.build()).execute().getResult();
		
		System.out.println("=========> Collection_Id " + createResponse.getCollectionId());
					
		Collection newCollection = new Collection();
		newCollection.setCollectionId(createResponse.getCollectionId());
		newCollection.setCorpusId(corpus.getCorpusId());
		newCollection.setStatus(1);
		
		collectionRepository.save(newCollection);
		
		List<CollectionData> listCollectionData = collectionDataRepository.findAll();
		
		discoveryService.addMultipeDocument(environmentId, createResponse.getCollectionId(), listCollectionData);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> trainingData(@RequestBody UploadDTO upload, HttpServletRequest request) {
		int corpusId = upload.getCorpusId();
		Corpus corpus = corpusRepository.findByCorpusId(corpusId);
		Collection collection = collectionRepository.findByCorpusId(corpusId);
		
		String environmentId = corpus.getEnviromentId();
		String collectionId = collection.getCollectionId();
		
		boolean trainingStatus = discoveryService.isTrainingAtCollection(environmentId, collectionId);
		
		if (!trainingStatus) {
			discoveryService.deleteAllTrainingData(environmentId, collectionId);
			
			List<FeedbackCollectionData> feedbackDatas = feedbackDataRepository.findByCorpusId(corpusId);
			
			List<CollectionData> collectionDatas = collectionDataRepository.findByCorpusId(corpusId);
			
			List<BaseData> data = new ArrayList<BaseData>();
			
			if(null != feedbackDatas && feedbackDatas.size() > 0) {
				data.addAll(feedbackDatas);
			}
			
			if(null != collectionDatas && collectionDatas.size() > 0) {
				data.addAll(collectionDatas);
			}
			
			List<String> result = new ArrayList<>();
			for (BaseData item : data) {
				String naturalLanguageQuery = collectionDataService.getQueryFromBaseData(item);
				if (naturalLanguageQuery != null && naturalLanguageQuery.length() > 0) {
					result.add(naturalLanguageQuery);
				}
			}
			System.out.println("====> Start Adding Querys");
			HashMap<String, String> lanQueIds = discoveryService.addQueryToTrainingData(result,
					environmentId, collectionId);
			System.out.println("====> End Adding Querys");
			System.out.println("====> lanQueIds " + lanQueIds);
			
			List<HashMap<String, RelevanceCollectionData>> documentIds = new ArrayList<HashMap<String,RelevanceCollectionData>>();
			
			for (FeedbackCollectionData item : feedbackDatas) {
				int feedbackId = item.getFeedback_Id();
				String naturalLanguageQuery = collectionDataService.getQueryFromBaseData(item);
				
				List<RelevanceCollectionData> listRelevances = relevanceCollectionDataService.
						findAllByFeedbackId(feedbackId);
				
				for (RelevanceCollectionData relevance : listRelevances) {
					HashMap<String, RelevanceCollectionData> e = new HashMap<String, RelevanceCollectionData>();
					e.put(naturalLanguageQuery, relevance);
					documentIds.add(e);
				}
			}
			
			System.out.println("====> documentIds " + documentIds);
			
			discoveryService.addCollectionDataToTrainingData(collectionDatas, lanQueIds, environmentId, collectionId);
			
			discoveryService.addFeedbackDataToTrainingData(documentIds, lanQueIds, environmentId, collectionId);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
