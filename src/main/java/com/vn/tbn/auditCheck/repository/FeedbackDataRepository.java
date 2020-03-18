package com.vn.tbn.auditCheck.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vn.tbn.auditCheck.model.FeedbackCollectionData;

public interface FeedbackDataRepository extends JpaRepository<FeedbackCollectionData, Integer>{
	List<FeedbackCollectionData> findByCorpusId(int corpusId);
}
