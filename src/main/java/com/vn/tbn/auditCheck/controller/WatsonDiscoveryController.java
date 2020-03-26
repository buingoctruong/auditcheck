package com.vn.tbn.auditCheck.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ibm.cloud.sdk.core.http.Response;
import com.ibm.watson.discovery.v1.model.DeleteCollectionResponse;
import com.vn.tbn.auditCheck.dto.QueryDTO;
import com.vn.tbn.auditCheck.dto.WatsonCollectionDTO;
import com.vn.tbn.auditCheck.model.BaseData;
import com.vn.tbn.auditCheck.model.Collection;
import com.vn.tbn.auditCheck.model.CollectionData;
import com.vn.tbn.auditCheck.model.Corpus;
import com.vn.tbn.auditCheck.model.FeedbackQuestion;
import com.vn.tbn.auditCheck.model.FeedbackRelevance;
import com.vn.tbn.auditCheck.repository.CollectionDataRepository;
import com.vn.tbn.auditCheck.repository.CollectionRepository;
import com.vn.tbn.auditCheck.repository.CorpusRepository;
import com.vn.tbn.auditCheck.repository.FeedbackRepository;
import com.vn.tbn.auditCheck.service.CorpusService;
import com.vn.tbn.auditCheck.service.DiscoveryService;
import com.vn.tbn.auditCheck.service.RelevanceService;
import com.vn.tbn.auditCheck.service.RetrievalService;

@RestController
@RequestMapping("watson")
public class WatsonDiscoveryController {
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
	FeedbackRepository feedbackDataRepository;
	
	@Autowired
	RetrievalService retrievalService;
	
	@Autowired
	RelevanceService relevanceService;
	
	Logger log = LoggerFactory.getLogger(WatsonDiscoveryController.class);
		
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index(ModelAndView modelAndView, HttpServletRequest request) {
		List<Corpus> listCorpus = corpusService.getAllCorpus();
		modelAndView.addObject("listCorpus", listCorpus);
		modelAndView.setViewName("watson");
		return modelAndView;
	}
	
