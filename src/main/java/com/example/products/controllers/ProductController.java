package com.example.products.controllers;

import com.example.products.dtos.ProductRecordDTO;
import com.example.products.models.ProductModel;
import com.example.products.repositories.ProductRepository;
import com.example.products.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "api/v1/products")
public class ProductController {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductService productService;

    @PostMapping
    public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDTO productRecordDTO) {
        var productModel = new ProductModel();
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.saveProduct(productModel, productRecordDTO));
    }

    @GetMapping
    public ResponseEntity<List<ProductModel>> getAllProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getAllProducts());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Object> getProductById(@PathVariable(value = "uuid") UUID uuid) {
        Optional<ProductModel> productById = productService.getProductById(uuid);
        if (productById.isEmpty()) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("O id solicitado não existe");
        }
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProductById(uuid));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Object> putProductByID(@PathVariable(value = "uuid") UUID uuid,
                                                 @RequestBody @Valid ProductRecordDTO productRecordDTO) {

        ProductModel product = productService.putProductByID(uuid, productRecordDTO);

        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("O produto com este ID não existe");
        }

        return ResponseEntity.status(HttpStatus.OK).body(product);
    }
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Object> deleteProductByID(@PathVariable(value = "uuid") UUID uuid,
                                                    @RequestBody @Valid ProductRecordDTO productRecordDTO) {

        Object product = productService.deleteProductByID(uuid, productRecordDTO);

        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("O produto com este ID não existe");
        }

        return ResponseEntity.status(HttpStatus.OK).body("Produto deletado com sucesso");
    }
}
