package com.ecommerce.customer.controller;

import com.ecommerce.library.model.Customer;
import com.ecommerce.library.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class OrderController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/check-out")
    public String checkout(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        Customer customer = customerService.findByUsername(principal.getName());
        if (customer.getPhoneNumber().trim().isEmpty() || customer.getAddress().trim().isEmpty() || customer.getCity().trim().isEmpty() || customer.getCountry().trim().isEmpty()) {
            model.addAttribute("customer",customer);
            model.addAttribute("error","you must fill in the address so we can ship it to you");
            return "account";
        }
        return "checkout";
    }
}
