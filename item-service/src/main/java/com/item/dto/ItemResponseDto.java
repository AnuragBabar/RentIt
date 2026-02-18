package com.item.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponseDto {

    private Long id;
    private String itemName;
    private String description;
    private double pricePerDay;
    private boolean available;
    private Long ownerId;
    private Long categoryId;
    private String imageUrl;
}
