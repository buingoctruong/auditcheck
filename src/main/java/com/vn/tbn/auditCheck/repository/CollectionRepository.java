package com.vn.tbn.auditCheck.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vn.tbn.auditCheck.model.Collection;
import com.vn.tbn.auditCheck.model.CollectionDataId;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, CollectionDataId> {
	Collection findByCorpusId(int corpusId);
}
