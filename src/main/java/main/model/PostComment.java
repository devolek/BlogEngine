package main.model;

import javax.persistence.*;
import java.util.Calendar;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
