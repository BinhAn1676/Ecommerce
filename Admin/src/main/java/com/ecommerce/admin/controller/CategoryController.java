package com.ecommerce.admin.controller;

import com.ecommerce.library.model.Category;
import com.ecommerce.library.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public String categories(Model model, Principal principal){
        if(principal==null){//check login
            return "redirect:/login";
        }
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories",categories);
        model.addAttribute("size",categories.size());
        model.addAttribute("title","Category");
        model.addAttribute("categoryNew",new Category());
        return "categories";
    }

    @PostMapping("/add-category")
    public String addCategory(@ModelAttribute("categoryNew") Category category, RedirectAttributes attributes){
        try {
            categoryService.save(category);
            attributes.addFlashAttribute("success", "Added successfully");
        }catch(DataIntegrityViolationException e){
            attributes.addFlashAttribute("failed","Duplicated name,the item is already existed in the list");
            return "redirect:/categories";
        }catch(Exception e){
            attributes.addFlashAttribute("failed","Error server");
            return "redirect:/categories";
        }
        return "redirect:/categories";
    }

    @RequestMapping(value = "/findById",method = {RequestMethod.PUT,RequestMethod.GET})
    @ResponseBody
    public Category findById(Long id){
        Category category = categoryService.findById(id);
        return categoryService.findById(id);
    }

    @GetMapping("/update-category")
    public String update(Category category,RedirectAttributes attributes){
        try{
            categoryService.update(category);
            attributes.addFlashAttribute("success","Updated Successfully");
        }catch(DataIntegrityViolationException e) {
            attributes.addFlashAttribute("failed", "Duplicated name,the item is already existed in the list");
            return "redirect:/categories";
        }catch(Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("failed","Error server");
        }
        return "redirect:/categories";
    }

    @RequestMapping(value = "/delete-category" , method = {RequestMethod.GET,RequestMethod.PUT})
    public String delete(Long id,RedirectAttributes attributes){
        try{
            categoryService.deleteById(id);
            attributes.addFlashAttribute("success","Deleted successfully");
        }catch(Exception e){
            attributes.addFlashAttribute("failed","Failed to delete");
            e.printStackTrace();
        }
        return "redirect:/categories";
    }

    @RequestMapping(value = "enable-category",method = {RequestMethod.PUT,RequestMethod.GET})
    public String enable(Long id,RedirectAttributes attributes){
        try{
            categoryService.enabledById(id);
            attributes.addFlashAttribute("success","Enabled successfully");
        }catch(Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("failed","error server");
        }
        return "redirect:/categories";
    }

}
