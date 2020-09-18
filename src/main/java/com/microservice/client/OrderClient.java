package com.microservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("order") //name-id that eureka server
public interface OrderClient {

	@GetMapping("/api/count/{id}")
	public int countBookSold(@PathVariable("id") long productId);
}
