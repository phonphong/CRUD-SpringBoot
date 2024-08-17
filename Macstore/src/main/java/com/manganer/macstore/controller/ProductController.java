package com.manganer.macstore.controller;

import com.manganer.macstore.entity.Product;
import com.manganer.macstore.entity.ProductForm;
import com.manganer.macstore.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;



@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    ProductRepository productRepository;
    private static final String UploadDir = "src/main/resources/static/images";

    @GetMapping
    public String showAllProducts(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "products/list";

    }

    @GetMapping("/create")
    public String showCreateProductPage(Model model) {
        model.addAttribute("productForm", new ProductForm());
        return "products/create";
    }

    @PostMapping("/create")
    public String createProduct(
            @Valid @ModelAttribute ProductForm productForm, BindingResult result, Model model) {
        if (productForm.getImageFile().isEmpty()) {
            result.addError(new FieldError("productForm", "error.imageFile", "Please select the image file."));
        }
        if (result.hasErrors()) {
            return "products/create";
        }
        MultipartFile multipartFile = productForm.getImageFile();
        Date createdDate = new Date();
        String storedFileName = createdDate.getTime() + "_" + multipartFile.getOriginalFilename();

        try {

            Path Uploadpath = Paths.get(UploadDir);
            if (!Files.exists(Uploadpath)) {
                Files.createDirectories(Uploadpath);
            }
            try (InputStream inputStream = multipartFile.getInputStream()) {
                Files.copy(inputStream, Paths.get(UploadDir + storedFileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

        Product product = new Product();

        product.setName(productForm.getName());
        product.setDescription(productForm.getDescription());
        product.setPrice(productForm.getPrice());
        product.setCategory(productForm.getCategory());
        product.setCreatedDate(createdDate);
        product.setImage(storedFileName);

        productRepository.save(product);

        return "redirect:/products";
    }

    @GetMapping("/edit")
    public String showEditProductPage(@RequestParam Long id, Model model) {
        try {
            Product product = productRepository.findById(id).get();
            model.addAttribute("product", product);


            ProductForm productForm = new ProductForm();
            productForm.setName(product.getName());
            productForm.setCategory(product.getCategory());
            productForm.setPrice(product.getPrice());
            productForm.setDescription(product.getDescription());
            model.addAttribute("productForm", productForm);

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            return "redirect:/products";
        }
        return "products/edit";
    }

    @PostMapping("/edit")
    public String updateProduct(@RequestParam Long id, @ModelAttribute @Valid ProductForm productForm, BindingResult result, Model model) {
        try {

            Product product = productRepository.findById(id).get();
            model.addAttribute("product", product);

            if (result.hasErrors()) {
                return "products/edit";
            }

            product.setName(productForm.getName());
            product.setCategory(productForm.getCategory());
            product.setPrice(productForm.getPrice());
            product.setDescription(productForm.getDescription());

            if (!productForm.getImageFile().isEmpty()) {
                Path oldImagePath = Paths.get(UploadDir + product.getImage());
                try {
                    Files.delete(oldImagePath);
                } catch (Exception e) {
                    System.out.println("Exception: " + e.getMessage());
                }

                MultipartFile multipartFile = productForm.getImageFile();
                Date createdDate = new Date();
                String storedFileName = createdDate.getTime() + "_" + multipartFile.getOriginalFilename();
                try (InputStream inputStream = multipartFile.getInputStream()) {
                    Files.copy(inputStream, Paths.get(UploadDir, storedFileName), StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception e) {
                    System.out.println("Exception: " + e.getMessage());
                }

                product.setImage(storedFileName);
            }

            productRepository.save(product);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

        return "redirect:/products";
    }


    @GetMapping("/delete")
    public String deleteProduct(@RequestParam Long id, Model model) {
        try {

            Product product = productRepository.findById(id).get();
            Path Imagepath = Paths.get("src/main/resources/static/images"+product.getImage());
            try {
                Files.delete(Imagepath);
            }
            catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
            }
            productRepository.delete(product);
        }
        catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        return "redirect:/products";
    }

}