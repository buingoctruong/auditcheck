package com.vn.tbn.auditCheck.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.vn.tbn.auditCheck.dto.FeedbackRelevanceDTO;
import com.vn.tbn.auditCheck.model.FeedbackQuestion;
import com.vn.tbn.auditCheck.model.FeedbackRelevance;
import com.vn.tbn.auditCheck.repository.FeedbackRepository;
import com.vn.tbn.auditCheck.repository.RelevanceRepository;

@RestController
@RequestMapping("/feedback")
public class FeedbackRelevanceController {
	@Autowired
	FeedbackRepository feedbackRepository;
	
	@Autowired
	RelevanceRepository relevanceRepository;
	
	@RequestMapping(value = "/relevance", method = RequestMethod.POST)
	public ResponseEntity<?> makeFeedbackRelevance(@RequestBody FeedbackRelevanceDTO dto) {
		int feedbackId;
		
		List<FeedbackQuestion> oldFeedback = feedbackRepository.
				findByQuestinAndCategory(dto.getQuestion(), dto.getCategory());
		
		if (null != oldFeedback && oldFeedback.size() > 0) {
			FeedbackQuestion feedback = oldFeedback.get(0);
			feedbackId = feedback.getFeedbackId();
		} else {
			feedbackId = feedbackRepository.findAll().size() + 1;
			
			FeedbackQuestion feedback = new FeedbackQuestion();
			
			feedback.setFeedbackId(feedbackId);
			feedback.setCorpusId(dto.getCorpusId());
			feedback.setCategory(dto.getCategory());
			feedback.setQuestion(dto.getQuestion());
			
			feedbackRepository.save(feedback);
		}
		
		FeedbackRelevance relevanceData = new FeedbackRelevance();
		relevanceData.setDataId(dto.getDataId());
		relevanceData.setFeedbackId(feedbackId);
		relevanceData.setRelevance(dto.getRelevance());
				
		relevanceRepository.save(relevanceData);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/irrelevance", method = RequestMethod.POST)
	public ResponseEntity<?> makeFeedbackIrrelevance(@RequestBody FeedbackRelevanceDTO result) {
		int corpusId = result.getCorpusId();
		String query = result.getQuestion();
		String category = result.getCategory();
		int relevance = result.getRelevance();
		int[] listDataId = result.getListDataId();
		int feedbackId;
		
		List<FeedbackQuestion> oldFeedback = feedbackRepository.
				findByQuestinAndCategory(query, category);
		
		if (null != oldFeedback && oldFeedback.size() > 0) {
			FeedbackQuestion feedback = oldFeedback.get(0);
			feedbackId = feedback.getFeedbackId();
		} else {
			feedbackId = feedbackRepository.findAll().size() + 1;
			
			FeedbackQuestion feedback = new FeedbackQuestion();
			feedback.setCorpusId(corpusId);
			feedback.setCategory(category);
			feedback.setFeedbackId(feedbackId);
			feedback.setQuestion(query);
			
			feedbackRepository.save(feedback);
		}
		
		List<FeedbackRelevance> listRelevances = new ArrayList<FeedbackRelevance>();
		
		for (int item : listDataId) {			
			FeedbackRelevance rel = new FeedbackRelevance();
			rel.setDataId(item);
			rel.setFeedbackId(feedbackId);
			rel.setRelevance(relevance);
			
			listRelevances.add(rel);
		}
		
		relevanceRepository.saveAll(listRelevances);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
