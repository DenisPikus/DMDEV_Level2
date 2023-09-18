package com.dpdev.entity;

import com.dpdev.entity.enums.Brand;
import com.dpdev.entity.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "stock")
@ToString(exclude = "stock")
@Builder
@Entity
public class Product implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Brand brand;

    @Enumerated(EnumType.STRING)
    private ProductType productType;

    private String description;

    private BigDecimal price;

    private Integer quantity;

    private boolean availability;

    private String image;

//    @ManyToOne(fetch = FetchType.LAZY)
//    private Stock stock;

}
