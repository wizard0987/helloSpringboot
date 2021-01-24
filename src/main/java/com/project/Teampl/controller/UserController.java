package com.project.Teampl.controller;

import com.project.Teampl.dto.user.EditUserForm;
import com.project.Teampl.dto.user.JoinForm;
import com.project.Teampl.dto.user.LoginForm;
import com.project.Teampl.model.user.User;
import com.project.Teampl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // 로그인 Form
    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "user/loginForm";
    }

    // 회원 가입 Form
    @GetMapping("/join")
    public String joinForm(Model model) {
        model.addAttribute("joinForm", new JoinForm());
        return "user/joinForm";
    }

    // 회원 가입
    @PostMapping("/join")
    public String joinProc(@Valid JoinForm form, BindingResult result) {

        // 아이디 중복 검사
        validateDuplicateUser(form.getUserid(), result);

        if(result.hasErrors()) {
            return "user/joinForm";
        }

        userService.save(User.createUser(form));
        return "redirect:/login";
    }
    private void validateDuplicateUser(String userid, BindingResult result) {
        User isDuplicateUser = userService.findByUserid(userid);
        if(isDuplicateUser != null) {
            result.rejectValue("userid", "duplicateUserid", "이미 존재하는 아이디입니다.");
        }
    }

    // 회원 수정 Form
    @GetMapping("/user/myPage")
    public String myPage(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String useridx = authentication.getName();

        User findUser = userService.findById(Long.parseLong(useridx));
        EditUserForm editUserForm = findUser.setEditForm(findUser);

        model.addAttribute("editUserForm", editUserForm);

        return "user/editMyForm";
    }

    // 회원 수정(마이페이지)
    @PostMapping("/user/myPage")
    public String myPageProc(@Valid EditUserForm editUserForm, BindingResult result) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String useridx = authentication.getName();

        User findUser = userService.findById(Long.parseLong(useridx));

        // 아이디 중복검사
        if(!editUserForm.getUserid().equals(findUser.getUserid())) {
            validateDuplicateUser(editUserForm.getUserid(), result);
        }

        if(result.hasErrors()) {
            return "user/editMyForm";
        }

        findUser.setUsername(editUserForm.getUsername());
        findUser.setUserid(editUserForm.getUserid());
        findUser.setUserpw(editUserForm.getUserpw());
        findUser.setEmail(editUserForm.getEmail());
        findUser.setRole(editUserForm.getRole());

        userService.updateById(findUser.getIdx(), findUser);

        return "home";
    }

    // 회원 삭제
    @PostMapping("/user/withdraw")
    public String withdraw() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String useridx = authentication.getName();

        userService.deleteById(Long.parseLong(useridx));

        return "redirect:/logout";
    }

    // 전체 회원 조회
    @GetMapping("/admin/all")
    public String admin(Model model){
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "user/userList";
    }

    // 특정 회원 정보 수정 Form
    @GetMapping("/admin/edit/{idx}")
    public String editUser(@PathVariable("idx") Long useridx, Model model) {

        User findUser = userService.findById(useridx);
        EditUserForm editUserForm = findUser.setEditForm(findUser);
        model.addAttribute("editUserForm", editUserForm);

        return "user/editUserForm";
    }

    // 특정 회원 수정(관리자모드)
    @PostMapping("/admin/edit/{idx}")
    public String editUserProc(@PathVariable("idx") Long useridx,
                               @Valid EditUserForm editUserForm, BindingResult result) {
        if(result.hasErrors()) {
            return "/user/editUserForm";
        }

        User setUser = new User();
        setUser.setUsername(editUserForm.getUsername());
        setUser.setUserid(editUserForm.getUserid());
        setUser.setUserpw(editUserForm.getUserpw());
        setUser.setEmail(editUserForm.getEmail());
        setUser.setRole(editUserForm.getRole());

        userService.updateById(useridx, setUser);

        return "redirect:/admin/all";
    }

}
