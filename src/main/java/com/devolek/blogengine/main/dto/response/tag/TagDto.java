package com.devolek.blogengine.main.dto.response.tag;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TagDto {
    private String name;
    private double weight;
}
