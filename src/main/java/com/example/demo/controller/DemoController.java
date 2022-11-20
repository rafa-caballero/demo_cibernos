package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.DemoService;

import io.swagger.annotations.Api;
import reactor.core.publisher.Flux;

@RestController
@Api
@RequestMapping("/product")
public class DemoController {

	@Autowired
	private DemoService demoService;

	@GetMapping("/{productId}/similar")
	public Flux<String> similar(@PathVariable Integer productId) {

		Flux<String> result = this.demoService.similar(productId, false);
		return result;

	}
}
