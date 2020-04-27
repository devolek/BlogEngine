package main.model;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id; //id поста

    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT")
    private int isActive; //скрыта или активна публикация: 0 или 1

    @Enumerated(EnumType.STRING)
    @Column(name = "moderation_status", columnDefinition = "ENUM('NEW', 'ACCEPTED', 'DECLINED') default 'NEW'")
    private ModerationStatus moderationStatus; //статус модерации, по умолчанию значение "NEW"

    @Column(name = "moderator_id")
    private int moderatorId; //ID пользователя-модератора, принявшего решение, или NULL

    @Column(name = "user_id", nullable = false)
    private int userId; //автор поста

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Calendar time; //дата и время публикации поста

    @Column(nullable = false)
    private String title; //заголовок поста

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text; //текст поста

    @Column(name = "view_count", nullable = false)
    private int viewCount; //количество просмотров поста

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public ModerationStatus getModerationStatus() {
        return moderationStatus;
    }

    public void setModerationStatus(ModerationStatus moderationStatus) {
        this.moderationStatus = moderationStatus;
    }

    public int getModeratorId() {
        return moderatorId;
    }

    public void setModeratorId(int moderatorId) {
        this.moderatorId = moderatorId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }
}
