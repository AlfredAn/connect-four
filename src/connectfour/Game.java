package connectfour;

import connectfour.player.AIPlayer;
import connectfour.player.HumanPlayer;
import connectfour.player.Player;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JOptionPane;

public final class Game
{
    public final ConnectFour applet;
    private Board board;
    private final Player[] players = new Player[2];
    private Player currentPlayer;
    
    public Game(ConnectFour applet)
    {
        this.applet = applet;
        
        board = new Board();
        
        for (int i = 0; i < 2; i++)
        {
            int playerType = JOptionPane.showOptionDialog(
                    applet,
                    "Player " + (i+1) + " (" + playerColorString(i) + "): Choose player type",
                    "Player " + (i+1),
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new String[]
                    {
                        "Human player",
                        "AI player"
                    },
                    JOptionPane.CLOSED_OPTION);
            
            if (playerType == 0)
            {
                players[i] = new HumanPlayer(this, "Player " + (i+1) + " (Human)", i);
            }
            else if (playerType == 1)
            {
                int difficulty = JOptionPane.showOptionDialog(
                        applet,
                        "Player " + (i+1) + " (" + playerColorString(i) + "): Choose AI difficulty",
                        "Player " + (i+1),
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new String[]
                        {
                            "Easy",
                            "Medium",
                            "Hard"
                        },
                        JOptionPane.CLOSED_OPTION);
                
                switch (difficulty)
                {
                    case 0:
                        players[i] = new AIPlayer(this, "Player " + (i+1) + " (AI - Easy)", i, 3, AIPlayer.DIFF_EASY);
                        break;
                    case 1:
                        players[i] = new AIPlayer(this, "Player " + (i+1) + " (AI - Medium)", i, 5, AIPlayer.DIFF_MEDIUM);
                        break;
                    case 2:
                        players[i] = new AIPlayer(this, "Player " + (i+1) + " (AI - Hard)", i, 8, AIPlayer.DIFF_HARD);
                        break;
                    default:
                        System.exit(0);
                }
            }
            else if (playerType == -1)
            {
                System.exit(0);
            }
        }
        
        startTurn(players[0]);
    }
    
    private String playerColorString(int playerId)
    {
        switch (playerId)
        {
            case 0:
                return "Blue";
            case 1:
                return "Red";
            default:
                return "Undefined (" + playerId + ")";
        }
    }
    
    private void startTurn(Player player)
    {
        player.startPlayerTurn(board);
        currentPlayer = player;
    }
    
    public void finishTurn(int move)
    {
        Board result = board.doMove(currentPlayer.id, move);
        if (result == null)
        {
            throw new IllegalStateException("Illegal move performed by player \"" + currentPlayer.name
                    + "\": " + move);
        }
        
        //result.chainScore(result, (byte)0);
        //System.out.println("----\n" + new Board(result.winPoint));
        
        byte winner = board.getWinner();
        
        if (winner == -2)
        {
            applet.repaint();
            JOptionPane.showMessageDialog(null, "Tie!");
            System.exit(0);
        }
        else if (winner != -1)
        {
            applet.repaint();
            JOptionPane.showMessageDialog(null, players[winner].name + " wins!");
            System.exit(0);
        }
        
        startTurn(players[1-currentPlayer.id]);
    }
    
    public void update()
    {
        currentPlayer.update();
    }
    
    public void draw(Graphics g)
    {
        int slot = -1;
        
        if (currentPlayer != null)
        {
            slot = currentPlayer.getHoveredMove();
        }
        
        board.draw(g, slot, currentPlayer.id, 16, 128);
    }
    
    public int getMouseColumn()
    {
        int w = 7;
        
        int xPos = 16;
        int yPos = 128;
        
        int xSize = 64;
        int ySize = 64;
        int xMargin = 16;
        int yMargin = 16;
        
        int xDist = xSize + xMargin;
        int yDist = ySize + yMargin;
        
        int abspos = applet.getMouseX() - xPos - (xMargin / 2);
        int col = (int)Math.floor((double)abspos / xDist);
        
        if (col >= 0 && col < w)
        {
            return col;
        }
        else
        {
            return -1;
        }
    }
    
    public static Color playerColor(int player)
    {
        switch (player)
        {
            case -1:
                return Color.LIGHT_GRAY;
            case 0:
                return Color.BLUE;
            case 1:
                return Color.RED;
            default:
                return Color.PINK;
        }
    }
}