package connectfour.player.ai;

import connectfour.Board;
import connectfour.player.AIPlayer;
import connectfour.player.Player;

public final class MinimaxAI extends AI
{
    public final int maxDepth;
    private long evals = 0;
    private final int difficulty;
    
    public MinimaxAI(Board board, Player player, int depth, int difficulty)
    {
        super(board, player);
        
        this.difficulty = difficulty;
        this.maxDepth = depth;
    }
    
    private Board[] boards;
    
    @Override
    public int runAI()
    {
        probableMove = 3;
        
        boards = new Board[maxDepth+1];
        boards[maxDepth] = currentBoard;
        
        for (int i = 0; i < maxDepth; i++)
        {
            boards[i] = new Board(currentBoard.width, currentBoard.height);
        }
        
        return minimax(maxDepth);
    }
    
    private int minimax(int depth)
    {
        long startTime = System.nanoTime();
        
        Board board = boards[depth];
        
        if (depth == 0 || board.getWinner() != -1)
        {
            throw new IllegalArgumentException();
        }
        
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        int move = -1;
        
        for (int i = 0; i < board.width; i++)
        {
            Board res = board.doMove(boards[depth-1], player.id, i);
            if (res == null)
            {
                continue;
            }
            
            int s = minValue(depth-1, alpha, beta);
            
            if (s > alpha)
            {
                alpha = s;
                move = i;
            }
        }
        
        long delta = System.nanoTime() - startTime;
        String s;
        
        if (delta >= 10_000_000)
        {
            s = (int)((double)(delta / 1_000_000)) + " ms";
        }
        else if (delta >= 10_000)
        {
            s = (int)((double)(delta / 1_000)) + " µs";
        }
        else
        {
            s = delta + " ns";
        }
        
        System.out.println("Turn " + (board.countMoves() + 1) + ": "
                + player.name + " selected column " + move
                + " with score " + alpha + " (" + s + ", "
                + evals + " static evaluation" + (evals > 1 ? "s" : "") + ")");
        
        return move;
    }
    
    private int maxValue(int depth, int alpha, int beta)
    {
        Board board = boards[depth];
        
        if (depth == 0 || board.getWinner() != -1)
        {
            evals++;
            return board.evaluate(board, player.id, player.id, difficulty);
        }
        
        for (int i = 0; i < board.width; i++)
        {
            Board res = board.doMove(boards[depth-1], player.id, i);
            if (res == null)
            {
                continue;
            }
            
            alpha = Math.max(alpha, minValue(depth-1, alpha, beta));
            if (alpha >= beta)
            {
                //prune branch
                return alpha;
            }
        }
        
        if (alpha == Integer.MIN_VALUE)
        {
            return -800;
        }
        
        return alpha;
    }
    
    private int minValue(int depth, int alpha, int beta)
    {
        Board board = boards[depth];
        
        if (depth == 0 || board.getWinner() != -1)
        {
            evals++;
            return board.evaluate(board, player.id, (byte)(1-player.id), difficulty);
        }
        
        for (int i = 0; i < board.width; i++)
        {
            Board res = board.doMove(boards[depth-1], 1-player.id, i);
            if (res == null)
            {
                continue;
            }
            
            beta = Math.min(beta, maxValue(depth-1, alpha, beta));
            
            if (alpha >= beta)
            {
                //prune branch
                return beta;
            }
        }
        
        if (beta == Integer.MAX_VALUE)
        {
            return -800;
        }
        
        return beta;
    }
}