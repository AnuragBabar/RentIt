package com.booking.client.dto;

import lombok.Data;

@Data
public class ItemResponse {
	private Long id;
	private String itemName;
	private String description;
	private boolean available;
	private double pricePerDay;

}
