package BotAlgorithms;

import EnumVariables.StatusCell;
import GameLogic.Board;
import GameLogic.Cell;
import GameLogic.Move;

import java.util.*;

/** This class is supposed to suggest a randomized move out of a board.
 * This it does by prefaring certain moves over others.
 * For example a move 3 away from the enemy could be more usefull then
 * just 1 away.
 * Created by Nibbla on 17.01.2017.
 */
public class ExtensionStrategy {
    Board board;
    int[][] weights;
    int weightSum = 0;
    int n=0;
    Random random = new Random();

    public Move getSuggestedMove(ArrayList<Move> freeMoves){
        int currentLimit = random.nextInt(weightSum+1);
        System.out.println("Weightsum" + weightSum);
        int currentSum = 0;
        Move m = null;
        outerloop: for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                currentSum += weights[x][y];
                if (currentSum >= currentLimit) {

                    System.out.println("Suggestet" + x + " " + y);
                    m = new Move(x, y);
                    break outerloop;
                }
            }
        }

        if (m == null){
            m = freeMoves.remove((int)(Math.random()*freeMoves.size()));
            System.out.println("SpecialRemoval: " + m.toString());
        }
        if (weightSum == 0){
            m = freeMoves.remove((int)(Math.random()*freeMoves.size()));
            System.out.println("SpecialRemoval" + m.toString());
        }



        for (int i = 0; i < freeMoves.size(); i++) {
            if (freeMoves.get(i).getX()==m.getX() && freeMoves.get(i).getY()==m.getY()){
                m = freeMoves.remove(i);
                System.out.println("Freemovesize" + freeMoves.size());
                break;
            }
        }

        return m;
    }


    public ExtensionStrategy(ArrayList<Move> freeMoves, Board b, StatusCell player){
        this.board = b;
        Cell[][] cells = b.getGrid();
         n = b.getSize();

        boolean[][] freeMove = new boolean[n][n];
        weights = new int[n][n];
        //distance to player
        int[][] disttancePlayer = new int[n][n];
        boolean[][] visitedPlayer = new boolean[n][n];



        int[][] disttanceEnemy = new int[n][n];
        boolean[][] visitedEnemy = new boolean[n][n];

        LinkedList<Cell> playerQueue = new LinkedList<Cell>();
        LinkedList<Cell> enemyQueue = new LinkedList<Cell>();

        settingUpDistanceMaps(cells,disttancePlayer,disttanceEnemy,n,player,playerQueue,enemyQueue,visitedPlayer,visitedEnemy);

        calcDistanceMap(disttancePlayer, visitedPlayer, playerQueue);
        calcDistanceMap(disttanceEnemy, visitedEnemy, enemyQueue);
/*
        System.out.println("EnemyDistances");
        for (int[] arr : disttanceEnemy) {
            System.out.println(Arrays.toString(arr));
        }

        System.out.println("FriendlyDistances");
        for (int[] arr : disttancePlayer) {
            System.out.println(Arrays.toString(arr));
        }


        System.out.println("Angepasst");
         */
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                if ( disttancePlayer[x][y] == 0) disttancePlayer[x][y] = -1;
                if ( disttanceEnemy[x][y] == 0) disttanceEnemy[x][y] = -1;

                if ( disttancePlayer[x][y] != -1) disttancePlayer[x][y] = Math.abs(disttancePlayer[x][y]-2);
                if ( disttanceEnemy[x][y] != -1) disttanceEnemy[x][y] = Math.abs(disttanceEnemy[x][y]-3);
            }
        }


        for (int i=0;i<n;i++) {
            for (int j = 0; j < n; j++) {
                freeMove[i][j] = false;
            }
        }
        for (int i = 0; i < freeMoves.size(); i++) {
            Move fm = freeMoves.get(i);
            freeMove[fm.getX()][fm.getY()] = true;
        }
        for (int i=0;i<n;i++) {
            for (int j = 0; j < n; j++) {
                if (freeMove[i][j] == false) {
                    disttancePlayer[i][j] = -1;
                    disttanceEnemy[i][j] = -1;
                }
            }
        }




        int max = -1;
        //calulating sum
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                int sum = disttancePlayer[x][y] + disttanceEnemy[x][y] + 1;
                if (sum <0) sum = 0;
                if (sum > max) max = sum;

                weights[x][y] = sum;
            }
        }
        max++;

        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
               if (weights[x][y] !=0) weights[x][y] = max - weights[x][y];
                weightSum += weights[x][y];
            }
        }

        System.out.println("EnemyDistances");
        for (int[] arr : disttanceEnemy) {
            System.out.println(Arrays.toString(arr));
        }

        System.out.println("FriendlyDistances");
        for (int[] arr : disttancePlayer) {
            System.out.println(Arrays.toString(arr));
        }
        System.out.println("PauerToThePeople");

        System.out.println("weights");
        for (int[] w : weights) {
            System.out.println(Arrays.toString(w));
        }
        System.out.println("PauerToThePeople");
      /*  */
    }

    private void calcDistanceMap(int[][] disttancePlayer, boolean[][] visitedPlayer, LinkedList<Cell> playerQueue) {
        if (playerQueue.isEmpty()){
            for (int x = 0; x < n; x++) {
                for (int y = 0; y < n; y++) {
                    disttancePlayer[x][y] = 1;
                }
            }
            return;
        }
        //destance search player
        while (!playerQueue.isEmpty()){
            Cell c =  playerQueue.poll();
            Cell[] neighbours = c.getNeighbors();
            for (int i = 0; i < neighbours.length; i++) {
                Cell neigh = neighbours[i];
                if (neigh == null) continue;
                if (visitedPlayer[neigh.getCoordXJ()][neigh.getCoordYI()]) continue;

               // if (disttancePlayer[neigh.getCoordYI()][neigh.getCoordXJ()] != -1) continue;
                disttancePlayer[neigh.getCoordXJ()][neigh.getCoordYI()] = disttancePlayer[c.getCoordXJ()][c.getCoordYI()]+1;
                visitedPlayer[neigh.getCoordXJ()][neigh.getCoordYI()]=true;
                playerQueue.add(neigh);
            }
        }
    }

    private void settingUpDistanceMaps(Cell[][] cells, int[][] disttancePlayer, int[][] disttanceEnemy, int n, StatusCell player, Queue playerQueue, Queue enemyQueue, boolean[][] visitedPlayer, boolean[][] visitedEnemy) {
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {


                if (cells[y][x].getStatus() == player) {
                    disttancePlayer[y][x] = 0;
                    disttanceEnemy[y][x] = -1;
                    playerQueue.add(cells[y][x]);
                    visitedEnemy[y][x] = true;
                    visitedPlayer[y][x] = true;
                }
                else if (cells[y][x].getStatus() == player.opposite()){
                    disttanceEnemy[y][x] = 0;
                    disttancePlayer[y][x] = -1;
                    enemyQueue.add(cells[y][x]);
                    visitedPlayer[y][x] = true;
                    visitedEnemy[y][x] = true;

                } else {
                    disttanceEnemy[y][x] = -1;
                    disttancePlayer[y][x] = -1;
                    visitedPlayer[y][x] = false;
                    visitedEnemy[y][x] = false;
                }


            }
        }
    }


}
