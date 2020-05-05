package main.controller;

import main.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

@RestController
public class ApiPostController {
    @Autowired
    private PostRepository postRepository;

    @GetMapping("/api/post/")
    public PostResponse getPosts(int offset, int limit, String mode){
        ArrayList<Post> posts = new ArrayList<>();
        Iterable<Post> postIterable = postRepository.findAll();
        int count = 0;
        for (Post post : postIterable){
            if(post.getIsActive() == 1 &&
                    post.getModerationStatus().equals(ModerationStatus.ACCEPTED) &&
                    post.getTime().before(Calendar.getInstance())){
                posts.add(post);
            }
            count++;
        }
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
}
