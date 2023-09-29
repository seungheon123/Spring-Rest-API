package hoho.test.service;

import hoho.test.domain.Item;
import hoho.test.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ItemService {
    @Autowired
    ItemRepository itemRepository;

    @Transactional
    public Long create(Item item){
        validateDuplciateItem(item);
        itemRepository.save(item);
        return item.getId();
    }

    private void validateDuplciateItem(Item item) {
        Item findItem = itemRepository.findByName(item);
        if(findItem!=null) throw  new IllegalStateException("이미 존재하는 상품입니다");
    }
}
