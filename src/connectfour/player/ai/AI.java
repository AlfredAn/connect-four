package connectfour.player.ai;

import connectfour.Board;
import connectfour.player.Player;

public abstract class AI implements Runnable
{
    private final Thread thread;
    final Board currentBoard;
    final Player player;
    
    int probableMove = -1;
    private int nextMove = -1;
    
    public AI(Board board, Player player)
    {
        this.currentBoard = board;
        this.player = player;
        
        thread = new Thread(this, player.name + " (turn " + (board.countMoves() + 1) + ")");
        thread.start();
    }
    
    @Override
    public final void run()
    {
        int m = runAI();
        
        nextMove = m;
    }
    
    abstract int runAI();
    
    public final int getProbableMove()
    {
        return probableMove;
    }
    
    public final boolean isFinished()
    {
        return (nextMove != -1);
    }
    
    public final int getNextMove()
    {
        return nextMove;
    }
}