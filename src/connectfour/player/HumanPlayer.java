package connectfour.player;

import connectfour.Board;
import connectfour.Game;

public class HumanPlayer extends Player
{
    public HumanPlayer(Game game, String name, int id)
    {
        super(game, name, id);
    }
    
    @Override
    public void update()
    {
        if (game.applet.getMouseLeftPressed() && currentBoard.isLegalMove(game.getMouseColumn()))
        {
            finishTurn();
        }
    }
    
    @Override
    public void startTurn(Board board) {}
    
    @Override
    public int getProbableMove()
    {
        int col = game.getMouseColumn();
        
        return currentBoard.isLegalMove(col) ? col : -1;
    }
    
    @Override
    int endTurn()
    {
        return game.getMouseColumn();
    }
}