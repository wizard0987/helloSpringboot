package com.project.Teampl.model.user;

import com.project.Teampl.dto.user.EditUserForm;
import com.project.Teampl.dto.user.JoinForm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="user")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    private String username;
    private String userid;
    private String userpw;
    private String email;
    private String role;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime regdate;

    public static User createUser(JoinForm form) {
        User user = new User();
        user.setUsername(form.getUsername());
        user.setUserid(form.getUserid());
        user.setUserpw(form.getUserpw());
        user.setEmail(form.getEmail());

        return user;
    }

    public EditUserForm setEditForm(User findUser) {
        EditUserForm form = new EditUserForm();
        form.setUsername(findUser.getUsername());
        form.setUserid(findUser.getUserid());
        form.setUserpw(findUser.getUserpw());
        form.setEmail(findUser.getEmail());
        form.setRole(findUser.getRole());

        return form;
    }
}
