package com.sparta.blog.service;

import com.sparta.blog.entity.Board;
import com.sparta.blog.entity.Comment;
import com.sparta.blog.entity.Good;
import com.sparta.blog.entity.User;
import com.sparta.blog.repository.BoardRepository;
import com.sparta.blog.repository.CommentRepository;
import com.sparta.blog.repository.GoodRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GoodService {

    private final BoardRepository boardRepository;
    private final GoodRepository goodRepository;
    private final CommentService commentService;
    private final CommentRepository commentRepository;


    public GoodService(BoardRepository boardRepository, GoodRepository goodRepository,
                       CommentService commentService, CommentRepository commentRepository) {
        this.boardRepository = boardRepository;
        this.goodRepository = goodRepository;
        this.commentService = commentService;
        this.commentRepository = commentRepository;
    }

    public ResponseEntity<String> good_push(Long id, User user) {
        Board board = commentService.findBoard(id);
        Good good = goodRepository.save(new Good(board, user));
        int good_num = board.getGood_num();

        if (findSize(user) >1  && good.getUser().equals(user)) {
            //동일한 유저일 경우 취소
            good_num--;
            if (good_num < 0 ) good_num = 0;
            board.setGood_num(good_num);
            boardRepository.save(board);
            goodRepository.deleteAll(findGood(user));

            return ResponseEntity.status(200).body("메세지 : 좋아요를 취소하셨습니다.");

        } else {
            //일반의 경우 좋아요 증가
            good_num++;
            board.setGood_num(good_num);
            boardRepository.save(board);

            return ResponseEntity.status(200).body("메세지 : 좋아요를 누르셨습니다.");
        }
    }

    public ResponseEntity<String> good_push_comment(Long id, User user) {
        Comment comment = commentService.findComment(id);
        Good good = goodRepository.save(new Good(comment, user)) ;
        int good_num = comment.getGood_num();

        if (findSize(user) >1  && good.getUser().equals(user)) {
            //동일한 유저일 경우 취소
            good_num--;
            if (good_num < 0 ) good_num = 0;
            comment.setGood_num(good_num);
            commentRepository.save(comment);
            goodRepository.deleteAll(findGood(user));

            return ResponseEntity.status(200).body("상태코드 : " + HttpStatus.OK.value() + "메세지 : 좋아요를 취소하셨습니다.");

        } else {
            //일반의 경우 좋아요 증가
            good_num++;
            comment.setGood_num(good_num);
            commentRepository.save(comment);

            return ResponseEntity.status(200).body("상태코드 : " + HttpStatus.OK.value() + "메세지 : 좋아요를 누르셨습니다.");
        }
    }


    private Long findSize(User user) { return (long) goodRepository.findAllByUser(user).size(); }

    private List findGood (User user) { return goodRepository.findAllByUser(user); }

}
