package com.vn.tbn.auditCheck.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.ibm.watson.discovery.v1.model.QueryOptions;
import com.ibm.watson.discovery.v1.model.QueryResponse;
import com.ibm.watson.discovery.v1.model.QueryResult;
import com.vn.tbn.auditCheck.dto.UploadDTO;
import com.vn.tbn.auditCheck.model.Collection;
import com.vn.tbn.auditCheck.model.Corpus;
import com.vn.tbn.auditCheck.model.ResultSearch;
import com.vn.tbn.auditCheck.repository.CollectionRepository;
import com.vn.tbn.auditCheck.repository.CorpusRepository;
import com.vn.tbn.auditCheck.service.CorpusService;
import com.vn.tbn.auditCheck.service.DiscoveryService;

@RestController
@RequestMapping("/search")
public class searchController {
	@Autowired
	CorpusService corpusService;
	
	@Autowired
	CorpusRepository corpusRepository;
	
	@Autowired
	CollectionRepository collectionRepository;
	
	@Autowired
	DiscoveryService discoveryService;
	
	Gson gson;
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index(ModelAndView modelAndView, HttpServletRequest request) {
		List<Corpus> listCorpus = corpusService.getAllCorpus();
		modelAndView.addObject("listCorpus", listCorpus);
		modelAndView.setViewName("search");
		return modelAndView;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<List<ResultSearch>> searchResult(@RequestBody UploadDTO uploadDTO) {
		Corpus corpus = corpusRepository.findByCorpusId(uploadDTO.getCorpusId());
		Collection collection = collectionRepository.findByCorpusId(uploadDTO.getCorpusId());
		
		String query = uploadDTO.getQuery();
				
		String environmentId = corpus.getEnviromentId();
		String collectionId = collection.getCollectionId();
		
		QueryOptions.Builder queryBuilder = new QueryOptions.Builder(environmentId, collectionId);
		queryBuilder.query(query);
		QueryResponse queryResponse = discoveryService.getDiscovery().query(queryBuilder.build()).
				execute().getResult();
		
		List<QueryResult> result = queryResponse.getResults();
		
		List<ResultSearch> searchLists = new ArrayList<ResultSearch>();
		
		for (QueryResult item : result) {
			ResultSearch temp = new ResultSearch();
			
			temp.setQuestion((String) item.get("question"));
			temp.setAnswer((String) item.get("answer"));
			temp.setCategory((String)item.get("category"));
			temp.setDataId((String) item.get("category"));
			temp.setConfidence(item.getResultMetadata().getConfidence());
			
	        searchLists.add(temp);
		}
				
		return new ResponseEntity<List<ResultSearch>>(searchLists, HttpStatus.OK);
	}
}
