package hoho.test.service;

import hoho.test.domain.Member;
import hoho.test.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Long join(Member member){
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
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
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()) throw new IllegalStateException("이미 존재하는 회원입니다");
    }





}
