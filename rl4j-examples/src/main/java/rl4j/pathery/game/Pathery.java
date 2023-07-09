package rl4j.pathery.game;

import java.util.Random;

import org.deeplearning4j.gym.StepReply;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.space.ArrayObservationSpace;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.space.ObservationSpace;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rl4j.pathery.game.util.MazeDescriptor;

public class Pathery implements MDP<PatheryState, Integer, DiscreteSpace> {


	private static final int ACTION_SIZE = 17*9; 
	
	 private DiscreteSpace actionSpace = new DiscreteSpace(ACTION_SIZE);
	 private ObservationSpace<PatheryState> observationSpace = new ArrayObservationSpace<>(new int[] {ACTION_SIZE+1});
	 
	@Override
	public ObservationSpace<PatheryState> getObservationSpace() {
		return observationSpace;
	}

	@Override
	public DiscreteSpace getActionSpace() {
		return actionSpace;
	}

	private Maze currentMaze;
	private final long seed;
	
	
	@Override
	public PatheryState reset() {
		if(currentMaze==null) {
			currentMaze = MazeGenerator.generate(new MazeDescriptor(), true, new Random(seed));
		}
		currentMaze.reset();
		return new PatheryState(currentMaze);
	}
	public Pathery(long seed) {
		super();
		this.seed = seed;
	}


	@Override
	public void close() {
		// probably not needed
		
	}

	int steps;
	
	@Override
	public StepReply<PatheryState> step(Integer action) {
		int y = action/MazeDescriptor.WIDTH;
		int x = action % MazeDescriptor.WIDTH;
		double reward = currentMaze.click(x, y);
//		double score = currentMaze.getReward();
//		if(!b) {
//			score = -0.5;
//		}
		System.out.println("click: " + x + "/" + y + "\t walls left: " + currentMaze.getWalls() + "\t reward: " + reward + "\t id " + currentMaze.getId() + "\t path: " + currentMaze.getScore() + "\t (" + steps++ +")");
		if(currentMaze.isFinished()) {
			System.out.println(currentMaze.toString());
		}


		StepReply<PatheryState> reply = new StepReply<PatheryState>(new PatheryState(currentMaze), reward, currentMaze.isFinished(), new JSONObject());
		return reply;

	}

	@Override
	public boolean isDone() {
		return currentMaze.isFinished();
	}

	@Override
	public MDP<PatheryState, Integer, DiscreteSpace> newInstance() {
		return new Pathery(seed);
	}

}
