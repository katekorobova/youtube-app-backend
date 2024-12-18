package com.verycoolprojects.youtubeapp.dto.search;

import lombok.Getter;

@Getter
public enum LocationData {
    SYDNEY("-33.8,151", "60km"),
    MELBOURNE("-37.9,145", "60km"),
    BRISBANE("-34.5, 148.9", "25km"),
    ADELAIDE("-34.9, 138.6", "60km"),
    PERTH("-32.1,115.9", "60km"),
    TASMANIA("-42,146.5", "300km"),
    NEW_ZEALAND("-42,172", "1000km");


    private final String location;
    private final String locationRadius;

    LocationData(String location, String locationRadius) {
        this.location = location;
        this.locationRadius = locationRadius;
    }
}
