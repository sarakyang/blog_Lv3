package com.sparta.blog.controller;

import com.sparta.blog.security.UserDetailsImpl;
import com.sparta.blog.service.GoodService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GoodController {

    public GoodController(GoodService goodService) {
        this.goodService = goodService;
    }

    private final GoodService goodService;

    @PostMapping("/good/board/{Id}")
    public ResponseEntity<String> push_Like (@PathVariable Long Id,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails){
        return goodService.good_push(Id, userDetails.getUser());
    }

    @PostMapping("/good/comment/{Id}")
    public ResponseEntity<String> push_Like_comment (@PathVariable Long Id,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails){
        return goodService.good_push_comment(Id, userDetails.getUser());
    }



}
