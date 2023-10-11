package hoho.test.service;

import hoho.test.domain.*;
import hoho.test.dto.OrderAllResponseDto;
import hoho.test.dto.OrderCreateDto;
import hoho.test.repository.*;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<OrderAllResponseDto> getAllOrder() {
        //전체 주문 조회
        List<Order> orders = orderRepository.findAll();
        List<OrderAllResponseDto> result = orders.stream()
                .flatMap(o -> o.getOrderItems().stream()
                        .map(oi -> new OrderAllResponseDto(
                                o.getId(), o.getMember().getName(), o.getDelivery().getAddress(),o.getOrderStatus(),
                                oi.getItem().getName(), oi.getOrderPrice(), oi.getCount()
                        )))
                .collect(Collectors.toList());
        return result;
    }
}
