package hoho.test.dto;

import hoho.test.domain.Delivery;
import lombok.Data;

@Data
public class OrderCreateDto {
    private Long member_id;
    private Long item_id;
    private int count;
}
