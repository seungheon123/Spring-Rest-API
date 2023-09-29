package hoho.test.api;


import hoho.test.domain.Member;
import hoho.test.dto.MemberJoinDto;
import hoho.test.dto.MemberWithdrawDto;
import hoho.test.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<?> createUser(@RequestBody MemberJoinDto userDto){
        try{
            Member member = new Member();
            member.setName(userDto.getName());
            member.setAddress(userDto.getAddress());
            Long id = memberService.join(member);
            return new ResponseEntity<>(id,HttpStatus.OK);
        }catch (IllegalStateException e){
            return new ResponseEntity<>("에러가 발생했습니다", HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestBody MemberWithdrawDto userDto){
        try{
            Member member = new Member();
            member.setId(userDto.getId());
            member.setName(userDto.getName());
            memberService.withdraw(member);
            return new ResponseEntity<>("Success",HttpStatus.OK);
        }catch (IllegalStateException e){
            return new ResponseEntity<>("에러가 발생했습니다",HttpStatus.CONFLICT);
        }
    }


}
