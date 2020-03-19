package com.vn.tbn.auditCheck.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.vn.tbn.auditCheck.dto.FeedBackDTO;
import com.vn.tbn.auditCheck.model.FeedbackCollectionData;
import com.vn.tbn.auditCheck.model.RelevanceCollectionData;
import com.vn.tbn.auditCheck.model.ResultSearch;
import com.vn.tbn.auditCheck.repository.FeedbackCollectionDataRepository;
import com.vn.tbn.auditCheck.repository.RelevanceCollectionDataRepository;

@RestController
@RequestMapping("/feedback")
public class FeedbackCollectionDataController {
	@Autowired
	FeedbackCollectionDataRepository feedbackCollectionDataRepository;
	
	@Autowired
	RelevanceCollectionDataRepository relevanceCollectionDataRepository;
	
	@RequestMapping(value = "/relevance", method = RequestMethod.POST)
	public ResponseEntity<?> makeFeedbackRelevance(@RequestBody FeedBackDTO feedbackDTO) {
		int feedbackId;
		
		List<FeedbackCollectionData> oldFeedback = feedbackCollectionDataRepository.
				findByQuestinAndCategory(feedbackDTO.getQuestion(), feedbackDTO.getCategory());
		
		if (null != oldFeedback && oldFeedback.size() > 0) {
			FeedbackCollectionData feedback = oldFeedback.get(0);
			feedbackId = feedback.getFeedbackId();
		} else {
			feedbackId = feedbackCollectionDataRepository.findAll().size() + 1;
			
			FeedbackCollectionData feedback = new FeedbackCollectionData();
			
			feedback.setFeedbackId(feedbackId);
			feedback.setCorpusId(feedbackDTO.getCorpusId());
			feedback.setCategory(feedbackDTO.getCategory());
			feedback.setQuestion(feedbackDTO.getQuestion());
			
			feedbackCollectionDataRepository.save(feedback);
		}
		
		RelevanceCollectionData relevanceData = new RelevanceCollectionData();
		relevanceData.setDataId(feedbackDTO.getDataId());
		relevanceData.setFeedbackId(feedbackId);
		relevanceData.setRelevance(feedbackDTO.getRelevance());
				
		relevanceCollectionDataRepository.save(relevanceData);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/irrelevance", method = RequestMethod.POST)
	public ResponseEntity<?> makeFeedbackIrrelevance(@RequestBody FeedBackDTO result) {
		int corpusId = result.getCorpusId();
		String query = result.getQuestion();
		String category = result.getCategory();
		int relevance = result.getRelevance();
		int[] listDataId = result.getListDataId();
		int feedbackId;
		
		List<FeedbackCollectionData> oldFeedback = feedbackCollectionDataRepository.
				findByQuestinAndCategory(query, category);
		
		if (null != oldFeedback && oldFeedback.size() > 0) {
			FeedbackCollectionData feedback = oldFeedback.get(0);
			feedbackId = feedback.getFeedbackId();
		} else {
			feedbackId = feedbackCollectionDataRepository.findAll().size() + 1;
			
			FeedbackCollectionData feedback = new FeedbackCollectionData();
			feedback.setCorpusId(corpusId);
			feedback.setCategory(category);
			feedback.setFeedbackId(feedbackId);
			feedback.setQuestion(query);
			
			feedbackCollectionDataRepository.save(feedback);
		}
		
		List<RelevanceCollectionData> listRelevances = new ArrayList<RelevanceCollectionData>();
		
		for (int item : listDataId) {			
			RelevanceCollectionData rel = new RelevanceCollectionData();
			rel.setDataId(item);
			rel.setFeedbackId(feedbackId);
			rel.setRelevance(relevance);
			
			listRelevances.add(rel);
		}
		
		relevanceCollectionDataRepository.saveAll(listRelevances);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
