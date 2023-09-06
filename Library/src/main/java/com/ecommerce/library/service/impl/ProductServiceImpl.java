package com.ecommerce.library.service.impl;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.repository.ProductRepository;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.utils.ImageUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageUpload imageUpload;

    @Override
    public List<ProductDto> findAll() {
        List<ProductDto> productDtos = new ArrayList<>();
        List<Product> products = productRepository.findAll();
        for(Product product : products){
            ProductDto productDto = new ProductDto();
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setDescription(product.getDescription());
            productDto.setCostPrice(product.getCostPrice());
            productDto.setSalePrice(product.getSalePrice());
            productDto.setCurrentQuantity(product.getCurrentQuantity());
            productDto.setCategory(product.getCategory());
            productDto.setActivated(product.getIs_activated());
            productDto.setDeleted(product.getIs_deleted());
            productDto.setImage(product.getImage());
            productDtos.add(productDto);
        }
        return productDtos;
    }

    @Override
    public Product save(MultipartFile imageProduct, ProductDto productDto) {
        try{
            Product product = new Product();
            if(imageProduct == null){
                product.setImage(null);
            }else{
                imageUpload.uploadImage(imageProduct);
                product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
            }
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setCategory(productDto.getCategory());
            product.setCostPrice(productDto.getCostPrice());
            product.setIs_activated(true);
            product.setIs_deleted(false);
            product.setCurrentQuantity(productDto.getCurrentQuantity());
            return productRepository.save(product);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Product update(MultipartFile imageProduct,ProductDto productDto) {
        try{
            Product product = productRepository.findById(productDto.getId()).get();
            if(imageProduct.isEmpty()){
                product.setImage(product.getImage());
            }else{
                if(imageUpload.checkExisted(imageProduct)==false){
                    imageUpload.uploadImage(imageProduct);
                }
                product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
            }
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setSalePrice(productDto.getSalePrice());
            product.setCostPrice(productDto.getCostPrice());
            product.setCurrentQuantity(productDto.getCurrentQuantity());
            product.setCategory(productDto.getCategory());
            return productRepository.save(product);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteById(Long id) {
        Product product = productRepository.findById(id).get();
        product.setIs_deleted(true);
        product.setIs_activated(false);
        productRepository.save(product);
    }

    @Override
    public void enableById(Long id) {
        Product product = productRepository.findById(id).get();
        product.setIs_deleted(false);
        product.setIs_activated(true);
        productRepository.save(product);
    }

    @Override
    public ProductDto findById(Long id) {
        Product product = productRepository.getById(id);
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setCurrentQuantity(product.getCurrentQuantity());
        productDto.setCategory(product.getCategory());
        productDto.setSalePrice(product.getSalePrice());
        productDto.setCostPrice(product.getCostPrice());
        productDto.setImage(product.getImage());
        productDto.setDeleted(product.getIs_deleted());
        productDto.setActivated(product.getIs_activated());
        return productDto;
    }

    @Override
    public Page<Product> pageProducts(Integer pageNo) {
        Pageable pageable= PageRequest.of(pageNo,5);
        Page<Product> pageProduct = productRepository.pageProduct(pageable);
        return pageProduct;
    }
}
