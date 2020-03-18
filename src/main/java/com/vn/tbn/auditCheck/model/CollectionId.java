package com.vn.tbn.auditCheck.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class CollectionId implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public Integer corpusId;
	
	public String collectionId;
}
