package com.devolek.blogengine.main.service.impl;

import com.devolek.blogengine.main.dto.post.PostResponseFactory;
import com.devolek.blogengine.main.dto.post.request.*;
import com.devolek.blogengine.main.dto.post.response.PostCalendarResponse;
import com.devolek.blogengine.main.dto.profile.response.MyStatisticResponse;
import com.devolek.blogengine.main.dto.universal.*;
import com.devolek.blogengine.main.enums.ModerationStatus;
import com.devolek.blogengine.main.exeption.NotFoundException;
import com.devolek.blogengine.main.model.Post;
import com.devolek.blogengine.main.model.PostVote;
import com.devolek.blogengine.main.model.Tag;
import com.devolek.blogengine.main.model.User;
import com.devolek.blogengine.main.repo.PostRepository;
import com.devolek.blogengine.main.repo.PostVotesRepository;
import com.devolek.blogengine.main.repo.TagRepository;
import com.devolek.blogengine.main.service.PostService;
import com.devolek.blogengine.main.service.dao.GlobalSettingsDao;
import com.devolek.blogengine.main.service.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class PostServiceImpl implements PostService {
    private final PostVotesRepository postVotesRepository;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final GlobalSettingsDao globalSettingsDao;
    private final UserDao userDao;

    public PostServiceImpl(PostVotesRepository postVotesRepository,
                           PostRepository postRepository,
                           TagRepository tagRepository,
                           GlobalSettingsDao globalSettingsDao,
                           UserDao userDao) {
        this.postVotesRepository = postVotesRepository;
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.globalSettingsDao = globalSettingsDao;
        this.userDao = userDao;
    }

    @Override
    public Post findPostById(int id) {
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public CollectionResponse getPosts(PostListRequest request) {
        int count = postRepository.getAvailablePostCount();
        int page = request.getOffset() / request.getLimit();
        List<Post> posts = new ArrayList<>();
        switch (request.getMode()) {
            case "recent": {
                posts = postRepository.getRecentPosts(PageRequest.of(page, request.getLimit()));
                break;
            }
            case "popular": {
                posts = postRepository.getPopularPosts(PageRequest.of(page, request.getLimit()));
                break;
            }
            case "best": {
                posts = postRepository.getBestPosts(PageRequest.of(page, request.getLimit()));
                break;
            }
            case "early": {
                posts = postRepository.getEarlyPosts(PageRequest.of(page, request.getLimit()));
                break;
            }
            default:
                break;
        }
        return PostResponseFactory.getPostsList(posts, count);
    }

    @Override
    public CollectionResponse searchPosts(SearchPostRequest request) {
        int count = postRepository.searchCount(request.getQuery());
        int page = request.getOffset() / request.getLimit();
        List<Post> posts = postRepository.search(request.getQuery(), PageRequest.of(page, request.getLimit()));
        return PostResponseFactory.getPostsList(posts, count);
    }

    @Override
    public Response getPostById(int id) {
        Post post = postRepository.findById(id).orElse(null);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        if (currentUserEmail.equals("anonymousUser")) {
            post = postRepository.getActiveById(id);
        } else if (post != null && post.getUser().equals(userDao.findByEmail(currentUserEmail))) {
            return PostResponseFactory.getSinglePost(post);
        }

        if (post == null) {
            throw new NotFoundException("", "");
        }
        post.setViewCount(post.getViewCount() + 1);
        postRepository.save(post);
        return PostResponseFactory.getSinglePost(post);
    }

    @Override
    public CollectionResponse getPostsByDate(int offset, int limit, String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar dateFrom = Calendar.getInstance();
        Calendar dateTo = Calendar.getInstance();
        dateFrom.setTime(sdf.parse(date));
        dateFrom.add(Calendar.DAY_OF_MONTH, -1);
        dateTo.setTime(sdf.parse(date));
        dateTo.add(Calendar.DAY_OF_MONTH, 1);

        int page = offset / limit;
        int count = postRepository.getCountByDate(dateFrom, dateTo);
        List<Post> posts = postRepository.getByDate(dateFrom, dateTo, PageRequest.of(page, limit));
        return PostResponseFactory.getPostsList(posts, count);
    }

    @Override
    public CollectionResponse getPostsByTag(PostByTagRequest request) {
        int count = postRepository.getCountByTag(request.getTag());
        int page = request.getOffset() / request.getLimit();
        List<Post> posts = postRepository.getByTag(request.getTag(), PageRequest.of(page, request.getLimit()));
        return PostResponseFactory.getPostsList(posts, count);
    }

    @Override
    public CollectionResponse getPostsModeration(PostModerationRequest request, int moderatorId) {
        ModerationStatus status = ModerationStatus.NEW;
        Integer moderator = null;
        switch (request.getStatus()) {
            case "new": {
                status = ModerationStatus.NEW;
                break;
            }
            case "declined": {
                status = ModerationStatus.DECLINED;
                moderator = moderatorId;
                break;
            }
            case "accepted": {
                status = ModerationStatus.ACCEPTED;
                moderator = moderatorId;
                break;
            }
            default:
                break;
        }
        int count = postRepository.getCountModeration(status, moderator);
        int page = request.getOffset() / request.getLimit();
        List<Post> posts = postRepository.getModerationPosts(status, moderator, PageRequest.of(page, request.getLimit()));
        return PostResponseFactory.getPostsList(posts, count);
    }

    @Override
    public CollectionResponse getMyPosts(PostModerationRequest request, int userId) {
        ModerationStatus status = ModerationStatus.NEW;
        int isActive = 1;
        switch (request.getStatus()) {
            case "inactive": {
                isActive = 0;
                status = null;
            }
            case "pending": {
                break;
            }
            case "declined": {
                status = ModerationStatus.DECLINED;
                break;
            }
            case "published": {
                status = ModerationStatus.ACCEPTED;
                break;
            }

            default:
                break;
        }

        int count = postRepository.getCountMyPost(status, isActive, userId);
        int page = request.getOffset() / request.getLimit();
        List<Post> posts = postRepository.getMyPosts(status, isActive,
                userId, PageRequest.of(page, request.getLimit()));
        return PostResponseFactory.getPostsList(posts, count);
    }

    @Override
    public Response addPost(PostAddRequest request, int userId) {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(request.getTime() * 1000);

        Response error = checkPostRequest(request);
        if (error != null) {
            return error;
        }

        Post newPost = new Post();
        newPost.setTime(date.before(Calendar.getInstance()) ? Calendar.getInstance() : date);
        newPost.setIsActive(request.getActive());
        newPost.setTitle(request.getTitle());
        newPost.setText(request.getText());



        newPost.setModerationStatus(
                globalSettingsDao.getSettings().get("POST_PREMODERATION") ?
                        ModerationStatus.NEW : ModerationStatus.ACCEPTED);

        newPost.setViewCount(0);
        newPost.setUser(userDao.findById(userId));
        newPost.setComments(new ArrayList<>());
        newPost.setPostVotes(new ArrayList<>());
        newPost.setTags(getTagsByList(request.getTags()));

        postRepository.save(newPost);

        return new OkResponse();
    }

    @Override
    public Response likePost(int userId, int postId, int value) {
        Post post = postRepository.findById(postId).orElse(null);
        User user = userDao.findById(userId);
        PostVote vote = postVotesRepository.findByUserAndPost(user, post);
        if (vote != null) {
            if (vote.getValue() == value) {
                return new OkResponse(false);
            } else postVotesRepository.delete(vote);
        }

        vote = new PostVote();
        vote.setPost(post);
        vote.setUser(user);
        vote.setTime(Calendar.getInstance());
        vote.setValue(value);
        postVotesRepository.save(vote);
        return new OkResponse();
    }

    @Override
    public Response editPost(int userId, int postId, PostAddRequest request) {
        Post post = postRepository.findById(postId).orElse(null);
        User user = userDao.findById(userId);
        if (post == null || user == null) {
            return new OkResponse(false);
        }

        Response error = checkPostRequest(request);
        if (error != null) {
            return error;
        }

        if (user.getIsModerator() != 1) {
            post.setModerationStatus(
                    globalSettingsDao.getSettings().get("POST_PREMODERATION") ?
                            ModerationStatus.NEW : ModerationStatus.ACCEPTED);
        }
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(request.getTime() * 1000);

        post.setTime(date.before(Calendar.getInstance()) ? Calendar.getInstance() : date);
        post.setIsActive(request.getActive());
        post.setTitle(request.getTitle());
        post.setText(request.getText());
        post.setTags(getTagsByList(request.getTags()));
        postRepository.save(post);
        return new OkResponse();
    }

    @Override
    public int getPostCountWithTag(Tag tag) {
        return postRepository.getCountByTag(tag.getName());
    }

    public int countAvailablePosts() {
        return postRepository.getAvailablePostCount();
    }

    private Response checkPostRequest(PostAddRequest request) {
        HashMap<String, String> errors = new HashMap<>();
        if (request.getTitle().isEmpty()) {
            errors.put("title", "Заголовок не установлен");
        } else if (request.getTitle().length() < 10) {
            errors.put("title", "Заголовок слишком короткий");
        }

        if (request.getText().isEmpty()) {
            errors.put("text", "Текст публикации не установлен");
        } else if (request.getText().length() < 500) {
            errors.put("text", "Текст публикации слишком короткий");
        }
        if (errors.size() != 0) {
            return new ErrorResponse(errors);
        }
        return null;
    }

    @Override
    public Response addPostDecision(AddModerationRequest request, int userId) {
        Post post = findPostById(request.getPostId());
        if (post == null) {
            return new FalseResponse();
        }
        post.setModeratorId(userId);
        switch (request.getDecision()) {
            case "accept": {
                post.setModerationStatus(ModerationStatus.ACCEPTED);
                break;
            }
            case "decline": {
                post.setModerationStatus(ModerationStatus.DECLINED);
                break;
            }
        }
        postRepository.save(post);

        return new OkResponse();
    }

    @Override
    public Response getCalendar(Integer year) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar now = Calendar.getInstance();
        Calendar before = Calendar.getInstance();
        Calendar after = Calendar.getInstance();
        before.set(now.get(Calendar.YEAR) - 1, Calendar.DECEMBER, 31);
        after.set(now.get(Calendar.YEAR) + 1, Calendar.JANUARY, 1);

        if (year != null && now.get(Calendar.YEAR) >= year) {
            before.set(Calendar.YEAR, year - 1);
            after.set(Calendar.YEAR, year + 1);
        }

        List<Post> posts = postRepository.getAvailablePosts(before, after);
        List<Calendar> dates = postRepository.getAvailableDate();
        TreeSet<Integer> years = new TreeSet<>(Collections.reverseOrder());
        for (Calendar calendar : dates) {
            years.add(calendar.get(Calendar.YEAR));
        }

        if (posts == null) {
            posts = new ArrayList<>();
        }
        TreeMap<String, Integer> postList = new TreeMap<>();
        for (Post post : posts) {
            String postDate = sdf.format(post.getTime().getTime());
            int count = postList.getOrDefault(postDate, 0);
            postList.put(postDate, count + 1);
        }

        return new PostCalendarResponse(years, postList);
    }

    public List<Tag> getTagsByList(List<String> tags) {
        List<Tag> tagList = new ArrayList<>();
        if (tags.size() != 0) {            //если тегов нет в запросе, блок пропускается
            tags.forEach(tag -> {
                Tag postTag;
                if (tagRepository.existsByNameIgnoreCase(tag)) {
                    postTag = tagRepository.findFirstByNameIgnoreCase(tag);
                } else {
                    postTag = new Tag();
                    postTag.setName(tag);
                }
                tagList.add(postTag);
            });
        }
        return tagList;
    }

    @Override
    public Response getStatistic(Integer userId) {

        if (!globalSettingsDao.getSettings().get("STATISTICS_IS_PUBLIC") &&
                userDao.findById(userId).getIsModerator() != 1) {
            return new MyStatisticResponse(0,
                    0,
                    0,
                    0,
                    new Date());
        }
        List<Post> posts = postRepository.getAvailablePosts(null, null);
        return UserServiceImpl.getStatisticResponse(posts);
    }
}
