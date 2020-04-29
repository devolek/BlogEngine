package main.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id; //id тэга

    @Column(nullable = false)
    private String name; //текст тэга

}
