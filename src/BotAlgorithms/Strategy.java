package BotAlgorithms;

import BotAlgorithms.MCTS_2.NodeTree_2;
import GameLogic.Move;

/**
 * Created by giogio on 1/14/17.
 */
public interface Strategy {
    public Move getMove();
    public NodeTree_2 getRootTreeMcts();
}
