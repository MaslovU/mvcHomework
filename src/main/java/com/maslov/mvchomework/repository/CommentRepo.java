package com.maslov.mvchomework.repository;

import com.maslov.mvchomework.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepo extends JpaRepository<Comment, Long> {

}
