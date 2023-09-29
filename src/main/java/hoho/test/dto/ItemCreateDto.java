package hoho.test.dto;

import lombok.Data;

@Data
public class ItemCreateDto {
    private String name;
    private int price;
    private int stock;
}
