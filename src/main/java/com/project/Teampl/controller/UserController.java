package com.project.Teampl.controller;

import com.project.Teampl.config.auth.PrincipalDetails;
import com.project.Teampl.dto.user.EditUserForm;
import com.project.Teampl.dto.user.JoinForm;
import com.project.Teampl.dto.user.LoginForm;
import com.project.Teampl.model.user.User;
import com.project.Teampl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    //일반 회원 세션 정보 확인
    @GetMapping("/test/login/user")
    public @ResponseBody String testLogin(Authentication authentication,
                                          @AuthenticationPrincipal PrincipalDetails userDetails) {
        System.out.println("================ /test/login/user =================");
//        System.out.println("authentication : " + authentication.getPrincipal()); // 암호화 된 패스워드를 제공하지 않는 특징이 있음
        PrincipalDetails userDetails2 = (PrincipalDetails) authentication.getPrincipal();

        System.out.println("authentication : " + authentication);
        System.out.println("userDetails.getUser() : " + userDetails.getUser());
        System.out.println("userDetails2.getUser() : " + userDetails2.getUser());

        return userDetails.toString();
    }

    // Oauth 계정 세션 정보 확인
    @GetMapping("/test/login/oauth")
    public @ResponseBody String testOauthLogin(Authentication authentication,
                                               @AuthenticationPrincipal OAuth2User oauth) {
        System.out.println("================ /test/login/oauth =================");
        OAuth2User oauth2 = (OAuth2User) authentication.getPrincipal();

        System.out.println("authentication : " + authentication);
        System.out.println("oauth.getAttributes() : " + oauth.getAttributes());
        System.out.println("oauth2.getAttributes() : " + oauth2.getAttributes());

        return authentication.toString();
    }

    // 일반, OAuth 계정으로 로그인하면 'PrincipalDetails'로 세션 등록
    @GetMapping("/test/login/all")
    public @ResponseBody String user(Authentication authentication,
                                     @AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("================ /test/login/all =================");

        PrincipalDetails principalDetails2 = (PrincipalDetails) authentication.getPrincipal();

        System.out.println("authentication : " + authentication);
        System.out.println("principalDetails.getAttributes() : " + principalDetails.getAttributes());
        System.out.println("principalDetails2.getAttributes() : " + principalDetails2.getAttributes());

        return principalDetails.toString();
//        return principalDetails.getUser().toString();
    }

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
    public String myPage(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println("authentication : " + authentication);
//        String useridx = authentication.getName();
        System.out.println("principalDetails : " + principalDetails);
        Long useridx = principalDetails.getUser().getIdx();

        User findUser = userService.findById(useridx);
        EditUserForm editUserForm = findUser.setEditForm(findUser);

        model.addAttribute("editUserForm", editUserForm);

        return "user/editMyForm";
    }

    // 회원 수정(마이페이지)
    @PostMapping("/user/myPage")
    public String myPageProc(@Valid EditUserForm editUserForm, BindingResult result,
                             @AuthenticationPrincipal PrincipalDetails principalDetails) {

        Long useridx = principalDetails.getUser().getIdx();
        User findUser = userService.findById(useridx);

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
