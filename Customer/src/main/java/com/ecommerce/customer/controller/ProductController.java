package com.ecommerce.customer.controller;

import com.ecommerce.library.dto.CategoryDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/products")
    public String products(Model model){
        List<CategoryDto> categoryDtos= categoryService.getCategoryAndProduct();
        List<Product> products = productService.getAllProducts();
        List<Product> listViewProducts = productService.listViewProducts();
        model.addAttribute("viewProducts",listViewProducts);
        model.addAttribute("categories",categoryDtos);
        model.addAttribute("products",products);

        return "shop";
    }

    @GetMapping("/find-product/{id}")
    public String findProductById(@PathVariable("id") Long id,Model model){
        Product product = productService.getProductById(id);
        Long categoryId = product.getCategory().getId();
        List<Product> products = productService.getRelatedProducts(categoryId);
        model.addAttribute("products",products);
        model.addAttribute("product",product);
        return "product-detail";
    }

    @GetMapping("/products-in-category/{id}")
    public  String getProductInCategory(@PathVariable("id") Long categoryId ,
                                        Model model){
        Category category =categoryService.findById(categoryId);
        List<Product> products = productService.getProductsInCategory(category.getId());
        List<CategoryDto> categories= categoryService.getCategoryAndProduct();
        model.addAttribute("categories",categories);
        model.addAttribute("category",category);
        model.addAttribute("products",products);
        return "products-in-category";
    }

    @GetMapping("/high-price")
    public String filterHighPrice(Model model){
        List<Product> products = productService.filterHighPrice();
        List<Category> categories = categoryService.findAllByActivated();
        List<CategoryDto> categoryDtos = categoryService.getCategoryAndProduct();
        model.addAttribute("categoryDtoList",categoryDtos);
        model.addAttribute("categories",categories);
        model.addAttribute("products",products);
        return "filter-high-price";
    }


    @GetMapping("/low-price")
    public String filterLowPrice(Model model){
        List<Product> products = productService.filterLowPrice();
        List<Category> categories = categoryService.findAllByActivated();
        List<CategoryDto> categoryDtos = categoryService.getCategoryAndProduct();
        model.addAttribute("categoryDtoList",categoryDtos);
        model.addAttribute("categories",categories);
        model.addAttribute("products",products);
        return "filter-low-price";
    }
}
