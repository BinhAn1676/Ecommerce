package com.ecommerce.admin.controller;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/products")
    public String products(Model model , Principal principal){
        if(principal==null){
            return "redirect:/login";
        }
        List<ProductDto> productDtos = productService.findAll();
        model.addAttribute("title","Manage products");
        model.addAttribute("products",productDtos);
        model.addAttribute("size",productDtos.size());
        return "products";
    }

    @GetMapping("/products/{pageNo}")
    public String productsPage(@PathVariable("pageNo") Integer pageNo,Model model,Principal principal){
        if(principal == null){
            return "redirect:/login";
        }else{
            Page<ProductDto> products = productService.pageProducts(pageNo);
            model.addAttribute("title","Manage Products");
            model.addAttribute("size",products.getSize());
            model.addAttribute("totalPages",products.getTotalPages());
            model.addAttribute("currentPage",pageNo);
            model.addAttribute("products",products);
        }
        return "products";
    }

    @GetMapping("/add-product")
    public String addProductForm(Model model,Principal principal){
        if(principal==null){
            return "redirect:/login";
        }
        model.addAttribute("product", new ProductDto());
        List<Category> categories = categoryService.findAllByActivated();
        model.addAttribute("categories",categories);
        return "add-product";
    }
    @PostMapping("/save-product")
    public String saveProduct(@ModelAttribute("product") ProductDto productDto,
                              @RequestParam("imageProduct")MultipartFile imageProduct,
                              RedirectAttributes attributes){
        try{
            productService.save(imageProduct,productDto);
            attributes.addFlashAttribute("success","Add successfully");
        }catch(Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error","Error server");
        }
        return "redirect:/products/0";
    }

    @GetMapping("/search-result/{pageNo}")
    public String searchProducts(@PathVariable("pageNo")Integer pageNo,
                                 Model model,
                                 Principal principal,
                                 @RequestParam("keyword")String keyword){
        if(principal==null){
            return "redirect:/login";
        }
        model.addAttribute("title","Search Result");
        Page<ProductDto> products = productService.searchProducts(pageNo,keyword);
        model.addAttribute("products",products);
        model.addAttribute("size",products.getSize());
        model.addAttribute("currentPage",pageNo);
        model.addAttribute("totalPages",products.getTotalPages());
        return "result-products";
    }

    @GetMapping("/update-product/{id}")
    public String updateProductForm(@PathVariable("id") Long id,Model model,Principal principal){
        if(principal == null){
            return "redirect:/login";
        }
        model.addAttribute("title","Update Product");
        ProductDto productDto = productService.findById(id);
        model.addAttribute("productDto",productDto);
        List<Category> categories = categoryService.findAllByActivated();
        model.addAttribute("categories",categories);
        return "update-product";
    }

    @PostMapping("/update-product/{id}")
    public String processUpdate(@PathVariable("id") Long id,
                                @ModelAttribute("productDto") ProductDto productDto,
                                @RequestParam("imageProduct") MultipartFile imageProduct,
                                RedirectAttributes attributes){
        try{
            productService.update(imageProduct,productDto);
            attributes.addFlashAttribute("success","Update successfully");
        }catch(Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error","error server");
        }
        return "redirect:/products";
    }

    @RequestMapping(value = "/enabled-product/{id}",method = {RequestMethod.PUT,RequestMethod.GET})
    public String enableProduct(@PathVariable("id") Long id,RedirectAttributes attributes){
        try{
            productService.enableById(id);
            attributes.addFlashAttribute("success","Enabled Successfully");

        }catch(Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error","error server");
        }
        return "redirect:/products";
    }

    @RequestMapping(value = "/delete-product/{id}",method = {RequestMethod.GET,RequestMethod.PUT})
    public String deleteProduct(@PathVariable("id")Long id,RedirectAttributes attributes){
        try{
            productService.deleteById(id);
            attributes.addFlashAttribute("success","Deleted Successfully");

        }catch(Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error","error server");
        }
        return "redirect:/products";
    }
}
