package main.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Calendar;

@Data
@Entity
@Table(name = "post_votes")
public class PostVote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id; //id лайка/дизлайка

    @ManyToOne
    private User user; //тот, кто поставил лайк / дизлайк

    @ManyToOne(cascade = CascadeType.ALL)
    private Post post; //пост, которому поставлен лайк / дизлайк

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Calendar time; //дата и время лайка / дизлайка

    @Column(nullable = false, columnDefinition = "TINYINT")
    private int value; //лайк или дизлайк: 1 или -1

}
