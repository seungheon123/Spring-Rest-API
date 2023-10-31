package hoho.test.api;


import hoho.test.domain.Member;
import hoho.test.dto.MemberJoinDto;
import hoho.test.dto.MemberSignInDto;
import hoho.test.dto.MemberWithdrawDto;
import hoho.test.dto.TokenDto;
import hoho.test.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member API",description = "회원가입, 로그인, 회원탈퇴 API")
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "회원가입")
    @PostMapping("/auth/join")
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

    @PostMapping("/auth/signin")
    public ResponseEntity<?>signIn(@RequestBody MemberSignInDto signInDto){
        try{
            TokenDto tokenDto = memberService.signin(signInDto);
            return new ResponseEntity<>(tokenDto,HttpStatus.OK);
        }catch (IllegalStateException e){
            return new ResponseEntity<>("에러가 발생했습니다",HttpStatus.CONFLICT);
        }
    }

}
