package connectfour;

import connectfour.player.AIPlayer;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Arrays;

public final class Board
{
    private final byte[][] cells;
    
    public final int width, height;
    
    private static byte[][] createFilled(int width, int height, byte val)
    {
        byte[][] c = new byte[width][height];
        for (int i = 0; i < width; i++)
        {
            Arrays.fill(c[i], val);
        }
        
        return c;
    }
    
    public Board()
    {
        this(7, 6);
    }
    public Board(int width, int height)
    {
        this(createFilled(width, height, (byte)-1), true);
    }
    public Board(Board board)
    {
        this(board.cells, true);
    }
    public Board(Board board, boolean copyArray)
    {
        this(board.cells, copyArray);
    }
    public Board(byte[][] cells)
    {
        this(cells, true);
    }
    public Board(byte[][] cells, boolean copyArray)
    {
        width = cells.length;
        height = cells[0].length;
        
        if (copyArray)
        {
            this.cells = new byte[width][];
            
            for (int i = 0; i < width; i++)
            {
                this.cells[i] = Arrays.copyOf(cells[i], height);
            }
        }
        else
        {
            this.cells = cells;
        }
    }
    
    public byte getCell(int x, int y)
    {
        return cells[x][y];
    }
    
    public void setCell(int x, int y, byte player)
    {
        cells[x][y] = player;
    }
    
    public void set(Board board)
    {
        if (board.width != width || board.height != height)
        {
            throw new IllegalArgumentException("Error in Board.set(): Board dimensions must match.");
        }
        
        for (int i = 0; i < width; i++)
        {
            System.arraycopy(board.cells[i], 0, cells[i], 0, height);
        }
    }
    
    public boolean isLegalMove(int row)
    {
        if (row == -1)
        {
            return false;
        }
        
        for (int i = height-1; i >= 0; i--)
        {
            if (cells[row][i] == -1)
            {
                return true;
            }
        }
        
        return false;
    }
    
