package com.example.tp.blog.Controller;

import com.example.tp.blog.entity.Article;
import com.example.tp.blog.entity.Comment;
import com.example.tp.blog.service.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleWebController {

    private final ArticleService articleService;

    // ── Liste des articles ───────────────────────────────────────────────────
    @GetMapping
    public String listArticles(Model model) {
        model.addAttribute("articles", articleService.getAllArticles());
        return "articles/list";
    }

    // ── Détail d'un article + commentaires ───────────────────────────────────
    @GetMapping("/{id}")
    public String viewArticle(@PathVariable Long id, Model model) {
        Article article = articleService.getArticleWithComments(id)
                .orElseThrow(() -> new RuntimeException("Article introuvable"));
        model.addAttribute("article", article);
        model.addAttribute("newComment", new Comment());
        return "articles/detail";
    }

    // ── Formulaire de création ───────────────────────────────────────────────
    @GetMapping("/new")
    public String newArticleForm(Model model) {
        model.addAttribute("article", new Article());
        return "articles/form";
    }

    // ── Sauvegarder un article ───────────────────────────────────────────────
    @PostMapping("/save")
    public String saveArticle(@Valid @ModelAttribute Article article,
                              BindingResult result,
                              RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            return "articles/form";
        }
        articleService.createArticle(article);
        redirectAttrs.addFlashAttribute("successMessage", "Article publié avec succès !");
        return "redirect:/articles";
    }

    // ── Formulaire d'édition ─────────────────────────────────────────────────
    @GetMapping("/edit/{id}")
    public String editArticleForm(@PathVariable Long id, Model model) {
        Article article = articleService.getArticleById(id)
                .orElseThrow(() -> new RuntimeException("Article introuvable"));
        model.addAttribute("article", article);
        return "articles/form";
    }

    // ── Mettre à jour ────────────────────────────────────────────────────────
    @PostMapping("/update/{id}")
    public String updateArticle(@PathVariable Long id,
                                @Valid @ModelAttribute Article article,
                                BindingResult result,
                                RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            return "articles/form";
        }
        articleService.updateArticle(id, article);
        redirectAttrs.addFlashAttribute("successMessage", "Article mis à jour !");
        return "redirect:/articles";
    }

    // ── Supprimer un article ─────────────────────────────────────────────────
    @GetMapping("/delete/{id}")
    public String deleteArticle(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        articleService.deleteArticle(id);
        redirectAttrs.addFlashAttribute("successMessage", "Article supprimé !");
        return "redirect:/articles";
    }

    // ── Ajouter un commentaire ───────────────────────────────────────────────
    @PostMapping("/{id}/comment")
    public String addComment(@PathVariable Long id,
                             @Valid @ModelAttribute("newComment") Comment comment,
                             BindingResult result,
                             RedirectAttributes redirectAttrs,
                             Model model) {
        if (result.hasErrors()) {
            Article article = articleService.getArticleWithComments(id).orElseThrow();
            model.addAttribute("article", article);
            return "articles/detail";
        }
        articleService.addComment(id, comment);
        redirectAttrs.addFlashAttribute("successMessage", "Commentaire ajouté !");
        return "redirect:/articles/" + id;
    }

    // ── Supprimer un commentaire ─────────────────────────────────────────────
    @GetMapping("/{articleId}/comment/delete/{commentId}")
    public String deleteComment(@PathVariable Long articleId,
                                @PathVariable Long commentId,
                                RedirectAttributes redirectAttrs) {
        articleService.deleteComment(commentId);
        redirectAttrs.addFlashAttribute("successMessage", "Commentaire supprimé !");
        return "redirect:/articles/" + articleId;
    }
}
