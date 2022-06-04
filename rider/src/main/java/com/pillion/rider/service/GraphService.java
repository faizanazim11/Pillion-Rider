package com.pillion.rider.service;

import java.util.LinkedList;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pillion.rider.model.Graph;
import com.pillion.rider.model.UserRoute;
import com.pillion.rider.repository.GraphRepository;

//Creating service class for spring boot that handles graph methods, marking it using marker annotation service.
@Service
public class GraphService {

    @Autowired
    private GraphRepository graphRepository;

    public TreeMap<String, TreeMap<String, Integer>> getGraph(String graphType) {
        return graphRepository.findByGraphType(graphType).orElse(new Graph()).getGraph();
    }

    public void addRoute(UserRoute userRoute) {
        LinkedList<String> locations = userRoute.getLocations();
        LinkedList<Integer> distances = userRoute.getDistances();

        try {
            Graph graph = graphRepository.findByGraphType("weighted").orElse(new Graph());
            if(graph.getGraphType()==null)
                graph.setGraphType("weighted");

            for (int i = 0; i < locations.size() - 1; i++)
                graph.addConnection(locations.get(i), locations.get(i + 1), distances.get(i));

            graph.display();
            graphRepository.save(graph);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
