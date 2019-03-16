package com.example.mvcDemo.web;

import com.example.mvcDemo.model.Article;
import com.example.mvcDemo.model.Customer;
import com.example.mvcDemo.service.ArticleService;
import com.example.mvcDemo.service.CustomerService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/customers")
@Slf4j
public class CustomerController {

    private static final String CUSTOMERS_FOLDER = "customers";

    @Autowired
    FileUtils fileUtils;

    @Autowired
    CustomerService customerService;

    @Autowired
    ArticleService articleService;

    @GetMapping
    public String getCustomers(Model model) {
        model.addAttribute("customers", customerService.fetch());
        model.addAttribute("title", "customers");
        return CUSTOMERS_FOLDER + "/customers";
    }

    @GetMapping("/delete/{id}")
    public String deleteCustomer(Model model, @PathVariable(value="id") String id) {
        customerService.delete(id);
        return "redirect:/customers";
    }

    @GetMapping("/create")
    public String getCustomerForm(Model model,
                                  @ModelAttribute("customer") Customer customer) {
        model.addAttribute("title", "createCustomer");
        model.addAttribute("articles", articleService.fetch());
        return CUSTOMERS_FOLDER + "/create";
    }

    @PostMapping("/create")
    public String createCustomerForm(
            Model model,
            @Valid @ModelAttribute("customer") Customer customer,
            final BindingResult bindingResult,
            @RequestParam(value = "articles", required = false) String[] articles,
            @RequestParam(value = "cancel", required = false) String cancelBtn,
            @RequestParam("file") MultipartFile file) {
        if(cancelBtn != null) return "redirect:/customers";

        boolean wasBusted = false;
        if (bindingResult.hasErrors()) {
            model.addAttribute("title", "createCustomer");
            wasBusted = true;
        }

        wasBusted = fileUtils.handleImageFile(file, wasBusted, model);

        if (wasBusted) return CUSTOMERS_FOLDER + "/create";

        customer.setPictureUrl(file.getOriginalFilename());


        List<Article> customerArticles = new ArrayList<>();
        if(articles != null) {
            for (int i = 0; i < articles.length; i++) {
                Optional<Article> a = articleService.get(articles[i]);
                if (a.isPresent()) {
                    customerArticles.add(a.get());
                }
            }
        }
        customer.setArticles(customerArticles);
        customerService.add(customer);
        return "redirect:/customers";
    }

    @GetMapping("/edit/{id}")
    public String getCustomer(Model model, @PathVariable(name = "id") String id) {

        Optional<Customer> a = customerService.get(id);
        if(a.isPresent()) {
            model.addAttribute("customer", a.get());
            model.addAttribute("articles", articleService.fetch());
            model.addAttribute("title", "editCustomer");
        } else {
            return "redirect:/customers";
        }

        return CUSTOMERS_FOLDER + "/edit";
    }

    @PostMapping("/edit/{id}")
    public String editCustomer(
              Model model,
              @Valid @ModelAttribute("customer") Customer customer,
              @RequestParam(value = "articles", required = false) String[] articles,
              final BindingResult bindingResult,
              @RequestParam(value = "cancel", required = false) String cancelBtn,
              @RequestParam("file") MultipartFile file) {
        if(cancelBtn != null) return "redirect:/customers";

        boolean fieldErrors = false;
        if (bindingResult.hasErrors()) {
            model.addAttribute("title", "editCustomer");
            fieldErrors = true;
        }

        boolean fileErrors = false;
        if(!file.isEmpty() && file.getOriginalFilename().length() > 0) {
            fileErrors = fileUtils.handleImageFile(file, fieldErrors, model);
            if (!fileErrors) customer.setPictureUrl(file.getOriginalFilename());
        }

        if (fileErrors || fieldErrors) return CUSTOMERS_FOLDER + "/edit";

        List<Article> customerArticles = new ArrayList<>();
        if(articles != null) {
            for (int i = 0; i < articles.length; i++) {
                Optional<Article> a = articleService.get(articles[i]);
                if (a.isPresent()) {
                    customerArticles.add(a.get());
                }
            }
        }
        customer.setArticles(customerArticles);
        customerService.update(customer);
        return "redirect:/customers";
    }
}
