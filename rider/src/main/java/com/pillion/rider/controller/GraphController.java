package com.pillion.rider.controller;

import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pillion.rider.model.UserRoute;
import com.pillion.rider.service.GraphService;

//Rest Controller annotation for spring boot rest controller.
@RestController()
public class GraphController {

    // Creating graph service object in lazy loading method using autowired
    // annotation.
    @Autowired
    private GraphService graphService;

    // Creating get mapping for graph calling function.
    @GetMapping("/graph/{graphType}")
    public TreeMap<String, TreeMap<String, Integer>> runGraph(@PathVariable String graphType) {
        System.out.println("Controller!!");
        // Calling graph service function rungraph.
        return graphService.getGraph(graphType);
    }

    @PostMapping("/addRoute")
    public void addRoute(@RequestBody UserRoute userRoute) {
        graphService.addRoute(userRoute);
    }

}
