package org.services;


import org.entities.AllNews;
import org.repositories.AllNewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AllNewsService {
    @Autowired
    private AllNewsRepository allNewsRepository;

    public List<AllNews> getAllNews() {
        return allNewsRepository.findAll();
    }

    public AllNews getOneNews(long id) {
        Optional<AllNews> allNews = allNewsRepository.findById(id);
        if (allNews.isPresent()) {
            return allNews.get();
        }
        return null;
    }

    public void saveNews(AllNews allNews) {

        allNewsRepository.save(allNews);
    }





}
