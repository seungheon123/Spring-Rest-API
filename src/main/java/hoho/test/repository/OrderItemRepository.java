package hoho.test.repository;

import hoho.test.domain.Item;
import hoho.test.domain.Order;
import hoho.test.domain.OrderItem;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class OrderItemRepository {
    @PersistenceContext
    private EntityManager em;

    public void createOrderItem(Item item, Order order, int price, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setItem(item);
        orderItem.setOrderPrice(price);
        orderItem.setCount(count);
        em.persist(orderItem);
        item.removeStock(count);
    }

}
