package com.item.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.item.dto.CategoryRequestDto;
import com.item.dto.CategoryResponseDto;
import com.item.service.CategoryService;
import com.item.service.RoleValidator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController

@RequestMapping("/categories")
@AllArgsConstructor
public class CategoryController {

	private final CategoryService service;

	@PostMapping
	public CategoryResponseDto addCategory(@Valid @RequestBody CategoryRequestDto dto, HttpServletRequest request) {
		RoleValidator.requireRole(request, "OWNER");
		return service.addCategory(dto);
	}

	@GetMapping
	public List<CategoryResponseDto> getAll() {
		return service.getAllCategories();
	}

}
