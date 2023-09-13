package com.sparta.blog.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sparta.blog.entity.Board;
import com.sparta.blog.entity.Comment;
import jakarta.persistence.FetchType;
import lombok.Getter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.LazyCollection;
import org.springframework.context.annotation.Lazy;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BoardResponseDto {
    private Long id;
    private String username;
    private String title;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int good_num;

    private List<Comment> comments;


    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.contents = board.getContents();
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
        this.username = board.getUser().getUsername();
        this.good_num = board.getGood_num();
    }

    public BoardResponseDto(Board board1, List<Comment> comments) {
        this.id = board1.getId();
        this.title = board1.getTitle();
        this.contents = board1.getContents();
        this.createdAt = board1.getCreatedAt();
        this.modifiedAt = board1.getModifiedAt();
        this.username = board1.getUser().getUsername();
        this.good_num = board1.getGood_num();
        this.comments = comments;
    }
}
