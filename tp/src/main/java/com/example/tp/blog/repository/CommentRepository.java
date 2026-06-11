package com.example.tp.blog.repository;

import com.example.tp.blog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByArticleIdOrderByCreatedAtDesc(Long articleId);
    long countByArticleId(Long articleId);
}
