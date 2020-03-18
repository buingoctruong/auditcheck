package com.vn.tbn.auditCheck.service;

import java.util.List;

import com.vn.tbn.auditCheck.model.RelevanceCollectionData;

public interface RelevanceCollectionDataService {
	public List<RelevanceCollectionData> findAllByFeedbackId(int feedbackId);
}
