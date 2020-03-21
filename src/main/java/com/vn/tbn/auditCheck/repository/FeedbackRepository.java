package com.vn.tbn.auditCheck.repository;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vn.tbn.auditCheck.model.FeedbackQuestion;

public interface FeedbackRepository extends JpaRepository<FeedbackQuestion, Integer>{
	List<FeedbackQuestion> findByCorpusId(int corpusId);
	
	@Query("SELECT f FROM FeedbackQuestion f WHERE f.question = ?1 AND f.category = ?2 ")
	List<FeedbackQuestion> findByQuestinAndCategory(@NotNull String question, String category);
}
