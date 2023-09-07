package com.sparta.blog.dto;

import com.sparta.blog.entity.Board;
import com.sparta.blog.repository.BoardRepository;
import lombok.Getter;

@Getter
public class CommentRequestDto {

    private Long board_id;

    private String contents;

}
