package com.kingyu.rlbird.game.pathery;

import com.kingyu.rlbird.rl.env.RlEnv;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;

public class PatheryStep implements RlEnv.Step {

    private final NDManager manager;
    private final NDList preObservation;
    private final NDList postObservation;
    private final NDList action;
    private final float reward;
    private final boolean terminal;
    
	public PatheryStep(NDManager manager, NDList preObservation, NDList postObservation, NDList action, float reward,
			boolean terminal) {
		this.manager = manager;
		this.preObservation = preObservation;
		this.postObservation = postObservation;
		this.action = action;
		this.reward = reward;
		this.terminal = terminal;
	}
    
    public NDList getPreObservation(NDManager manager) {
        preObservation.attach(manager);
        return preObservation;
    }

    public NDList getPreObservation() {
        return preObservation;
    }

    public NDList getPostObservation(NDManager manager) {
        postObservation.attach(manager);
        return postObservation;
    }
    public NDList getPostObservation() {
        return postObservation;
    }
    public NDManager getManager() {
        return this.manager;
    }
    public NDList getAction() {
        return action;
    }

    public NDArray getReward() {
        return manager.create(reward);
    }
    public boolean isTerminal() {
        return terminal;
    }

    public void close() {
        this.manager.close();
    }
}