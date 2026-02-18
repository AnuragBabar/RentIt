package com.booking.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.booking.client.dto.ItemResponse;

@FeignClient(name = "item-service", url = "http://localhost:8082", configuration = FeignAuthInterceptor.class)
public interface ItemServiceClient {

	@GetMapping("/items/{id}")
	ItemResponse getItemById(@PathVariable Long id);

	@PutMapping("/items/{id}/book")
	void markItemAsBooked(@PathVariable Long id);

	@PutMapping("/items/{id}/available")
	void markItemAsAvailable(@PathVariable Long id);

}
