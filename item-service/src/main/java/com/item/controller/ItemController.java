package com.item.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.item.dto.ItemRequestDto;
import com.item.dto.ItemResponseDto;
import com.item.service.ItemService;
import com.item.service.RoleValidator;
import com.item.service.FilesStorageService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RestController

@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

	private final ItemService service;
	private final FilesStorageService storageService;

	@PostMapping("/upload")
	public String uploadImage(@RequestParam("file") MultipartFile file) {
		return storageService.save(file);
	}

	@PostMapping("/addItem")
	public ItemResponseDto addItem(@Valid @RequestBody ItemRequestDto dto, HttpServletRequest request) {
		RoleValidator.requireRole(request, "OWNER");
		return service.addItem(dto);
	}

	@PutMapping("/{id}")
	public ItemResponseDto updateItem(@PathVariable Long id, @Valid @RequestBody ItemRequestDto dto,
			HttpServletRequest request) {
		RoleValidator.requireRole(request, "OWNER");
		return service.updateItem(id, dto);
	}

	@GetMapping("/{id}")
	public ItemResponseDto getItem(@PathVariable Long id) {
		return service.getItemById(id);
	}

	@GetMapping("/category/{categoryId}")
	public List<ItemResponseDto> getByCategory(@PathVariable Long categoryId) {
		return service.getItemsByCategory(categoryId);
	}

	@GetMapping("/owner/{ownerId}")
	public List<ItemResponseDto> getByOwner(@PathVariable Long ownerId, HttpServletRequest request) {
		RoleValidator.requireRole(request, "OWNER");
		return service.getItemsByOwner(ownerId);
	}

	@GetMapping("/available")
	public List<ItemResponseDto> getAvailableItems() {
		return service.getAvailableItems();
	}

	@PutMapping("/{id}/book")
	public ItemResponseDto markAsBooked(@PathVariable Long id) {
		return service.markItemAsBooked(id);
	}

	@PutMapping("/{id}/available")
	public ItemResponseDto markItemAsAvailable(@PathVariable Long id) {
		return service.markItemAsAvailable(id);
	}

	@org.springframework.web.bind.annotation.DeleteMapping("/{id}")
	public void deleteItem(@PathVariable Long id, HttpServletRequest request) {
		RoleValidator.requireRole(request, "OWNER");
		service.deleteItem(id);
	}

}
