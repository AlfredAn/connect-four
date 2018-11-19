package connectfour.player;

import connectfour.Board;
import connectfour.Game;

public abstract class Player
{
    public final String name;
    public final byte id;
    
    public final Game game;
    Board currentBoard;
    private boolean isMyTurn = false;
    
    public Player(Game game, String name, int id)
    {
        this.game = game;
        this.name = name;
        this.id = (byte)id;
    }
    
    public void update() {}
    
    public final void startPlayerTurn(Board board)
    {
        currentBoard = board;
        isMyTurn = true;
        startTurn(board);
    }
    
    public final int getHoveredMove()
    {
        if (!isMyTurn)
        {
            return -1;
        }
        
        return getProbableMove();
    }
    
    final void finishTurn()
    {
        int move = endTurn();
        
        if (move < 0 || move >= currentBoard.width)
        {
            throw new IllegalStateException("Illegal move by player \"" + name + "\": " + move);
        }
        
        isMyTurn = false;
        currentBoard = null;
        
        game.finishTurn(move);
    }
    
    abstract void startTurn(Board board);
    abstract int getProbableMove();
    abstract int endTurn();
}