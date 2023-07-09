package rl4j.pathery.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import rl4j.pathery.game.util.TileType;

public class Maze {
	
	public static final int PX_PER_TILE = 10;

	private final int id;
	private final TileType[][] map;
	private int walls;
	private final TileType[] targets;
	private final Point start;
	private int currentPath;
	private final int initialPath;
	private final boolean isDoneOnBlocked;
	
	public Maze(int id, TileType[][] map, int walls, TileType[] targets, Point start, boolean isDoneOnBlocked) {
		this.id = id;
		this.map = map;
		this.walls = walls;
		this.targets = targets;
		this.start = start;
		currentPath = PathCalc.pathLength(this);
		initialPath = currentPath;
		this.isDoneOnBlocked = isDoneOnBlocked;
	}
	
	public void reset() {
		for(int y = 0; y<map.length; y++) {
			for(int x = 0; x<map[0].length; x++) {
				if(map[y][x]==TileType.USER_BLOCK) {
					map[y][x] = TileType.NORMAL;
					walls++;
				}
			}
		}
		currentPath = PathCalc.pathLength(this);
		lastScore = 0;
	}
	
	public int getScore() {
		return Math.max(currentPath-initialPath, 0);
	}
	
	public boolean isBlocked() {
		return currentPath<0;
	}
	
	public int getCurrentPath() {
		return currentPath;
	}
	
	public int getWalls() {
		return walls;
	}
	
	public Point getStart() {
		return start;
	}
	
	public TileType[][] getMap() {
		return map;
	}
	
	public TileType[] getTargets() {
		return targets;
	}
	
	public boolean isFinished() {
		return walls <= 0 || (isDoneOnBlocked && isBlocked());
	}
	
	
	private int lastScore;
	
	public double click(int x, int y) {
		if(walls<=0) {
			throw new RuntimeException("out of walls");
		}
		TileType t = map[y][x];
		boolean b = true;
		if(t==TileType.NORMAL) {
			map[y][x] = TileType.USER_BLOCK;
			walls--;
			lastScore = getScore();
			currentPath = PathCalc.pathLength(this);
		} else if(t==TileType.USER_BLOCK) {
//			map[y][x] = TileType.NORMAL;
//			walls++;
			b = false;
		} else {
			b = false;
		}
		if(isBlocked()) {
			return -0.5;
		}
		if(!b) {
			return -0.1;
		}
		double score = getScore() - lastScore;
//		if(score<0) {
//			score = score / 100d;
//		}
		return score;
	}
	
	
	public static String toString(TileType[][] map) {
		StringBuilder str = new StringBuilder();
		for(int y = 0; y<map.length; y++) {
			for(int x = 0; x<map[0].length; x++) {
				str.append(map[y][x].string);
				str.append(" ");
			}
			str.append("\n");
		}
		return str.toString();
	}
	
	public String toString() {
		return toString(map);
	}
	
//	public BufferedImage toImage(BufferedImage img) {
//		// assume img is right size
//        Graphics g = img.getGraphics();
//        g.clearRect(0, 0, img.getWidth(), img.getHeight());
//        g.setColor(Color.BLACK);
//        // set upper header (background, remaining walls as text)
//        g.fillRect(0, 0, img.getWidth(), img.getHeight());
//        g.setColor(Color.WHITE);
//        g.drawString(""+ id, 5, 10);
//        g.setColor(Color.BLACK);
//        g.drawRect(0, 11, img.getWidth(), img.getHeight()-11);
//        for(int y = 0; y<map.length; y++) {
//        	for(int x = 0; x<map[0].length; x++) {
//        		// draw rect for each type, in proper color
//        		g.setColor(map[y][x].color);
//        		g.fillRect(x*PX_PER_TILE, y*PX_PER_TILE+11, PX_PER_TILE, PX_PER_TILE);
//
//        	}
//        }
//        return img;
//	}
	

	public int getId() {
		return id;
	}


}
