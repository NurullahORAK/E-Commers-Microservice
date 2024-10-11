package org.orak.stock.product.web;

import org.orak.stock.product.api.ProductDto;
import org.orak.stock.product.api.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @PostMapping
    public ProductResponse create(@RequestBody ProductRequest request){
        return toResponse(productService.save(toDto(request)));
    }
    @GetMapping("/{id}")
    public ProductResponse get(@PathVariable(value = "id")String id){
        return toResponse(productService.getProduct(id));
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable(value = "id")String id){
        productService.delete(id);
    }
    @PutMapping("/{id}")
    public ProductResponse update(@RequestBody ProductRequest request,@PathVariable(value = "id")String id){
        return toResponse(productService.update(toDto(request),id));
    }

    private ProductResponse toResponse(ProductDto save) {
        return ProductResponse.builder()
                .productId(save.getProductId())
                .categoryId(save.getCategoryId())
                .stock(save.getStock())
                .name(save.getName())
                .price(save.getPrice()).build();
    }

    private ProductDto toDto(ProductRequest request) {
        return ProductDto.builder()
                .name(request.getName())
                .categoryId(request.getCategoryId())
                .price(request.getPrice())
                .stock(request.getStock()).build();
    }

}
