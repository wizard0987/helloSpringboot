package com.project.Teampl.dto.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class EditUserForm {
    @NotEmpty(message = "이름을 입력해주셔야 합니다.")
    private String username;
    @NotEmpty(message = "아이디를 입력해주셔야 합니다.")
    private String userid;
    @NotEmpty(message = "패스워드를 입력해주셔야 합니다.")
    private String userpw;
    @Email(message = "이메일 형식에 맞게 작성해주세요")
    private String email;
    private String role;
}
