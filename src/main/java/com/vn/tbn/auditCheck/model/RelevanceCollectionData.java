package com.vn.tbn.auditCheck.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "t_feedback_relevance")
@IdClass(RelevanceDataId.class)
public class RelevanceCollectionData {
	public Integer getFeedbackId() {
		return feedbackId;
	}

	public void setFeedbackId(Integer feedbackId) {
		this.feedbackId = feedbackId;
	}

	public Integer getDataId() {
		return dataId;
	}

	public void setDataId(Integer dataId) {
		this.dataId = dataId;
	}

	public Integer getRelevance() {
		return relevance;
	}

	public void setRelevance(Integer relevance) {
		this.relevance = relevance;
	}

	@Id
	@Column(name = "feedback_id", nullable = false)
	public Integer feedbackId;
	
	@Id
	@Column(name = "data_id", nullable = false)
	public Integer dataId;
	
	@Column(name = "relevance")
	public Integer relevance;
}
