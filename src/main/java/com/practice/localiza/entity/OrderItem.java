package com.practice.localiza.entity;




import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
        import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "order_item")
public class OrderItem { // 'OrderItem' é 'OrderItem'

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    private Long carId;
    private String carName;
    private Double purchasedPrice;

    @ElementCollection
    @CollectionTable(name = "order_item_accessories")
    private List<String> purchasedAccessories;

    private Integer quantity;

    public OrderItem() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem that = (OrderItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}