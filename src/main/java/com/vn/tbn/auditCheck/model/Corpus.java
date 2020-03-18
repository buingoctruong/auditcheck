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
@Table(name = "m_corpus")
public class Corpus {
	public Integer getCorpusId() {
		return corpusId;
	}

	public void setCorpusId(Integer corpusId) {
		this.corpusId = corpusId;
	}

	public String getCorpusName() {
		return corpusName;
	}

	public void setCorpusName(String corpusName) {
		this.corpusName = corpusName;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public String getEnviromentId() {
		return enviromentId;
	}

	public void setEnviromentId(String enviromentId) {
		this.enviromentId = enviromentId;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "corpus_id")
	public Integer corpusId;
	
	@Column(name = "corpus_name", length = 500)
	public String corpusName;
	
	@Column(name = "language_code", length = 10)
	public String languageCode;
	
	@Column(name = "environment_id", length = 32500)
	public String enviromentId;
}
