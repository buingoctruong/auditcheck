package com.vn.tbn.auditCheck.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vn.tbn.auditCheck.model.Collection;
import com.vn.tbn.auditCheck.repository.CollectionRepository;
import com.vn.tbn.auditCheck.service.CollectionService;

@Service
public class CollectionServiceImpl implements CollectionService {
	@Autowired
	CollectionRepository collectionRepository;
	
	@Override
	public Collection findbyCorpusId(int corpusId) {
		return collectionRepository.findByCorpusId(corpusId);
	}
}
