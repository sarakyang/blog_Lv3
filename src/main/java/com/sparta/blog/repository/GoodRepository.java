package com.sparta.blog.repository;

import com.sparta.blog.entity.Good;
import com.sparta.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface GoodRepository extends JpaRepository <Good, Long> {

    List <Good> findAllByUser(User user);
}
