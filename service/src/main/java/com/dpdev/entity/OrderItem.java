package com.dpdev.entity;

import com.dpdev.exception.OrderItemException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"order", "product"})
@ToString(exclude = {"order", "product"})
@Builder
@Entity
@Table(name = "order_item")
public class OrderItem implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Orders order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private Integer quantity;

    private BigDecimal price;

    public void setOrders(Orders order) {
        if (!validateOrderItem()) {
            throw new OrderItemException("Order item validation failed.");
        } else {
            this.order = order;
            this.order.getOrderItems().add(this);
        }
    }

    private boolean validateOrderItem() {
        return product.getQuantity() != 0
                && product.getQuantity() - this.quantity >= 0;
    }
}
