package hoho.test.repository;

import hoho.test.domain.Item;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ItemRepository {
    @PersistenceContext
    private EntityManager em;

    public void save(Item item){
        em.persist(item);
    }

    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i",Item.class)
                .getResultList();
    }

    public void update(Item item){
        Item findItem = em.find(Item.class, item.getId());
        findItem.setName(item.getName());
        findItem.setPrice(item.getPrice());
        findItem.setStockQuantity(item.getStockQuantity());
    }

    public Item findByName(Item item){
        try {
            return em.createQuery("select i from Item i" +
                            " where i.name = :name", Item.class)
                    .setParameter("name", item.getName())
                    .getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }


}
