package hoho.test.dto;

import hoho.test.domain.Address;
import lombok.Data;

@Data
public class MemberJoinDto {

    private String name;

    private Address address;
}
