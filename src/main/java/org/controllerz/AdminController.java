package org.controllerz;


import org.entities.AllNews;
import org.services.AllNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path="/v1/admin")
public class AdminController {

    @Autowired
    private AllNewsService allNewsService;


    @GetMapping(path = "/news")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<AllNews>> getNews() {
        List<AllNews>allNews = allNewsService.getAllNews();
        return new ResponseEntity<>(allNews, HttpStatus.OK);
    }
}
