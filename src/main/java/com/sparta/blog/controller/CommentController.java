package com.sparta.blog.controller;

import com.sparta.blog.dto.CommentRequestDto;
import com.sparta.blog.dto.CommentResponseDto;
import com.sparta.blog.security.UserDetailsImpl;
import com.sparta.blog.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CommentController {

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    private final CommentService commentService;

    @PostMapping("/comment")
    public CommentResponseDto create_com (@RequestBody CommentRequestDto commentRequestDto,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.create_com(commentRequestDto, userDetails.getUser());
    }

    @PutMapping("/comment/{id}")
    public ResponseEntity<String> update_com (@PathVariable Long id,
                                              @RequestBody CommentRequestDto commentRequestDto,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.updateComment(id,commentRequestDto,userDetails.getUser());
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.deleteComment(id, userDetails.getUser());
    }
}
