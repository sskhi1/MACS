/*******************************************************************************
 * File: Trailblazer.cpp
 *
 * Implementation of the graph algorithms that comprise the Trailblazer
 * assignment.
 */

#include "Trailblazer.h"
#include "TrailblazerGraphics.h"
#include "TrailblazerTypes.h"
#include "TrailblazerPQueue.h"
#include "random.h"
#include "PQueue.h"
#include "map.h"
#include "hashset.h"

using namespace std;

/* Function: shortestPath
 * 
 * Finds the shortest path between the locations given by start and end in the
 * specified world.	 The cost of moving from one edge to the next is specified
 * by the given cost function.	The resulting path is then returned as a
 * Vector<Loc> containing the locations to visit in the order in which they
 * would be visited.	If no path is found, this function should report an
 * error.
 *
 * In Part Two of this assignment, you will need to add an additional parameter
 * to this function that represents the heuristic to use while performing the
 * search.  Make sure to update both this implementation prototype and the
 * function prototype in Trailblazer.h.
 */

//							Dijkstra's Algorithm

/*
struct Node{ // I chose saving each node's parent, color and candidate distance togeter as a struct.
	Loc parent;
	Color c;
	double distance;
};


Loc p = makeLoc(-1, -1); // just for initialization.
void initAll(Grid<Node>& data){
	for(int i = 0; i < data.numRows(); i++){
		for(int j = 0; j < data.numCols(); j++){
			data[i][j].c = GRAY; // Color all nodes gray.
			data[i][j].parent = p;
			data[i][j].distance = double(INT_MAX);
		}
	}
}

void findResultVec(Vector<Loc>& res, Loc end, Grid<Node>& data){
	Vector<Loc> fakePath;
	while(end != p){
        fakePath.add(end);
        end = data[end.row][end.col].parent;
    }

	for(int i = fakePath.size() - 1; i >= 0; i--) {
        res.add(fakePath[i]);
    }
}

Vector<Loc>
shortestPath(Loc start,
             Loc end,
             Grid<double>& world,
             double costFn(Loc from, Loc to, Grid<double>& world)) {
	Vector<Loc> res;
	TrailblazerPQueue<Loc> pq;

	Grid<Node> data(world.numRows(), world.numCols());
	initAll(data);

	data[start.row][start.col].c = YELLOW;
	colorCell(world, start, YELLOW);
	data[start.row][start.col].distance = 0;
	pq.enqueue(start, 0);
	while(!pq.isEmpty()){
		Loc curr = pq.dequeueMin();
		data[curr.row][curr.col].c = GREEN;
		colorCell(world, curr, GREEN);
		if(curr == end) break;
		for(int i = curr.row - 1; i <= curr.row + 1; i++){
            for(int j = curr.col - 1; j <= curr.col + 1; j++){	
                if (world.inBounds(i, j) && !(i == curr.row && j == curr.col)) {
					Loc next = makeLoc(i, j);
                    if (data[i][j].c == GRAY) {
                        data[i][j].c = YELLOW;
                        colorCell(world, next, YELLOW);
                        data[i][j].distance = costFn(curr, next, world) + data[i][j].distance;
                        data[i][j].parent = curr;
                        pq.enqueue(next, costFn(curr, next, world) + data[i][j].distance);
                    }
					if((data[i][j].c == YELLOW) && (data[i][j].distance > costFn(curr, next, world) + data[curr.row][curr.col].distance)) {
                        data[i][j].distance = costFn(curr, next, world) + data[curr.row][curr.col].distance;
                        data[i][j].parent = curr;
                        pq.decreaseKey(next, costFn(curr, next, world) + data[curr.row][curr.col].distance);
                    }
                }
            }
        }
	}

	findResultVec(res, end, data);

    return res;
}
*/

//								A* search

struct Node{ // I chose saving each node's parent, color and candidate distance togeter as a struct.
	Loc parent;
	Color c;
	double distance;
};


Loc p = makeLoc(-1, -1); // just for initialization.
void initAll(Grid<Node>& data){
	for(int i = 0; i < data.numRows(); i++){
		for(int j = 0; j < data.numCols(); j++){
			data[i][j].c = GRAY; // Color all nodes gray.
			data[i][j].parent = p;
			data[i][j].distance = double(INT_MAX);
		}
	}
}

void findResultVec(Vector<Loc>& res, Loc end, Grid<Node>& data){
	Vector<Loc> fakePath;
	while(end != p){
        fakePath.add(end);
        end = data[end.row][end.col].parent;
    }

	for(int i = fakePath.size() - 1; i >= 0; i--) {
        res.add(fakePath[i]);
    }
}

