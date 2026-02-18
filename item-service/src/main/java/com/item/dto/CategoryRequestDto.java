package com.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequestDto {
	
	@NotBlank(message = "Category name connot be empty..")
	private String name;

}
