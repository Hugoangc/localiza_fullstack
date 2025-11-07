package com.practice.localiza.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class CartItemRequestDTO {
    private Long carId;
    private List<Long> accessoryIds;
}