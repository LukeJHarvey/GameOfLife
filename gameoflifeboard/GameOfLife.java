/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gameoflifeboard;

import java.io.*;
import java.util.Scanner;
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
    static int numRows;
    static int numColumns;
    public static int columnWidth;
    public static int rowHeight;
    boolean paused = true;
    boolean nextOn = false;
    int lastChanged[] = new int[2];
    Board board;

    public static void main(String[] args) throws IOException {
        if (args.length == 3) {
            String filename = args[3];
            Scanner in = new Scanner(new File(filename));

        } else {
            numRows = Integer.parseInt(args[0]);
            numColumns = Integer.parseInt(args[1]);
        }

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

                    int row = ypos / rowHeight;
                    int column = xpos / columnWidth;
                    if (row < numRows && row >= 0 && column < numColumns && column >= 0) {
                        System.out.println(row + ", " + column);
                        board.changeTile(row, column, board.getPhase(row, column) == -1 ? 1 : -1);
                    }
                }
                if (e.BUTTON3 == e.getButton()) {
                    reset();
                }
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (e.BUTTON1 == e.getButton()) {
                    int xpos = e.getX() - w.getX(0);
                    int ypos = e.getY() - w.getY(0);

                    int row = ypos / rowHeight;
                    int column = xpos / columnWidth;
                    if (row < numRows && row >= 0 && column < numColumns && column >= 0) {
                        if (lastChanged[0] != row || lastChanged[1] != column) {
                            lastChanged = new int[] {row, column};
                            System.out.println(row + ", " + column);
                            board.changeTile(row, column, board.getPhase(row, column) == -1 ? 1 : -1);
                        }
                    }
                    repaint();
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
                if (e.VK_N == e.getKeyCode()) {
                    nextOn = !nextOn;
                }
                if (e.VK_RIGHT == e.getKeyCode()) {
                    board.nextMove();
                }
                if (e.VK_SPACE == e.getKeyCode()) {
                    paused = !paused;
                }
                if (e.VK_R == e.getKeyCode()) {
                    reset();
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


        for (int zrow = 0; zrow < numRows; zrow++) {
            for (int zcolumn = 0; zcolumn < numColumns; zcolumn++) {
                if (board.isAlive(zrow, zcolumn)) {
                    g.setColor(Color.BLACK);
                    g.fillRect(w.getX(0) + zcolumn * columnWidth,
                               w.getY(0) + zrow * rowHeight,
                               columnWidth,
                               rowHeight);
                } else  {
                    if (paused)
                        g.setColor(Color.RED);
                    else
                        g.setColor(Color.WHITE);

                    g.fillRect(w.getX(0) + zcolumn * columnWidth,
                               w.getY(0) + zrow * rowHeight,
                               columnWidth,
                               rowHeight);
                }
                if (nextOn) {
                    g.setColor(Color.MAGENTA);
                    g.drawString("" + board.getNextTo(zrow, zcolumn),
                                 w.getX(0) + zcolumn * columnWidth + columnWidth / 2 - 4,
                                 w.getY(0) + zrow * rowHeight + rowHeight / 2 + 4);
                }
            }
        }
        g.setColor(Color.blue);
        //horizontal lines
        for (int zi = 1; zi < numRows; zi++) {
            g.drawLine(w.getX(0) , w.getY(0) + zi * w.getHeight2() / numRows ,
                       w.getX(w.getWidth2()) , w.getY(0) + zi * w.getHeight2() / numRows );
        }
        //vertical lines
        for (int zi = 1; zi < numColumns; zi++) {
            g.drawLine(w.getX(0) + zi * w.getWidth2() / numColumns , w.getY(0) ,
                       w.getX(0) + zi * w.getWidth2() / numColumns, w.getY(w.getHeight2())  );
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
        board.resetBoard();
    }
/////////////////////////////////////////////////////////////////////////
    public void animate() {
        if (animateFirstTime) {
            animateFirstTime = false;
            if (w.xsize != getSize().width || w.ysize != getSize().height) {
                w.xsize = getSize().width;
                w.ysize = getSize().height;
            }
            board = new Board(numColumns, numRows);
        }
        if (!paused) {
            board.nextMove();
        }
        columnWidth = w.getWidth2() / numColumns;
        rowHeight = w.getHeight2() / numRows;
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

class Window {

    static final int TOP_BORDER = 2;
    static final int SIDE_BORDER = 8;
    static final int BOTTOM_BORDER = 8;
    static final int YTITLE = 22;

    static final int WINDOW_WIDTH = 512 + (SIDE_BORDER * 2);
    static final int WINDOW_HEIGHT = 512 + TOP_BORDER + YTITLE + BOTTOM_BORDER;

    static int xsize = -1;
    static int ysize = -1;

    public int getX(int x) {
        return (x + SIDE_BORDER);
    }
    public int getY(int y) {
        return (y + TOP_BORDER + YTITLE);
    }

    public int getYNormal(int y) {
        return (-y + TOP_BORDER + YTITLE + getHeight2());
    }

    public int getWidth2() {
        return (xsize - SIDE_BORDER * 2);
    }
    public int getHeight2() {
        return (ysize - TOP_BORDER - YTITLE - BOTTOM_BORDER);
    }
}