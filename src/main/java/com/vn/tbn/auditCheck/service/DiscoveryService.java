package com.vn.tbn.auditCheck.service;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.google.gson.JsonObject;
import com.ibm.watson.discovery.v1.Discovery;
import com.sun.istack.NotNull;
import com.vn.tbn.auditCheck.model.CollectionData;
import com.vn.tbn.auditCheck.model.RelevanceCollectionData;

public interface DiscoveryService {
	public Discovery getDiscovery();
	
	public boolean addMultipeDocument(@NotNull String environmentId, @NotNull String collectionId, List<CollectionData> data);
	
	public CompletableFuture<String> updateDocumet(String documentId,String environmentId, String collectionId, JsonObject data);
	
	public String getDocumentId(JsonObject data, String documentId, String environmentId, String collectionId);
	
	public boolean isTrainingAtCollection(String environmentId, String collectionId);
	
	public void deleteAllTrainingData(String environmentId, String collectionId);
	
	public HashMap<String, String> addQueryToTrainingData(List<String> data, String environmentId,
			String collectionId);
	
	public void addCollectionDataToTrainingData(List<CollectionData> data, HashMap<String, String> lanQueIds,
			String environmentId, String Collectionid);
	
	public void addFeedbackDataToTrainingData(List<HashMap<String, RelevanceCollectionData>> documentIds, 
			HashMap<String, String> lanQueIds, String environmentId, String CollectionId);
}
