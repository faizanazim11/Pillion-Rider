package com.pillion.rider.controller;

import java.util.ArrayList;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pillion.rider.model.StopsDetails;
import com.pillion.rider.service.StopsService;

@RestController
public class StopsController {
    
    @Autowired
    private StopsService stopsService;

    @GetMapping("/stops")
    public TreeMap<String, ArrayList<Integer>> getStops() {
        return stopsService.getStops();
    }

    @PostMapping("/stops")
    public void setStops(@RequestBody StopsDetails stopsDetails) {
        stopsService.setStops(stopsDetails);
    }
    
}
