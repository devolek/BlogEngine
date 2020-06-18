package com.devolek.blogengine.main.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Calendar;

@Data
@Entity
@Table(name = "post_votes")
public class PostVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; //id лайка/дизлайка

    @ManyToOne
    private User user; //тот, кто поставил лайк / дизлайк

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post; //пост, которому поставлен лайк / дизлайк

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Calendar time; //дата и время лайка / дизлайка

    @Column(nullable = false, columnDefinition = "TINYINT")
    private int value; //лайк или дизлайк: 1 или -1

}
