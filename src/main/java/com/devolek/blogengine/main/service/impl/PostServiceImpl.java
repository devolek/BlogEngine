package com.devolek.blogengine.main.service.impl;

import com.devolek.blogengine.main.dto.request.post.*;
import com.devolek.blogengine.main.dto.response.post.PostCalendarResponse;
import com.devolek.blogengine.main.dto.response.post.PostFullResponse;
import com.devolek.blogengine.main.dto.response.post.PostResponseFactory;
import com.devolek.blogengine.main.dto.response.profile.MyStatisticResponse;
import com.devolek.blogengine.main.dto.response.universal.*;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PostServiceImpl implements PostService {
    private final PostVotesRepository postVotesRepository;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final GlobalSettingsDao globalSettingsDao;
    private final UserDao userDao;

    @Value("${post.text.minLength}")
    private int textMinLength;
    @Value("${post.title.minLength}")
    private int titleMinLength;


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
    public PostListResponse getPosts(PostListRequest request) {
        int count = 0;
        int page = request.getOffset() / request.getLimit();
        List<Post> posts = new ArrayList<>();
        switch (request.getMode()) {
            case "recent": {
                Page<Post> postsPage = postRepository.getRecentPosts(PageRequest.of(page, request.getLimit()));
                count = (int) postsPage.getTotalElements();
                posts = postsPage.get().collect(Collectors.toList());
                break;
            }
            case "popular": {
                Page<Post> postsPage = postRepository.getPopularPosts(PageRequest.of(page, request.getLimit()));
                count = (int) postsPage.getTotalElements();
                posts = postsPage.get().collect(Collectors.toList());
                break;
            }
            case "best": {
                Page<Post> postsPage = postRepository.getBestPosts(PageRequest.of(page, request.getLimit()));
                count = (int) postsPage.getTotalElements();
                posts = postsPage.get().collect(Collectors.toList());
                break;
            }
            case "early": {
                Page<Post> postsPage = postRepository.getEarlyPosts(PageRequest.of(page, request.getLimit()));
                count = (int) postsPage.getTotalElements();
                posts = postsPage.get().collect(Collectors.toList());
                break;
            }
            default:
                break;
        }
        return PostResponseFactory.getPostsList(posts, count);
    }

    @Override
    public PostListResponse searchPosts(SearchPostRequest request) {
        int page = request.getOffset() / request.getLimit();
        Page<Post> postsPage = postRepository.search(request.getQuery(), PageRequest.of(page, request.getLimit()));
        int count = (int) postsPage.getTotalElements();
        List<Post> posts = postsPage.get().collect(Collectors.toList());
        return PostResponseFactory.getPostsList(posts, count);
    }

    @Override
    public PostFullResponse getPostById(int id) {
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
    public PostListResponse getPostsByDate(int offset, int limit, String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar dateFrom = Calendar.getInstance();
        Calendar dateTo = Calendar.getInstance();
        dateFrom.setTime(sdf.parse(date));
        dateFrom.add(Calendar.DAY_OF_MONTH, -1);
        dateTo.setTime(sdf.parse(date));
        dateTo.add(Calendar.DAY_OF_MONTH, 1);

        int page = offset / limit;
        Page<Post> postsPage = postRepository.getByDate(dateFrom, dateTo, PageRequest.of(page, limit));
        int count = (int) postsPage.getTotalElements();
        List<Post> posts = postsPage.get().collect(Collectors.toList());
        return PostResponseFactory.getPostsList(posts, count);
    }

    @Override
    public PostListResponse getPostsByTag(PostByTagRequest request) {
        int page = request.getOffset() / request.getLimit();
        Page<Post> postsPage = postRepository.getByTag(request.getTag(), PageRequest.of(page, request.getLimit()));
        int count = (int) postsPage.getTotalElements();
        List<Post> posts = postsPage.get().collect(Collectors.toList());
        return PostResponseFactory.getPostsList(posts, count);
    }

    @Override
    public PostListResponse getPostsModeration(PostModerationRequest request, int moderatorId) {
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
        int page = request.getOffset() / request.getLimit();
        Page<Post> postsPage = postRepository.getModerationPosts(status, moderator, PageRequest.of(page, request.getLimit()));
        int count = (int) postsPage.getTotalElements();
        List<Post> posts = postsPage.get().collect(Collectors.toList());
        return PostResponseFactory.getPostsList(posts, count);
    }

    @Override
    public PostListResponse getMyPosts(PostModerationRequest request, int userId) {
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

        int page = request.getOffset() / request.getLimit();
        Page<Post> postsPage = postRepository.getMyPosts(status, isActive,
                userId, PageRequest.of(page, request.getLimit()));
        int count = (int) postsPage.getTotalElements();
        List<Post> posts = postsPage.get().collect(Collectors.toList());
        return PostResponseFactory.getPostsList(posts, count);
    }

    @Override
    public Response addPost(PostAddRequest request, int userId) {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(request.getTimestamp() * 1000);

        Response error = checkPostRequest(request);
        if (error != null) {
            return error;
        }

        Post newPost = new Post();
        newPost.setTime(date.before(Calendar.getInstance()) ?
                Calendar.getInstance() : date);
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

        return UniversalResponseFactory.getTrueResponse();
    }

    @Override
    public Response likePost(int userId, int postId, int value) {
        Post post = postRepository.findById(postId).orElse(null);
        User user = userDao.findById(userId);
        PostVote vote = postVotesRepository.findByUserAndPost(user, post);
        if (vote != null) {
            if (vote.getValue() == value) {
                return new SimpleResponse(false);
            } else postVotesRepository.delete(vote);
        }

        vote = new PostVote();
        vote.setPost(post);
        vote.setUser(user);
        vote.setTime(Calendar.getInstance());
        vote.setValue(value);
        postVotesRepository.save(vote);
        return UniversalResponseFactory.getTrueResponse();
    }

    @Override
    public Response editPost(int userId, int postId, PostAddRequest request) {
        Post post = postRepository.findById(postId).orElse(null);
        User user = userDao.findById(userId);
        if (post == null || user == null) {
            return new SimpleResponse(false);
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
        date.setTimeInMillis(request.getTimestamp() * 1000);

        post.setTime(date.before(Calendar.getInstance()) ?
                Calendar.getInstance() : date);
        post.setIsActive(request.getActive());
        post.setTitle(request.getTitle());
        post.setText(request.getText());
        post.setTags(getTagsByList(request.getTags()));
        postRepository.save(post);
        return UniversalResponseFactory.getTrueResponse();
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
        } else if (request.getTitle().length() < titleMinLength) {
            errors.put("title", "Заголовок слишком короткий, введите не менее " + titleMinLength + "символов");
        }

        if (request.getText().isEmpty()) {
            errors.put("text", "Текст публикации не установлен");
        } else if (request.getText().length() < textMinLength) {
            errors.put("text", "Текст публикации слишком короткий, введите не менее " + textMinLength + " символов");
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
            return UniversalResponseFactory.getFalseResponse();
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

        return UniversalResponseFactory.getTrueResponse();
    }

    @Override
    public PostCalendarResponse getCalendar(Integer year) {
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
    public MyStatisticResponse getStatistic(Integer userId) {

        if (!globalSettingsDao.getSettings().get("STATISTICS_IS_PUBLIC") &&
                userDao.findById(userId).getIsModerator() != 1) {
            return new MyStatisticResponse(0,
                    0,
                    0,
                    0,
                    Calendar.getInstance());
        }
        List<Post> posts = postRepository.getAvailablePosts(null, null);
        return UserServiceImpl.getStatisticResponse(posts);
    }
}
