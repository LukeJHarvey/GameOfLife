/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gameoflifeboard;

import java.io.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;

 
public class GameOfLife extends JFrame implements Runnable {

    static Window w = new Window();
    
    boolean animateFirstTime = true;
    
    Image image;
    Graphics2D g;
    
    static GameOfLife frame1;
    final int numRows = 32;
    final int numColumns = 32;
    public static int columnWidth;
    public static int rowHeight;
    boolean paused = true;
    Tile lastChanged;

    public static void main(String[] args) {
        frame1 = new GameOfLife();
        frame1.setSize(w.WINDOW_WIDTH, w.WINDOW_HEIGHT);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setVisible(true);
    }

    public GameOfLife() {

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.BUTTON1 == e.getButton()) {
                    int xpos = e.getX() - w.getX(0);
                    int ypos = e.getY() - w.getY(0);
                    
                    int row = ypos/rowHeight;
                    int column = xpos/columnWidth;
                    if(row>numRows) row = numRows-1;
                    if(row<0) row = 0;
                    if(column>numColumns) column = numColumns-1;
                    if(column<0) column = 0;
            
                    System.out.println(row + ", " + column);
                    Tile.board[row][column].changeAlive();
                    Tile.checkNextTo();
                    //left button
                }
                if (e.BUTTON3 == e.getButton()) {
                    //right button
                    reset();
                }
                repaint();
            }
        });

    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {
        repaint();
        if (e.BUTTON1 == e.getButton()) {
            int xpos = e.getX() - w.getX(0);
            int ypos = e.getY() - w.getY(0);

            int row = ypos/rowHeight;
            int column = xpos/columnWidth;
            if(row>numRows) row = numRows-1;
            if(row<0) row = 0;
            if(column>numColumns) column = numColumns-1;
            if(column<0) column = 0;
            
            if(lastChanged!=Tile.board[row][column]) {
                lastChanged=Tile.board[row][column];
                System.out.println(row + ", " + column);
                Tile.board[row][column].changeAlive();
                Tile.checkNextTo();
            }
            //left button
        }
      }
    });

    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseMoved(MouseEvent e) {
        repaint();
      }
    });
        addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.VK_RIGHT == e.getKeyCode())
                {
                }
                if (e.VK_LEFT == e.getKeyCode())
                {
                    
                }
                if (e.VK_UP == e.getKeyCode())
                {
                }
                if (e.VK_DOWN == e.getKeyCode())
                {
                }
                if (e.VK_SPACE == e.getKeyCode())
                {
                    paused = !paused;
                }

                repaint();
            }
        });
        init();
        start();
    }




    Thread relaxer;
////////////////////////////////////////////////////////////////////////////
    public void init() {
        requestFocus();
    }
////////////////////////////////////////////////////////////////////////////
    public void destroy() {
    }
