package com.vn.tbn.auditCheck.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vn.tbn.auditCheck.model.RelevanceCollectionData;
import com.vn.tbn.auditCheck.repository.RelevanceCollectionDataRepository;
import com.vn.tbn.auditCheck.service.RelevanceCollectionDataService;

@Service
public class RelevanceCollectionDataServiceImpl implements RelevanceCollectionDataService {
	@Autowired
	RelevanceCollectionDataRepository relevanceCollectionDataRepository;
	
	@Override
	public List<RelevanceCollectionData> findAllByFeedbackId(int feedbackId) {
		return relevanceCollectionDataRepository.findByFeedbackId(feedbackId);
	}
}
