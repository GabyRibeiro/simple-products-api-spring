package com.example.products.services;

import com.example.products.controllers.ProductController;
import com.example.products.dtos.ProductRecordDTO;
import com.example.products.models.ProductModel;
import com.example.products.repositories.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    public ProductModel saveProduct(ProductModel productModel, ProductRecordDTO productRecordDTO) {
        BeanUtils.copyProperties(productRecordDTO, productModel);

        return productRepository.save(productModel);
    }

    public List<ProductModel> getAllProducts() {
        List<ProductModel> productList = productRepository.findAll();

        if (!productList.isEmpty()) {
            for (ProductModel product : productList) {
                UUID uuid = product.getIdProduct();
                product.add(linkTo(methodOn(ProductController.class).getProductById(uuid)).withSelfRel());
            }
        }

        return productList;
    }

    public Optional<ProductModel> getProductById(UUID uuid) {
        Optional<ProductModel> productByID = productRepository.findById(uuid);

        if (productByID.isEmpty()) {
            return Optional.empty();
        }

        productByID.get().add(linkTo(methodOn(ProductController.class).getAllProducts()).withSelfRel());

        return productByID;
    }

    public ProductModel putProductByID(UUID uuid, ProductRecordDTO productRecordDTO) {
        Optional<ProductModel> productByID = productRepository.findById(uuid);
        if (productByID.isEmpty()) {
            return null;
        }
        var productModel = productByID.get();
        BeanUtils.copyProperties(productRecordDTO, productModel);
        return productRepository.save(productModel);
    }

    public Object deleteProductByID(UUID uuid, ProductRecordDTO productRecordDTO) {
        Optional<ProductModel> productByID = productRepository.findById(uuid);

        if (productByID.isEmpty()) {
            return null;
        }

        productRepository.delete(productByID.get());

        return productByID;
    }

}
