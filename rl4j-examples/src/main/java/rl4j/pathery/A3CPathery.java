package rl4j.pathery;

import java.io.IOException;

import org.deeplearning4j.rl4j.learning.async.a3c.discrete.A3CDiscreteDense;
import org.deeplearning4j.rl4j.learning.configuration.A3CLearningConfiguration;
import org.deeplearning4j.rl4j.network.configuration.ActorCriticDenseNetworkConfiguration;
import org.deeplearning4j.rl4j.policy.ACPolicy;
import org.deeplearning4j.rl4j.space.Box;
import org.deeplearning4j.rl4j.util.DataManager;

import rl4j.pathery.game.Pathery;
import rl4j.pathery.game.PatheryState;


// Using https://github.com/openai/gym-http-api
// python gym_http_server.py
public class A3CPathery {

    private static A3CLearningConfiguration CARTPOLE_A3C =
    		A3CLearningConfiguration.builder()
    		.numThreads(16)
    		.nStep(200)
    		.rewardFactor(0.1)
    		.maxEpochStep(500000)
    		.build();
//    (
//                    123,            //Random seed
//                    200,            //Max step By epoch
//                    500000,         //Max step
//                    16,              //Number of threads
//                    5,              //t_max
//                    10,             //num step noop warmup
//                    0.1,           //reward scaling
//                    0.99,           //gamma
//                    10.0           //td-error clipping
//            );


    private static final ActorCriticDenseNetworkConfiguration CARTPOLE_NET_A3C = 
    		ActorCriticDenseNetworkConfiguration.builder()
    		.numLayers(4)
    		.numHiddenNodes(308)
    		.useLSTM(true)
    		.build();


    public static void main(String[] args) throws IOException {
        A3CcartPole();
    }

    public static void A3CcartPole() throws IOException {

        DataManager manager = recordTraining();

//        GymEnv mdp = defineMdp();
        Pathery mdp = new Pathery(3);

        A3CDiscreteDense<PatheryState> a3c = startActorCritique(manager, mdp);

        //start the training
        a3c.train();

        ACPolicy<PatheryState> pol = a3c.getPolicy();

        pol.save("/tmp/val1/", "/tmp/pol1");

        //close the mdp (http connection)
        mdp.close();

        reloadSavedPolicy();
    }

    private static void reloadSavedPolicy() throws IOException {
        ACPolicy<Box> pol2 = ACPolicy.load("/tmp/val1/", "/tmp/pol1");
    }

    private static A3CDiscreteDense<PatheryState> startActorCritique(DataManager manager, Pathery mdp) {
        return new A3CDiscreteDense<PatheryState>(mdp, CARTPOLE_NET_A3C, CARTPOLE_A3C);
    }

//    private static GymEnv defineMdp() {
//        GymEnv mdp = null;
//        try {
//        mdp = new GymEnv("CartPole-v0", false, false);
//        } catch (RuntimeException e){
//            System.out.print("To run this example, download and start the gym-http-api repo found at https://github.com/openai/gym-http-api.");
//        }
//        return mdp;
//    }

    private static DataManager recordTraining() throws IOException {
        return new DataManager(true);
    }
}
