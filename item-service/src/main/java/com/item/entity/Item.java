package com.item.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String itemName;
	private String description;
	private double pricePerDay;
	private boolean available;

	private Long ownerId;
	private Long categoryId;
	private String imageUrl;

}
