package com.item.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ItemRequestDto {

	@NotBlank(message = "Enter item name..")
	private String itemName;

	private String description;

	@Positive
	private double pricePerDay;

	@NotNull
	private Long ownerId;

	@NotNull
	private Long categoryId;

	private String imageUrl;

}
