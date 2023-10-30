package hoho.test.service;

import hoho.test.auth.provider.TokenProvider;
import hoho.test.domain.Member;
import hoho.test.dto.MemberSignInDto;
import hoho.test.dto.TokenDto;
import hoho.test.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    @Transactional
    public Long join(Member member){
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    @Transactional
    public TokenDto signin(MemberSignInDto signInDto){
        Member findMember = memberRepository.findByName(signInDto.getName());
        return tokenProvider.generateToken(findMember.getId(),findMember.getName());
    }

    @Transactional
    public void withdraw(Member member){
        Member newMember = checkExistMember(member);
        memberRepository.delete(newMember);
    }

    private Member checkExistMember(Member member) {
        Member findMember = memberRepository.findOne(member.getId());
        if(findMember==null) throw new IllegalStateException("존재하지 않는 회원입니다");
        else return findMember;
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByName(member.getName());
        if(findMember != null) throw new IllegalStateException("이미 존재하는 회원입니다");
    }





}
