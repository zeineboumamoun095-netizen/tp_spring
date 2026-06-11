package com.example.tp.blog.service;

import com.example.tp.blog.entity.Article;
import com.example.tp.blog.entity.Comment;
import com.example.tp.blog.repository.ArticleRepository;
import com.example.tp.blog.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    // ── Articles ─────────────────────────────────────────────────────────────

    public List<Article> getAllArticles() {
        return articleRepository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<Article> getArticleById(Long id) {
        return articleRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Article> getArticleWithComments(Long id) {
        return articleRepository.findByIdWithComments(id);
    }

    public Article createArticle(Article article) {
        return articleRepository.save(article);
    }

    public Article updateArticle(Long id, Article updated) {
        Article existing = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article introuvable avec l'id : " + id));
        existing.setTitle(updated.getTitle());
        existing.setContent(updated.getContent());
        return articleRepository.save(existing);
    }

    public void deleteArticle(Long id) {
        if (!articleRepository.existsById(id)) {
            throw new RuntimeException("Article introuvable avec l'id : " + id);
        }
        articleRepository.deleteById(id);
    }

    // ── Commentaires ──────────────────────────────────────────────────────────

    public Comment addComment(Long articleId, Comment comment) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article introuvable avec l'id : " + articleId));
        comment.setArticle(article);
        return commentRepository.save(comment);
    }

    public void deleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new RuntimeException("Commentaire introuvable avec l'id : " + commentId);
        }
        commentRepository.deleteById(commentId);
    }

    public List<Comment> getCommentsByArticle(Long articleId) {
        return commentRepository.findByArticleIdOrderByCreatedAtDesc(articleId);
    }
}
