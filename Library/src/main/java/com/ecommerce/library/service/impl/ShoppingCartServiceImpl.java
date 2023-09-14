package com.ecommerce.library.service.impl;

import com.ecommerce.library.model.CartItem;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.repository.CartItemRepository;
import com.ecommerce.library.repository.ShoppingCartRepository;
import com.ecommerce.library.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public ShoppingCart addItemToCart(Product product, int quantity, Customer customer) {
        ShoppingCart cart = customer.getShoppingCart();
        if(cart == null){
            cart = new ShoppingCart();
        }
        Set<CartItem> cartItems = cart.getCartItem();
        CartItem cartItem = findCartItems(cartItems, product.getId());
        if(cartItems == null ){
            cartItems = new HashSet<>();
            if(cartItem == null){
                cartItem = new CartItem();
                cartItem.setProduct(product);
                cartItem.setTotalPrice(quantity * product.getCostPrice());
                cartItem.setQuantity(quantity);
                cartItem.setCart(cart);
                cartItems.add(cartItem);
                cartItemRepository.save(cartItem);
            }
        }else{
            if(cartItem == null){
                cartItem = new CartItem();
                cartItem.setProduct(product);
                cartItem.setTotalPrice(quantity * product.getCostPrice());
                cartItem.setQuantity(quantity);
                cartItem.setCart(cart);
                cartItems.add(cartItem);
                cartItemRepository.save(cartItem);
            }else{
                cartItem.setQuantity(cartItem.getQuantity()+quantity);
                cartItem.setTotalPrice(cartItem.getTotalPrice() + (quantity * product.getCostPrice()));
                cartItemRepository.save(cartItem);
            }
        }
        cart.setCartItem(cartItems);
        Integer totalItems = totalItems(cart.getCartItem());
        Double totalPrice = totalPrice(cart.getCartItem());
        cart.setTotalPrice(totalPrice);
        cart.setTotalItem(totalItems);
        cart.setCustomer(customer);
        return shoppingCartRepository.save(cart);
    }

    @Override
    @Transactional
    public ShoppingCart updateItemInCart(Product product, int quantity, Customer customer) {
        ShoppingCart cart = customer.getShoppingCart();
        Set<CartItem> cartItems = cart.getCartItem();
        CartItem cartItem = findCartItems(cartItems, product.getId());
        cartItem.setQuantity(quantity);
        cartItem.setTotalPrice(quantity * product.getCostPrice());
        cartItemRepository.save(cartItem);

        int totalItems = totalItems(cartItems);
        double totalPrice = totalPrice(cartItems);
        cart.setTotalItem(totalItems);
        cart.setTotalPrice(totalPrice);
        return shoppingCartRepository.save(cart);
    }

    @Override
    public ShoppingCart deleteItemInCart(Product product, Customer customer) {
        ShoppingCart cart = customer.getShoppingCart();
        Set<CartItem> cartItems = cart.getCartItem();
        CartItem cartItem = findCartItems(cartItems, product.getId());
        cartItems.remove(cartItem);
        cartItemRepository.delete(cartItem);
        double totalPrice = totalPrice(cartItems);
        int totalItems = totalItems(cartItems);
        cart.setCartItem(cartItems);
        cart.setTotalItem(totalItems);
        cart.setTotalPrice(totalPrice);
        return shoppingCartRepository.save(cart);
    }

    @Override
    @Transactional
    public void deleteCartById(Long id,Customer customer) {
        ShoppingCart shoppingCart = shoppingCartRepository.getById(id);
        for (CartItem cartItem : shoppingCart.getCartItem()) {
            CartItem temp = cartItem;
            cartItemRepository.delete(temp);
        }
        shoppingCartRepository.delete(shoppingCart);
    }

    private CartItem findCartItems(Set<CartItem> cartItems,Long productId){
        if(cartItems== null){
            return null;
        }else{
            CartItem cartItem = null;
            for (CartItem item : cartItems) {
                if(item.getProduct().getId() == productId){
                    cartItem = item;
                }
            }
            return cartItem;
        }
    }

    private int totalItems(Set<CartItem> cartItems){
        int totalItems = 0;
        for(CartItem item : cartItems){
            totalItems += item.getQuantity();
        }
        return totalItems;
    }
    private Double totalPrice(Set<CartItem> cartItems){
        Double totalPrice = 0.0;
        for(CartItem item : cartItems){
            totalPrice += item.getTotalPrice();
        }
        return totalPrice;
    }
}
