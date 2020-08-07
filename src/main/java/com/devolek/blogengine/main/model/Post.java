package com.devolek.blogengine.main.model;

import com.devolek.blogengine.main.enums.ModerationStatus;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;

@Data
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; //id поста

    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT")
    private int isActive; //скрыта или активна публикация: 0 или 1

    @Enumerated(EnumType.STRING)
    @Column(name = "moderation_status", columnDefinition = "ENUM('NEW', 'ACCEPTED', 'DECLINED') default 'NEW'")
    private ModerationStatus moderationStatus; //статус модерации, по умолчанию значение "NEW"

    @Column(name = "moderator_id")
    private Integer moderatorId; //ID пользователя-модератора, принявшего решение, или NULL

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.ALL)
    private User user; //автор поста

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar time; //дата и время публикации поста

    @Column(nullable = false)
    private String title; //заголовок поста

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text; //текст поста

    @Column(name = "view_count", nullable = false)
    private int viewCount; //количество просмотров поста

    @OneToMany(
            mappedBy = "post",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<PostComment> comments;

    @OneToMany(mappedBy = "post")
    private List<PostVote> postVotes;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "tag2Post",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;
}
