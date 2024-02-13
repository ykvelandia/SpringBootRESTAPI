package org.adaschool.api.controller.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.adaschool.api.exception.ProductNotFoundException;
import org.adaschool.api.repository.product.Product;
import org.adaschool.api.repository.product.ProductDto;
import org.adaschool.api.repository.user.User;
import org.adaschool.api.service.product.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/products/")
public class ProductsController {

    private final ProductsService productsService;

    @Autowired
    public ProductsController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @Operation(summary = "Create new product")
    @ApiResponse(responseCode = "201",description = "Product created")
    @PostMapping
        public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createProduct = productsService.save(product);
        URI createdProductUri = URI.create("");
        return ResponseEntity.created(createdProductUri).body(createProduct);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productsService.all();
        return ResponseEntity.ok(products);
    }

    @GetMapping("{id}")
    public ResponseEntity<Product> findById(@PathVariable("id") String id) {
        Product product = productsService.findById(id).orElseThrow(()->new ProductNotFoundException(id));
        return ResponseEntity.ok(product);
    }

    @PutMapping("{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") String id, @RequestBody ProductDto productDto) {
        Optional<Product> product = productsService.findById(id);
        if(product.isPresent()){
            Product existingProduct = product.get();
            existingProduct.setName(productDto.getName());
            existingProduct.setDescription(productDto.getDescription());
            existingProduct.setCategory(productDto.getCategory());
            existingProduct.setTags(productDto.getTags());
            existingProduct.setPrice(productDto.getPrice());
            existingProduct.setImageUrl(productDto.getImageUrl());
            productsService.save(existingProduct);
            return ResponseEntity.ok(null);
        } else {
            throw new ProductNotFoundException(id);
        }

    }

    @DeleteMapping ("{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") String id) {
        Optional<Product> existingProduct = productsService.findById(id);
        if(existingProduct.isPresent()){
            productsService.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            throw new ProductNotFoundException(id);
        }

    }
}