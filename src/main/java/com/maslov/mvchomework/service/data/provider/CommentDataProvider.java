package com.maslov.mvchomework.service.data.provider;

import com.maslov.mvchomework.domain.Comment;
import com.maslov.mvchomework.repository.CommentRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentDataProvider {

    private final CommentRepo commentRepo;

    public Comment create(Comment comment) {
        return commentRepo.save(comment);
    }
}
