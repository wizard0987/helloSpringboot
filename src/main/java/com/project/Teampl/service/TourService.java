package com.project.Teampl.service;

import com.project.Teampl.dto.tour.TourForm;
import com.project.Teampl.model.tour.Tour;

import java.util.List;

public interface TourService {
    Tour save(Tour tour);

    List<Tour> findAll();

    Tour findById(Long tourIdx);

    void updateById(Long useridx, TourForm tourForm);

    void deleteById(Long tourIdx);
}
