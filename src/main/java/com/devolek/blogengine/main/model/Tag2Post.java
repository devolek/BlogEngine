package com.devolek.blogengine.main.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tag2post")
public class Tag2Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; //id связи

    @Column(name = "post_id")
    private int postId; //id поста

    @Column(name = "tag_id")
    private int tagId; //id тэга

}
