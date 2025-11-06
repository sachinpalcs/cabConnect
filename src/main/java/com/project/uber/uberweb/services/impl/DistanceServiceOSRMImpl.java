package com.project.uber.uberweb.services.impl;

import com.project.uber.uberweb.services.DistanceService;
import lombok.Data;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class DistanceServiceOSRMImpl implements DistanceService {


    private static final String OSRM_API_BASE_URL = "https://router.project-osrm.org/route/v1/driving/";
    private static final double EARTH_RADIUS_KM = 6371.0;

    @Override
    public BigDecimal calculateDistance(Point src, Point dest) {
//Call the third party api called OSRM to fetch the distance
        try {
            String uri = src.getX() + "," + src.getY() + ";" + dest.getX() + "," + dest.getY();
            OSRMResponseDto responseDto = RestClient.builder()
                    .baseUrl(OSRM_API_BASE_URL)
                    .build()
                    .get()
                    .uri(uri)
                    .retrieve()
                    .body(OSRMResponseDto.class);

            return responseDto.getRoutes().get(0).getDistance().divide(new BigDecimal("1000"));
        } catch (Exception e) {
//            throw new RuntimeException("Error getting data from OSRM " + e.getMessage());
            System.err.println("OSRM API call failed, falling back to Haversine formula: " + e.getMessage());
            return calculateHaversineDistance(src, dest);

        }
    }

    private BigDecimal calculateHaversineDistance(Point src, Point dest) {
        double lat1 = src.getY();
        double lon1 = src.getX();
        double lat2 = dest.getY();
        double lon2 = dest.getX();

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.asin(Math.sqrt(a));

        double distanceKm = EARTH_RADIUS_KM * c;
        return BigDecimal.valueOf(distanceKm).setScale(2, RoundingMode.HALF_UP);
    }
}

@Data
class OSRMResponseDto {
    private List<OSRMRoute> routes;
}

@Data
class OSRMRoute {
    private BigDecimal distance;
}
