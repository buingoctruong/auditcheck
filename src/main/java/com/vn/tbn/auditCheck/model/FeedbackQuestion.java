package com.vn.tbn.auditCheck.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "t_feedback_question")
public class FeedbackQuestion extends BaseData {
	@Id
	@Column(name = "feedback_id", nullable = false)
	public Integer feedbackId;
	
	@Column(name = "corpus_id", nullable = false)
	public Integer corpusId;
	
	@Column(name = "category", length = 32500)
	public String category;
	
	@Column(name = "question", length = 32500)
	public String question;
	
	public Integer getFeedbackId() {
		return feedbackId;
	}

	public void setFeedbackId(Integer feedbackId) {
		this.feedbackId = feedbackId;
	}

	public Integer getCorpusId() {
		return corpusId;
	}

	public void setCorpusId(Integer corpusId) {
		this.corpusId = corpusId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}
}
