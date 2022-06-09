package com.pillion.rider.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pillion.rider.model.GetPathRequest;
import com.pillion.rider.model.Graph;
import com.pillion.rider.model.GraphMapper;
import com.pillion.rider.model.GraphMatrix;
import com.pillion.rider.model.Routes;
import com.pillion.rider.model.User;
import com.pillion.rider.repository.GraphMapperRepository;
import com.pillion.rider.repository.GraphMatrixRepository;
import com.pillion.rider.repository.GraphRepository;
import com.pillion.rider.repository.RoutesRepository;
import com.pillion.rider.repository.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

    private GraphConverter graphConverter = new GraphConverter();

    private LinkedList<LinkedList<Integer>> paths;

    public Object getGraph(String graphType) {
        if (graphType.equals("weighted"))
            return graphRepository.findByGraphType(graphType).orElse(new Graph()).getGraph();
        else
            return graphMatrixRepository.findByGraphType(graphType).orElse(new GraphMatrix()).getGraph();
    }

    public void addRoute(Routes routes) {

        routesRepository.save(routes);

        try {
            Graph graph = graphRepository.findByGraphType("weighted").orElse(new Graph());
            graph.setGraphType("weighted");
            graph.setGraph(new TreeMap<>());
            List<Routes> allRoutes = routesRepository.findAll();
            for (Routes routes2 : allRoutes) {
                LinkedList<String> locations = routes2.getLocations();
                LinkedList<Integer> distances = routes2.getDistances();
                for (int i = 0; i < locations.size() - 1; i++)
                    graph.addConnection(locations.get(i), locations.get(i + 1), distances.get(i));
            }
            graph.display();
            graphRepository.save(graph);
            GraphMatrix graphMatrix = graphMatrixRepository.findByGraphType("matrix").orElse(new GraphMatrix());
            if (graphMatrix.getGraphType() == null)
                graphMatrix.setGraphType("matrix");
            graphConverter.setGraphMapperRepository(graphMapperRepository);
            graphMatrix.setGraph(graphConverter.convertToMatrix(graph));
            graphMatrixRepository.save(graphMatrix);
            GraphMatrix graphBinary = graphMatrixRepository.findByGraphType("binary").orElse(new GraphMatrix());
            if (graphBinary.getGraphType() == null)
                graphBinary.setGraphType("binary");
            graphBinary.setGraph(graphConverter.convertToBinary(graph));
            graphMatrixRepository.save(graphBinary);
            routesRepository.save(routes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stackCopyUtil(Stack<Integer> stack) {
        LinkedList<Integer> ll = new LinkedList<>();
        while (!stack.isEmpty())
            ll.push(stack.pop());
        paths.push(ll);
    }

    @SuppressWarnings("unchecked")
    private void getPathsUtil(Integer sourceIndex, Integer targetIndex, Stack<Integer> tempStack,
            Integer[][] binaryGraph) {
        for (int i = 0; i < binaryGraph.length; i++) {
            if (binaryGraph[tempStack.peek()][i] == 1) {
                if (i == targetIndex) {
                    tempStack.push(i);
                    stackCopyUtil((Stack<Integer>) tempStack.clone());
                    tempStack.pop();
                } else if (!tempStack.contains(i)) {
                    tempStack.push(i);
                    getPathsUtil(sourceIndex, targetIndex, (Stack<Integer>) tempStack.clone(), binaryGraph);
                    tempStack.pop();
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public LinkedList<LinkedList<Integer>> getPaths(String source, String target) {
        paths = new LinkedList<>();
        Stack<Integer> dfsStack = new Stack<>();
        GraphMapper graphMapper = graphMapperRepository.findById("graphMap").orElse(null);
        if (graphMapper == null)
            return paths;
        if (!graphMapper.getGraphMap().containsKey(source) || !graphMapper.getGraphMap().containsKey(target))
            return paths;
        Integer sourceIndex = graphMapper.getGraphMap().get(source);
        Integer targetIndex = graphMapper.getGraphMap().get(target);
        GraphMatrix graphBinary = graphMatrixRepository.findByGraphType("binary").orElse(null);
        if (graphBinary == null)
            return paths;
        dfsStack.push(sourceIndex);
        Integer[][] binaryGraph = graphBinary.getGraph();
        for (int i = 0; i < binaryGraph.length; i++) {
            if (binaryGraph[sourceIndex][i] == 1) {
                if (i == targetIndex) {
                    dfsStack.push(i);
                    stackCopyUtil((Stack<Integer>) dfsStack.clone());
                    dfsStack.pop();
                } else {
                    dfsStack.push(i);
                    getPathsUtil(sourceIndex, targetIndex, (Stack<Integer>) dfsStack.clone(), binaryGraph);
                    dfsStack.pop();
                }
            }
        }
        return paths;
    }

    private Integer max(Integer a, Integer b) {
        return (a > b) ? a : b;
    }

    private Integer lcs(Character[] X, Character[] Y) {
        Integer m = X.length;
        Integer n = Y.length;
        Integer L[][] = new Integer[m + 1][n + 1];
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0 || j == 0)
                    L[i][j] = 0;
                else if (X[i - 1] == Y[j - 1])
                    L[i][j] = L[i - 1][j - 1] + 1;
                else
                    L[i][j] = max(L[i - 1][j], L[i][j - 1]);
            }
        }
        return L[m][n];
    }

    private LinkedList<Integer> convertToIndexes(LinkedList<String> route) {
        LinkedList<Integer> inRoute = new LinkedList<>();
        GraphMapper graphMapper = graphMapperRepository.findById("graphMap").orElse(null);
        if (graphMapper == null)
            return inRoute;
        for (String location : route)
            inRoute.push(graphMapper.getGraphMap().get(location));
        return inRoute;
    }

    public HashMap<String, LinkedList<String>> getRiders(GetPathRequest getPathRequest) {
        HashMap<String, LinkedList<String>> riders = new HashMap<>();
        getPaths(getPathRequest.getSource(), getPathRequest.getTarget());
        List<Routes> routes = routesRepository.findAll();
        for (LinkedList<Integer> path : paths) {
            System.out.println("Path size: " + path.size());
            Character[] X = new Character[path.size()];
            for (int i = 0; i < path.size(); i++)
                X[i] = Character.forDigit(path.get(i), 10);
            for (Routes route : routes) {
                Character[] Y = new Character[route.getLocations().size()];
                LinkedList<Integer> inroute = convertToIndexes(route.getLocations());
                for (int i = 0; i < route.getLocations().size(); i++) {
                    Y[i] = Character.forDigit(inroute.get(i), 10);
                }
                List<Character> rev = Arrays.asList(Y);
                Collections.reverse(rev);
                Y = rev.toArray(Y);
                Integer lcsLength = lcs(X, Y);
                System.out.println(lcsLength + " " + route.getEmail());
                if (lcsLength == path.size())
                    riders.put(route.getEmail(), route.getLocations());
            }
        }
        return riders;
    }

}
