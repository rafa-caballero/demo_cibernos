package com.example.demo.service;

import reactor.core.publisher.Flux;

public interface DemoService {

	Flux<String> similar(Integer productid, boolean mock);
}
