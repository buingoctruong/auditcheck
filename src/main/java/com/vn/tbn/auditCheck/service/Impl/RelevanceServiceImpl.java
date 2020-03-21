package com.vn.tbn.auditCheck.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vn.tbn.auditCheck.model.FeedbackRelevance;
import com.vn.tbn.auditCheck.repository.RelevanceRepository;
import com.vn.tbn.auditCheck.service.RelevanceService;

@Service
public class RelevanceServiceImpl implements RelevanceService {
	@Autowired
	RelevanceRepository relevanceRepository;
	
	@Override
	public List<FeedbackRelevance> findAllByFeedbackId(int feedbackId) {
		return relevanceRepository.findByFeedbackId(feedbackId);
	}
}
