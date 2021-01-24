package com.project.Teampl.model.tour;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {
    private String city;
    private String street;
    private int buildingNo;

//    protected Address() {    }

    public Address(String city, String street, int buildingNo) {
        this.city = city;
        this.street = street;
        this.buildingNo = buildingNo;
    }
}
