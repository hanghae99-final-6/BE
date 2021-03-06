package com.sparta.finalproject6.dto.requestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import lombok.*;


import java.util.List;

@Getter
@NoArgsConstructor
public class PostRequestDto {

    private String title;
    private String content;

    private String regionCategory;
    private String priceCategory;
    private List<ThemeCategoryDto> themeCategories;


    @Builder
    public PostRequestDto(String title, String content,String regionCategory, String priceCategory,List<ThemeCategoryDto> themeCategories) {
        this.title = title;
        this.content = content;
        this.regionCategory = regionCategory;
        this.priceCategory = priceCategory;
        this.themeCategories = themeCategories;
    }

}
