package com.maslov.mvchomework.service;

import com.maslov.mvchomework.domain.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> createComment();

    List<Comment> updateComment();

    List<Comment> deleteComment();
}
