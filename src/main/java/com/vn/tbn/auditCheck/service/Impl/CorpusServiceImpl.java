package com.vn.tbn.auditCheck.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vn.tbn.auditCheck.model.Corpus;
import com.vn.tbn.auditCheck.repository.CorpusRepository;
import com.vn.tbn.auditCheck.service.CorpusService;

@Service
public class CorpusServiceImpl implements CorpusService {
	@Autowired
	CorpusRepository corpusRepository;
	
	@Override
	public List<Corpus> getAllCorpus() {
		return corpusRepository.findAll();
	}
}