Vector<Loc>
shortestPath(Loc start, Loc end,
			Grid<double>& world,
			double costFunction(Loc one, Loc two, Grid<double>& world),
			double heuristic(Loc start, Loc end, Grid<double>& world)) {
	Vector<Loc> res;
	TrailblazerPQueue<Loc> pq;

	Grid<Node> data(world.numRows(), world.numCols());
	initAll(data);

	data[start.row][start.col].c = YELLOW;
	colorCell(world, start, YELLOW);
	data[start.row][start.col].distance = 0;
	pq.enqueue(start, heuristic(start, end, world));
	while(!pq.isEmpty()){
		Loc curr = pq.dequeueMin();
		data[curr.row][curr.col].c = GREEN;
		colorCell(world, curr, GREEN);
		if(curr == end) break;
		for(int i = curr.row - 1; i <= curr.row + 1; i++){
            for(int j = curr.col - 1; j <= curr.col + 1; j++){	
                if (world.inBounds(i, j) && !(i == curr.row && j == curr.col)) {
					Loc next = makeLoc(i, j);
                    if (data[i][j].c == GRAY) {
                        data[i][j].c = YELLOW;
                        colorCell(world, next, YELLOW);
                        data[i][j].distance = costFunction(curr, next, world) + data[i][j].distance;
                        data[i][j].parent = curr;
                        pq.enqueue(next, costFunction(curr, next, world) + data[i][j].distance + heuristic(next, end, world));
                    }
					if((data[i][j].c == YELLOW) && (data[i][j].distance > costFunction(curr, next, world) + data[curr.row][curr.col].distance)) {
                        data[i][j].distance = costFunction(curr, next, world) + data[curr.row][curr.col].distance;
                        data[i][j].parent = curr;
                        pq.decreaseKey(next, costFunction(curr, next, world) + data[curr.row][curr.col].distance + heuristic(next, end, world));
                    }
                }
            }
        }
	}

	findResultVec(res, end, data);

    return res;
}

// aq cluster-ebis shenaxva yvelgan vcade rac momapiqrda(struqt, map, hashmap...) magram mainc 
// nela mushaobs large da huge world-ebze rac ar vici chemi bralia tu ara da shecdomad itvleba tu ara.
// amitom extension-shi davwere union find struqtura romelic swrapad mushaobs (amastan shedarebit).
Set<Edge> createMaze(int numRows, int numCols) {
	Grid<Loc> mazeGrid(numRows, numCols);
	Set<Edge> maze;
	Map<Loc, HashSet<Loc>> cluster;
	for(int i = 0; i < numRows; i++){
		for(int j = 0; j < numCols; j++){
			Loc curr = makeLoc(i, j);
			mazeGrid[i][j] = curr;
			HashSet<Loc> clust;
			clust += curr;
			cluster.put(curr, clust);
		}
	}
	
	PriorityQueue<Edge> pq;
	for(int i = 0; i < numRows; i++){
		for(int j = 0; j < numCols; j++){
			Loc curr = mazeGrid[i][j];
			if(mazeGrid.inBounds(i, j - 1)){
				Loc left = mazeGrid[i][j-1];
				Edge e = makeEdge(curr, left);
				pq.enqueue(e, randomReal(0, 1));
			}
			if(mazeGrid.inBounds(i, j + 1)){
				Loc right = mazeGrid[i][j+1];
				Edge e = makeEdge(curr, right);
				pq.enqueue(e, randomReal(0, 1));
			}
			if(mazeGrid.inBounds(i - 1, j)){
				Loc up = mazeGrid[i-1][j];
				Edge e = makeEdge(curr, up);
				pq.enqueue(e, randomReal(0, 1));
			}
			if(mazeGrid.inBounds(i + 1, j)){
				Loc down = mazeGrid[i+1][j];
				Edge e = makeEdge(curr, down);
				pq.enqueue(e, randomReal(0, 1));
			}
		}
	}
	
	while(pq.size() > 0){
		Edge e = pq.dequeue();
		if(cluster[e.start] != cluster[e.end]){
			cluster[e.start] += cluster[e.end];
			cluster[e.end] += cluster[e.start];
		
			foreach(Loc node in cluster[e.start]){
                cluster[node] += cluster[e.end];
            }
			foreach(Loc node in cluster[e.end]){
                cluster[node] += cluster[e.start];
            }
			
			maze.add(e);
		}
	}
	
    return maze;
}
