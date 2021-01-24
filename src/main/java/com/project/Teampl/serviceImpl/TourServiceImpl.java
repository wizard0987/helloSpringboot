package com.project.Teampl.serviceImpl;

import com.project.Teampl.dto.tour.TourForm;
import com.project.Teampl.exception.ResourceNotFoundException;
import com.project.Teampl.model.tour.Address;
import com.project.Teampl.model.tour.Tour;
import com.project.Teampl.repository.TourRepository;
import com.project.Teampl.service.TourService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TourServiceImpl implements TourService {
    private final TourRepository tourRepository;

    @Override
    @Transactional
    public Tour save(Tour tour) {
        return tourRepository.save(tour);
    }

    @Override
    public List<Tour> findAll() {
        return tourRepository.findAll();
    }

    @Override
    public Tour findById(Long tourIdx) {
        return tourRepository.findById(tourIdx)
                .orElseThrow(()-> new ResourceNotFoundException("Tour", "idx", tourIdx));
    }

    @Override
    @Transactional
    public void updateById(Long useridx, TourForm tourForm) {
        Tour findTour = tourRepository.findById(useridx)
                .orElseThrow(() -> new ResourceNotFoundException("Tour", "idx", useridx));
        findTour.setName(tourForm.getName());
        findTour.setAddress(new Address(tourForm.getCity(),
                                        tourForm.getStreet(),
                                        tourForm.getBuildingNo()));
        findTour.setOnelineDesc(tourForm.getOnelineDesc());
        findTour.setMainDesc(tourForm.getMainDesc());
        findTour.setLastModifiedDate(LocalDateTime.now());
    }

    @Override
    @Transactional
    public void deleteById(Long tourIdx) {
        tourRepository.deleteById(tourIdx);
    }
}
