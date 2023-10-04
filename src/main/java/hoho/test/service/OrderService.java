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
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, MemberRepository memberRepository, ItemRepository itemRepository) {
        this.orderRepository = orderRepository;
        this.memberRepository = memberRepository;
        this.itemRepository = itemRepository;
    }

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

    @Transactional
    public void deleteOrder(Long id) {
        Order order = checkDeliveryStatus(id);
        orderRepository.delete(order);
    }

    private Order checkDeliveryStatus(Long id) {
        Order findOrder = orderRepository.findById(id);
        if(findOrder.getDelivery().getDeliveryStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송 된 주문입니다");
        }
        else return findOrder;
    }
}
