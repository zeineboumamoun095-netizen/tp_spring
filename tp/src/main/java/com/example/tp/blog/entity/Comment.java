package com.example.tp.blog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le texte du commentaire est obligatoire")
    @Size(min = 2, max = 1000, message = "Le commentaire doit avoir entre 2 et 1000 caractères")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    @NotBlank(message = "L'auteur est obligatoire")
    @Size(min = 2, max = 100)
    @Column(nullable = false)
    private String author;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Relation @ManyToOne avec Article
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Article article;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
