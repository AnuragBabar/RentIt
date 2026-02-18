package com.item.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.item.dto.ItemRequestDto;
import com.item.dto.ItemResponseDto;
import com.item.entity.Item;
import com.item.exception.ItemAlreadyBookedException;
import com.item.exception.ItemNotFoundException;
import com.item.repository.ItemRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class ItemService implements ItemServiceInterface {

	private final ItemRepository itemRepo;
	private final ModelMapper modelMapper;

	private final String UPLOAD_DIR = "uploads/items/";

	@Override
	public ItemResponseDto addItem(ItemRequestDto dto) {
		Item item = new Item();
		item.setItemName(dto.getItemName());
		item.setDescription(dto.getDescription());
		item.setPricePerDay(dto.getPricePerDay());
		item.setOwnerId(dto.getOwnerId());
		item.setCategoryId(dto.getCategoryId());
		item.setImageUrl(dto.getImageUrl());
		item.setAvailable(true);

		Item saveItem = itemRepo.save(item);
		return modelMapper.map(saveItem, ItemResponseDto.class);
	}

	@Override
	public ItemResponseDto getItemById(Long id) {
		Item item = itemRepo.findById(id)
				.orElseThrow(() -> new ItemNotFoundException("Item not found with id " + id));
		return modelMapper.map(item, ItemResponseDto.class);
	}

	@Override
	public List<ItemResponseDto> getItemsByCategory(Long categoryId) {
		return itemRepo.findByCategoryId(categoryId)
				.stream()
				.map(item -> modelMapper.map(item, ItemResponseDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public List<ItemResponseDto> getItemsByOwner(Long ownerId) {
		return itemRepo.findByOwnerId(ownerId)
				.stream()
				.map(item -> modelMapper.map(item, ItemResponseDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public List<ItemResponseDto> getAvailableItems() {
		return itemRepo.findByAvailableTrue()
				.stream()
				.map(item -> modelMapper.map(item, ItemResponseDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public ItemResponseDto markItemAsBooked(Long itemId) {

		Item item = itemRepo.findById(itemId)
				.orElseThrow(() -> new ItemNotFoundException("Item not found"));

		if (!item.isAvailable()) {
			throw new ItemAlreadyBookedException("Item already booked");
		}

		item.setAvailable(false);
		return modelMapper.map(itemRepo.save(item), ItemResponseDto.class);
	}

	public ItemResponseDto markItemAsAvailable(Long id) {
		Item item = itemRepo.findById(id)
				.orElseThrow(() -> new ItemNotFoundException("Item not found"));

		item.setAvailable(true);
		return modelMapper.map(itemRepo.save(item), ItemResponseDto.class);
	}

	@Override
	public ItemResponseDto updateItem(Long id, ItemRequestDto dto) {
		Item item = itemRepo.findById(id)
				.orElseThrow(() -> new ItemNotFoundException("Item not found with id " + id));

		item.setItemName(dto.getItemName());
		item.setDescription(dto.getDescription());
		item.setPricePerDay(dto.getPricePerDay());
		item.setCategoryId(dto.getCategoryId());
		item.setImageUrl(dto.getImageUrl());

		// Note: ownerId typically shouldn't change, but can be added if needed

		Item updatedItem = itemRepo.save(item);

		return modelMapper.map(updatedItem, ItemResponseDto.class);
	}

	@Override
	public void deleteItem(Long id) {
		if (!itemRepo.existsById(id)) {
			throw new ItemNotFoundException("Item not found with id " + id);
		}
		itemRepo.deleteById(id);
	}

}
