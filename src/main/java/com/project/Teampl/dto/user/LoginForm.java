package com.project.Teampl.dto.user;

import com.project.Teampl.model.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class LoginForm {
    @NotEmpty(message = "아이디를 입력해주셔야 합니다.")
    private String userid;
    @NotEmpty(message = "비밀번호를 입력해주셔야 합니다.")
    private String userpw;
    private String auth;

    public LoginForm() { }

    public LoginForm(User user) {
        this.userid = userid;
        this.userpw = userpw;
        this.auth = auth;
    }
}
