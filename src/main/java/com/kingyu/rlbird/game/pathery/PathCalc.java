package com.kingyu.rlbird.game.pathery;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import com.kingyu.rlbird.game.pathery.util.TileType;

public class PathCalc {

	
	public static int pathLength(Maze m) {
		return pathLength(m.getStart(), m.getMap(), m.getTargets());
	}
	
	public static int pathLength(Point start, TileType[][] map, TileType[] targets) {
		int length = 0;
		Point[] currentStart = new Point[1];
		currentStart[0] = start;
		int stackSize = map.length*map[0].length+1;
		for(TileType t : targets) {
			int l = pathLength(currentStart, map, t, stackSize, map[0].length, map.length);
			if(l==-1) {
				pathLength(currentStart, map, t, stackSize, map[0].length, map.length);
				return -1;
			}
			length += l;
		}
		return length;
	}

	private static int pathLength(Point[] currentStart, TileType[][] map, TileType target, int stackSize, int width, int height) {
		Set<Point> used = new HashSet<>();
		used.add(currentStart[0]);
		Point[] array = new Point[stackSize];
		int size = 0;
		array[size++] = currentStart[0];
		for (int iterationEnd = 0, iteration = 0, index = 0; iterationEnd < size; iteration++) {
			iterationEnd = size;
			while (index < iterationEnd) {
				Point p = array[index++];
				if(map[p.y][p.x]==target) {
					currentStart[0] = p;
					return iteration;
				}
				Point up = new Point(p.x, p.y-1);
				if(exists(up, width, height) && !TileType.blocked(map[up.y][up.x]) && !used.contains(up)) {
					array[size++] = up;
					used.add(up);
				}
				Point right = new Point(p.x+1, p.y);
				if(exists(right, width, height) && !TileType.blocked(map[right.y][right.x]) && !used.contains(right)) {
					array[size++] = right;
					used.add(right);
				}
				Point down = new Point(p.x, p.y+1);
				if(exists(down, width, height) && !TileType.blocked(map[down.y][down.x]) && !used.contains(down)) {
					array[size++] = down;
					used.add(down);
				}
				Point left = new Point(p.x-1, p.y);
				if(exists(left, width, height) && !TileType.blocked(map[left.y][left.x]) && !used.contains(left)) {
					array[size++] = left;
					used.add(left);
				}
			}
		}
		return -1;
	}

	private static boolean exists(Point p, int width, int height) {
		if(p.x<0 || p.y<0) {
			return false;
		}
		if(p.x >=width || p.y>=height) {
			return false;
		}
		return true;
	}
}
