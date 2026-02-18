package com.item.service;

import java.util.List;

import com.item.dto.ItemRequestDto;
import com.item.dto.ItemResponseDto;

public interface ItemServiceInterface {

	ItemResponseDto addItem(ItemRequestDto dto);

	ItemResponseDto getItemById(Long id);

	List<ItemResponseDto> getItemsByCategory(Long categoryId);

	List<ItemResponseDto> getItemsByOwner(Long ownerId);

	List<ItemResponseDto> getAvailableItems();

	public ItemResponseDto markItemAsBooked(Long itemId);

	ItemResponseDto updateItem(Long id, ItemRequestDto dto);

	void deleteItem(Long id);
}
