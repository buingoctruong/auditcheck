package com.vn.tbn.auditCheck.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vn.tbn.auditCheck.model.Corpus;

@Repository
public interface CorpusRepository extends JpaRepository<Corpus, Integer> {
	Corpus findByCorpusId(int corpusId);
}
