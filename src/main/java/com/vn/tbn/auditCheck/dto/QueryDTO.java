package com.vn.tbn.auditCheck.dto;

import java.util.List;

import lombok.Data;

@Data
public class QueryDTO {
	private int corpusId;
	
	private List<String> queries;

	public List<String> getQueries() {
		return queries;
	}

	public void setQueries(List<String> queries) {
		this.queries = queries;
	}
	
	public int getCorpusId() {
		return corpusId;
	}

	public void setCorpusId(int corpusId) {
		this.corpusId = corpusId;
	}
}
