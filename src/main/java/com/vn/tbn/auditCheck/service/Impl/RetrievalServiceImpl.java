package com.vn.tbn.auditCheck.service.Impl;

import org.springframework.stereotype.Service;

import com.vn.tbn.auditCheck.model.BaseData;
import com.vn.tbn.auditCheck.model.CollectionData;
import com.vn.tbn.auditCheck.model.FeedbackQuestion;
import com.vn.tbn.auditCheck.service.RetrievalService;

@Service
public class RetrievalServiceImpl implements RetrievalService {
	@Override
	public String getQueryFromBaseData(BaseData data) {
		String question = null;
		String naturalLanguageQuery = null;
		String category = null;
		if (data instanceof FeedbackQuestion) {
			question = ((FeedbackQuestion) data).getQuestion();
			category = ((FeedbackQuestion) data).getCategory();
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
