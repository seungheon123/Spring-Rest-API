package hoho.test.repository;

import hoho.test.domain.Delivery;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class DeliveryRepository {
    @PersistenceContext
    private EntityManager em;

    public void save(Delivery delivery){
        em.persist(delivery);
    }
}
