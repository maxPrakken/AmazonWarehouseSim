package com.nhlstenden.amazonsimulatie.dijkstra;
import com.nhlstenden.amazonsimulatie.models.*;

import org.springframework.boot.test.IntegrationTest;

import java.util.*;

public class Node implements Comparable<Node>, Object3D, Updatable{
	
	private boolean isStellage = false;
	public Stellage stellage = null;

	public int x, y, z;
	private UUID uuid;

	public String weight;
	public ArrayList<Edge> adjacencies = new ArrayList<Edge>();
	private List<Node> shortestPath = new LinkedList<>();
	public double shortestDistance = Double.POSITIVE_INFINITY;
	public Node parent;
	private double rotationX = 0;
    private double rotationY = 0;
	private double rotationZ = 0;

	public Node(String weight){
		this.weight = weight;
		this.uuid = UUID.randomUUID();
	}

	public Node(String weight, boolean isstellage){
		this.weight = weight;
		this.isStellage = isstellage;
	}

	@Override
    public boolean update() {
		if(stellage != null){
			if(stellage.getX() != this.x && stellage.getZ() != this.z){
			stellage.setPos(x, 1.5, z);
			}
		}
        return true;
	}

	//public void addStellage(Stellage s) {
		//stellage = s;
		//if(stellage.parent != null) {
			//stellage.parent = null;
		//}
		//stellage.setPos(this.x, 1.5, this.z);
	//}

	public List<Node> getShortestPath() {
		return shortestPath;
	}

	public void setShortestPath(List<Node> shortestpath) {
		this.shortestPath = shortestpath; 
	}

	public boolean getIsStellage() {
		return isStellage;
	}

	public void setIsStellage(boolean b) {
		isStellage = b;
	}

	public String toString(){
			return weight;
	}

	public int compareTo(Node other){
		return Double.compare(shortestDistance, other.shortestDistance);
	}
	

	@Override
    public String getUUID() {
        return this.uuid.toString();
    }

    @Override
    public double getX() {
        return this.x;
    }

    @Override
    public double getY() {
        return this.y;
    }

    @Override
    public double getZ() {
        return this.z;
    }

    @Override
    public double getRotationX() {
        return this.rotationX;
    }

    @Override
    public double getRotationY() {
        return this.rotationY;
    }

    @Override
    public double getRotationZ() {
        return rotationZ;
    }
	@Override
	public String getType() {
		if(isStellage == true){
			return "true";
		}else{
		return "false";
		}
		//return Node.class.getSimpleName().toLowerCase();
	}
}