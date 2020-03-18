package com.vn.tbn.auditCheck.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "m_collection_data")
@IdClass(CollectionDataId.class)
public class CollectionData extends BaseData {
	public Integer getCorpusId() {
		return corpusId;
	}

	public void setCorpusId(Integer corpusId) {
		this.corpusId = corpusId;
	}

	public Integer getDataId() {
		return dataId;
	}

	public void setDataId(Integer dataId) {
		this.dataId = dataId;
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

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	@Id
	@Column(name = "corpus_id")
	public Integer corpusId;
	
	@Id
	@Column(name = "data_id")
	public Integer dataId;
	
	@Column(name = "category", length = 1000)
	public String category;
	
	@Column(name = "question", length = 1000)
	public String question;
	
	@Column(name = "answer", length = 1000)
	public String answer;
}
