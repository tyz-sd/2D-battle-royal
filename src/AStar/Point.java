package AStar;

public class Point implements Comparable<Point>{
	public int x;
	public int y;
	public int g = 0x3fffff;
	public int h = 0x3fffff;
	public int f = this.g + this.h;
	public Point father = this;
	public Point(int x,int y) {
		this.x = x;
		this.y = y;
	}
	@Override
	public boolean equals(Object e) {
		if (e instanceof Point)
			return this.x == ((Point)e).x && this.y == ((Point)e).y;
		return false;
	}
	@Override
	public int hashCode() {
		return x*x - y*y;
	}
	@Override
	public int compareTo(Point e) {
		if (this.f < e.f)
			return -1;
		else if (this.f > e.f)
			return 1;
		return 0;
	}
}
