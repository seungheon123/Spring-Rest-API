package hoho.test.repository;

import hoho.test.domain.Order;
import hoho.test.dto.OrderCreateDto;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class OrderRepository {
    @PersistenceContext
    private EntityManager em;

    public void save(Order order){
        em.persist(order);
    }

}
