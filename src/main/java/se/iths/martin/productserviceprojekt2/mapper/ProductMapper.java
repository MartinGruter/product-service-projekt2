package se.iths.martin.productserviceprojekt2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import se.iths.martin.productserviceprojekt2.dto.ProductRequestDTO;
import se.iths.martin.productserviceprojekt2.dto.ProductResponseDTO;
import se.iths.martin.productserviceprojekt2.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    // från dto till entitet
    @Mapping(target = "id", ignore = true)
    Product toEntity(ProductRequestDTO requestDTO);

    // från entitet till dto
    ProductResponseDTO toResponseDTO(Product entity);
}
