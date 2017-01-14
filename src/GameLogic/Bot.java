package GameLogic;

import BotAlgorithms.MCTS.MCTS;
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

    }
    public Bot(BotType type, StatusCell color, Match match, int maxTime, int depthlvl){
        this.type = type;
        this.color = color;
        this.match = match;
        this.maxTime = maxTime;
        this.depthlvl = depthlvl;
        if(type == BotType.MCTS)
            strategy = new MCTS(match.getBoard(),color,maxTime,depthlvl);


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

        System.out.println(type+" with "+maxTime+" and "+depthlvl);
    }
}
