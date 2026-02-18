package com.item.service;

import java.util.List;

import com.item.dto.CategoryRequestDto;
import com.item.dto.CategoryResponseDto;

public interface CategoryServiceInterface {
	
	CategoryResponseDto addCategory(CategoryRequestDto dto);
    List<CategoryResponseDto> getAllCategories();

}
