package com.pillion.rider.service;

import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pillion.rider.model.Graph;
import com.pillion.rider.model.GraphMatrix;
import com.pillion.rider.model.Routes;
import com.pillion.rider.repository.GraphMapperRepository;
import com.pillion.rider.repository.GraphMatrixRepository;
import com.pillion.rider.repository.GraphRepository;
import com.pillion.rider.repository.RoutesRepository;
import com.pillion.rider.util.GraphConverter;

//Creating service class for spring boot that handles graph methods, marking it using marker annotation service.
@Service
public class GraphService {

    @Autowired
    private GraphRepository graphRepository;

    @Autowired
    private RoutesRepository routesRepository;

    @Autowired
    private GraphMatrixRepository graphMatrixRepository;

    @Autowired
    private GraphMapperRepository graphMapperRepository;

    private GraphConverter graphConverter = new GraphConverter();

    public Object getGraph(String graphType) {
        if(graphType.equals("weighted"))
            return graphRepository.findByGraphType(graphType).orElse(new Graph()).getGraph();
        else if(graphType.equals("matrix"))
            return graphMatrixRepository.findByGraphType(graphType).orElse(new GraphMatrix()).getGraph();
        return null;
    }

    public void addRoute(Routes routes) {

        LinkedList<String> locations = routes.getLocations();
        LinkedList<Integer> distances = routes.getDistances();

        try {
            Graph graph = graphRepository.findByGraphType("weighted").orElse(new Graph());
            if(graph.getGraphType()==null)
                graph.setGraphType("weighted");

            for (int i = 0; i < locations.size() - 1; i++)
                graph.addConnection(locations.get(i), locations.get(i + 1), distances.get(i));

            graph.display();
            graphRepository.save(graph);
            GraphMatrix graphMatrix = graphMatrixRepository.findByGraphType("matrix").orElse(new GraphMatrix());
            if(graphMatrix.getGraphType()==null)
                graphMatrix.setGraphType("matrix");
            graphConverter.setGraphMapperRepository(graphMapperRepository);
            graphMatrix.setGraph(graphConverter.convertToMatrix(graph));
            graphMatrixRepository.save(graphMatrix);
            routesRepository.save(routes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
