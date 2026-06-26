package com.terxiel.store.mappers;

import com.terxiel.store.dtos.ProductSummary;
import com.terxiel.store.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.name", target = "category")
    ProductSummary toDto(Product product);
}
