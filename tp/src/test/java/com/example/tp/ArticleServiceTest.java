package com.example.tp;

import com.example.tp.blog.entity.Article;
import com.example.tp.blog.entity.Comment;
import com.example.tp.blog.repository.ArticleRepository;
import com.example.tp.blog.repository.CommentRepository;
import com.example.tp.blog.service.ArticleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitaires - ArticleService")
class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private ArticleService articleService;

    private Article sampleArticle;

    @BeforeEach
    void setUp() {
        sampleArticle = Article.builder()
                .id(1L)
                .title("Introduction à Spring Boot")
                .content("Contenu de l'article de test")
                .build();
    }

    @Test
    @DisplayName("createArticle() doit sauvegarder l'article")
    void createArticle_ShouldSave() {
        when(articleRepository.save(any(Article.class))).thenReturn(sampleArticle);

        Article result = articleService.createArticle(sampleArticle);

        assertThat(result.getTitle()).isEqualTo("Introduction à Spring Boot");
        verify(articleRepository).save(sampleArticle);
    }

    @Test
    @DisplayName("addComment() doit associer le commentaire à l'article")
    void addComment_ShouldLinkToArticle() {
        Comment comment = Comment.builder()
                .text("Super article !")
                .author("Ahmed")
                .build();

        when(articleRepository.findById(1L)).thenReturn(Optional.of(sampleArticle));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Comment result = articleService.addComment(1L, comment);

        assertThat(comment.getArticle()).isEqualTo(sampleArticle);
        verify(commentRepository).save(comment);
    }

    @Test
    @DisplayName("addComment() doit lever une exception si l'article n'existe pas")
    void addComment_WhenArticleNotFound_ShouldThrow() {
        when(articleRepository.findById(99L)).thenReturn(Optional.empty());

        Comment comment = Comment.builder().text("test").author("test").build();

        assertThatThrownBy(() -> articleService.addComment(99L, comment))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("deleteArticle() doit lever une exception si l'article n'existe pas")
    void deleteArticle_WhenNotFound_ShouldThrow() {
        when(articleRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> articleService.deleteArticle(99L))
                .isInstanceOf(RuntimeException.class);
    }
}
