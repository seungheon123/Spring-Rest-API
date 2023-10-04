package hoho.test.api;

import hoho.test.domain.Order;
import hoho.test.dto.OrderAllResponseDto;
import hoho.test.dto.OrderCreateDto;
import hoho.test.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/order/new")
    public ResponseEntity<?> create(@RequestBody OrderCreateDto orderDto){
        try{
            orderService.createOrder(orderDto);
            return new ResponseEntity<>("새로운 주문", HttpStatus.OK);
        }catch (IllegalStateException e){
            return new ResponseEntity<>("에러가 발생했습니다", HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/order/delete/{order_id}")
    public ResponseEntity<?> delete(@PathVariable(name = "order_id") Long id){
        try{
            orderService.deleteOrder(id);
            return new ResponseEntity<>("주문을 취소했습니다",HttpStatus.OK);
        }catch (IllegalStateException e){
            return new ResponseEntity<>("에러가 발생했습니다",HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/order")
    public ResponseEntity<?> getAll(){
        try{
            List<OrderAllResponseDto> allOrder = orderService.getAllOrder();
            return new ResponseEntity<>(allOrder,HttpStatus.OK);
        }catch (IllegalStateException e){
            return new ResponseEntity<>("에러가 발생했습니다",HttpStatus.CONFLICT);
        }
    }

}
