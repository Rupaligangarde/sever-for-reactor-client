package com.client.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CatalogStock {
    private String variantId;
    private String sellerId;
    private String offeringId;
    private String stockGeoTypeKey;
    private String stockGeoTypeId;
    private String shippingOptionType;
    private String stateOfStock;
    private Boolean hasStock;
    private Integer quantity;
    private String sourceUpdatedAt;
}
