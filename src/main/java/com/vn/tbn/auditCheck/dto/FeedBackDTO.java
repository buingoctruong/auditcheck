package com.vn.tbn.auditCheck.dto;

public class FeedBackDTO {
	private int corpusId;
	private String question;
	private String category;
	private int dataId;
	private int relevance;
	private int[] listDataId;
	public int getCorpusId() {
		return corpusId;
	}
	public void setCorpusId(int corpusId) {
		this.corpusId = corpusId;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getDataId() {
		return dataId;
	}
	public void setDataId(int dataId) {
		this.dataId = dataId;
	}
	public int getRelevance() {
		return relevance;
	}
	public void setRelevance(int relevance) {
		this.relevance = relevance;
	}
	public int[] getListDataId() {
		return listDataId;
	}
	public void setListDataId(int[] listDataId) {
		this.listDataId = listDataId;
	}
}
