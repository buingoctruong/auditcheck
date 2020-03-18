package com.vn.tbn.auditCheck.service.Impl;

import org.springframework.stereotype.Service;

import com.vn.tbn.auditCheck.model.BaseData;
import com.vn.tbn.auditCheck.model.CollectionData;
import com.vn.tbn.auditCheck.model.FeedbackCollectionData;
import com.vn.tbn.auditCheck.service.CollectionDataService;

@Service
public class CollectionDataServiceImpl implements CollectionDataService {
	@Override
	public String getQueryFromBaseData(BaseData data) {
		String question = null;
		String naturalLanguageQuery = null;
		String category = null;
		if (data instanceof FeedbackCollectionData) {
			question = ((FeedbackCollectionData) data).getQuestion();
			category = ((FeedbackCollectionData) data).getCategory();
			if (category != null) {
				naturalLanguageQuery = category + "." + question;
			} else {
				naturalLanguageQuery = question;
			}
		} else if (data instanceof CollectionData) {
			question = ((CollectionData) data).getQuestion();
			category = ((CollectionData) data).getCategory();
			if (category != null) {
				naturalLanguageQuery = category + "." + question;
			} else {
				naturalLanguageQuery = question;
			}
		}
		return naturalLanguageQuery;
	}
}
