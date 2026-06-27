package com.terxiel.store.mappers;

import com.terxiel.store.dtos.ProductSummary;
import com.terxiel.store.dtos.UpdateProductRequest;
import com.terxiel.store.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.id", target = "categoryId")
    ProductSummary toDto(Product product);

    Product toEntity(ProductSummary productSummary);

    void update(UpdateProductRequest updateProductRequest, @MappingTarget Product product);
}
