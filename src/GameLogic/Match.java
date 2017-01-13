package GameLogic;

import EnumVariables.BotType;
import EnumVariables.GameType;
import EnumVariables.StatusCell;
import GameLogic.History.History;
import GameLogic.History.RecordMove;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by giogio on 1/11/17.
 */
public class Match {
    private int boardSize;
    private Board board;
    private GameType gameType;
    private boolean swapRule;
    private Player[] players = new Player[2];
    private int currentPlayer=0;
    private StatusCell bot1Color;
    private StatusCell bot2Color;
    private BotType bot1Type;
    private BotType bot2Type;
    private boolean botTurn, paused, won;
    private ArrayList<Observer> observers;
    private History history;
    private TimeGame timeGame;
    private Timer timer;
    private int maxTimeRed,maxTimeBlue,depthLvlRed,depthlvlBlue;

    public Match(int boardSize, GameType gameType, boolean swapRule) {
        this.boardSize = boardSize;
        this.gameType = gameType;
        this.swapRule = swapRule;
        initialize();
    }
    public Match(int boardSize, GameType gameType, boolean swapRule, StatusCell botColor, BotType botType) {
        this.boardSize = boardSize;
        this.gameType = gameType;
        this.swapRule = swapRule;
        this.bot1Color = botColor;
        this.bot1Type = botType;
        initialize();
    }

    public Match(int boardSize, GameType gameType, boolean swapRule, StatusCell botColor, BotType botType,int maxTimeBlue, int depthlvlBlue) {
        this.boardSize = boardSize;
        this.gameType = gameType;
        this.swapRule = swapRule;
        this.bot1Color = botColor;
        this.bot1Type = botType;
        this.maxTimeBlue = maxTimeBlue;
        this.depthlvlBlue = depthlvlBlue;
        initialize();
    }

    public Match(int boardSize, GameType gameType, boolean swapRule, StatusCell botColor1, StatusCell botColor2, BotType bot1Type, BotType bot2Type) {
        this.boardSize = boardSize;
        this.gameType = gameType;
        this.swapRule = swapRule;
        this.bot1Color = botColor1;
        this.bot2Color = botColor2;
        this.bot1Type = bot1Type;
        this.bot2Type = bot2Type;
        initialize();

    }
    public Match(int boardSize, GameType gameType, boolean swapRule, StatusCell botColor1, StatusCell botColor2, BotType bot1Type, BotType bot2Type,int maxTimeBlue,int maxTimeRed, int depthlvlBlue, int depthLvlRed) {
        this.boardSize = boardSize;
        this.gameType = gameType;
        this.swapRule = swapRule;
        this.bot1Color = botColor1;
        this.bot2Color = botColor2;
        this.bot1Type = bot1Type;
        this.bot2Type = bot2Type;
        this.maxTimeBlue = maxTimeBlue;
        this.depthlvlBlue = depthlvlBlue;
        this.maxTimeRed = maxTimeRed;
        this.depthLvlRed = depthLvlRed;
        initialize();

    }

