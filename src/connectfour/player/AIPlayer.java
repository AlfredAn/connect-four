package connectfour.player;

import connectfour.Board;
import connectfour.Game;
import connectfour.player.ai.AI;
import connectfour.player.ai.MinimaxAI;

public class AIPlayer extends Player
{
    private AI ai;
    public final int depth;
    final int difficulty;
    
    public static final int
            DIFF_EASY = 0,
            DIFF_MEDIUM = 1,
            DIFF_HARD = 2;
    
    public AIPlayer(Game game, String name, int id, int depth, int difficulty)
    {
        super(game, name, id);
        
        this.depth = depth;
        this.difficulty = difficulty;
    }
    
    @Override
    public void startTurn(Board board)
    {
        ai = new MinimaxAI(board, this, depth, difficulty);
    }
    
    @Override
    public void update()
    {
        if (ai.isFinished())
        {
            finishTurn();
        }
    }
    
    @Override
    public int getProbableMove()
    {
        return ai.getProbableMove();
    }
    
    @Override
    int endTurn()
    {
        return ai.getNextMove();
    }
}