package com.vn.tbn.auditCheck.repository;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vn.tbn.auditCheck.model.FeedbackCollectionData;

public interface FeedbackCollectionDataRepository extends JpaRepository<FeedbackCollectionData, Integer>{
	List<FeedbackCollectionData> findByCorpusId(int corpusId);
	
	@Query("SELECT f FROM FeedbackCollectionData f WHERE f.question = ?1 AND f.category = ?2 ")
	List<FeedbackCollectionData> findByQuestinAndCategory(@NotNull String question, String category);
}
