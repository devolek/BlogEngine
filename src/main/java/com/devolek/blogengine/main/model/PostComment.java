package com.devolek.blogengine.main.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Calendar;

@Data
@Entity
@Table(name = "post_comments")
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; //id комментария

    @ManyToOne(cascade = CascadeType.ALL)
    private PostComment parent; //комментарий, на который оставлен этот комментарий (может быть NULL, если комментарий оставлен просто к посту)

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post; //пост, к которому написан комментарий

    @ManyToOne(cascade = CascadeType.ALL)
    private User user; //автор комментария

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Calendar time; //дата и время комментария

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text; //текст комментария

}
