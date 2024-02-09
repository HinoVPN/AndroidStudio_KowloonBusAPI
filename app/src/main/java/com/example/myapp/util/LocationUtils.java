package com.example.myapp.util;

import android.location.Location;

import com.example.myapp.data.model.BusRouteStopDetail;

import java.util.List;

public class LocationUtils {
    public static Location findClosestLocation(Location currentLocation, List<Location> locationList) {
        Location closestLocation = null;
        float minDistance = Float.MAX_VALUE;

        for (Location location : locationList) {
            float distance = calculateDistance(currentLocation, location);

            if (distance < minDistance) {
                minDistance = distance;
                closestLocation = location;
            }
        }

        return closestLocation;
    }

    public static Integer findClosestBusStopIndex(Location currentLocation, List<BusRouteStopDetail> busRouteStopDetailList) {
        float minDistance = Float.MAX_VALUE;
        Integer seqIdx = -1;

        for(int i = 0; i< busRouteStopDetailList.size();i++){
            double latitude = busRouteStopDetailList.get(i).latitude;
            double longitude = busRouteStopDetailList.get(i).longitude;
            Location stopLoction = new Location("") {{ setLatitude(latitude); setLongitude(longitude); }};

            float distance = calculateDistance(currentLocation, stopLoction);
            if (distance < minDistance) {
                minDistance = distance;
                seqIdx = i;
            }
        }
        return seqIdx;
    }

    private static float calculateDistance(Location location1, Location location2) {
        float[] results = new float[1];
        Location.distanceBetween(location1.getLatitude(), location1.getLongitude(),
                location2.getLatitude(), location2.getLongitude(), results);
        return results[0];
    }
}
