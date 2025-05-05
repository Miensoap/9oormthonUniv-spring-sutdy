package jpabook.jpashop.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jpabook.jpashop.api.MemberApiControllerV1.CreateMemberResponse;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v2/members")
@RestController
@RequiredArgsConstructor
public class MemberApiControllerV2 {

    private final MemberService memberService;

    @GetMapping
    public MemberListResponse getMembers() {

        // 응답으로 엔티티를 반환하기보다 별도 DTO 사용
        // 엔티티 변경에 대해 독립적
        // 컬렉션을 직접 반환하면 API 스펙 변경이 어려움
        // ex) size ... 루트가 배열 -> 객체가 되는 경우

        return MemberListResponse.of(memberService.findMembers());
    }

    public record MemberListResponse(
            List<MemberResponse> members,
            int count) {

        public static MemberListResponse of(List<Member> members) {
            List<MemberResponse> memberResponses = members.stream()
                    .map(MemberResponse::of)
                    .toList();

            return new MemberListResponse(memberResponses, members.size());
        }
    }

    public record MemberResponse(
            Long id,
            String name,
            Address address) {

        public static MemberResponse of(Member member) {
            return new MemberResponse(member.getId(), member.getName(), member.getAddress());
        }
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @PostMapping
    public CreateMemberResponse saveMember(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.name);
        Address address = new Address(request.city, request.street, request.zipcode);
        member.setAddress(address);

        Long memberId = memberService.join(member);
        return new CreateMemberResponse(memberId);
    }

    public record CreateMemberRequest(
            @NotEmpty String name,
            String city,
            String street,
            String zipcode) {
    }

    @PutMapping("/{id}")
    public UpdateMemberResponse updateMember(
            @PathVariable Long id,
            @RequestBody @Valid UpdateMemberRequest member) {

        memberService.update(id, member.name());
        Member findMember = memberService.findOne(id);

        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    public record UpdateMemberRequest(@NotEmpty String name) {
    }

    public record UpdateMemberResponse(
            Long id,
            String name) {
    }
}
