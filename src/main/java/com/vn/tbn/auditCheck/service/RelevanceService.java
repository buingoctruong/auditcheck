package com.vn.tbn.auditCheck.service;

import java.util.List;

import com.vn.tbn.auditCheck.model.FeedbackRelevance;

public interface RelevanceService {
	public List<FeedbackRelevance> findAllByFeedbackId(int feedbackId);
}
