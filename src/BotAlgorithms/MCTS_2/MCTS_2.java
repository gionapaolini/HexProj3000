package BotAlgorithms.MCTS_2;

import BotAlgorithms.ExtensionStrategy;
import BotAlgorithms.MCTS.NodeTree;
import BotAlgorithms.Strategy;
import EnumVariables.StatusCell;
import GameLogic.Board;
import GameLogic.Move;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by giogio on 1/21/17.
 */
public class MCTS_2 implements Strategy{
    private final static Logger log = Logger.getLogger( MCTS_2.class.getName() );

    private boolean extensionStrategy;
    private StatusCell ally;
    private StatusCell enemy;
    private Board realBoard;
    private int maxtTime, n_expansion;
    private NodeTree_2 root;
    private NodeTree_2 lastMove;


    public MCTS_2(Board realBoard, StatusCell color, int maxtTime, int depthLevel, boolean ExtensionStrategy){
        this.realBoard = realBoard;
        this.ally = color;
        if(color == StatusCell.Blue)
            enemy = StatusCell.Red;
        else
            enemy = StatusCell.Blue;
        this.maxtTime = maxtTime;
        this.extensionStrategy = ExtensionStrategy;
    }

    public Move start(){
        setNewRoot();
        double startTime = System.currentTimeMillis();
        n_expansion = 0;
        while (System.currentTimeMillis() - startTime <maxtTime){
            expansion(selection(root));

        }
        System.out.println("Expansions: "+n_expansion);

        NodeTree_2 m = getBestMove();
        while (m == null){
            expansion(selection(root));
            m = getBestMove();
        }

        return m.getMove();

    }

    public NodeTree_2 getBestMove(){
        NodeTree_2 bestNode = null;
        double bestValue = -999999999;
        for(NodeTree_2 child: root.getChildren()){

            double value = child.getGames();
            if(value>bestValue) {
                bestNode = child;
                bestValue = value;
            }
        }
        lastMove = bestNode;
        return bestNode;
    }

    public NodeTree_2 getBestMoveGiona(){
        NodeTree_2 bestNode = null;
        double bestValue = -999999999;
        for(NodeTree_2 child: root.getChildren()){
            if(child.isDeadCell())
                continue;
            if(child.isWinningMove() || child.isLosingMove()){
                lastMove = child;
                return child ;
            }
            double value = UCB1(child);
            if(value>bestValue) {
                bestNode = child;
                bestValue = value;
            }
        }
        lastMove = bestNode;
        return bestNode;
    }

    public NodeTree_2 selection(NodeTree_2 node){

        if(node.getState().getFreeMoves().size()>0)
            return node;

        NodeTree_2 bestNode = node;
        double bestValue = -999999999;
        for(NodeTree_2 child: node.getChildren()){
            double value = UCB1(child);
            log.info(child.toString() + " UCB1: " + value);
            if(value>bestValue) {
                bestNode = child;
                bestValue = value;
            }
        }
        return selection(bestNode);
    }

    public void setNewRoot(){
        if(lastMove!=null) {
            Board copy = lastMove.getState().getCopy();

            for (NodeTree_2 child : lastMove.getChildren()) {
                Move move = child.getMove();
                copy.putStone(move.getX(), move.getY(), enemy);
                if (copy.isEqual(realBoard)) {
                    root = child;
                    root.setParent(null);
                    System.out.println("USED________");
                    return;
                }
                copy.setEmpty(move.getX(), move.getY());
            }
            lastMove = null;
            setNewRoot();
        }else {
            root = new NodeTree_2(null);
            root.setColor(enemy);
            root.setState(realBoard.getCopy());
        }
    }

    public void expansion(NodeTree_2 node){
        if (node == null) log.severe("Expansion With null node");

        if (extensionStrategy == false) standardExpansion(node);
        else specialExpansion(node);



    }

    private void specialExpansion(NodeTree_2 node) {
        ArrayList<Move> moves = node.getState().getFreeMoves();
        NodeTree_2 newNode = new NodeTree_2(node);
        ExtensionStrategy es = new ExtensionStrategy(moves,node.getState(),ally);


        newNode.setMove(es.getSuggestedMove(moves));
        n_expansion++;
        if (!newNode.isWinningMove() && !newNode.isLosingMove()){
            simulate(newNode);
        }
        newNode.incrementGame();
    }

    private void standardExpansion(NodeTree_2 node) {

        if(!node.isWinningMove() && !node.isLosingMove() && !node.isDeadCell()) {
            ArrayList<Move> moves = node.getState().getFreeMoves();
            NodeTree_2 newNode = new NodeTree_2(node);
            Move move = moves.remove((int) (Math.random() * moves.size()));
            newNode.setMove(move);
            n_expansion++;
            if (!newNode.isWinningMove() && !newNode.isLosingMove() && !newNode.isDeadCell()){
                simulate(newNode);
            }else {
                if(!newNode.isDeadCell()) {
                    if(newNode.getColor() == ally){
                        if(newNode.isWinningMove())
                            newNode.incrementWin(1);
                        // newNode.incrementWin(10);
                        else
                            newNode.incrementWin(1);
                        //newNode.incrementWin(5);
                    }else {
                        if (newNode.isWinningMove())
                            newNode.incrementWin(0);
                        //newNode.incrementWin(-10);
                        else
                            newNode.incrementWin(0);
                        //newNode.incrementWin(-5);
                    }
                }
            }
            newNode.incrementGame();
        }else{
            if(!node.isDeadCell()) {
                if(node.getColor() == ally){
                    if(node.isWinningMove())
                        node.incrementWin(1);
                        // newNode.incrementWin(10);
                    else
                        node.incrementWin(1);
                    // newNode.incrementWin(5);
                }else {
                    if (node.isWinningMove())
                        node.incrementWin(0);
                        // newNode.incrementWin(-10);
                    else
                        node.incrementWin(0);
                    // newNode.incrementWin(-5);
                }
            }
            node.incrementGame();
        }
    }

    public void simulate(NodeTree_2 node){
        StatusCell current;
        if(node.getColor() == StatusCell.Blue)
            current = StatusCell.Red;
        else
            current = StatusCell.Blue;

        Board copy = node.getState().getCopy();
        ArrayList<Move> freeMoves = copy.getFreeMoves();

        int n_moves = freeMoves.size();
        for(int i=0;i<n_moves/2;i++){
            Move move = freeMoves.remove((int)(Math.random()*freeMoves.size()));
            copy.putStone(move.getX(),move.getY(),current);
        }

        if(current == StatusCell.Blue)
            current = StatusCell.Red;
        else
            current = StatusCell.Blue;

        for(Move move: freeMoves){
            copy.putStone(move.getX(),move.getY(),current);
        }

        if(copy.hasWon(ally))
            node.incrementWin(1);
        else
        // node.incrementWin(-1);
            node.incrementWin(0);

    }


    public double UCB1(NodeTree_2 node){

        float vi = (float) node.getWins() / node.getGames();
        int np = node.getGames();
        int ni = node.getParent().getGames();
        float a = 0.5f;
        double C = Math.sqrt(2);
        //if(vi>a)
        //    C = 0;

        return vi + C * Math.sqrt(Math.log(np)/ni);
    }


    @Override
    public Move getMove() {
        return start();
    }

    @Override
    public NodeTree_2 getRootTreeMcts() {
        return root;
    }

    @Override
    public void resetTree() {
        lastMove = null;
        System.out.println("DOOONE ___________________________");
    }

    @Override
    public void updateBoard(Board board) {
        realBoard = board;
    }
}
