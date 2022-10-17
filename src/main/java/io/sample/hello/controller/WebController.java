package io.sample.hello.controller;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import io.sample.hello.service.RDSDatasourceService;
import io.sample.hello.service.Result;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class WebController {

	@Autowired
	RDSDatasourceService rdsDatasourceService;
	

	@GetMapping(value = "/index")
	public String index(Model model) {
		Map<String, Result> dsResults = rdsDatasourceService.loadRDSDatasources();
		//log.info("INDEX Datasource Results={}", dsResults);
		model.addAttribute("results", dsResults.values().stream().collect(Collectors.toList()));
		return "index";
	}
	
	@GetMapping(value = "/rerun")
	public String rerun(Model model) {
		rdsDatasourceService.invalidateCache();
		Map<String, Result> dsResults = rdsDatasourceService.loadRDSDatasources();
		//log.info("Rerun Datasource Results={}", dsResults);
		model.addAttribute("results", dsResults.values().stream().collect(Collectors.toList()));
		return "index";
	}

}