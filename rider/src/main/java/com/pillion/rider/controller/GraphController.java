package com.pillion.rider.controller;

import java.util.HashMap;
import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;

import com.pillion.rider.model.GetPathRequest;
import com.pillion.rider.model.Routes;
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
    public Object runGraph(@PathVariable String graphType) {
        System.out.println("Controller!!");
        // Calling graph service function rungraph.
        return graphService.getGraph(graphType);
    }

    @PostMapping("/addRoute")
    public void addRoute(
            @AuthenticationPrincipal(expression = "attributes['name']") String email,
            @RequestBody UserRoute userRoute) {
        Routes routes = new Routes();
        routes.setEmail(email);
        routes.setDistances(userRoute.getDistances());
        routes.setLocations(userRoute.getLocations());
        graphService.addRoute(routes);
    }

    @PostMapping("/paths")
    public LinkedList<LinkedList<Integer>> getPaths(@RequestBody GetPathRequest getPathRequest)
    {
        return graphService.getPaths(getPathRequest.getSource(), getPathRequest.getTarget());
    }

    @PostMapping("/riders")
    public HashMap<String, LinkedList<String>> getRiders(@RequestBody GetPathRequest getPathRequest)
    {
        return graphService.getRiders(getPathRequest);
    }

}
