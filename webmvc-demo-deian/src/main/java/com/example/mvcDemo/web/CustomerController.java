package com.example.mvcDemo.web;

import com.example.mvcDemo.model.Article;
import com.example.mvcDemo.service.ArticleService;
import com.example.mvcDemo.service.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.nio.file.FileSystemException;
import java.util.Optional;

@Controller
@RequestMapping("/customers")
@Slf4j
public class CustomerController {

    private static final String ARTICLES_FOLDER = "customers";

    @Autowired
    FileUtils fileUtils;

    @Autowired
    ArticleService articleService;

    @GetMapping
    public String getArticles(Model model) {
        model.addAttribute("articles", articleService.fetch());
        model.addAttribute("title", "articles");
        return ARTICLES_FOLDER + "/articles";
    }

    @GetMapping("/delete/{id}")
    public String deleteArticle(Model model, @PathVariable(value="id") String id) {
        articleService.delete(id);
        return "redirect:/articles";
    }

    @GetMapping("/create")
    public String getArticleForm(
            Model model,
            @ModelAttribute("article") Article article) {
        model.addAttribute("title", "createArticle");
        return ARTICLES_FOLDER + "/create";
    }

    @PostMapping("/create")
    public String createArticleForm(
            Model model,
            @Valid @ModelAttribute("article") Article article,
            final BindingResult bindingResult,
            @RequestParam(value = "cancel", required = false) String cancelBtn,
            @RequestParam("file") MultipartFile file) {
        if(cancelBtn != null) return "redirect:/articles";

        boolean wasBusted = false;
        if (bindingResult.hasErrors()) {
            model.addAttribute("title", "createArticle");
            wasBusted = true;
        }

        wasBusted = fileUtils.handleImageFile(file, wasBusted, model);

        if (wasBusted) return ARTICLES_FOLDER + "/create";
        article.setPictureUrl(file.getOriginalFilename());

        articleService.add(article);
        return "redirect:/articles";
    }

    @GetMapping("/edit/{id}")
    public String getArticle(Model model, @PathVariable(name = "id") String id) {

        Optional<Article> a = articleService.get(id);
        if(a.isPresent()) {
            model.addAttribute("article", a.get());
            model.addAttribute("title", "editArticle");
        } else {
            return "redirect:/articles";
        }

        return ARTICLES_FOLDER + "/edit";
    }

    @PostMapping("/edit/{id}")
    public String editArticle(
              Model model,
              @Valid @ModelAttribute("article") Article article,
              final BindingResult bindingResult,
              @RequestParam(value = "cancel", required = false) String cancelBtn,
              @RequestParam("file") MultipartFile file) {
        if(cancelBtn != null) return "redirect:/articles";

        boolean fieldErrors = false;
        if (bindingResult.hasErrors()) {
            model.addAttribute("title", "editArticle");
            fieldErrors = true;
        }

        boolean fileErrors = false;
        if(!file.isEmpty() && file.getOriginalFilename().length() > 0) {
            fileErrors = fileUtils.handleImageFile(file, fieldErrors, model);
            if (!fileErrors) article.setPictureUrl(file.getOriginalFilename());
        }

        if (fileErrors || fieldErrors) return ARTICLES_FOLDER + "/edit";

        articleService.update(article);
        return "redirect:/articles";
    }

    @ExceptionHandler ({FileSystemException.class})
    @Order(1)
    public ResponseEntity<String> handle(Exception ex) {
        log.error("Article Controller Error:",ex);
        return ResponseEntity.badRequest().body(ex.toString());
    }
}
