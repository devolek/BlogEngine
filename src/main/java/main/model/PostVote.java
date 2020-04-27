package main.model;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "post_votes")
public class PostVote {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id; //id лайка/дизлайка

    @Column(name = "user_id", nullable = false)
    private int userId; //тот, кто поставил лайк / дизлайк

    @Column(name = "post_id", nullable = false)
    private int postId; //пост, которому поставлен лайк / дизлайк

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Calendar time; //дата и время лайка / дизлайка

    @Column(nullable = false, columnDefinition = "TINYINT")
    private int value; //лайк или дизлайк: 1 или -1

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
