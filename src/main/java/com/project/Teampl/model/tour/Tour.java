package com.project.Teampl.model.tour;

import com.project.Teampl.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="tour")
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    private String name;
    @Embedded
    private Address address;
    private String onelineDesc;
    @Lob
    private String mainDesc;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private User user;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime regDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastModifiedDate;

    public static Tour createTour(String name, Address address,
                                  String onelineDesc, String mainDesc, User user) {
        Tour tour = new Tour();
        tour.setName(name);
        tour.setAddress(address);
        tour.setOnelineDesc(onelineDesc);
        tour.setUser(user);
        tour.setMainDesc(mainDesc);
        tour.setRegDate(LocalDateTime.now());

        return tour;
    }
}
