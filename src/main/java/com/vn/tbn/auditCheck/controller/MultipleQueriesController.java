package com.vn.tbn.auditCheck.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ibm.cloud.sdk.core.http.Response;
import com.ibm.watson.discovery.v1.model.QueryResponse;
import com.ibm.watson.discovery.v1.model.QueryResult;
import com.vn.tbn.auditCheck.dto.QueryDTO;
import com.vn.tbn.auditCheck.model.Collection;
import com.vn.tbn.auditCheck.model.Corpus;
import com.vn.tbn.auditCheck.model.ResultSearch;
import com.vn.tbn.auditCheck.repository.CollectionRepository;
import com.vn.tbn.auditCheck.repository.CorpusRepository;
import com.vn.tbn.auditCheck.service.CorpusService;
import com.vn.tbn.auditCheck.service.DiscoveryService;

@RestController
@RequestMapping("/queries")
public class MultipleQueriesController {
	@Autowired
	CorpusService corpusService;
	
	@Autowired
	CorpusRepository corpusRepository;
	
	@Autowired
	CollectionRepository collectionRepository;
	
	@Autowired
	DiscoveryService discoveryService;
	
	Logger log = LoggerFactory.getLogger(WatsonDiscoveryController.class);
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index(ModelAndView modelAndView, HttpServletRequest request) {
		List<Corpus> listCorpus = corpusService.getAllCorpus();
		modelAndView.addObject("listCorpus", listCorpus);
		modelAndView.setViewName("multipleQueries");
		return modelAndView;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<HashMap<String, List<ResultSearch>>> searchResult(@RequestBody QueryDTO dto) {
		int corpusId = dto.getCorpusId();
		List<String> queries = dto.getQueries();
		
		Corpus corpus = corpusRepository.findByCorpusId(corpusId);
		Collection collection = collectionRepository.findByCorpusId(corpusId);
		
		String environmentId = corpus.getEnviromentId();
		String collectionId = collection.getCollectionId();
		
		HashMap<String, List<ResultSearch>> resultList = new HashMap<String, List<ResultSearch>>();
		
		for (String query : queries) {
			
			// just get 5 results that have the highest confidence
			Response<QueryResponse> queryResponse = discoveryService.GetCollectionByQuery(environmentId, 
					collectionId, query, (long) 5);
			
			if (queryResponse.getStatusCode() != 200) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			
			List<QueryResult> result = queryResponse.getResult().getResults();
					
			List<ResultSearch> searchLists = new ArrayList<ResultSearch>();
			
			for (QueryResult item : result) {
				ResultSearch temp = new ResultSearch();
				
				temp.setQuestion((String) item.get("question"));
				temp.setAnswer((String) item.get("answer"));
				temp.setCategory((String)item.get("category"));
				temp.setDataId((String) item.get("data_id"));
				temp.setConfidence(item.getResultMetadata().getConfidence());
				
		        searchLists.add(temp);
			}
			resultList.put(query, searchLists);
		}
		
		return new ResponseEntity<HashMap<String, List<ResultSearch>>>(resultList, HttpStatus.OK);
	}
}
