package com.vn.tbn.auditCheck.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "m_collection")
@IdClass(CollectionId.class)
public class Collection {
	public Integer getCorpusId() {
		return corpusId;
	}

	public void setCorpusId(Integer corpusId) {
		this.corpusId = corpusId;
	}

	public String getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Id
	@Column(name = "corpus_id")
	public Integer corpusId;
	
	@Id
	@Column(name = "collection_id", length = 1000)
	public String collectionId;
	
	@Column(name = "status")
	public Integer status;
}
