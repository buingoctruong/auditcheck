package com.vn.tbn.auditCheck.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vn.tbn.auditCheck.model.CollectionData;
import com.vn.tbn.auditCheck.model.CollectionDataId;

public interface CollectionDataRepository extends JpaRepository<CollectionData, CollectionDataId> {
	List<CollectionData> findByCorpusId(int corpusId);
}
