package hoho.test.service;

import hoho.test.domain.*;
import hoho.test.dto.OrderCreateDto;
import hoho.test.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Transactional
    public void createOrder(OrderCreateDto orderDto) {
        //엔티티 조회
        Member findMember = memberRepository.findOne(orderDto.getMember_id());
        Item findItem = itemRepository.findOne(orderDto.getItem_id());
        //배송 정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(findMember.getAddress());
        delivery.setDeliveryStatus(DeliveryStatus.READY);

        //주문 상품 생성
        int orderPrice = findItem.getPrice() * orderDto.getCount();
        OrderItem orderItem = OrderItem.createOrderItem(findItem, orderPrice, orderDto.getCount());

        //주문 생성
        Order order = Order.createOrder(findMember,delivery,orderItem);
        orderRepository.save(order);

    }
}
