package com.kingyu.rlbird.game.pathery;

import java.awt.Point;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.kingyu.rlbird.game.pathery.util.MazeDescriptor;
import com.kingyu.rlbird.game.pathery.util.TileType;

public class MazeGenerator {
	// output is byte[] or bmp?
	// 1px resolution (1px for blockCount)
	
	static int id;
	
	public static Maze generate(MazeDescriptor desc, Random r) {
		TileType[][] map = new  TileType[desc.height][desc.width];
		for(int y = 0; y<map.length; y++) {
			for(int x = 0; x<map[0].length; x++) {
				map[y][x] = TileType.NORMAL;
			}
		}
		int walls = r.nextInt(desc.wallMax-desc.wallMin) + desc.wallMin;
		int wps = r.nextInt(desc.wpMax-desc.wpMin) + desc.wpMin;
		TileType[] targets = new TileType[wps + 1];
		int blocks = r.nextInt(desc.blockMax-desc.blockMin) + desc.blockMin;
		
		// set START on one tile at left, rest is MAZE_WALL
		// finish is right side
		for(int y = 0; y<desc.height; y++) {
			map[y][0] = TileType.MAZE_WALL;
			map[y][desc.width-1] = TileType.FINISH;
		}

		int startY = r.nextInt(desc.height);
		map[startY][0] = TileType.START;
		Point start = new Point(0, startY);
		Set<Point> taken = new HashSet<>();
		taken.add(start);
		
		// set WPs
		for(int i = 0; i<wps; i++) {
			TileType w = TileType.wps()[i];
			targets[i] = w;
			Point p = randomPoint(1, desc.width-1, desc.height, taken, r);
			map[p.y][p.x] = w;
			taken.add(p);
		}
		targets[wps] = TileType.FINISH;

		// while(valid path) -> set blocks
		for(int i = 0; i<walls; i++) {
			// set wall (include taken)
			int pathLength = -1;
			Point p = null;
			do {
				p = randomPoint(1, desc.width-1, desc.height, taken, r);
				if(map[p.y][p.x]!=TileType.NORMAL) {
					throw new RuntimeException("we got a problem at " + p);
				}
				map[p.y][p.x] = TileType.MAZE_WALL;
				pathLength = PathCalc.pathLength(start, map, targets);
				map[p.y][p.x] = TileType.NORMAL;
				// do path search
				// if invalid path, remove and try again
			} while(pathLength==-1);
			map[p.y][p.x] = TileType.MAZE_WALL;
			taken.add(p);
		}
		// return maze;
		return new Maze(++id, map, blocks, targets, start);
	}


	private static Point randomPoint(int minX, int maxX, int maxY, Set<Point> taken, Random r) {
		int diff = maxX-minX;
		Point p = null;
		do {
			int x = r.nextInt(diff)+minX;
			int y = r.nextInt(maxY);
			p = new Point(x, y);
		} while(taken.contains(p));
		return p;
	}
}
