package com.sparta.blog.service;

import com.sparta.blog.dto.CommentRequestDto;
import com.sparta.blog.dto.CommentResponseDto;
import com.sparta.blog.entity.Board;
import com.sparta.blog.entity.Comment;
import com.sparta.blog.entity.User;
import com.sparta.blog.entity.UserRoleEnum;
import com.sparta.blog.repository.BoardRepository;
import com.sparta.blog.repository.CommentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

    public CommentService(CommentRepository commentRepository, BoardRepository boardRepository) {
        this.commentRepository = commentRepository;
        this.boardRepository = boardRepository;
    }

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;


    public CommentResponseDto create_com(CommentRequestDto commentRequestDto, User user) {
        Board board = findBoard(commentRequestDto.getBoard_id());
        Comment comment = commentRepository.save(new Comment(commentRequestDto,user,board));
        return new CommentResponseDto(comment);
    }

    // 수정
    @Transactional
    public ResponseEntity<String> updateComment(Long id, CommentRequestDto commentRequestDto, User user) {
        Comment comment = findComment(id);
        UserRoleEnum role = user.getRole();

        if(role == UserRoleEnum.ADMIN) {
            comment.update(commentRequestDto, user);
            return ResponseEntity.status(200).body("상태코드 : " + HttpStatus.OK.value() + " 메세지 : 관리자 권한으로 댓글 수정 성공");
        }else if (!comment.getUser().getUsername().equals(user.getUsername())) {
            return ResponseEntity.status(400).body("상태코드 : " + HttpStatus.BAD_REQUEST.value()  + " 메세지 : 당신의 댓글이 아닙니다.");
        } else{
            comment.update(commentRequestDto, user);
            return ResponseEntity.status(200).body("상태코드 : " + HttpStatus.OK.value() + " 메세지 : 게시물 수정 성공");
        }
    }

    // 삭제
    public ResponseEntity<String> deleteComment(Long id, User user) throws IllegalArgumentException {
        Comment comment = findComment(id);
        UserRoleEnum role = user.getRole();

        if (role == UserRoleEnum.ADMIN) {
            commentRepository.delete(comment);
            return ResponseEntity.status(200).body("상태코드 : " + HttpStatus.OK.value() + " 메세지 : 관리자 권한으로 댓글 삭제 성공");
        } else if(!comment.getUser().getUsername().equals(user.getUsername())) {
            return ResponseEntity.status(400).body("상태코드 : " + HttpStatus.BAD_REQUEST.value() + " 메세지 : 당신 댓글이 아닙니다.");
        } else {
            commentRepository.delete(comment);
            return ResponseEntity.status(200).body("상태코드 : " + HttpStatus.OK.value() + " 메세지 : 댓글 삭제 성공");
        }

    }


    public Board findBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("선택한 게시글이 없습니다."));
    }

    public Comment findComment(Long id) {
        return commentRepository.findById(id).orElseThrow(() ->new IllegalArgumentException("선택한 댓글이 없습니다."));
    }
}
