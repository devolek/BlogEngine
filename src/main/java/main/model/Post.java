package main.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Calendar;

@Data
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

}
