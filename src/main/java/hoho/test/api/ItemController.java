package hoho.test.api;

import hoho.test.domain.Item;
import hoho.test.dto.ItemCreateDto;
import hoho.test.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping("/item/create")
    public ResponseEntity<?> create(@RequestBody ItemCreateDto itemDto){
        try {
            Item item = new Item();
            item.setName(itemDto.getName());
            item.setPrice(itemDto.getPrice());
            item.setStockQuantity(itemDto.getStock());
            Long id = itemService.create(item);
            return new ResponseEntity<>(id, HttpStatus.OK);
        }catch (IllegalStateException e){
            return new ResponseEntity<>("에러가 발생했습니다",HttpStatus.CONFLICT);
        }

    }
}
