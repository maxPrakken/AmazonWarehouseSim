package com.nhlstenden.amazonsimulatie.models;

import com.nhlstenden.amazonsimulatie.dijkstra.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/*
 * Deze class is een versie van het model van de simulatie. In dit geval is het
 * de 3D wereld die we willen modelleren (magazijn). De zogenaamde domain-logic,
 * de logica die van toepassing is op het domein dat de applicatie modelleerd, staat
 * in het model. Dit betekent dus de logica die het magazijn simuleert.
 */
public class World implements Model {
    /*
     * De wereld bestaat uit objecten, vandaar de naam worldObjects. Dit is een lijst
     * van alle objecten in de 3D wereld. Deze objecten zijn in deze voorbeeldcode alleen
     * nog robots. Er zijn ook nog meer andere objecten die ook in de wereld voor kunnen komen.
     * Deze objecten moeten uiteindelijk ook in de lijst passen (overerving). Daarom is dit
     * een lijst van Object3D onderdelen. Deze kunnen in principe alles zijn. (Robots, vrachrtwagens, etc)
     */
    private List<Object3D> worldObjects;

    /*
     * Dit onderdeel is nodig om veranderingen in het model te kunnen doorgeven aan de controller.
     * Het systeem werkt al as-is, dus dit hoeft niet aangepast te worden.
     */
    PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private Graph graph;
    List<Node> nodes = new ArrayList<>();

    /*
     * De wereld maakt een lege lijst voor worldObjects aan. Daarin wordt nu één robot gestopt.
     * Deze methode moet uitgebreid worden zodat alle objecten van de 3D wereld hier worden gemaakt.
     */
    public World() {
        graph = new Graph();
        this.worldObjects = new ArrayList<>();
        BuildGraph();
        initGraph();
        this.worldObjects.add(new Robot(graph));
        this.worldObjects.add(new Robot(graph));

        CreateGrid(10, 10, 10);
    }

    public void CreateGrid(int width, int height, int tilesize) {
        for(int w = 0; w < width; w++) {
            for(int h = 0; h < height; h++) {
                boolean isdark = false;
                if(h % 2 == 0) {
                    if(w % 2 == 2) 
                        isdark = true;
                    else
                        isdark = false;
                }else {
                    if(h % 2 == 0)
                        isdark = false;
                    else
                        isdark = true;
                }


                this.worldObjects.add(new Plane(w * tilesize, h * tilesize, tilesize, isdark));
            }
        }
    }

    public void initGraph(){

        for (Node n: nodes){
            graph.addMap(n);
        }

        int columnCounter = 1;
        int rowCounter = 1;
        int nodeCounter= 0;
        final int down = 9;
        final int right = 1;

        for(Node n: nodes){   
            
                boolean actionPerformed = false;

                if((columnCounter < 9 && rowCounter < 5) && !actionPerformed){
                    graph.addEdge(new Edge(graph.getNodeByIndex(n.getIndex()), graph.getNodeByIndex((n.getIndex() + right)), 1));
                    graph.addEdge(new Edge(graph.getNodeByIndex(n.getIndex()), graph.getNodeByIndex((n.getIndex() + down)), 1));
                    System.out.print("Rechts & Onder " + columnCounter + " " + rowCounter + " ");
                    columnCounter++;
                    System.out.print("Updated: " + columnCounter + " " + rowCounter + " ");
                    actionPerformed = true;
                }

                if(columnCounter == 9 && rowCounter < 5 && !actionPerformed){
                    graph.addEdge(new Edge(graph.getNodeByIndex(n.getIndex()),graph.getNodeByIndex(n.getIndex() + down), 1));
                    System.out.print("Onder " + columnCounter + " " + rowCounter + " ");
                    columnCounter = 1;
                    rowCounter++;
                    actionPerformed = true;
                }

                if(rowCounter == 5 && columnCounter < 9 && !actionPerformed){
                    graph.addEdge(new Edge(graph.getNodeByIndex(n.getIndex()),graph.getNodeByIndex(n.getIndex() + right), 1));
                    System.out.print("Rechts " + columnCounter + " " + rowCounter + " ");
                    columnCounter++;
                    actionPerformed = true;
                }  
            System.out.println("Node " + nodeCounter);   
                nodeCounter++;
        }

    }    



    private void BuildGraph(){
        final int rowSize = 5;
        final int colSize = 5; 
        final int rowSpacing = 3;
        final int colSpacing = 3;
        final int offset = 5;
        final int nodeVal = 1;
        final int sourceNode = 40;
        int counter = 0;

        for(int i = 0; i < rowSize; i++){
            for(int j = 0; j< colSize; j++){
                if(counter == sourceNode){
                    nodes.add(new Node("Source", counter, i*rowSpacing + offset, j*colSpacing + offset, nodeVal));
                    counter++;
                }
                else{
                    nodes.add(new Node("Node", counter,i*rowSpacing + offset, j*colSpacing + offset, nodeVal));
                    counter++;
                }
            }
        }
    }
 
    /*
     * Deze methode wordt gebruikt om de wereld te updaten. Wanneer deze methode wordt aangeroepen,
     * wordt op elk object in de wereld de methode update aangeroepen. Wanneer deze true teruggeeft
     * betekent dit dat het onderdeel daadwerkelijk geupdate is (er is iets veranderd, zoals een positie).
     * Als dit zo is moet dit worden geupdate, en wordt er via het pcs systeem een notificatie gestuurd
     * naar de controller die luisterd. Wanneer de updatemethode van het onderdeel false teruggeeft,
     * is het onderdeel niet veranderd en hoeft er dus ook geen signaal naar de controller verstuurd
     * te worden.
     */
    @Override
    public void update() {
        for (Object3D object : this.worldObjects) {
            if(object instanceof Updatable) {
                if (((Updatable)object).update()) {
                    pcs.firePropertyChange(Model.UPDATE_COMMAND, null, new ProxyObject3D(object));
                }
            }
        }
    }

    /*
     * Standaardfunctionaliteit. Hoeft niet gewijzigd te worden.
     */
    @Override
    public void addObserver(PropertyChangeListener pcl) {
        pcs.addPropertyChangeListener(pcl);
    }

    /*
     * Deze methode geeft een lijst terug van alle objecten in de wereld. De lijst is echter wel
     * van ProxyObject3D objecten, voor de veiligheid. Zo kan de informatie wel worden gedeeld, maar
     * kan er niks aangepast worden.
     */
    @Override
    public List<Object3D> getWorldObjectsAsList() {
        ArrayList<Object3D> returnList = new ArrayList<>();

        for(Object3D object : this.worldObjects) {
            returnList.add(new ProxyObject3D(object));
        }

        return returnList;
    }
}