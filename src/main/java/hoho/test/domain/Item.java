package hoho.test.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Item {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    public void removeStock(int count){
        int rest = this.stockQuantity - count;
        if(rest<0){
            throw new IllegalStateException("재고가 부족합니다");
        }
        this.stockQuantity = rest;
    }
}
