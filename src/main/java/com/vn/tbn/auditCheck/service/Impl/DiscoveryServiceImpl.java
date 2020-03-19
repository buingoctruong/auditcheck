package com.vn.tbn.auditCheck.service.Impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.ibm.cloud.sdk.core.http.HttpMediaType;
import com.ibm.cloud.sdk.core.http.Response;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.discovery.v1.Discovery;
import com.ibm.watson.discovery.v1.model.AddTrainingDataOptions;
import com.ibm.watson.discovery.v1.model.Collection;
import com.ibm.watson.discovery.v1.model.CreateTrainingExampleOptions;
import com.ibm.watson.discovery.v1.model.DeleteAllTrainingDataOptions;
import com.ibm.watson.discovery.v1.model.DocumentAccepted;
import com.ibm.watson.discovery.v1.model.GetCollectionOptions;
import com.ibm.watson.discovery.v1.model.TrainingExample;
import com.ibm.watson.discovery.v1.model.TrainingQuery;
import com.ibm.watson.discovery.v1.model.UpdateDocumentOptions;
import com.sun.istack.NotNull;
import com.vn.tbn.auditCheck.model.CollectionData;
import com.vn.tbn.auditCheck.model.RelevanceCollectionData;
import com.vn.tbn.auditCheck.service.CollectionDataService;
import com.vn.tbn.auditCheck.service.DiscoveryService;

@Service
public class DiscoveryServiceImpl implements DiscoveryService {
	@Value("${discovery.apikey}")
	String apiKey;
	
	@Value("${discovery.url}")
	String url;
	
	@Value("${discovery.date}")
	String version;
	
	@Autowired
	CollectionDataService collectionDataService;
	
	@Override
	public Discovery getDiscovery() {
		IamAuthenticator authenticator = new IamAuthenticator(apiKey);
		Discovery discovery = new Discovery(version, authenticator);
		discovery.setServiceUrl(url);
		
		return discovery;
	}
	
	@Override
	public boolean addMultipeDocument(@NotNull String environmentId,
			@NotNull String collectionId, List<CollectionData> data) {
		System.out.println("Start uploading Documents");
		List<CompletableFuture<String>> queries = data.stream()
				.map(new Function<CollectionData, CompletableFuture<String>>() {
					@Override
					public CompletableFuture<String> apply(CollectionData collectionData) {
						String documentId = String.valueOf(collectionData.getDataId());
						JsonObject data = new JsonObject();
						data.addProperty("data_id", documentId);
						data.addProperty("category", collectionData.getCategory());
						data.addProperty("question", collectionData.getQuestion());
						data.addProperty("answer", collectionData.getAnswer());
						
						return updateDocumet(documentId, environmentId, collectionId, data);
					}
				}).collect(Collectors.toList());
		
		System.out.println("End uploading Documents");
		
		return true;
	}
	
	@Override
	@Async("otherExecutor")
	public CompletableFuture<String> updateDocumet(String Id, String environmentId,
			String collectionId, JsonObject data) {
		CompletableFuture<String> documentIds = CompletableFuture.completedFuture(
				getDocumentId(data, Id, environmentId, collectionId));
		return documentIds;
	}
	
	@Override
	public String getDocumentId(JsonObject data, String documentId, String environmentId,
			String collectionId) {
		String updatedDocumentJson = data.toString();
		InputStream updatedDocumentStream = new ByteArrayInputStream(updatedDocumentJson.getBytes());

		UpdateDocumentOptions.Builder updateBuilder = new UpdateDocumentOptions.Builder(environmentId,
				collectionId, documentId);
		updateBuilder.file(updatedDocumentStream);
		updateBuilder.filename(documentId);
		updateBuilder.fileContentType(HttpMediaType.APPLICATION_JSON);
		
		Response<DocumentAccepted> response = getDiscovery().updateDocument(updateBuilder.build()).execute();
		DocumentAccepted updateResponse = response.getResult();
		
		return updateResponse.getDocumentId();
	}
	
	@Override
	public boolean isTrainingAtCollection(String environmentId, String collectionId) {
		GetCollectionOptions getOptions = new GetCollectionOptions.Builder(environmentId, collectionId).build();
		Collection getResponse = getDiscovery().getCollection(getOptions).execute().getResult();
		return getResponse.getTrainingStatus().isProcessing();
	}
	
	@Override
	public int deleteAllTrainingData(String environmentId, String collectionId) {
		DeleteAllTrainingDataOptions options = new DeleteAllTrainingDataOptions.Builder(
				environmentId, collectionId).build();
		return getDiscovery().deleteAllTrainingData(options).execute().getStatusCode();
	}
	
	@Override
	public HashMap<String, String> addQueryToTrainingData(List<String> data, String environmentId,
			String collectionId) {
		HashMap<String, String> result = new HashMap<String, String>();
		String filter = null;
		
		for (String query : data) {
			AddTrainingDataOptions options = new AddTrainingDataOptions.Builder(environmentId, collectionId)
					  .naturalLanguageQuery(query)
					  .filter(filter)
					  .build();

			TrainingQuery response = getDiscovery().addTrainingData(options).execute().getResult();
			result.put(response.getNaturalLanguageQuery(), response.getQueryId());
		}
		return result;
	}
	
	@Override
	public void addCollectionDataToTrainingData(List<CollectionData> data, HashMap<String, String> lanQueIds,
			String environmentId, String CollectionId) {
		for (CollectionData item : data) {
			String naturalLanguageQuery = collectionDataService.getQueryFromBaseData(item);
			String queryId = lanQueIds.get(naturalLanguageQuery);
			String documentId = String.valueOf(item.getDataId());
			int relevance = 10;
			
			CreateTrainingExampleOptions options = new CreateTrainingExampleOptions.Builder(environmentId, CollectionId, queryId)
					  .documentId(documentId)
					  .relevance(relevance)
					  .build();
			Response<TrainingExample> response = getDiscovery().createTrainingExample(options).execute();
			TrainingExample result = response.getResult();
		}
	}
	
	@Override
	public void addFeedbackDataToTrainingData(List<HashMap<String, RelevanceCollectionData>> documentIds, 
			HashMap<String, String> lanQueIds, String environmentId, String CollectionId) {
		for (HashMap<String, RelevanceCollectionData> item : documentIds) {
			String naturalLanguageQuery = null;
			String documentId = null;
			int relevance = 10;
			for (Entry<String, RelevanceCollectionData> entry : item.entrySet()) {
				naturalLanguageQuery = entry.getKey();
				documentId = String.valueOf(entry.getValue().getDataId());
				relevance = entry.getValue().getRelevance();
			}
			String queryId = lanQueIds.get(naturalLanguageQuery);
			CreateTrainingExampleOptions options = new CreateTrainingExampleOptions.Builder(environmentId, CollectionId, queryId)
					  .documentId(documentId)
					  .relevance(relevance)
					  .build();
			TrainingExample response = getDiscovery().createTrainingExample(options).execute().getResult();
			
		}
	}
}