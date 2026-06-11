package com.example.tp.blog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "articles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    @Size(min = 3, max = 200, message = "Le titre doit avoir entre 3 et 200 caractères")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Le contenu est obligatoire")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Relation @OneToMany avec Comment
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