    public int countMoves()
    {
        int m = 0;
        
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                if (cells[x][y] != -1)
                {
                    m++;
                }
            }
        }
        
        return m;
    }
    
    /**
     *
     * @return -1 if game is not finished, -2 if tie, otherwise player number (0 or 1)
     */
    public byte getWinner()
    {
        boolean isFull = true;
        
        //scan for horizontal rows
        for (int y = 0; y < height; y++)
        {
            int len = 0;
            byte player = -1;
            for (int x = 0; x < width; x++)
            {
                byte c = cells[x][y];
                if (c == -1)
                {
                    len = 0;
                    player = -1;
                    isFull = false;
                }
                if (c == player)
                {
                    len++;
                    if (len == 4)
                    {
                        return player;
                    }
                }
                else
                {
                    len = 1;
                    player = c;
                }
            }
        }
        
        if (isFull)
        {
            return -2;
        }
        
        //scan for vertical rows
        for (int x = 0; x < width; x++)
        {
            int len = 0;
            byte player = -1;
            for (int y = 0; y < height; y++)
            {
                byte c = cells[x][y];
                if (c == -1)
                {
                    len = 0;
                    player = -1;
                }
                else if (c == player)
                {
                    len++;
                    if (len == 4)
                    {
                        return player;
                    }
                }
                else
                {
                    len = 1;
                    player = c;
                }
            }
        }
        
        //scan for diagonal rows (lower left to upper right)
        int max = width + height - 1;
        
        for (int s = 3; s < max - 3; s++)
        {
            int x = Math.min(s, width - 1);
            int y = Math.max(s - width + 1, 0);
            
            int len = 0;
            byte player = -1;
            
            int searchlen = Math.min(s+1, max - s);
            
            for (int i = 0; i < searchlen; i++)
            {
                byte c = cells[x][y];
                if (c == -1)
                {
                    len = 0;
                    player = -1;
                }
                else if (c == player)
                {
                    len++;
                    if (len == 4)
                    {
                        return player;
                    }
                }
                else
                {
                    len = 1;
                    player = c;
                }
                
                x--;
                y++;
            }
        }
        //scan for diagonal rows (lower right to upper left)
        for (int s = 3; s < max - 3; s++)
        {
            int x = Math.max(width - 1 - s, 0);
            int y = Math.max(s - width + 1, 0);
            
            int len = 0;
            byte player = -1;
            int searchlen = Math.min(s+1, max - s);
            for (int i = 0; i < searchlen; i++)
            {
                byte c = cells[x][y];
                if (c == -1)
                {
                    len = 0;
                    player = -1;
                }
                else if (c == player)
                {
                    len++;
                    if (len == 4)
                    {
                        return player;
                    }
                }
                else
                {
                    len = 1;
                    player = c;
                }
                
                x++;
                y++;
            }
        }
        
        return -1;
    }
    
    private boolean[][][] winPoint;
    public int evaluate(Board board, byte thisPlayer, byte turn, int difficulty)
    {
        int score = 0;
        
        if (winPoint == null)
        {
            winPoint = new boolean[2][width][height];
        }
        
        for (int i = 0; i < 2; i++)
        {
            for (int j = 0; j < width; j++)
            {
                Arrays.fill(winPoint[i][j], false);
            }
        }
        
        boolean isFull = true;
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                int c = cells[x][y];
                
                if (c == -1)
                {
                    isFull = false;
                }
                else
                {
                    if (c == thisPlayer)
                    {
                        score -= Math.abs(3 - x);
                    }
                    else
                    {
                        score += Math.abs(3 - x);
                    }
                }
            }
        }
        
        if (isFull)
        {
            //tie
            return -800;
        }
        
        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                byte c = cells[x][y];
                for (int p = 0; p < 2; p++)
                {
                    if (c != 1-p) //if this cell doesn't belong to the other player
                    {
                        //horizontal
                        int w = checkChains(x, y, p, 1, 0);
                        if (w >= 0)
                        {
                            //win for player p
                            return (1000 - countMoves()) * ((p == thisPlayer) ? 1 : -1);
                        }
                        
                        //vertical
                        w = checkChains(x, y, p, 0, 1);
                        if (w >= 0)
                        {
                            return (1000 - countMoves()) * ((p == thisPlayer) ? 1 : -1);
                        }
                        
                        //diagonal (->lower right)
                        w = checkChains(x, y, p, 1, 1);
                        if (w >= 0)
                        {
                            return (1000 - countMoves()) * ((p == thisPlayer) ? 1 : -1);
                        }
                        
                        //diagonal (->upper right)
                        w = checkChains(x, y, p, 1, -1);
                        if (w >= 0)
                        {
                            return (1000 - countMoves()) * ((p == thisPlayer) ? 1 : -1);
                        }
                    }
                }
            }
        }
        
        int chainScore, counterScore;
        
        switch (difficulty)
        {
            case AIPlayer.DIFF_EASY:
                chainScore = -7;
                counterScore = -3;
                break;
            case AIPlayer.DIFF_MEDIUM:
                chainScore = 6;
                counterScore = 5;
                break;
            case AIPlayer.DIFF_HARD:
                chainScore = 50;
                counterScore = 15;
                break;
            default:
                throw new IllegalArgumentException("Invalid difficulty: " + difficulty);
        }
        
        //find lowest win point in each row
        for (int x = 0; x < width; x++)
        {
            int my = height;
            
            //scan column
            for (int y = height - 1; y >= 0; y--)
            {
                if (cells[x][y] == -1)
                {
                    boolean stop = false;
                    if (winPoint[thisPlayer][x][y])
                    {
                        if (turn == thisPlayer && my == y+1)
                        {
                            //guaranteed win in next turn
                            return 999 - countMoves();
                        }
                        else if (turn != thisPlayer && my == y+1)
                        {
                            //can be countered
                            score += counterScore;
                        }
                        else
                        {
                            //possible win
                            score += chainScore;// - 4 * (my - y);
                            stop = true;
                        }
                    }
                    if (winPoint[1-thisPlayer][x][y])
                    {
                        if (turn != thisPlayer && my == y+1)
                        {
                            //guaranteed loss in next turn
                            return -999 + countMoves();
                        }
                        else if (turn != thisPlayer && my == y+1)
                        {
                            //can be countered
                            score -= counterScore;
                        }
                        else
                        {
                            //possible loss
                            score -= chainScore;// - 4 * (my - y);
                            break;
                        }
                    }
                    if (stop)
                    {
                        break;
                    }
                }
                else
                {
                    my = y;
                }
            }
        }
        
        return score;
    }
    
    //returns the winning player, if there is one. otherwise -1
    private int checkChains(int x, int y, int p, int dx, int dy)
    {
        byte c = cells[x][y];
        byte a = 1; //number of cells in chain
        
        if (c == 1-p) //if this cell belongs to the other player
        {
            return -1;
        }
        
        //forward
        for (int d = 1; d < 4; d++)
        {
            int ax = x + d * dx;
            int ay = y + d * dy;
            
            if (ax >= width || ay >= height || ax < 0 || ay < 0 || cells[ax][ay] != p || a == 4)
            {
                break;
            }
            a++;
        }
        //backward
        for (int d = -1; d > -4; d--)
        {
            int ax = x + d * dx;
            int ay = y + d * dy;
            
            if (ax < 0 || ay < 0 || ax >= width || ay >= height || cells[ax][ay] != p || a == 4)
            {
                break;
            }
            a++;
        }
        
        if (a == 4)
        {
            if (c == -1)
            {
                //possible 4 in a row
                winPoint[p][x][y] = true;
            }
            else
            {
                //4 in a row
                return p;
            }
        }
        
        return -1;
    }
    
    public Board doMove(int player, int row)
    {
        return doMove(this, player, row);
    }
    public Board doMove(Board dest, int player, int row)
    {
        if (row < 0 || row >= width)
        {
            throw new IllegalArgumentException("Invalid row: " + row);
        }
        
        if (dest == null)
        {
            dest = new Board(this);
        }
        else if (dest != this)
        {
            dest.set(this);
        }
        
        for (int i = height-1; i >= 0; i--)
        {
            if (dest.cells[row][i] == -1)
            {
                dest.cells[row][i] = (byte)player;
                return dest;
            }
        }
        
        return null;
    }
    
    public void draw(Graphics g)
    {
        draw(g, -1, -1);
    }
    public void draw(Graphics g, int slot, int player)
    {
        draw(g, slot, player, 16, 16, 64, 64, 16, 16);
    }
    public void draw(Graphics g, int slot, int player, int xPos, int yPos)
    {
        draw(g, slot, player, xPos, yPos, 64, 64, 16, 16);
    }
    public void draw(Graphics g, int slot, int player, int xPos, int yPos, int xSize, int ySize, int xMargin, int yMargin)
    {
        int xDist = xSize + xMargin;
        int yDist = ySize + yMargin;
        
        g.setColor(Color.BLACK);
        g.fillRect(xPos, yPos, width * xDist + xMargin, height * yDist + yMargin);
        
        if (slot != -1 && player != -1)
        {
            g.setColor(Game.playerColor(player));
            g.fillOval(xPos + xMargin + slot * xDist, yPos - yDist, xSize, ySize);
        }
        
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                g.setColor(Game.playerColor(cells[x][y]));
                g.fillOval(xPos + xMargin + x * xDist, yPos + yMargin + y * yDist, xSize, ySize);
            }
        }
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                switch (cells[x][y])
                {
                    case -1:
                        sb.append(" ");
                        break;
                    case 0:
                        sb.append("O");
                        break;
                    case 1:
                        sb.append("X");
                        break;
                }
            }
            
            sb.append("\n");
        }
        
        return sb.toString();
    }
}