    private void initialize(){
        board = new Board(boardSize);
        observers = new ArrayList<Observer>();
        paused=false;
        history = new History();
        timeGame = new TimeGame();
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                timeGame.increment();
                notifyObserver();

            }
        });

    }

    public void startMatch(){
        botTurn = false;
        if(gameType==GameType.Multiplayer){
            players[0] = new Human(StatusCell.Blue, this);
            players[1] = new Human(StatusCell.Red, this);
            currentPlayer = 0;
        }else if(gameType == GameType.HumanVsBot){
            if(bot1Color == StatusCell.Blue){
                players[0] = new Human(StatusCell.Red, this);
                if(bot1Type==BotType.PathFinding)
                    players[1] = new Bot(bot1Type, bot1Color, this);
                else
                    players[1] = new Bot(bot1Type, bot1Color, this, maxTimeBlue,depthlvlBlue);

                botTurn = true;
                currentPlayer = 1;
                players[currentPlayer].makeMove(0,0);
            }else {
                players[0] = new Human(StatusCell.Blue, this);
                if(bot1Type==BotType.PathFinding)
                    players[1] = new Bot(bot1Type, bot1Color, this);
                else
                    players[1] = new Bot(bot1Type, bot1Color, this, maxTimeBlue,depthlvlBlue);
                currentPlayer = 0;
            }

        }else {
            System.out.println("Here");
            System.out.println(maxTimeRed+" "+depthLvlRed);

            if(bot1Type == BotType.PathFinding)
                players[0] = new Bot(bot1Type, bot1Color, this);
            else
                players[0] = new Bot(bot1Type, bot1Color, this,maxTimeBlue,depthlvlBlue);
            if(bot2Type == BotType.PathFinding)
                players[1] = new Bot(bot2Type, bot2Color, this);
            else
                players[1] = new Bot(bot2Type, bot2Color, this,maxTimeRed,depthLvlRed);
            botTurn = true;
            if(bot1Color==StatusCell.Blue)
                currentPlayer=0;
            else
                currentPlayer=1;

            players[currentPlayer].makeMove(0,0);
        }

        timer.start();

        notifyObserver();




    }

    private void switchPlayer(){
        currentPlayer++;
        currentPlayer=currentPlayer%2;
        if(!paused && players[currentPlayer] instanceof Bot){
            botTurn = true;
            players[currentPlayer].makeMove(0,0);

        }
    }

    public void setBotTurn(boolean turn){
        botTurn = turn;
    }

    public Board getBoard(){
        return board;
    }

    public boolean isSwapRule(){
        return swapRule;
    }

    public History getHistory(){
        return history;
    }
    public void putStone(int x, int y){
        if(!paused) {
            board.putStone(x, y, players[currentPlayer].getColor());
            history.addRecord(new RecordMove(players[currentPlayer].getColor(),x,y));
            if (board.hasWon(players[currentPlayer].getColor())) {
                System.out.println("WON"+players[currentPlayer].getColor());
                won = true;
                pause();
            } else {
                switchPlayer();
            }
            notifyImportant();
            System.out.println("Notified");
        }
    }

    public void addObserver(Observer observer){
        observers.add(observer);
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isBotTurn() {
        return botTurn;
    }

    public StatusCell getCurrentColorPlayer(){
        return players[currentPlayer].getColor();
    }


    public void notifyImportant(){
        for(Observer observer:observers){
            observer.update(true);
        }
    }

    public void notifyObserver(){
        for(Observer observer:observers){
            observer.update(false);
        }
    }

    public boolean isWon() {
        return won;
    }

    public TimeGame getTime(){
        return timeGame;
    }

    public void pause(){
        paused=!paused;
        if(paused)
            timer.stop();
        else
            timer.start();
    }

    public void loadHistory(History history){
        board = new Board(boardSize);
        for (RecordMove recordMove: history.getRecords()){
            if(recordMove.isStatus())
                board.putStone(recordMove.getX(),recordMove.getY(),recordMove.getColor());
        }
    }

    public void undo(){
        int count = 1;
        while (count <= history.getRecords().size() && !history.getRecords().get(history.getRecords().size() - count).isStatus()) {
            count++;
        }
        if (!(count > history.getRecords().size()))
            history.getRecords().get(history.getRecords().size() - count).deleteMove();

        count=1;
        while (count <= history.getRecords().size() && !history.getRecords().get(history.getRecords().size() - count).isStatus()) {
            count++;
        }
        if(count>history.getRecords().size()){
            if(players[0].getColor()==StatusCell.Blue)
                currentPlayer=0;
            else
                currentPlayer=1;
        }else if(history.getRecords().get(history.getRecords().size()-count).getColor()==StatusCell.Blue){
            if(players[0].getColor()==StatusCell.Blue)
                currentPlayer=1;
            else
                currentPlayer=0;
        }else{
            if(players[0].getColor()==StatusCell.Red)
                currentPlayer=1;
            else
                currentPlayer=0;
        }

        loadHistory(history);


    }

    public GameType getGameType(){
        return gameType;
    }

}
