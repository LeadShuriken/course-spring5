package com.example.mvcDemo.web;

import com.example.mvcDemo.model.Customer;
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
    public String getCustomerForm(
            Model model,
            @ModelAttribute("customer") Customer customer) {
        model.addAttribute("title", "createCustomer");
        return CUSTOMERS_FOLDER + "/create";
    }

//    @PostMapping("/create")
//    public String createArticleForm(
//            Model model,
//            @Valid @ModelAttribute("customer") Customer customer,
//            final BindingResult bindingResult,
//            @RequestParam(value = "cancel", required = false) String cancelBtn,
//            @RequestParam("file") MultipartFile file) {
//        if(cancelBtn != null) return "redirect:/customer";
//
//        boolean wasBusted = false;
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("title", "createCustomer");
//            wasBusted = true;
//        }
//
//        wasBusted = fileUtils.handleImageFile(file, wasBusted, model);
//
//        if (wasBusted) return CUSTOMERS_FOLDER + "/create";
//
//        customer.setPictureUrl(file.getOriginalFilename());
//
//        customerService.add(customer);
//        return "redirect:/customers";
//    }
//
//    @GetMapping("/edit/{id}")
//    public String getArticle(Model model, @PathVariable(name = "id") String id) {
//
//        Optional<Cus> a = customerService.get(id);
//        if(a.isPresent()) {
//            model.addAttribute("article", a.get());
//            model.addAttribute("title", "editArticle");
//        } else {
//            return "redirect:/articles";
//        }
//
//        return CUSTOMERS_FOLDER + "/edit";
//    }
//
//    @PostMapping("/edit/{id}")
//    public String editArticle(
//              Model model,
//              @Valid @ModelAttribute("article") Article article,
//              final BindingResult bindingResult,
//              @RequestParam(value = "cancel", required = false) String cancelBtn,
//              @RequestParam("file") MultipartFile file) {
//        if(cancelBtn != null) return "redirect:/articles";
//
//        boolean fieldErrors = false;
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("title", "editArticle");
//            fieldErrors = true;
//        }
//
//        boolean fileErrors = false;
//        if(!file.isEmpty() && file.getOriginalFilename().length() > 0) {
//            fileErrors = fileUtils.handleImageFile(file, fieldErrors, model);
//            if (!fileErrors) article.setPictureUrl(file.getOriginalFilename());
//        }
//
//        if (fileErrors || fieldErrors) return CUSTOMERS_FOLDER + "/edit";
//
//        articleService.update(article);
//        return "redirect:/articles";
//    }
//
//    @ExceptionHandler ({FileSystemException.class})
//    @Order(1)
//    public ResponseEntity<String> handle(Exception ex) {
//        log.error("Article Controller Error:",ex);
//        return ResponseEntity.badRequest().body(ex.toString());
//    }
}
