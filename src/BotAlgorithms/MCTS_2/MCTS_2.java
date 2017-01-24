package BotAlgorithms.MCTS_2;

import BotAlgorithms.ExtensionStrategy;
import BotAlgorithms.Strategy;
import EnumVariables.StatusCell;
import GameLogic.Board;
import GameLogic.Cell;
import GameLogic.Move;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by giogio on 1/21/17.
 */
public class MCTS_2 implements Strategy{
    private final static Logger log = Logger.getLogger( MCTS_2.class.getName() );
    private final int depthLevel;

    private boolean extensionStrategy;
    private StatusCell ally;
    private StatusCell enemy;
    private Board realBoard;
    private int maxtTime, n_expansion;
    private NodeTree_2 root;
    private NodeTree_2 lastMove;

    int[][] blueWinboard;
    int[][] bluePlayboard;

    int[][] redWinboard;
    int[][] redPlayboard;


    public MCTS_2(Board realBoard, StatusCell color, int maxtTime, int depthLevel, boolean ExtensionStrategy){
        this.depthLevel = depthLevel;
        this.realBoard = realBoard;
        this.ally = color;
        if(color == StatusCell.Blue)
            enemy = StatusCell.Red;
        else
            enemy = StatusCell.Blue;
        this.maxtTime = maxtTime;
        this.extensionStrategy = ExtensionStrategy;

        int s = realBoard.getSize();

        this.blueWinboard = new int[s][s];
        this.bluePlayboard = new int[s][s];
        this.redWinboard = new int[s][s];
        this.redPlayboard = new int[s][s];



    }

    public Move start(){
        setNewRoot();
        resetRave();
        double startTime = System.currentTimeMillis();
        n_expansion = 0;
        if (depthLevel==1){
            monteCarloSearch(root,startTime);
        }else {
            while (System.currentTimeMillis() - startTime < maxtTime) {

                expansion(selection(root));

            }
        }
        printTree(root);
        System.out.println("Expansions: "+n_expansion);
        NodeTree_2 m = null;
        if (depthLevel==1){ m = getBestValue();}else
        {m = getBestMove();}


        return m.getMove();

    }

    private void printTree(NodeTree_2 root) {

        System.out.println(" WINS/TOTAL: "+root.getWins()+"/"+root.getGames());
        printChild(root);
        System.out.println("Root: "+root.getGames());
    }

    private void printChild(NodeTree_2 leaf) {
        for(NodeTree_2 nodeTree: leaf.getChildren()) {
            for (int i = 0; i < nodeTree.getDepth(); i++) {
                System.out.print("-- ");
            }
            System.out.println(" WINS/TOTAL: "+nodeTree.getWins()+"/"+nodeTree.getGames());
            if(nodeTree.getChildren().size()>0){
                printChild(nodeTree);
            }
        }
    }

    private void monteCarloSearch(NodeTree_2 root, double startTime) {


        ArrayList<Move> moves = root.getState().getFreeMoves();
        for (int i = 0; i < moves.size(); i++) {
            NodeTree_2 newNode = new NodeTree_2(root);
            newNode.setMove(moves.get(i));
            n_expansion++;
        }

        int n = root.getChildren().size();
        while (System.currentTimeMillis() - startTime < maxtTime) {
            for (int i = 0; i < n; i++) {
                n_expansion++;
                simulateQuick(root.getChildren().get(i));
                root.getChildren().get(i).incrementGame();
            }
        }

    }

    private void resetRave() {
        int s = this.realBoard.getSize();
        this.blueWinboard = new int[s][s];
        this.bluePlayboard = new int[s][s];
        this.redWinboard = new int[s][s];
        this.redPlayboard = new int[s][s];
    }

    private Move startQuick() {
        setNewRoot();
        double startTime = System.currentTimeMillis();
        n_expansion = 0;
        while (System.currentTimeMillis() - startTime <maxtTime){
            expansion(selection(root));

        }
        System.out.println("Expansions: "+n_expansion);

        NodeTree_2 m = getMostSimulations();
        NodeTree_2 m2 = getBestValue();

        System.out.println("Moves to choose:");
        System.out.println(m);
        System.out.println(m2);

        while (m == null){
            expansion(selection(root));
            m = getMostSimulations();
        }

        return m.getMove();

    }


