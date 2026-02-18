package com.item.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.item.dto.*;
import com.item.entity.Category;
import com.item.repository.CategoryRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class CategoryService implements CategoryServiceInterface {

	private ModelMapper modelMapper;
	private CategoryRepository categoryRepo;
	
	@Override
	public CategoryResponseDto addCategory(CategoryRequestDto dto) {
		Category category = modelMapper.map(dto, Category.class);
		Category saveCategory = categoryRepo.save(category);
		return modelMapper.map(saveCategory, CategoryResponseDto.class);
	}

	@Override
	public List<CategoryResponseDto> getAllCategories() {
		return categoryRepo.findAll()
				.stream()
				.map(catgry -> modelMapper.map(catgry, CategoryResponseDto.class))
				.collect(Collectors.toList());
	}

}
