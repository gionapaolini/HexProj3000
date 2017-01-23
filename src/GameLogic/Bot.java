package GameLogic;

import BotAlgorithms.AlphaBeta.AlphaBeta;
import BotAlgorithms.ExtensionStrategy;
import BotAlgorithms.MCTS_2.NodeTree_2;
import BotAlgorithms.MCTS_2.MCTS_2;
import BotAlgorithms.PathFinding.PathFinderBot;
import BotAlgorithms.Strategy;
import EnumVariables.BotType;
import EnumVariables.StatusCell;

/**
 * Created by giogio on 1/11/17.
 */
public class Bot extends Player{
    private BotType type;
    private int maxTime, depthlvl;
    private Strategy strategy;

    public Bot(BotType type, StatusCell color, Match match){
        this.type = type;
        this.color = color;
        this.match = match;
        strategy = new PathFinderBot(match, color);


    }
    public Bot(BotType type, StatusCell color, Match match, int maxTime, int depthlvl){
        this.type = type;
        this.color = color;
        this.match = match;
        this.maxTime = maxTime;
        this.depthlvl = depthlvl;
        if(type == BotType.MCTS)
            strategy = new MCTS_2(match.getBoard(),color,maxTime,depthlvl,false);
        if(type == BotType.MCTS_ExtensionStrategy)
            strategy = new MCTS_2(match.getBoard(),color,maxTime,depthlvl,true);
        if(type == BotType.AlphaBeta)
            strategy = new AlphaBeta(match.getBoard(),color,maxTime,depthlvl);




    }

    @Override
    public void makeMove(int x, int y) {
        Thread t = new Thread((() -> makeMoveThread()));
        t.start();
    }

    private void makeMoveThread(){

        Move move = strategy.getMove();

        match.setBotTurn(false);
        super.makeMove(move.getX(), move.getY());
        System.gc();
        System.out.println(type+" with "+maxTime+" and "+depthlvl);
    }

    public NodeTree_2 getRootTreeMcts(){
        return strategy.getRootTreeMcts();
    }
    public void resetTree(){
        strategy.resetTree();
    }
    public void updateBoard(Board board){
        strategy.updateBoard(board);
    }
}
