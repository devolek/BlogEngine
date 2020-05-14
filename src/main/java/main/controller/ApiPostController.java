package main.controller;

import main.enums.ModerationStatus;
import main.model.*;
import main.repo.PostRepository;
import main.repo.TagRepository;
import main.repo.UserRepository;
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
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TagRepository tagRepository;

    @GetMapping
    public PostResponse getPosts(int offset, int limit, String mode){
        Iterable<Post> postIterable = postRepository.findAll();
        Map<String, Object> map = getVisiblePosts(postIterable);
        ArrayList<Post> posts = (ArrayList<Post>) map.get("posts");
        int count = (int) map.get("count");

        switch (mode){
            case "recent" : {
                posts.sort((o1, o2) -> {
                    if (o1.getTime().before(o2.getTime())){
                        return -1;
                    }
                    if (o1.getTime().after(o2.getTime())){
                        return 1;
                    }
                        return 0;
                });
                break;
            }
            case "popular" : {
                posts.sort((Comparator.comparingInt(o -> o.getComments().size())));
                break;
            }
            case "best" : {
                posts.sort(((o1, o2) -> {
                    List<PostVote> o1voters = o1.getPostVotes();
                    List<PostVote> o2voters = o2.getPostVotes();
                    int o1Likes = 0;
                    int o2Likes = 0;
                    for (PostVote postVote : o1voters){
                        if (postVote.getValue() == 1){
                            o1Likes++;
                        }
                    }
                    for (PostVote postVote : o2voters){
                        if (postVote.getValue() == 1){
                            o2Likes++;
                        }
                    }
                    return Integer.compare(o1Likes, o2Likes);
                }));
                break;
            }
            case "early" : {
                posts.sort((o1, o2) -> {
                    if (o1.getTime().before(o2.getTime())){
                        return 1;
                    }
                    if (o1.getTime().after(o2.getTime())){
                        return -1;
                    }
                    return 0;
                });
                break;
            }
            default: break;
        }
        posts = new ArrayList<>(posts.subList(offset, Math.min(posts.size() - offset, limit)));

    return new PostResponse(count, posts);
    }

    @GetMapping("/search")
    public PostResponse searchPost(int offset, int limit, String query){
        Iterable<Post> postIterable;
        if (query != null && !query.isEmpty()) {
            postIterable = postRepository.findAllByTitleContainingOrTextContaining(query, query);
        }
        else {
            postIterable = postRepository.findAll();
        }

        Map<String, Object> map = getVisiblePosts(postIterable);
        ArrayList<Post> posts = (ArrayList<Post>) map.get("posts");
        int count = (int) map.get("count");

        postIterable.forEach(posts::add);

        posts = new ArrayList<>(posts.subList(offset, Math.min(posts.size() - offset, limit)));
        return new PostResponse(count, posts);
    }

    @GetMapping("/{ID}")
    public ResponseEntity getPost(@PathVariable int ID){
        Optional<Post> post = postRepository.findById(ID);
        return post.isPresent() ? new ResponseEntity(post, HttpStatus.OK) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/byDate")
    public Map<String, Object> getPostByDate(int offset, int limit, String date) throws ParseException {
        Iterable<Post> postIterable;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar time = Calendar.getInstance();
        time.setTime(sdf.parse(date));

        if (date != null && !date.isEmpty()){
            postIterable = postRepository.findAllByTimeContaining(time);
        }
        else {
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
    public PostResponse getPostByTag(int offset, int limit, Tag tag) {
        Iterable<Post> postIterable;
        if (tag != null){
            postIterable = postRepository.findAllByTagsContains(tag);
        }
        else {
            postIterable = postRepository.findAll();
        }

        Map<String, Object> map = getVisiblePosts(postIterable);
        ArrayList<Post> posts = (ArrayList<Post>) map.get("posts");
        int count = (int) map.get("count");

        posts = new ArrayList<>(posts.subList(offset, Math.min(posts.size() - offset, limit)));
        return new PostResponse(count, posts);
    }

    @GetMapping("/moderation")
    public Map<String, Object> getPostModeration(int offset, int limit, ModerationStatus moderationStatus, int moderatorId){
        Iterable<Post> postIterable = postRepository.findAllByModerationStatusAndModeratorId(moderationStatus, moderatorId);

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
            for (Post post : postIterable){
                count++;
            }
            switch (status){
                case "inactive" : {
                    for (Post post : postIterable){
                        if (post.getIsActive() == 0){
                            posts.add(post);
                        }
                    }
                    break;
                }
                case "pending" : {
                    for (Post post : postIterable){
                        if (post.getIsActive() == 1 && post.getModerationStatus().equals(ModerationStatus.NEW)){
                            posts.add(post);
                        }
                    }
                    break;
                }
                case "declined" : {
                    for (Post post : postIterable){
                        if (post.getIsActive() == 1 && post.getModerationStatus().equals(ModerationStatus.DECLINED)){
                            posts.add(post);
                        }
                    }
                    break;
                }
                case "published" : {
                    for (Post post : postIterable){
                        if (post.getIsActive() == 1 && post.getModerationStatus().equals(ModerationStatus.ACCEPTED)){
                            posts.add(post);
                        }
                    }
                    break;
                }
                default: break;
            }
        }
        HashMap<String, Object> model = new HashMap<>();
        posts = new ArrayList<>(posts.subList(offset, Math.min(posts.size() - offset, limit)));
        model.put("count", count);
        model.put("posts", posts);
        return model;
    }

    @PostMapping
    public Map<String, Object> addPost(User user, String time, int active, String title , String text, String tags) throws ParseException {
        HashMap<String, Object> model = new HashMap<>();
        HashMap<String, Object> errors = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse(time));

        if (title.isEmpty()){
            errors.put("title", "Заголовок не установлен");
        }
        else if (title.length() < 10){
            errors.put("title", "Заголовок слишком короткий");
        }

        if (text.isEmpty()){
            errors.put("text", "Текст публикации не установлен");
        }
        else if (text.length() < 500){
            errors.put("text", "Текст публикации слишком короткий");
        }

        if (!errors.isEmpty()){
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
        if (tags.contains(",")){
            String[] tag = tags.split(",\\s+");
            for (String str : tag){
            Optional<Tag> tagOptional = tagRepository.findByName(str);
            if (tagOptional.isPresent()){
                tagList.add(tagOptional.get());
            }
            else {
                Tag newTag = new Tag();
                newTag.setName(str);
                tagList.add(tagRepository.save(newTag));
            }
            }
        }
        else {
            Optional<Tag> tagOptional = tagRepository.findByName(tags.trim());
            if (tagOptional.isPresent()){
                tagList.add(tagOptional.get());
            }
            else {
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


    private Map<String, Object> getVisiblePosts(Iterable<Post> postIterable){
        HashMap<String, Object> model = new HashMap<>();
        ArrayList<Post> posts = new ArrayList<>();
        int count = 0;
        for (Post post : postIterable){
            if(post.getIsActive() == 1 &&
                    post.getModerationStatus().equals(ModerationStatus.ACCEPTED) &&
                    !post.getTime().after(Calendar.getInstance())){
                posts.add(post);
                count++;
            }
        }
        model.put("count", count);
        model.put("posts", posts);
        return model;
    }
}
