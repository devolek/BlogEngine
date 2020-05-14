package main.controller;

import main.enums.ModerationStatus;
import main.model.GlobalSetting;
import main.model.Post;
import main.model.Tag;
import main.repo.GlobalSettingRepository;
import main.repo.PostRepository;
import main.repo.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class ApiGeneralController {
    @Autowired
    private GlobalSettingRepository globalSettingRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private PostRepository postRepository;

    @GetMapping("/api/init")
    public Map<String, Object> getInit(){
        HashMap<String, Object> model = new HashMap<>();
        model.put("title", "DevPub");
        model.put("subtitle", "Рассказы разработчиков");
        model.put("phone", "+7 965 787-12-34");
        model.put("email", "mail@mail.ru");
        model.put("copyright", "Наумов Валентин");
        model.put("copyrightFrom", "2020");
        return model;
    }

    @GetMapping("/api/settings")
    public Map<String, Object> getSettings(){
        HashMap<String, Object> model = new HashMap<>();
        Iterable<GlobalSetting> iterable = globalSettingRepository.findAll();
        for (GlobalSetting globalSetting : iterable){
            model.put(globalSetting.getCode(), globalSetting.getValue().equals("YES"));
        }
        return model;
    }

    @PutMapping("/api/settings")
    public void saveSettings(@RequestBody Map<String, Object> model){
        for (String code : model.keySet()){
            Optional<GlobalSetting> setting = globalSettingRepository.findByCode(code);
            if (setting.isPresent()){
                GlobalSetting globalSetting = setting.get();
                globalSetting.setValue(model.get(code).equals(true) ? "YES" : "NO");
                globalSettingRepository.save(globalSetting);
            }
        }
    }

    @GetMapping("/api/tag")
    public Map<String, Object> getTags(String query){
        ArrayList<Map<String, Object>> tagList = new ArrayList<>();
        HashMap<String, Object> model = new HashMap<>();

        Iterable<Tag> tags;
        if (query == null || query.isEmpty()){
            tags = tagRepository.findAll();
        }
        else {
            tags = tagRepository.findAllByNameStartsWith(query);
        }

        Iterable<Post> postIterable = postRepository.findAll();
        int postCount = 0;
        for (Post post : postIterable){
            if(post.getIsActive() == 1 &&
                    post.getModerationStatus().equals(ModerationStatus.ACCEPTED) &&
                    !post.getTime().after(Calendar.getInstance())){
                postCount++;
            }
        }
        if (postCount == 0){
            model.put("tags", tagList);
            return model;
        }
        int maxWeight = 0;

        for (Tag tag : tagRepository.findAll()){
            int postWithTag = postRepository.findAllByTagsContains(tag).size();
            int tagWeight = postWithTag/postCount;
            maxWeight = Math.max(maxWeight, tagWeight);
        }

        for (Tag tag : tags){
            int postWithTag = postRepository.findAllByTagsContains(tag).size();
            int tagWeight = (postWithTag/postCount)/maxWeight;
            HashMap<String, Object> tagWithWeight = new HashMap<>();
            tagWithWeight.put("name", tag.getName());
            tagWithWeight.put("weight", tagWeight);
            tagList.add(tagWithWeight);
        }
        model.put("tags", tagList);
        return model;
    }

}
