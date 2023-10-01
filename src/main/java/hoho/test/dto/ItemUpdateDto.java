package hoho.test.dto;

import lombok.Data;

@Data
public class ItemUpdateDto {
    private Long id;
    private String name;
    private int price;
    private int stock;
}
