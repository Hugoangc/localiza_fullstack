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
public class OrderItem { // 'OrderItem' é 'OrderItem'

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id") // Define o nome da coluna de chave estrangeira no banco
    @JsonIgnore
    private Order order;

    // Dados "congelados" do carro no momento da compra
    private Long carId;
    private String carName;
    private Double purchasedPrice; // O 'calculatedPrice' do CartItem

    @ElementCollection // Boa forma de armazenar uma lista simples de strings
    private List<String> purchasedAccessories; // Armazena os nomes dos acessórios

    private Integer quantity; // Por padrão será 1, mas é bom ter

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