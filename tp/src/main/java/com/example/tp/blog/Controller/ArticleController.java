package com.example.tp.blog.Controller;

import com.example.tp.blog.entity.Article;
import com.example.tp.blog.entity.Comment;
import com.example.tp.blog.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
@Tag(name = "Blog", description = "API de gestion des articles et commentaires")
public class ArticleController {

    private final ArticleService articleService;

    // ── GET /api/articles ────────────────────────────────────────────────────
    @GetMapping
    @Operation(summary = "Lister tous les articles")
    public ResponseEntity<List<Article>> getAllArticles() {
        return ResponseEntity.ok(articleService.getAllArticles());
    }

    // ── GET /api/articles/{id} ───────────────────────────────────────────────
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un article avec ses commentaires")
    public ResponseEntity<Article> getArticle(@PathVariable Long id) {
        return articleService.getArticleWithComments(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ── POST /api/articles ───────────────────────────────────────────────────
    @PostMapping
    @Operation(summary = "Créer un article")
    public ResponseEntity<Article> createArticle(@Valid @RequestBody Article article) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(articleService.createArticle(article));
    }

    // ── PUT /api/articles/{id} ───────────────────────────────────────────────
    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un article")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id,
                                                 @Valid @RequestBody Article article) {
        return ResponseEntity.ok(articleService.updateArticle(id, article));
    }

    // ── DELETE /api/articles/{id} ────────────────────────────────────────────
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un article")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }

    // ── POST /api/articles/{id}/comments ─────────────────────────────────────
    @PostMapping("/{id}/comments")
    @Operation(summary = "Ajouter un commentaire à un article")
    public ResponseEntity<Comment> addComment(@PathVariable Long id,
                                              @Valid @RequestBody Comment comment) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(articleService.addComment(id, comment));
    }

    // ── GET /api/articles/{id}/comments ──────────────────────────────────────
    @GetMapping("/{id}/comments")
    @Operation(summary = "Lister les commentaires d'un article")
    public ResponseEntity<List<Comment>> getComments(@PathVariable Long id) {
        return ResponseEntity.ok(articleService.getCommentsByArticle(id));
    }

    // ── DELETE /api/articles/comments/{commentId} ─────────────────────────────
    @DeleteMapping("/comments/{commentId}")
    @Operation(summary = "Supprimer un commentaire")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        articleService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
