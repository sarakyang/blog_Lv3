package com.sparta.blog.service;

import com.sparta.blog.dto.BoardRequestDto;
import com.sparta.blog.dto.BoardResponseDto;
import com.sparta.blog.entity.Board;
import com.sparta.blog.entity.Comment;
import com.sparta.blog.entity.User;
import com.sparta.blog.entity.UserRoleEnum;
import com.sparta.blog.repository.BoardRepository;
import com.sparta.blog.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BoardService {

    @Autowired
    private final BoardRepository boardRepository;

    @Autowired
    private final CommentRepository commentRepository;

    public BoardService(BoardRepository boardRepository, CommentRepository commentRepository) {
        this.boardRepository = boardRepository;
        this.commentRepository = commentRepository;
    }


    // 무슨 직렬화 어쩌구 저쩌구 그냥 프시록에서는 Json으로 반환하지 못하는 값을 반환하게 만들어줌,
    //spring.jackson.serialization.fail-on-empty-beans=false

    //원하는 값을 찾아야하는데 너무 싫다.


    // 전체 조회
    public Map<Long, BoardResponseDto> getBoard() {

        List<Board> board = boardRepository.findAllByOrderByModifiedAtDesc();
        Map<Long, BoardResponseDto> boardResponseDtos = new HashMap<>();
        for (Board board1 : board) {
            List<Comment> comments = commentRepository.findAllByBoardIdOrderByModifiedAt(board1.getId());
            BoardResponseDto boardResponseDto = new BoardResponseDto(board1, comments);
            boardResponseDtos.put(board1.getId(),boardResponseDto);
        }
        return boardResponseDtos;
    }

    //생성
    public BoardResponseDto createBoard(BoardRequestDto boardRequestDto, User user) {
        Board board = boardRepository.save(new Board(boardRequestDto, user));
        return new BoardResponseDto(board);
    }

    // 게시물 id로 조회
    public BoardResponseDto getBoardById(Long id) {
        Board board = boardRepository.findBoardById(id).orElseThrow(() -> new RuntimeException("게시물이 존재하지 않습니다"));
        List<Comment> comment = commentRepository.findAllByBoardIdOrderByModifiedAt(id);
        return new BoardResponseDto(board, comment);
    }

    // 수정
    @Transactional
    public ResponseEntity<String> updateBoard(Long id, BoardRequestDto boardRequestDto, User user) {
        Board board = findBoard(id);
        UserRoleEnum role = user.getRole();

        if(role == UserRoleEnum.ADMIN) {
            board.update(boardRequestDto, user);
            return ResponseEntity.status(200).body("상태코드 : " + HttpStatus.OK.value() + " 메세지 : 관리자 권한으로 게시물 수정 성공");
        }else if (!board.getUser().getUsername().equals(user.getUsername())) {
            return ResponseEntity.status(400).body("상태코드 : " + HttpStatus.BAD_REQUEST.value()  + " 메세지 : 당신의 게시물이 아닙니다.");
        } else{
            board.update(boardRequestDto, user);
            return ResponseEntity.status(200).body("상태코드 : " + HttpStatus.OK.value() + " 메세지 : 게시물 수정 성공");
        }
    }

    // 삭제
    public ResponseEntity<String> deleteBoard(Long id, User user) throws IllegalArgumentException {
        Board board = findBoard(id);
        UserRoleEnum role = user.getRole();

        if (role == UserRoleEnum.ADMIN) {
            boardRepository.delete(board);
            return ResponseEntity.status(200).body("상태코드 : " + HttpStatus.OK.value() + " 메세지 : 관리자 권한으로 게시물 삭제 성공");
        } else if(!board.getUser().getUsername().equals(user.getUsername())) {
            return ResponseEntity.status(400).body("상태코드 : " + HttpStatus.BAD_REQUEST.value() + " 메세지 : 당신의 게시물이 아닙니다.");
        } else {
            boardRepository.delete(board);
            return ResponseEntity.status(200).body("상태코드 : " + HttpStatus.OK.value() + " 메세지 : 게시물 삭제 성공");
        }

    }

    // DB에서 찾기
    private Board findBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("선택한 게시글이 없습니다."));
    }
}
