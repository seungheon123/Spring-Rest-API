package hoho.test.dto;

import hoho.test.domain.Address;
import hoho.test.domain.OrderStatus;
import lombok.Data;

@Data
public class OrderAllResponseDto {
    private Long OrderId;
    private String name; //주문한 사람
    private Address address; //배송지
    private OrderStatus orderStatus;

    private String itemName; //상품 이름
    private int orderPrice; //주문 가격
    private int count; //주문 수량

    public OrderAllResponseDto(Long orderId, String name, Address address, OrderStatus orderStatus, String itemName, int orderPrice, int count) {
        OrderId = orderId;
        this.name = name;
        this.address = address;
        this.orderStatus = orderStatus;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.count = count;
    }
}
