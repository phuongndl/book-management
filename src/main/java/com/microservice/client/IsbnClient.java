package com.microservice.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "data", url = "${app.book.isbn}")
public interface IsbnClient {
	
	@GetMapping("/volumes")
	Map<String, Object> fetchBookByISBN(@RequestParam("q") String query);
}
