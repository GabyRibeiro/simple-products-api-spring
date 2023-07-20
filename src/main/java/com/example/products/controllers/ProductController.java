package com.example.products.controllers;

import com.example.products.dtos.ProductRecordDTO;
import com.example.products.models.ProductModel;
import com.example.products.repositories.ProductRepository;
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
public class ProductController {
    @Autowired
    ProductRepository productRepository;

    @PostMapping("/products")
    public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDTO productRecordDTO) {
        System.out.println("productRecordDTO" + productRecordDTO);
        //Chamou a depedência dessa forma pois é necessário transferir as infos do DTO para o Model
        var productModel = new ProductModel();

        //Aqui copia o valor para o model
        BeanUtils.copyProperties(productRecordDTO, productModel);

        //Na hora de salvar tem que retornar no body o que foi salvo, e o CREATED é equivalente ao 201
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductModel>> getAllProducts() {
        List<ProductModel> productList = productRepository.findAll();
        if (!productList.isEmpty()) {
            for (ProductModel product : productList) {
                UUID uuid = product.getIdProduct();
                product.add(linkTo(methodOn(ProductController.class).getProductById(uuid)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(productList);
    }

    @GetMapping("/products/{uuid}")
    public ResponseEntity<Object> getProductById(@PathVariable(value = "uuid") UUID uuid) {
        //Aqui vai dar esse erro de opcional, tenho que pegar o retorno do repositorio antes e validar se vazio ou não
        Optional<ProductModel> productByID = productRepository.findById(uuid);

        if (productByID.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("O producto com este ID não existe");
        }

        productByID.get().add(linkTo(methodOn(ProductController.class).getAllProducts()).withSelfRel());

        return ResponseEntity.status(HttpStatus.OK).body(productByID.get());
    }

    @PutMapping("/products/{uuid}")
    public ResponseEntity<Object> putProductByID(@PathVariable(value = "uuid") UUID uuid,
                                                 @RequestBody @Valid ProductRecordDTO productRecordDTO) {
        Optional<ProductModel> productByID = productRepository.findById(uuid);

        if (productByID.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("O produto com este ID não existe");
        }

        //Varíavel temporaria para fazer a edição
        var productModel = productByID.get();

        BeanUtils.copyProperties(productRecordDTO, productModel);

        return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(productModel));
    }
    @DeleteMapping("/products/{uuid}")
    public ResponseEntity<Object> deleteProductByID(@PathVariable(value = "uuid") UUID uuid,
                                                    @RequestBody @Valid ProductRecordDTO productRecordDTO) {

        Optional<ProductModel> productByID = productRepository.findById(uuid);

        if (productByID.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("O produto com este ID não existe");
        }

        //Não preciso copiar ou chamar o model para salvar em cima pois já sabemos o produto a deletar pelo UUID
        productRepository.delete(productByID.get());
        return ResponseEntity.status(HttpStatus.OK).body("Produto deletado com sucesso");
    }
}
