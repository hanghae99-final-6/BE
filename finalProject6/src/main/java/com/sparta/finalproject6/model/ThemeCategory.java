package com.sparta.finalproject6.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class ThemeCategory extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "THEME_CATEGORY_ID")
    @Id
    private Long id;

    private String themeCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private Post post;


    public ThemeCategory(String themeCategory, Post post) {
        this.themeCategory = themeCategory;
        this.post = post;
    }

}

