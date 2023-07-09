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

	private static final Logger logger = LoggerFactory.getLogger(Pathery.class);
//
//	private final NDManager manager;
//	private NDList currentObservation;
//	private BufferedImage currentImg;
//
////	private static boolean currentTerminal = false;
////	private static float currentReward = 0.2f;
//
//	private ActionSpace actionSpace;
//
//	private Maze currentMaze;
//
//	private final ReplayBuffer replayBuffer;
//	
//	private int gameStep = 0;
//
//	public Pathery(NDManager manager, int batchSize, int replayBufferSize) {
//		this.manager = manager;
//		replayBuffer = new LruReplayBuffer(batchSize, replayBufferSize);
//		currentImg = new BufferedImage(170, 90 + 11, BufferedImage.TYPE_4BYTE_ABGR);
//		currentObservation = createObservation(currentImg);
//
//		actionSpace = new ActionSpace();
//		// encode all coords to each a single int
//		for (int y = 0; y < MazeDescriptor.HEIGHT; y++) {
//			for (int x = 0; x < MazeDescriptor.WIDTH; x++) {
//				int[] ia = new int[MazeDescriptor.HEIGHT * MazeDescriptor.WIDTH];
//				ia[y * MazeDescriptor.WIDTH + x] = 1;
//				actionSpace.add(new NDList(manager.create(ia)));
//			}
//		}
//		currentMaze = MazeGenerator.generate(new MazeDescriptor(), new Random());
//	}
//
//	// TODO aggregate all (or just x, e.g. 4?) previous steps here?
//
//	private final Queue<NDArray> imgQueue = new ArrayDeque<>(4);
//
//	/**
//	 * Convert image to CNN input. Copy the initial frame image, stack into NDList,
//	 * then replace the fourth frame with the current frame to ensure that the batch
//	 * picture is continuous.
//	 *
//	 * @param currentImg the image of current frame
//	 * @return the CNN input
//	 */
//	public NDList createObservation(BufferedImage currentImg) {
//		NDArray observation = GameUtil.imgPreprocess(currentImg, 170, 101);
//		if (imgQueue.isEmpty()) {
//			for (int i = 0; i < 4; i++) {
//				imgQueue.offer(observation);
//			}
//			return new NDList(NDArrays.stack(new NDList(observation, observation, observation, observation), 1));
//		} else {
//			imgQueue.remove();
//			imgQueue.offer(observation);
//			NDArray[] buf = new NDArray[4];
//			int i = 0;
//			for (NDArray nd : imgQueue) {
//				buf[i++] = nd;
//			}
//			return new NDList(NDArrays.stack(new NDList(buf[0], buf[1], buf[2], buf[3]), 1));
//		}
//	}
//
//	public void reset() {
////		currentReward = 0.2f;
////		currentTerminal = false;
////		currentMaze = MazeGenerator.generate(new MazeDescriptor(), new Random());
//	}
//
//	public NDList getObservation() {
//		return currentObservation;
//	}
//
//	@Override
//	public ActionSpace getActionSpace() {
//		return actionSpace;
//	}
//
//	private int trainStep = 0;
//
//	@Override
//	public void step(NDList action, boolean training) {
//		int[] ia = action.singletonOrThrow().toIntArray();
//		int idx = 0;
//		for (; idx < ia.length; idx++) {
//			if (ia[idx] == 1) {
//				break;
//			}
//		}
//		int y = idx / MazeDescriptor.WIDTH;
//		int x = idx % MazeDescriptor.WIDTH;
//
//		currentMaze.click(x, y);
//
//		int newScore = currentMaze.getScore();
//		// TODO if blocked, higher chance to randomly select action to revert?
//
//		NDList preObservation = currentObservation;
//		currentObservation = createObservation(currentImg);
//
//		PatheryStep step = new PatheryStep(manager.newSubManager(), preObservation, currentObservation, action,
//				newScore, currentMaze.isFinished());
//		if (training) {
//			replayBuffer.addStep(step);
//		}
//		logger.info("GAME_STEP " + gameStep +
//				" TRAIN_STEP " + trainStep++ + " / " + " ACTION WAS " + x + "/" + y + " / \n" + currentMaze.toString()
//				+ " / " + "REWARD " + newScore + " / " + "SCORE " + currentMaze.getScore());
//		if (currentMaze.isFinished()) {
//			currentMaze = MazeGenerator.generate(new MazeDescriptor(), new Random());
//		}
//	}
//
//	public int getTrainStep() {
//		return trainStep;
//	}
//	
//	@Override
//	public Step[] runEnvironment(RlAgent agent, boolean training) {
//        Step[] batchSteps = new Step[0];
////        reset();
//
//        // run the game
//        NDList action = null;
//        // if blocked give 50% chance to undo last move
//        if(currentMaze.isBlocked() && Math.random()<0.5d) {
//        	Point p = currentMaze.getLastSet();
//        	action = actionSpace.get(p.y*MazeDescriptor.WIDTH +p.x);
//        } else {
//        	action = agent.chooseAction(this, training);
//        }
//        step(action, training);
//        if (training) {
//            batchSteps = this.getBatch();
//        }
//        if (gameStep % 5000 == 0) {
//            this.closeStep();
//        }
////        if (gameStep <= OBSERVE) {
////            trainState = "observe";
////        } else {
////            trainState = "explore";
////        }
//        gameStep++;
//        return batchSteps;
//	}
//
//	public Step[] getBatch() {
//		return replayBuffer.getBatch();
//	}
//
//	/**
//	 * Close the steps in replayBuffer which are not pointed to.
//	 */
//	public void closeStep() {
//		replayBuffer.closeStep();
//	}
//
//	public void close() {
//		manager.close();
//	}
	
//	   final private static int MAX_STEP = 20;
//	    final private static int SEED = 1234;
//	    final private static int ACTION_SIZE = 10;
//	    final private static HardToyState[] states = genToyStates(MAX_STEP, SEED);
//	    @Getter
//	    private DiscreteSpace actionSpace = new DiscreteSpace(ACTION_SIZE);
//	    @Getter
//	    private ObservationSpace<HardToyState> observationSpace = new ArrayObservationSpace(new int[] {ACTION_SIZE});
//	    private HardToyState hardToyState;
//
//	    public static void printTest(IDQN idqn) {
//	        INDArray input = Nd4j.create(MAX_STEP, ACTION_SIZE);
//	        for (int i = 0; i < MAX_STEP; i++) {
//	            input.putRow(i, Nd4j.create(states[i].toArray()));
//	        }
//	        INDArray output = Nd4j.max(idqn.output(input), 1);
//	        Logger.getAnonymousLogger().info(output.toString());
//	    }


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
//		System.out.println("new maze...");
//		currentMaze = MazeGenerator.generate(new MazeDescriptor(), false, new Random());
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
		//		System.out.println(currentMaze.toString());


		StepReply<PatheryState> reply = new StepReply<PatheryState>(new PatheryState(currentMaze), reward, currentMaze.isFinished(), new JSONObject());
		return reply;
		// TODO set block at int idx, if possible
		// sysout current score
		// return stepReply with new observation

	}

	@Override
	public boolean isDone() {
		return currentMaze.isFinished();
	}

	@Override
	public MDP<PatheryState, Integer, DiscreteSpace> newInstance() {
		return new Pathery(seed);
	}

	// TODO
	// generate valid mazes //
	// save as picture //
	// solver to score moves (all moves exist, invalid don't change result or
	// blocks) //
	// feed into net
	// adapt net structure?

}
