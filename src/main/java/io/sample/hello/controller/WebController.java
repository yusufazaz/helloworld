package io.sample.hello.controller;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.sample.hello.config.RDSDataSources;
import io.sample.hello.service.Result;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class WebController {

	@Autowired
	RDSDataSources rdsSDataSources;

	@GetMapping(value = "/index")
	public String index(Model model) {
		Map<String, Result> dsResults = rdsSDataSources.getDatasourceResults();
		log.info("Datasource Results={}", dsResults);
		model.addAttribute("results", dsResults.values().stream().collect(Collectors.toList()));
		return "index";
	}
}