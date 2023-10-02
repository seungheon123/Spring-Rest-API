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
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private DeliveryRepository deliveryRepository;

    @Transactional
    public void createOrder(OrderCreateDto orderDto) {
        Member findMember = memberRepository.findOne(orderDto.getMember_id());
        Delivery delivery = new Delivery();
        delivery.setAddress(findMember.getAddress());
        delivery.setDeliveryStatus(DeliveryStatus.READY);
        deliveryRepository.save(delivery);

        Item findItem = itemRepository.findOne(orderDto.getItem_id());
        int orderPrice = findItem.getPrice() * orderDto.getCount();
        Order order = new Order();
        order.setMember(findMember);
        order.setDelivery(delivery);
        order.setOrderStatus(OrderStatus.ORDER);
        orderRepository.save(order);
        orderItemRepository.createOrderItem(findItem,order,orderPrice,orderDto.getCount());

    }
}
