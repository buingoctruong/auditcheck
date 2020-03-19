package com.vn.tbn.auditCheck.repository;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vn.tbn.auditCheck.model.RelevanceCollectionData;

@Repository
public interface RelevanceCollectionDataRepository extends JpaRepository<RelevanceCollectionData, Integer> {	
	@Query("SELECT r FROM RelevanceCollectionData r WHERE r.feedbackId = ?1 ")
	List<RelevanceCollectionData> findByFeedbackId(@NotNull int feedbackId);

}
