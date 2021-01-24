package com.project.Teampl.dto.tour;

import com.project.Teampl.model.tour.Tour;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter @Setter
public class TourForm {

    private Long idx;

    @NotEmpty(message = "여행지명을 입력해주세요.")
    private String name;
    @NotEmpty(message = "도시명을 입력해주세요.")
    private String city;
    @NotEmpty(message = "도로명을 입력해주세요.")
    private String street;
    @Min(value = 1, message = "건물번호를 입력해주세요.")
    private int buildingNo;
    @Size(min = 1, max = 100, message = "한줄설명을 작성해주세요.")
    private String onelineDesc;
    @Size(min = 1, max = 500, message = "상세설명을 작성해주세요.")
    private String mainDesc;

    public void setTourForm(Tour tour) {
        System.out.println("tour :" + tour);
        this.idx         = tour.getIdx();
        this.name        = tour.getName();
        this.city        = tour.getAddress().getCity();
        this.street      = tour.getAddress().getStreet();
        this.buildingNo  = tour.getAddress().getBuildingNo();
        this.onelineDesc = tour.getOnelineDesc();
        this.mainDesc    = tour.getMainDesc();
    }
}
