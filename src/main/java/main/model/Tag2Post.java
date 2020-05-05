package main.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tag2post")
public class Tag2Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id; //id связи

    @ManyToOne
    private Post post; //id поста

    @ManyToOne
    private Tag tag; //id тэга

}
