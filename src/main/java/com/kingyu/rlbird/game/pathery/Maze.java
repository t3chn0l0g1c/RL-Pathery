package com.kingyu.rlbird.game.pathery;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import com.kingyu.rlbird.game.pathery.util.MazeDescriptor;
import com.kingyu.rlbird.game.pathery.util.TileType;

public class Maze {
	
	public static final int PX_PER_TILE = 10;

	private final int id;
	private final TileType[][] map;
	private int walls;
	private final TileType[] targets;
	private final Point start;
	private int currentPath;
	private final int initialPath;
	private final List<Point> setWalls;
	
	public Maze(int id, TileType[][] map, int walls, TileType[] targets, Point start) {
		this.id = id;
		this.map = map;
		this.walls = walls;
		this.targets = targets;
		this.start = start;
		currentPath = PathCalc.pathLength(this);
		initialPath = currentPath;
		setWalls = new ArrayList<>(walls);
	}
	
	public int getScore() {
		return currentPath-initialPath;
	}
	
	public boolean isBlocked() {
		return currentPath<0;
	}
	
	public int getCurrentPath() {
		return currentPath;
	}
	
//	public void revertLast() {
//		if(setWalls.isEmpty()) {
//			throw new RuntimeException("cannot revert, no walls set");
//		}
//		Point p = setWalls.get(setWalls.size()-1);
//		click(p.x, p.y);
//	}
	
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
		return walls <= 0;
	}
	
	
	public Point getLastSet() {
		if(setWalls.isEmpty()) {
			throw new RuntimeException("No walls set");
		}
		return setWalls.get(setWalls.size()-1);
	}
	
	public void click(int x, int y) {
		if(walls<=0) {
			throw new RuntimeException("out of walls");
		}
		Point p = new Point(x, y);
		TileType t = map[y][x];
		if(t==TileType.NORMAL) {
			map[y][x] = TileType.USER_BLOCK;
			walls--;
			setWalls.remove(p);
		} else if(t==TileType.USER_BLOCK) {
			map[y][x] = TileType.NORMAL;
			walls++;
			setWalls.add(p);
		}
		currentPath = PathCalc.pathLength(this);
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
	
	public BufferedImage toImage(BufferedImage img) {
		// assume img is right size
        Graphics g = img.getGraphics();
        g.clearRect(0, 0, img.getWidth(), img.getHeight());
        g.setColor(Color.BLACK);
        // set upper header (background, remaining walls as text)
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        g.setColor(Color.WHITE);
        g.drawString(""+ id, 5, 10);
        g.setColor(Color.BLACK);
        g.drawRect(0, 11, img.getWidth(), img.getHeight()-11);
        for(int y = 0; y<map.length; y++) {
        	for(int x = 0; x<map[0].length; x++) {
        		// draw rect for each type, in proper color
        		g.setColor(map[y][x].color);
        		g.fillRect(x*PX_PER_TILE, y*PX_PER_TILE+11, PX_PER_TILE, PX_PER_TILE);

        	}
        }
        return img;
	}
	
	public static void main(String[] args) throws IOException {
		Maze m = MazeGenerator.generate(new MazeDescriptor(), new Random(0));
		BufferedImage img = m.toImage(new BufferedImage(170, 90+11, BufferedImage.TYPE_4BYTE_ABGR));
		ImageIO.write(img, "PNG", new File("C:\\tmp\\maze.png"));
	}
}
