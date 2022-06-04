package com.pillion.rider.model;

import java.util.TreeMap;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Graph class storing graph structure using TreeMap and generating getter, setter and constructors using lombok annotations.
@Document(collection = "graphs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Graph {

    @Id
    private String id;
    
    // Graph structure.
    private TreeMap<String, TreeMap<String, Integer>> graph;

    private String graphType;

    // Graph class method to print stores graph.
    public void display() {
        if (graph == null) {
            System.out.println("Graph is empty!!!");
            return;
        }
        System.out.println("Size of entyset = " + graph.size());
        System.out.println("[");
        for (Map.Entry<String, TreeMap<String, Integer>> entry : graph.entrySet()) {
            System.out.println("Size of subentryset = " + entry.getValue().size());
            System.out.println(entry.getKey() + ":{");
            for (Map.Entry<String, Integer> subEntry : entry.getValue().entrySet())
                System.out.println("\t" + subEntry.getKey() + ":" + subEntry.getValue() + ",");
            System.out.println("},");
        }
        System.out.println("]");
    }

    public void addConnection(String location1, String location2, Integer distance) {
        if (graph == null)
            graph = new TreeMap<String, TreeMap<String, Integer>>();
        if (graph.containsKey(location1)) {
            if (graph.containsKey(location2)) {
                Integer value = graph.get(location1).get(location2);
                if (value == 0) {
                    graph.get(location1).put(location2, distance);
                    graph.get(location2).put(location1, distance);
                    return;
                }
                value = (value + distance) / 2;
                graph.get(location1).put(location2, value);
                graph.get(location2).put(location1, value);
                return;
            } else {
                TreeMap<String, Integer> node = new TreeMap<>();
                for (String loc : graph.keySet())
                    node.put(loc, 0);
                node.put(location1, distance);
                node.put(location2, 1);
                for (String loc : graph.keySet())
                    graph.get(loc).put(location2, 0);
                graph.get(location1).put(location2, distance);
                graph.put(location2, node);
                return;
            }
        } else {
            if (graph.containsKey(location2)) {
                TreeMap<String, Integer> node = new TreeMap<>();
                for (String loc : graph.keySet())
                    node.put(loc, 0);
                node.put(location2, distance);
                node.put(location1, 1);
                for (String loc : graph.keySet())
                    graph.get(loc).put(location1, 0);
                graph.get(location2).put(location1, distance);
                graph.put(location1, node);
                return;
            } else {
                TreeMap<String, Integer> node1 = new TreeMap<>();
                TreeMap<String, Integer> node2 = new TreeMap<>();
                for (String loc : graph.keySet()) {
                    node1.put(loc, 0);
                    node2.put(loc, 0);
                }
                node1.put(location2, distance);
                node1.put(location1, 1);
                node2.put(location1, distance);
                node2.put(location2, 1);
                for (String loc : graph.keySet()) {
                    graph.get(loc).put(location1, 0);
                    graph.get(loc).put(location2, 0);
                }
                graph.put(location1, node1);
                graph.put(location2, node2);
                return;
            }
        }
    }

}
