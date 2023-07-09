package rl4j.pathery.game;

import java.util.Arrays;

import org.deeplearning4j.rl4j.space.Encodable;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import rl4j.pathery.game.util.TileType;

public class PatheryState implements Encodable{

	private final int wallsLeft;
	private final double[] data; // wallsLeft + maze data
	
	public PatheryState(Maze m) {
		TileType[][] map = m.getMap();
		wallsLeft = m.getWalls();
		data = new double[(map.length * map[0].length)+1];
		data[0] = m.getWalls();
		int i = 1;
		for(int y = 0; y<map.length; y++) {
			for(int x = 0; x<map[0].length; x++) {
				data[i++] = map[y][x].ordinal();
			}
		}
	}
	
	public PatheryState(int wallsLeft, double[] data) {
		this.wallsLeft = wallsLeft;
		this.data = data;
	}
	
	@Override
	public double[] toArray() {
		return data;
	}

	@Override
	public boolean isSkipped() {
		return false;
	}

	@Override
	public INDArray getData() {
		return Nd4j.create(data);
	}

	@Override
	public PatheryState dup() {
		return new PatheryState(wallsLeft, Arrays.copyOf(data, data.length));
	}

}