	@RequestMapping(value = "/collection/data", method = RequestMethod.POST)
	public ResponseEntity<?> uploadData(@RequestBody QueryDTO upload, HttpServletRequest request) {
		Corpus corpus = corpusRepository.findByCorpusId(upload.getCorpusId());
		Collection oldCollection = collectionRepository.findByCorpusId(upload.getCorpusId());
		
		String environmentId = corpus.getEnviromentId();
		
		// delete current collections on watson and in table
		if (oldCollection != null) {
			String collectionId = oldCollection.getCollectionId();
			
			// delete current collection on watson discovery
			Response<DeleteCollectionResponse> deleteResponse = discoveryService.
					DeleteCollection(environmentId, collectionId);
			
			// check deletion is successful or not
			if (deleteResponse.getStatusCode() != 200) {
				log.info("status code of deletion : " + deleteResponse.getStatusCode());
				log.info("status message of deletion : " + deleteResponse.getStatusMessage());
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				DeleteCollectionResponse deleteResult = deleteResponse.getResult();
				log.info("result of deletion : " + deleteResult);
				
				// delete current collection in m_collection table
				collectionRepository.delete(oldCollection);
			}
		}
		
		String language = corpus.getLanguageCode();
		
		long currentDateTime = System.currentTimeMillis();
		Date currentDate = new Date(currentDateTime);
	    DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String collectionName = corpus.getCorpusId() + "_" + df.format(currentDate);
		
		// create collection on watson
		Response<com.ibm.watson.discovery.v1.model.Collection> createResponse = discoveryService.
				CreateCollection(environmentId, collectionName, language);
		
		// check creation is successful or not
		if (createResponse.getStatusCode() != 201) {
			log.info("status code of creation : " + createResponse.getStatusCode());
			log.info("status message of creattion : " + createResponse.getStatusMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			com.ibm.watson.discovery.v1.model.Collection createResult = createResponse.getResult();
			log.info("result of creation : " + createResult);
			
			Collection newCollection = new Collection();
			newCollection.setCollectionId(createResult.getCollectionId());
			newCollection.setCorpusId(corpus.getCorpusId());
			newCollection.setStatus(1);
			
			collectionRepository.save(newCollection);
			
			List<CollectionData> listCollectionData = collectionDataRepository.findAll();
			
			// up all document to collection on watson
			discoveryService.addMultipeDocument(environmentId, createResult.getCollectionId(), 
					listCollectionData);
			log.info("uploading is successful");
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/collection/training", method = RequestMethod.POST)
	public ResponseEntity<?> trainingData(@RequestBody QueryDTO upload, HttpServletRequest request) {
		int corpusId = upload.getCorpusId();
		Corpus corpus = corpusRepository.findByCorpusId(corpusId);
		Collection collection = collectionRepository.findByCorpusId(corpusId);
		
		String environmentId = corpus.getEnviromentId();
		String collectionId = collection.getCollectionId();
		
		boolean trainingStatus = discoveryService.isTrainingAtCollection(environmentId, collectionId);
		
		log.info("training status : " + trainingStatus);
		
		// Check the current collection is training or not
		if (!trainingStatus) {
			// Check delete training data is successful or not
			if (discoveryService.deleteAllTrainingData(environmentId, collectionId) == 204) {
				log.info("Delete All Training Data is successful");
				
				List<FeedbackQuestion> feedbackDatas = feedbackDataRepository.findByCorpusId(corpusId);
				log.info("====> feedbackDatas : " + feedbackDatas);
				
				List<CollectionData> collectionDatas = collectionDataRepository.findByCorpusId(corpusId);
				log.info("====> collectionDatas : " + collectionDatas);
				
				List<BaseData> data = new ArrayList<BaseData>();
				
				if(null != feedbackDatas && feedbackDatas.size() > 0) {
					data.addAll(feedbackDatas);
				}
				
				if(null != collectionDatas && collectionDatas.size() > 0) {
					data.addAll(collectionDatas);
				}
				
				List<String> result = new ArrayList<>();
				for (BaseData item : data) {
					String naturalLanguageQuery = retrievalService.getQueryFromBaseData(item);
					if (naturalLanguageQuery != null && naturalLanguageQuery.length() > 0) {
						result.add(naturalLanguageQuery);
					}
				}
				log.info("====> result " + result);
				
				// add all query to training data
				HashMap<String, String> lanQueIds = discoveryService.addQueryToTrainingData(result,
						environmentId, collectionId);
				
				log.info("Add query to training data is successful");
				log.info("====> lanQueIds " + lanQueIds);
				
				List<HashMap<String, FeedbackRelevance>> documentIds = new ArrayList<HashMap<String,FeedbackRelevance>>();
				
				for (FeedbackQuestion item : feedbackDatas) {
					int feedbackId = item.getFeedbackId();
					String naturalLanguageQuery = retrievalService.getQueryFromBaseData(item);
					
					List<FeedbackRelevance> listRelevances = relevanceService.
							findAllByFeedbackId(feedbackId);
					
					for (FeedbackRelevance relevance : listRelevances) {
						HashMap<String, FeedbackRelevance> e = new HashMap<String, FeedbackRelevance>();
						e.put(naturalLanguageQuery, relevance);
						documentIds.add(e);
					}
				}
				
				log.info("====> documentIds " + documentIds);
				
				discoveryService.addCollectionDataToTrainingData(collectionDatas, lanQueIds, environmentId, collectionId);
				
				discoveryService.addFeedbackDataToTrainingData(documentIds, lanQueIds, environmentId, collectionId);
				
				log.info("Training succesful");
								
			} else {
				log.info("Delete All Training Data is fail");
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/collection/status/{corpusId}", method = RequestMethod.GET)
	public ResponseEntity<WatsonCollectionDTO> getCollectionStatus(@PathVariable("corpusId") int corpusId,
			HttpServletRequest request) {
		Corpus corpus = corpusRepository.findByCorpusId(corpusId);
		Collection collection = collectionRepository.findByCorpusId(corpusId);
		
		String environmentId = corpus.getEnviromentId();
		String collectionId = collection.getCollectionId();
		
		Response<com.ibm.watson.discovery.v1.model.Collection> createResponse = discoveryService.
				GetCollection(environmentId, collectionId);
		
		if (200 != createResponse.getStatusCode()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			com.ibm.watson.discovery.v1.model.Collection result = createResponse.getResult();
			WatsonCollectionDTO obj = new WatsonCollectionDTO();
			String name = null != result.getName() ? result.getName() : "";
			String dateOdUpdate = null != result.getUpdated() ?
					String.valueOf(result.getUpdated().getTime()) : "";
			String dateOfTraining = null != result.getTrainingStatus().getSuccessfullyTrained() ?
				String.valueOf(result.getTrainingStatus().getSuccessfullyTrained().getTime()) : "";
				
			obj.setName(name);
			obj.setDateOfUpdate(dateOdUpdate);
			obj.setDataOfTraining(dateOfTraining);
			obj.setProcessing(result.getTrainingStatus().isProcessing());
			obj.setAvailable(result.getTrainingStatus().isAvailable());
			
			return new ResponseEntity<WatsonCollectionDTO>(obj, HttpStatus.OK);
		}
	}
}
