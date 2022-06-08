package com.pillion.rider.service;

import java.util.ArrayList;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pillion.rider.model.Stops;
import com.pillion.rider.model.StopsDetails;
import com.pillion.rider.repository.StopsRepository;

@Service
public class StopsService {
    
    @Autowired
    private StopsRepository stopsRepository;

    public TreeMap<String, ArrayList<Integer>> getStops() {
        return stopsRepository.findById("stops").orElse(new Stops()).getStops();
    }

    public void setStops(StopsDetails stopsDetails) {
        Stops stops = stopsRepository.findById("stops").orElse(new Stops());
        if(stops.getId()==null)
            stops.setId("stops");
        TreeMap<String, ArrayList<Integer>> updatedStops = stops.getStops();
        if(updatedStops==null)
            updatedStops = new TreeMap<>();
        updatedStops.put(stopsDetails.getStreetName(), stopsDetails.getCoords());
        stops.setStops(updatedStops);
        stopsRepository.save(stops);
    }
    
}
