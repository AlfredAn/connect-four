package connectfour;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JFrame;

public class ConnectFour extends Applet implements Runnable, MouseListener, MouseMotionListener
{
    public static final int FRAMEWIDTH = 608, FRAMEHEIGHT = 640;
    
    private int mouseX, mouseY;
    private boolean mouseLeft, mouseRight, mouseLeftPressed, mouseRightPressed;
    
    private Image image;
    private Game game;
    
    @Override
    public void run()
    {
        while (true)
        {
            game.update();
            mouseLeftPressed = false;
            mouseRightPressed = false;
            
            repaint();
            
            try
            {
                Thread.sleep(10);
            }
            catch (InterruptedException e) {}
        }
    }
    
    @Override
    public void update(Graphics g)
    {
        paint(g);
    }
    
    @Override
    public void paint(Graphics g)
    {
        if (image == null)
        {
            image = createImage(FRAMEWIDTH, FRAMEHEIGHT);
        }
        
        Graphics g2 = image.getGraphics();
        
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(0, 0, FRAMEWIDTH, FRAMEHEIGHT);
        
        if (game != null)
        {
            game.draw(g2);
        }
        
        g.drawImage(image, 0, 0, null);
    }
    
    @Override
    public void start()
    {
        Thread thread = new Thread(this, "Main Thread");
        thread.start();
    }
    
    @Override
    public void init()
    {
        addMouseListener(this);
        addMouseMotionListener(this);
        requestFocus();
        
        game = new Game(this);
    }
    
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Connect Four");
        
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setSizeWithoutInsets(frame, FRAMEWIDTH, FRAMEHEIGHT);
        
        ConnectFour c4 = new ConnectFour();
        c4.init();
        
        frame.add(c4);
        frame.setVisible(true);
        
        c4.start();
    }
    
    private static void setSizeWithoutInsets(Frame frame, int width, int height)
    {
        frame.setSize(width, height);
        frame.pack();
        
        Insets insets = frame.getInsets();
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int frameWidth = width + insets.left + insets.right;
        int frameHeight = height + insets.top + insets.bottom;
        
        frame.setBounds(screenSize.width/2 - frameWidth/2,
                screenSize.height/2 - frameHeight/2, frameWidth, frameHeight);
    }
    
    @Override
    public void mouseMoved(MouseEvent me)
    {
        Point p = me.getPoint();
        mouseX = p.x;
        mouseY = p.y;
    }
    
    @Override
    public void mouseDragged(MouseEvent me)
    {
        Point p = me.getPoint();
        mouseX = p.x;
        mouseY = p.y;
    }
    
    @Override
    public void mouseEntered(MouseEvent me) {}
    
    @Override
    public void mouseExited(MouseEvent me) {}
    
    @Override
    public void mousePressed(MouseEvent me)
    {
        switch (me.getButton())
        {
            case MouseEvent.BUTTON1:
                mouseLeft = true;
                mouseLeftPressed = true;
                break;
            case MouseEvent.BUTTON2:
                mouseRight = true;
                mouseRightPressed = true;
                break;
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent me)
    {
        switch (me.getButton())
        {
            case MouseEvent.BUTTON1:
                mouseLeft = false;
                break;
            case MouseEvent.BUTTON2:
                mouseRight = false;
                break;
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent me) {}
    
    public int getMouseX()
    {
        return mouseX;
    }
    
    public int getMouseY()
    {
        return mouseY;
    }
    
    public boolean getMouseLeft()
    {
        return mouseLeft;
    }
    
    public boolean getMouseRight()
    {
        return mouseRight;
    }
    
    public boolean getMouseLeftPressed()
    {
        return mouseLeftPressed;
    }
    
    public boolean getMouseRightPressed()
    {
        return mouseRightPressed;
    }
}
