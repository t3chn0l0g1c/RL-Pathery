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
//	private final List<Point> setWalls;
	private final boolean isDoneOnBlocked;
	
	public Maze(int id, TileType[][] map, int walls, TileType[] targets, Point start, boolean isDoneOnBlocked) {
		this.id = id;
		this.map = map;
		this.walls = walls;
		this.targets = targets;
		this.start = start;
		currentPath = PathCalc.pathLength(this);
		initialPath = currentPath;
//		setWalls = new ArrayList<>(walls);
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
		return walls <= 0 || (isDoneOnBlocked && isBlocked());
	}
	
	
//	public Point getLastSet() {
//		if(setWalls.isEmpty()) {
//			throw new RuntimeException("No walls set");
//		}
//		return setWalls.get(setWalls.size()-1);
//	}
	
//	private double reward;
	
	private int lastScore;
	
	// TODO calculate score here
	// -1 for blocked or invalid click
	public double click(int x, int y) {
		if(walls<=0) {
			throw new RuntimeException("out of walls");
		}
//		Point p = new Point(x, y);
		TileType t = map[y][x];
		boolean b = true;
		if(t==TileType.NORMAL) {
			map[y][x] = TileType.USER_BLOCK;
			walls--;
//			setWalls.remove(p);
			lastScore = getScore();
			currentPath = PathCalc.pathLength(this);
		} else if(t==TileType.USER_BLOCK) {
//			map[y][x] = TileType.NORMAL;
//			walls++;
//			setWalls.add(p);
			b = false;
		} else {
			b = false;
		}
//		currentPath = PathCalc.pathLength(this);
		if(isBlocked()) {
			return -0.5;
		}
		if(!b) {
			return -0.1; // no score but no penalty either for non legal move. feels wrong, but so much penalty is messing up results...
		}
		double score = getScore() - lastScore;
//		if(score<0) {
			// we don't want -22 or such for a valid move, even if it is bad...
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
	

	public int getId() {
		return id;
	}
	
	
	public static void main(String[] args) throws IOException {
		// TODO PathFinder has a bug!
		// invalid mazes were generated
		// no improvement in path length measured even though it should!
		/**
		 * example
X               #               F 
X     X X                       F 
X   X                           F 
X         X X                   F 
X X       1                     F 
S           X     X             F 
X                               F 
X           X                   F 
X                               F 

click
8/0
13/8
12/1
14/6
0/6
4/8
9/0
11/7
0/1
13/0
9/4 <-- that should increase path by 1?
7/4 <-- that one definately
10/8
14/6
6/8
3/3
10/2
8/3
1/7
4/4
2/6
1/4
12/3
1/8

		 */
//		.
		TileType[][] map = new TileType[9][17];
		for(int y = 0; y<map.length; y++) {
			for(int x = 0; x<map[0].length; x++) {
				map[y][x] = TileType.NORMAL;
			}
		}
		for(int y = 0; y<map.length; y++) {
			map[y][0] = TileType.MAZE_WALL;
			map[y][map[0].length-1] = TileType.FINISH;
		}
		map[5][0] = TileType.START;
		map[4][1] = TileType.MAZE_WALL;
		map[2][2] = TileType.MAZE_WALL;
		map[1][3] = TileType.MAZE_WALL;
		map[1][4] = TileType.MAZE_WALL;
		map[3][5] = TileType.MAZE_WALL;
		map[3][6] = TileType.MAZE_WALL;
		map[4][5] = TileType.WP_A;
		map[5][6] = TileType.MAZE_WALL;
		map[7][6] = TileType.MAZE_WALL;
		map[5][8] = TileType.MAZE_WALL;
		
		Maze m = new Maze(-1, map, 24, new TileType[] {TileType.WP_A,  TileType.FINISH}, new Point(0, 5), true); 
		System.out.println(m.toString());
		System.out.println(m.getScore());
		m.click(8, 0);
		System.out.println(m.getScore());
		m.click(7, 4);
		System.out.println(m.toString());
		System.out.println(m.getScore());
//		Maze m = MazeGenerator.generate(new MazeDescriptor(), false, new Random(0));
		BufferedImage img = m.toImage(new BufferedImage(170, 90+11, BufferedImage.TYPE_4BYTE_ABGR));
		ImageIO.write(img, "PNG", new File("C:\\tmp\\maze.png"));
	}


}
