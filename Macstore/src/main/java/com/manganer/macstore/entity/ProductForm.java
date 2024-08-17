package com.manganer.macstore.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

public class ProductForm {
    @NotEmpty(message = "cannot be empty ")
    private String name;
    @NotEmpty(message = "Category cannot be empty")
    private String category;
    @Min(1)
    private double price;
    private String description;
    private MultipartFile imageFile;

    public @NotEmpty(message = "cannot be empty ") String getName() {
        return name;
    }

    public void setName(@NotEmpty(message = "cannot be empty ") String name) {
        this.name = name;
    }

    public @NotEmpty(message = "cannot be empty ") String getCategory() {
        return category;
    }

    public void setCategory(@NotEmpty(message = "cannot be empty") String category) {
        this.category = category;
    }

    @Min(1)
    public double getPrice() {
        return price;
    }

    public void setPrice(@Min(1) double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    public void setId(Long id) {
    }
}


