package com.vn.tbn.auditCheck.dto;

import lombok.Data;

@Data
public class SingleQueryDTO {
	private int corpusId;
	
	private String query;

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
	
	public int getCorpusId() {
		return corpusId;
	}

	public void setCorpusId(int corpusId) {
		this.corpusId = corpusId;
	}
}
