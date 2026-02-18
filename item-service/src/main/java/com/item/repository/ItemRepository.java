package com.item.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.item.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
	
	List<Item> findByCategoryId(Long categoryId);

    List<Item> findByOwnerId(Long ownerId);

    List<Item> findByAvailableTrue();
}
