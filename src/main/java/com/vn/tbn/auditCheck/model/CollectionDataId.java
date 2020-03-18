package com.vn.tbn.auditCheck.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class CollectionDataId implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public Integer corpusId;
	
	public Integer dataId;
}
