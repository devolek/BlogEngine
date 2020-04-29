package main.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Calendar;

@Data
@Entity
@Table(name = "post_comments")
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id; //id комментария

    @Column(name = "parent_id")
    private int parentId; //комментарий, на который оставлен этот комментарий (может быть NULL, если комментарий оставлен просто к посту)

    @Column(name = "post_id", nullable = false)
    private int postId; //пост, к которому написан комментарий

    @Column(name = "user_id", nullable = false)
    private int userId; //автор комментария

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Calendar time; //дата и время комментария

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text; //текст комментария

}
