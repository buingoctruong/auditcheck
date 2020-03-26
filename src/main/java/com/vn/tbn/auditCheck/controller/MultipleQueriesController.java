package com.vn.tbn.auditCheck.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.vn.tbn.auditCheck.model.Corpus;
import com.vn.tbn.auditCheck.service.CorpusService;

@RestController
@RequestMapping("/queries")
public class MultipleQueriesController {
	@Autowired
	CorpusService corpusService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index(ModelAndView modelAndView, HttpServletRequest request) {
		List<Corpus> listCorpus = corpusService.getAllCorpus();
		modelAndView.addObject("listCorpus", listCorpus);
		modelAndView.setViewName("multipleQueries");
		return modelAndView;
	}
}
