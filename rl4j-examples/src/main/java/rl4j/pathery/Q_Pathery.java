package rl4j.pathery;

import java.io.IOException;

import org.deeplearning4j.rl4j.learning.Learning;
import org.deeplearning4j.rl4j.learning.async.nstep.discrete.AsyncNStepQLearningDiscreteDense;
import org.deeplearning4j.rl4j.learning.configuration.AsyncQLearningConfiguration;
import org.deeplearning4j.rl4j.learning.configuration.QLearningConfiguration;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscreteDense;
import org.deeplearning4j.rl4j.network.configuration.DQNDenseNetworkConfiguration;
import org.deeplearning4j.rl4j.network.dqn.IDQN;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.util.DataManager;

import rl4j.pathery.game.Pathery;
import rl4j.pathery.game.PatheryState;


/**
 * Stock prediction using Toy abstraction.
 * Stock state is a toy state. Based on our action (buy/sell) the toy/stock state can change -
 */
public class Q_Pathery {


    public static QLearningConfiguration STOCK_QL = QLearningConfiguration.builder()
    		.maxEpochStep(150)
    		.maxStep(80000)
    		.expRepMaxSize(150)
//    		.batchSize(150)
    		.updateStart(0)
    		.rewardFactor(0.1)
    		.epsilonNbStep(50)
    		.doubleDQN(true)
    		.build();
////            new QLearning.QLConfiguration(
//                    123,   //Random seed
//                    150,//Max step By epoch
//                    80000, //Max step
//                    150, //Max size of experience replay
//                    150,    //size of batches
//                    100,   //target update (hard)
//                    0,     //num step noop warmup
//                    0.1,  //reward scaling
//                    0.99,  //gamma
//                    10.0,  //td-error clipping
//                    0.1f,  //min epsilon
//                    150,  //num step for eps greedy anneal
//                    true   //double DQN
//            );


    public static AsyncQLearningConfiguration STOCK_ASYNC_MULTI_THREAD_QL =
    		AsyncQLearningConfiguration.builder()
//    		.maxEpochStep(100)
    		.maxStep(80000)
    		.numThreads(8)
//    		.updateStart(50)
    		.rewardFactor(0.1)
    		.epsilonNbStep(50)
    		.build();
//    
//            new AsyncNStepQLearningDiscrete.AsyncNStepQLConfiguration(
//                    123,        //Random seed
//                    100,     //Max step By epoch
//                    80000,      //Max step
//                    16,          //Number of threads
//                    5,          //t_max
//                    100,        //target update (hard)
//                    0,          //num step noop warmup
//                    0.1,        //reward scaling
//                    0.99,       //gamma
//                    10.0,       //td-error clipping
//                    0.1f,       //min epsilon
//                    5        //num step for eps greedy anneal
//            );


    public static DQNDenseNetworkConfiguration STOCK_NET =
    		DQNDenseNetworkConfiguration.builder()
    		.numLayers(4)
    		.numHiddenNodes(154)
    		.build();

    public static void main(String[] args) throws IOException {
        simpleStock();
//        stockAsyncMultiThread();

    }

    public static void simpleStock() throws IOException {

//        DataManager manager = getDataManager();

        Pathery mdp = new Pathery(3);

        Learning<PatheryState, Integer, DiscreteSpace, IDQN> dql = new QLearningDiscreteDense<PatheryState>(mdp, STOCK_NET, STOCK_QL);

//        mdp.setFetchable(dql);

        dql.train();

        mdp.close();

    }

//    private static SimpleToy createStockAbstraction() {
//        return new SimpleToy(20);
//    }

    private static DataManager getDataManager() throws IOException {
        return new DataManager();
    }


    public static void stockAsyncMultiThread() throws IOException {

        DataManager manager = getDataManager();

        Pathery mdp = new Pathery(2);

        AsyncNStepQLearningDiscreteDense<PatheryState> dql = new AsyncNStepQLearningDiscreteDense<PatheryState>(mdp, STOCK_NET, STOCK_ASYNC_MULTI_THREAD_QL);

//        mdp.setFetchable(dql);

        dql.train();

        mdp.close();

    }

}
