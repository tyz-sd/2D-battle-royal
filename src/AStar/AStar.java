package AStar;

import java.util.*;
import map.GameMap;
public class AStar {
	public final static int block_size = 32;
	private int height;
	private int width;
	final static int[]dx = {0,0,1,1,1,-1,-1,-1};
	final static int[]dy = {1,-1,-1,1,0,0,-1,1};
	public int[][] graph;
	public AStar(GameMap gameMap) {
		this.graph = gameMap.getPlayerPassableMap();
		this.height = gameMap.getHeight()/block_size;
		this.width = gameMap.getWidth()/block_size;
	}
	// AStar�㷨Ѱ·
	public LinkedList<Point> search(int st_x, int st_y, int ed_x, int ed_y)  {
		LinkedList<Point> path = new LinkedList<Point>();
		HashSet<Point> searched = new HashSet<Point>();
		HashSet<Point> searching = new HashSet<Point>();
		PriorityQueue<Point> queue = new PriorityQueue<Point>();
		Point[][] pointGraph = new Point[height][width];
		for (int i=0;i<height;i++)
			for (int j=0;j<width;j++) {
				pointGraph[i][j] = new Point(i,j);
				pointGraph[i][j].h = 10* (Math.abs(ed_x-i) + Math.abs(ed_y-j));
				pointGraph[i][j].f = pointGraph[i][j].g + pointGraph[i][j].h;
			}
		searching.add(pointGraph[st_x][st_y]);
		queue.add(pointGraph[st_x][st_y]);
		pointGraph[st_x][st_y].g = 0;
		pointGraph[st_x][st_y].f = pointGraph[st_x][st_y].g + pointGraph[st_x][st_y].h;
		while(!queue.isEmpty()) {
			Point p = queue.poll();
			if (p.equals(new Point(ed_x,ed_y)))
				break;
			for (int i=0;i<8;i++) {
				int x_ = p.x + dx[i];
				int y_ = p.y + dy[i];
				if (x_<0||x_>=width||y_<0||y_>=height)
					continue;
				if (this.graph[x_][y_] == 0)
					continue;
				Point p1 = pointGraph[x_][y_];
				int dis = 10;
				if (dx[i]!=0 && dy[i]!=0)
					dis = 14;
				if (searched.contains(p1))
					continue;
				else if (searching.contains(p1)) {
					if (pointGraph[x_][y_].g > pointGraph[p.x][p.y].g + dis) {
						queue.remove(pointGraph[x_][y_]);
						pointGraph[x_][y_].g = pointGraph[p.x][p.y].g + dis;
						pointGraph[x_][y_].f = pointGraph[x_][y_].g + pointGraph[x_][y_].h;
						pointGraph[x_][y_].father = pointGraph[p.x][p.y];
						queue.add(pointGraph[x_][y_]);
					}
				}
				else {
					pointGraph[x_][y_].g = pointGraph[x_][y_].g + dis;
					pointGraph[x_][y_].f = pointGraph[x_][y_].g + pointGraph[x_][y_].h;
					pointGraph[x_][y_].father = pointGraph[p.x][p.y];
					searching.add(pointGraph[x_][y_]);
					queue.add(pointGraph[x_][y_]);
				}
				if (pointGraph[x_][y_].equals(new Point(ed_x,ed_y))) {
					queue.clear();
					break;
				}
			}
			searching.remove(p);
			searched.add(p);
		}
		
		
		Point p = pointGraph[ed_x][ed_y];
		path.add(p);
		while (!p.father.equals(p)) {
			path.add(p.father);
			p = p.father;
		}
		Object[] temp = path.toArray();
		path.clear();
		for (int i=temp.length-1;i>=0;i--)
			path.add((Point)temp[i]);
		return path;
	}
}


