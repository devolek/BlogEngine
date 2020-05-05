package main.model;

import lombok.Data;

import java.util.List;

@Data
public class PostResponse {
    private int count;
    private List<Post> posts;

    public PostResponse(int count, List<Post> posts) {
        this.count = count;
        this.posts = posts;
    }
}
