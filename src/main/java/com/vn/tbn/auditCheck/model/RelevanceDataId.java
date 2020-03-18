package com.vn.tbn.auditCheck.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class RelevanceDataId implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public Integer feedbackId;
	
	public Integer dataId;

}
