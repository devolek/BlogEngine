package com.devolek.blogengine.main.dto.tag.response;

import com.devolek.blogengine.main.dto.universal.Dto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TagResponse implements Dto {
    private String name;
    private double weight;
}
