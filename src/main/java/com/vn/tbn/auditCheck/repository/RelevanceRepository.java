package com.vn.tbn.auditCheck.repository;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vn.tbn.auditCheck.model.FeedbackRelevance;

@Repository
public interface RelevanceRepository extends JpaRepository<FeedbackRelevance, Integer> {	
	@Query("SELECT r FROM FeedbackRelevance r WHERE r.feedbackId = ?1 ")
	List<FeedbackRelevance> findByFeedbackId(@NotNull int feedbackId);

}
