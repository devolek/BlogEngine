package com.devolek.blogengine.main.controller;


import com.devolek.blogengine.main.dto.post.request.PostListRequest;
import com.devolek.blogengine.main.dto.post.request.SearchPostRequest;
import com.devolek.blogengine.main.dto.post.response.PostResponseDto;
import com.devolek.blogengine.main.dto.universal.CollectionResponse;
import com.devolek.blogengine.main.dto.universal.Dto;
import com.devolek.blogengine.main.dto.user.response.UserDto;
import com.devolek.blogengine.main.enums.ModerationStatus;
import com.devolek.blogengine.main.model.Post;
import com.devolek.blogengine.main.model.PostVote;
import com.devolek.blogengine.main.model.Tag;
import com.devolek.blogengine.main.model.User;
import com.devolek.blogengine.main.repo.PostRepository;
import com.devolek.blogengine.main.repo.TagRepository;
import com.devolek.blogengine.main.repo.UserRepository;
import com.devolek.blogengine.main.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/post")
public class ApiPostController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final PostService postService;

    public ApiPostController(PostRepository postRepository, UserRepository userRepository,
                             TagRepository tagRepository, PostService postService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<?> getPosts(PostListRequest request) {
        return ResponseEntity.ok(postService.getPosts(request));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchPost(SearchPostRequest request) {
        return ResponseEntity.ok(postService.searchPosts(request));
    }

    @GetMapping("/{ID}")
    public ResponseEntity getPost(@PathVariable int ID) {
        Optional<Post> post = postRepository.findById(ID);
        return post.isPresent() ? new ResponseEntity(post, HttpStatus.OK) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/byDate")
    public Map<String, Object> getPostByDate(int offset, int limit, String date) throws ParseException {
        Iterable<Post> postIterable;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar time = Calendar.getInstance();
        time.setTime(sdf.parse(date));

        if (date != null && !date.isEmpty()) {
            postIterable = postRepository.findAllByTimeContaining(time);
        } else {
            postIterable = postRepository.findAll();
        }

        Map<String, Object> map = getVisiblePosts(postIterable);
        ArrayList<Post> posts = (ArrayList<Post>) map.get("posts");
        int count = (int) map.get("count");

        posts = new ArrayList<>(posts.subList(offset, Math.min(posts.size() - offset, limit)));
        HashMap<String, Object> model = new HashMap<>();
        model.put("count", count);
        model.put("posts", posts);
        return model;
    }

    @GetMapping("/byTag")
    public ResponseEntity<?> getPostByTag(int offset, int limit, Tag tag) {
        Iterable<Post> postIterable;
        if (tag != null) {
            postIterable = postRepository.findAllByTagsContains(tag);
        } else {
            postIterable = postRepository.findAll();
        }

        Map<String, Object> map = getVisiblePosts(postIterable);
        ArrayList<Post> posts = (ArrayList<Post>) map.get("posts");
        int count = (int) map.get("count");

        posts = new ArrayList<>(posts.subList(offset, Math.min(posts.size() - offset, limit)));

        List<Dto> responses = new ArrayList<>();
        posts.forEach(post -> {
            int postLikes = (int) post.getPostVotes().stream().filter(postVote -> postVote.getValue() == 1).count();
            int postDislikes = (int) post.getPostVotes().stream().filter(postVote -> postVote.getValue() == -1).count();
            responses.add(new PostResponseDto(post.getId(), post.getTime(),
                    new UserDto(post.getUser().getId(), post.getUser().getName()),
                    post.getTitle(), post.getText().substring(0, 50), postLikes,
                    postDislikes, post.getComments().size(), post.getViewCount()));
        });
        return ResponseEntity.ok(new CollectionResponse(count, responses));
    }

    @GetMapping("/moderation")
    public Map<String, Object> getPostModeration(int offset, int limit, ModerationStatus moderationStatus, int moderatorId) {
        Iterable<Post> postIterable = postRepository.findAllByModerationStatus(moderationStatus);

        Map<String, Object> map = getVisiblePosts(postIterable);
        ArrayList<Post> posts = (ArrayList<Post>) map.get("posts");
        int count = (int) map.get("count");

        posts = new ArrayList<>(posts.subList(offset, Math.min(posts.size() - offset, limit)));

        HashMap<String, Object> model = new HashMap<>();
        model.put("count", count);
        model.put("posts", posts);
        return model;
    }

    @GetMapping("/my")
    public Map<String, Object> getMyPosts(int offset, int limit, String status, int userId) {
        Optional<User> user = userRepository.findById(userId);
        ArrayList<Post> posts = new ArrayList<>();
        int count = 0;

        if (user.isPresent()) {
            Iterable<Post> postIterable = postRepository.findAllByUser(user.get());
            for (Post post : postIterable) {
                count++;
            }
            switch (status) {
                case "inactive": {
                    for (Post post : postIterable) {
                        if (post.getIsActive() == 0) {
                            posts.add(post);
                        }
                    }
                    break;
                }
                case "pending": {
                    for (Post post : postIterable) {
                        if (post.getIsActive() == 1 && post.getModerationStatus().equals(ModerationStatus.NEW)) {
                            posts.add(post);
                        }
                    }
                    break;
                }
                case "declined": {
                    for (Post post : postIterable) {
                        if (post.getIsActive() == 1 && post.getModerationStatus().equals(ModerationStatus.DECLINED)) {
                            posts.add(post);
                        }
                    }
                    break;
                }
                case "published": {
                    for (Post post : postIterable) {
                        if (post.getIsActive() == 1 && post.getModerationStatus().equals(ModerationStatus.ACCEPTED)) {
                            posts.add(post);
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
        HashMap<String, Object> model = new HashMap<>();
        posts = new ArrayList<>(posts.subList(offset, Math.min(posts.size() - offset, limit)));
        model.put("count", count);
        model.put("posts", posts);
        return model;
    }

    @PostMapping
    public Map<String, Object> addPost(User user, String time, int active, String title, String text, String tags) throws ParseException {
        HashMap<String, Object> model = new HashMap<>();
        HashMap<String, Object> errors = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse(time));

        if (title.isEmpty()) {
            errors.put("title", "Заголовок не установлен");
        } else if (title.length() < 10) {
            errors.put("title", "Заголовок слишком короткий");
        }

        if (text.isEmpty()) {
            errors.put("text", "Текст публикации не установлен");
        } else if (text.length() < 500) {
            errors.put("text", "Текст публикации слишком короткий");
        }

        if (!errors.isEmpty()) {
            model.put("result", false);
            model.put("errors", errors);
            return model;
        }

        Post newPost = new Post();
        newPost.setTime(calendar.before(Calendar.getInstance()) ? Calendar.getInstance() : calendar);
        newPost.setIsActive(active);
        newPost.setTitle(title);
        newPost.setText(text);
        newPost.setModerationStatus(ModerationStatus.NEW);
        newPost.setViewCount(0);
        newPost.setUser(user);

        ArrayList<Tag> tagList = new ArrayList<>();
        if (tags.contains(",")) {
            String[] tag = tags.split(",\\s+");
            for (String str : tag) {
                Optional<Tag> tagOptional = tagRepository.findByName(str);
                if (tagOptional.isPresent()) {
                    tagList.add(tagOptional.get());
                } else {
                    Tag newTag = new Tag();
                    newTag.setName(str);
                    tagList.add(tagRepository.save(newTag));
                }
            }
        } else {
            Optional<Tag> tagOptional = tagRepository.findByName(tags.trim());
            if (tagOptional.isPresent()) {
                tagList.add(tagOptional.get());
            } else {
                Tag newTag = new Tag();
                newTag.setName(tags.trim());
                tagList.add(tagRepository.save(newTag));
            }
        }

        newPost.setTags(tagList);
        postRepository.save(newPost);
        model.put("result", true);
        return model;
    }


    private Map<String, Object> getVisiblePosts(Iterable<Post> postIterable) {
        HashMap<String, Object> model = new HashMap<>();
        ArrayList<Post> posts = new ArrayList<>();
        int count = 0;
        for (Post post : postIterable) {
            if (post.getIsActive() == 1 &&
                    post.getModerationStatus().equals(ModerationStatus.ACCEPTED) &&
                    !post.getTime().after(Calendar.getInstance())) {
                posts.add(post);
                count++;
            }
        }
        model.put("count", count);
        model.put("posts", posts);
        return model;
    }
}
