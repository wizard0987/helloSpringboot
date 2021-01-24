package com.project.Teampl.controller;

import com.project.Teampl.dto.tour.TourForm;
import com.project.Teampl.model.tour.Address;
import com.project.Teampl.model.tour.Tour;
import com.project.Teampl.model.user.User;
import com.project.Teampl.service.TourService;
import com.project.Teampl.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TourController {
    private final TourService tourService;
    private final UserService userService;

    @GetMapping("/tour/new")
    public String newTourForm(Model model) {
        model.addAttribute("tourForm", new TourForm());
        return "tour/createTourForm";
    }

    @PostMapping("/tour/new")
    public String createLoc(@Valid TourForm tourForm, BindingResult result) {
        if (result.hasErrors()) {
            return "/tour/createTourForm";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String useridx = authentication.getName();
        User findUser = userService.findById(Long.parseLong(useridx));

        Tour tour = Tour.createTour(tourForm.getName(),
                new Address(tourForm.getCity(), tourForm.getStreet(), tourForm.getBuildingNo()),
                tourForm.getOnelineDesc(), tourForm.getMainDesc(), findUser);
        tourService.save(tour);

        return "redirect:/tour/all";
    }

    @GetMapping("/tour/all")
    public String tourList(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String useridx = authentication.getName();

        List<Tour> tourList = tourService.findAll();
        User findUser = userService.findById(Long.parseLong(useridx));
        model.addAttribute("tourList", tourList);
        model.addAttribute("useridx", findUser.getIdx());

        return "tour/tourList";
    }

    @GetMapping("/tour/edit/{idx}")
    public String editTourForm(@PathVariable("idx") Long touridx, Model model) {
        Tour findTour = tourService.findById(touridx);
        TourForm tourForm = new TourForm();
        tourForm.setTourForm(findTour);

        model.addAttribute("tourForm", tourForm);

        return "tour/editTourForm";
    }

    @PostMapping("/tour/edit/{idx}")
    public String editTourProc(@PathVariable("idx") Long touridx,
                               @Valid TourForm tourForm, BindingResult result) {
        if (result.hasErrors()) {
            return "tour/editTourForm";
        }

        tourService.updateById(touridx, tourForm);

        return "redirect:/tour/all";
    }

    @PostMapping("tour/delete/{idx}")
    public String deleteLoc(@PathVariable("idx") Long touridx) {
        tourService.deleteById(touridx);
        return "redirect:/tour/all";
    }
}
