package hoho.test.dto;

import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Data
public class MemberSignInDto {
    private String name;

}