////////////////////////////////////////////////////////////////////////////
    public void paint(Graphics gOld) {
        if (image == null || w.xsize != getSize().width || w.ysize != getSize().height) {
            w.xsize = getSize().width;
            w.ysize = getSize().height;
            image = createImage(w.xsize, w.ysize);
            g = (Graphics2D) image.getGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }

//fill background
        g.setColor(Color.cyan);

        g.fillRect(0, 0, w.xsize, w.ysize);

        int x[] = {w.getX(0), w.getX(w.getWidth2()), w.getX(w.getWidth2()), w.getX(0), w.getX(0)};
        int y[] = {w.getY(0), w.getY(0), w.getY(w.getHeight2()), w.getY(w.getHeight2()), w.getY(0)};
//fill border
        g.setColor(Color.white);
        g.fillPolygon(x, y, 4);
// draw border
        g.setColor(Color.red);
        g.drawPolyline(x, y, 5);
        
        if (animateFirstTime) {
            gOld.drawImage(image, 0, 0, null);
            return;
        }
        

        for (int zrow=0;zrow<numRows;zrow++)
        {
            for (int zcolumn=0;zcolumn<numColumns;zcolumn++)
            {
                if(Tile.board[zrow][zcolumn].alive) {
                    g.setColor(Color.BLACK);
                    g.fillRect(w.getX(0)+zcolumn*columnWidth,
                      w.getY(0)+zrow*rowHeight,
                      columnWidth,
                      rowHeight);
                }
                else  {
                    if(paused)
                        g.setColor(Color.RED);
                    else
                        g.setColor(Color.WHITE);
                    
                    g.fillRect(w.getX(0)+zcolumn*columnWidth,
                      w.getY(0)+zrow*rowHeight,
                      columnWidth,
                      rowHeight);
                }
//                g.setColor(Color.MAGENTA);
//                g.drawString(""+Tile.board[zrow][zcolumn].nextTo,
//                        w.getX(0)+zcolumn*columnWidth + columnWidth/2,
//                        w.getY(0)+zrow*rowHeight + rowHeight/2);
                        //w.getX(Tile.board[zrow][zcolumn].getRow()*columnWidth+(columnWidth/2)-5),
                        //w.getY(Tile.board[zrow][zcolumn].getColumn()*rowHeight+(rowHeight/2))+5);
            }
        }
        g.setColor(Color.blue);
//horizontal lines
        for (int zi=1;zi<numRows;zi++)
        {
            g.drawLine(w.getX(0) ,w.getY(0)+zi*w.getHeight2()/numRows ,
            w.getX(w.getWidth2()) ,w.getY(0)+zi*w.getHeight2()/numRows );
        }
//vertical lines
        for (int zi=1;zi<numColumns;zi++)
        {
            g.drawLine(w.getX(0)+zi*w.getWidth2()/numColumns ,w.getY(0) ,
            w.getX(0)+zi*w.getWidth2()/numColumns,w.getY(w.getHeight2())  );
        }
        gOld.drawImage(image, 0, 0, null);
    }
////////////////////////////////////////////////////////////////////////////
// needed for     implement runnable
    public void run() {
        while (true) {
            animate();
            repaint();
            double seconds = 0.05;    //time that 1 frame takes.
            int miliseconds = (int) (1000.0 * seconds);
            try {
                Thread.sleep(miliseconds);
            } catch (InterruptedException e) {
            }
        }
    }
/////////////////////////////////////////////////////////////////////////
    public void reset() {  
        Tile.setBoard(numColumns, numRows);
    }
/////////////////////////////////////////////////////////////////////////
    public void animate() {

        if (animateFirstTime) {
            animateFirstTime = false;
            if (w.xsize != getSize().width || w.ysize != getSize().height) {
                w.xsize = getSize().width;
                w.ysize = getSize().height;
            }
            reset();
        }
        if(!paused) {
            Tile.nextMove();
            Tile.checkNextTo();
        }
        columnWidth = w.getWidth2()/numColumns;
        rowHeight = w.getHeight2()/numRows;
    }

////////////////////////////////////////////////////////////////////////////
    public void start() {
        if (relaxer == null) {
            relaxer = new Thread(this);
            relaxer.start();
        }
    }
////////////////////////////////////////////////////////////////////////////
    public void stop() {
        if (relaxer.isAlive()) {
            relaxer.stop();
        }
        relaxer = null;
    }  
}

/////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////

class Window
        {
    
    static final int TOP_BORDER = 2;
    static final int SIDE_BORDER = 8;
    static final int BOTTOM_BORDER = 8;
    static final int YTITLE = 22;
    
    static final int WINDOW_WIDTH = 512 + (SIDE_BORDER*2);
    static final int WINDOW_HEIGHT = 512 + TOP_BORDER + YTITLE + BOTTOM_BORDER;
    
    static int xsize = -1;
    static int ysize = -1;
    
    public int getX(int x) {
        return (x+SIDE_BORDER);
    }
    public int getY(int y) {
        return (y + TOP_BORDER + YTITLE);
    }
    
    public int getYNormal(int y) {
        return (-y + TOP_BORDER + YTITLE + getHeight2());
    }
        
    public int getWidth2() {
        return (xsize - SIDE_BORDER*2);
    }
    public int getHeight2() {
        return (ysize - TOP_BORDER - YTITLE - BOTTOM_BORDER);
    }
}