    public NodeTree_2 getMostSimulations(){
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

    public NodeTree_2 getBestValue() {
        NodeTree_2 bestNode = null;
        double bestValue = -999999999;
        for(NodeTree_2 child: root.getChildren()){

            double value = child.getWins()/(child.getGames()*1.0);
            if(value>bestValue) {
                bestNode = child;
                bestValue = value;
            }
        }
        lastMove = bestNode;
        return bestNode;
    }

    public NodeTree_2 getBestMove(){
        NodeTree_2 bestNode = null;
        double bestValue = -999999999;
        for(NodeTree_2 child: root.getChildren()){
            if(child.isDeadCell())
                continue;
            if(child.isWinningMove() || child.isLosingMove()){
                lastMove = child;
                return child ;
            }
            int value = child.getGames();
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

            double value;
            if (depthLevel==2) value = rave(child);
                else value = UCB1(child);
            //log.info(child.toString() + " UCB1: " + value);
            if(value>bestValue) {
                bestNode = child;
                bestValue = value;
            }
        }
        return selection(bestNode);
    }

    private double rave(NodeTree_2 node) {
        Move m = node.getMove();
        int y = m.getY();
        int x = m.getX();

        int[][] wins= redWinboard; //= new int[0][];
        int[][] plays= redPlayboard;// = new int[0][];
        StatusCell color = node.getColor();

        if (color==StatusCell.Blue) {
            wins = blueWinboard;
            plays = bluePlayboard;
        } else if (color==StatusCell.Red) {
            wins = redWinboard;
            plays = redPlayboard;
        } else if (color==StatusCell.Empty) return 0;
        if (ally ==StatusCell.Empty) return 0;

        float vi = (float) node.getWins() / node.getGames();
        float vi2 = (float) wins[y][x] / plays[y][x];


        double beta = betaFunction(node.getGames(),plays[y][x]);
        int np = node.getGames();
        int ni = node.getParent().getGames();
        float a = 0.85f;
        double C = Math.sqrt(0.04);
       // if(vi>a)
        //    C = 0;
        //System.out.println("1-beta * :" + vi + " beta*: " + vi2 + " val: " +  C * Math.sqrt(Math.log(ni)/np));

        return (1-beta)*vi+beta*vi2 + C * Math.sqrt(Math.log(ni)/np);


    }

    private double betaFunction(int games, int raveGames) {
        double nominator = raveGames;
        double denominator = games+raveGames+4*0.12*games*raveGames;
        double val = nominator/denominator;
        System.out.println("games:" + games + " raveGames: " + raveGames + " val: " + val);

        return val;
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
                    root.setLosingMove(false);
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
            simulateQuick(newNode);
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
                if (depthLevel==2) simulateQuick(newNode); else
                simulateQuick(newNode);
            }else {
                if(!newNode.isDeadCell()) {
                    if(newNode.getColor() == ally){
                        if(newNode.isWinningMove())
                            newNode.incrementWin(1,true);

                        else
                            newNode.incrementWin(1, true);
                    }else {
                        if (newNode.isWinningMove())
                            newNode.incrementWin(1, false);
                        else
                            newNode.incrementWin(1, false);
                    }
                }
            }
            newNode.incrementGame();
        }else{
            if(!node.isDeadCell()) {
                if(node.getColor() == ally){
                    if(node.isWinningMove())
                        node.incrementWin(1, true);
                    else
                        node.incrementWin(1, true);
                }else {
                    if (node.isWinningMove())
                        node.incrementWin(1, false);

                    else
                        node.incrementWin(1, false);

                }
            }
            node.incrementGame();
        }
    }



    public void simulateQuick(NodeTree_2 node){
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

        if(copy.hasWon(ally)) {
            node.incrementWin(1, node.getColor()==ally);
            //rave(copy, ally, true);
            //rave(copy,enemy,false);
        }
        else {
            node.incrementWin(1, node.getColor()!=ally);
            //rave(copy, ally, false);
            //rave(copy,enemy,true);
        }

    }

    public void simulateRave(NodeTree_2 node){
        StatusCell current;






        if(node.getColor() == StatusCell.Blue)
            current = StatusCell.Red;
        else
            current = StatusCell.Blue;



        Board copy = node.getState().getCopy();
        ArrayList<Move> freeMoves = copy.getFreeMoves();

        if(copy.hasWon(ally)) {
            node.incrementWin(1, node.getColor()==ally);
            rave(copy, ally, true);
            rave(copy,enemy,false);
            return;
        } else if (copy.hasWon(enemy)){
            node.incrementWin(1, node.getColor()!=ally);
            rave(copy, ally, false);
            rave(copy,enemy,true);
            return;
        }

        while (true){
            Move move = freeMoves.remove((int)(Math.random()*freeMoves.size()));
            copy.putStone(move.getX(),move.getY(),current);

            if(copy.hasWon(ally)) {
                node.incrementWin(1, node.getColor()==ally);
                rave(copy, ally, true);
                rave(copy,enemy,false);
                return;
            } else if (copy.hasWon(enemy)){
                node.incrementWin(1, node.getColor()!=ally);
                rave(copy, ally, false);
                rave(copy,enemy,true);
                return;
            }

        }

    }


    private void rave(Board copy, StatusCell ally, boolean b) {
        Cell[][] cells = copy.getGrid();
        int s = copy.getSize();
        int[][] wins = new int[0][];
        int[][] plays = new int[0][];

        if (ally==StatusCell.Blue) {
            wins = blueWinboard;
            plays = bluePlayboard;
        }
        if (ally==StatusCell.Red) {
            wins = redWinboard;
            plays = redPlayboard;
        }
        if (ally ==StatusCell.Empty) return;


        for (int i = 0; i < s; i++) {
            for (int j = 0; j < s; j++) {
                if (cells[i][j].getStatus() == ally) {
                    if (b) {
                        wins[i][j]++;
                    }
                        plays[i][j]++;
                }
            }
        }
    }


    public double UCB1(NodeTree_2 node){

        float vi = (float) node.getWins() / node.getGames();
        int np = node.getGames();
        int ni = node.getParent().getGames();
        float a = 0.65f;
        double C = Math.sqrt(0.14);
       // if(vi>a)
          //C = 0;

        return vi + C * Math.sqrt(Math.log(ni)/np);
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
