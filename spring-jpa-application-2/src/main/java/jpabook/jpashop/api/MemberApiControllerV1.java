package jpabook.jpashop.api;

import jakarta.validation.Valid;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/members")
@RestController
@RequiredArgsConstructor
public class MemberApiControllerV1 {

    private final MemberService memberService;

    @GetMapping
    public List<Member> getMembers() {
        return memberService.findMembers();
    }

    @PostMapping
    public CreateMemberResponse saveMember(@RequestBody @Valid Member member) {
        Long memberId = memberService.join(member);
        return new CreateMemberResponse(memberId);
    }

    public record CreateMemberResponse(Long id) {
    }
}
