package hoho.test.service;

import hoho.test.domain.Item;
import hoho.test.dto.ItemCreateDto;
import hoho.test.dto.ItemUpdateDto;
import hoho.test.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository){
        this.itemRepository = itemRepository;
    }

    @Transactional
    public Long create(Item item){
        validateDuplciateItem(item);
        itemRepository.save(item);
        return item.getId();
    }

    @Transactional
    public void Update(Long id, ItemUpdateDto itemDto){
        Item item = new Item();
        item.setId(id);
        item.setName(itemDto.getName());
        item.setPrice(itemDto.getPrice());
        item.setStockQuantity(itemDto.getStock());
        itemRepository.update(item);
    }
    @Transactional
    public void delete(Long id) {
        Item item = checkExistItem(id);
        itemRepository.deleteById(item);
    }

    private Item checkExistItem(Long id) {
        Item findItem = itemRepository.findOne(id);
        if(findItem==null) throw new IllegalStateException("상품이 존재하지 않습니다");
        else return findItem;
    }

    private void validateDuplciateItem(Item item) {
        Item byName = itemRepository.findByName(item);
        if(byName != null) throw new IllegalStateException("이미 존재하는 상품입니다");
    }


}
