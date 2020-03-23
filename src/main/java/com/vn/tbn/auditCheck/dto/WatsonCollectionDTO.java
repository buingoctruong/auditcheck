package com.vn.tbn.auditCheck.dto;

public class WatsonCollectionDTO {
	private String name;
	private String dateOfUpdate;
	private String dataOfTraining;
	private boolean processing;
	private boolean available;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDateOfUpdate() {
		return dateOfUpdate;
	}
	public void setDateOfUpdate(String dateOfUpdate) {
		this.dateOfUpdate = dateOfUpdate;
	}
	public String getDataOfTraining() {
		return dataOfTraining;
	}
	public void setDataOfTraining(String dataOfTraining) {
		this.dataOfTraining = dataOfTraining;
	}
	public boolean isProcessing() {
		return processing;
	}
	public void setProcessing(boolean processing) {
		this.processing = processing;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
}
