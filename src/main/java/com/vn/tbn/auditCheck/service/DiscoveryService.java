package com.vn.tbn.auditCheck.service;

import java.util.HashMap;
import java.util.List;

import com.ibm.cloud.sdk.core.http.Response;
import com.ibm.watson.discovery.v1.Discovery;
import com.ibm.watson.discovery.v1.model.Collection;
import com.ibm.watson.discovery.v1.model.DeleteCollectionResponse;
import com.ibm.watson.discovery.v1.model.QueryResponse;
import com.sun.istack.NotNull;
import com.vn.tbn.auditCheck.model.CollectionData;
import com.vn.tbn.auditCheck.model.FeedbackRelevance;

public interface DiscoveryService {
	public Discovery getDiscovery();
	
	public Response<Collection> GetCollection(String environmentId,
			String collectionId);
	
	public Response<QueryResponse> GetCollectionByQuery(String environmentId,
			String collectionId, String query);
	
	public Response<DeleteCollectionResponse> DeleteCollection(String environmentId,
			String collectionId);
	
	public Response<Collection> CreateCollection(String environmentId, 
			String collectionName, String language);
	
	public void addMultipeDocument(@NotNull String environmentId,
			@NotNull String collectionId, List<CollectionData> data);
	
	public boolean isTrainingAtCollection(String environmentId, String collectionId);
	
	public int deleteAllTrainingData(String environmentId, String collectionId);
	
	public HashMap<String, String> addQueryToTrainingData(List<String> data, String environmentId,
			String collectionId);
			
	public void addCollectionDataToTrainingData(List<CollectionData> data, HashMap<String, String> lanQueIds,
			String environmentId, String Collectionid);
	
	public void addFeedbackDataToTrainingData(List<HashMap<String, FeedbackRelevance>> documentIds, 
			HashMap<String, String> lanQueIds, String environmentId, String CollectionId);
}
