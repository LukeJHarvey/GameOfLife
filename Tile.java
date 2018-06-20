
package gameoflife;
public class Tile {
    public static Tile board[][];
    static int phase = 0;
    boolean alive;
    int row;
    int column;
    int nextTo = 0;

    public Tile(int x, int y, boolean alive) {
        this.row = x;
        this.column = y;
        this.alive = alive;
        board[x][y] = this;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
    
    public void resetPhase() {
        phase = 0;
    }
    
    public int getPhase() {
        return phase;
    }
    
    public int incrementPhase() {
        return ++phase;
    }

    public static void setBoard(int row, int column) {
        board = new Tile[column][row];
        for(int x = 0; x<board.length;x++)
        {
            for(int y = 0; y<board[0].length;y++)
            {
                board[x][y] = new Tile(x,y,false);
            }
        }
    }

    public void setRow(int x) {
        this.row = x;
    }

    public void setColumn(int y) {
        this.column = y;
    }
    
    public boolean getAlive() {
        return this.alive;
    }
    public void changeAlive() {
        alive = !alive;
    }
    
    public static void nextMove() {
        Tile newBoard[][] = board;
        checkNextTo();
        for(int row = 0; row<board.length;row++)
        {
            for(int column = 0; column<board[0].length;column++)
            {
                Tile current = board[row][column];
                Tile newT = newBoard[row][column];
                if(current.alive && (current.nextTo==2 || current.nextTo == 3)){
                    newT.alive = true;
                }
                else if(!current.alive && current.nextTo == 3){
                    newT.alive = true;
                }
                else {
                    newT.alive = false;
                }
            }
        }
        board = newBoard;
        return;
    }
    public static void checkNextTo() {
        for(int x = 0; x<board.length;x++)
        {
            for(int y = 0; y<board[0].length;y++)
            {
                nextTo(board[x][y]);
            }
        }
    }
    private static int nextTo(Tile x) {
        int count = 0;
        if(x.row == 0)
        {
            if(x.column == 0) {
                count = (board[x.row+1][x.column].alive) ? count+1 : count;
                count = (board[x.row+1][x.column+1].alive) ? count+1 : count;
                count = (board[x.row][x.column+1].alive) ? count+1 : count;
            }
            else if(x.column == board[x.row].length-1) {
                count = (board[x.row+1][x.column].alive) ? count+1 : count;
                count = (board[x.row+1][x.column-1].alive) ? count+1 : count;
                count = (board[x.row][x.column-1].alive) ? count+1 : count;
            }
            else {
                count = (board[x.row+1][x.column].alive) ? count+1 : count;
                count = (board[x.row+1][x.column-1].alive) ? count+1 : count;
                count = (board[x.row][x.column-1].alive) ? count+1 : count;
                count = (board[x.row+1][x.column+1].alive) ? count+1 : count;
                count = (board[x.row][x.column+1].alive) ? count+1 : count;
            }
        }
        else if(x.row == board.length-1) {
            if(x.column == 0) {
                count = (board[x.row-1][x.column].alive) ? count+1 : count;
                count = (board[x.row-1][x.column+1].alive) ? count+1 : count;
                count = (board[x.row][x.column+1].alive) ? count+1 : count;
            }
            else if(x.column == board[x.row].length-1) {
                count = (board[x.row-1][x.column].alive) ? count+1 : count;
                count = (board[x.row-1][x.column-1].alive) ? count+1 : count;
                count = (board[x.row][x.column-1].alive) ? count+1 : count;
            }
            else {
                count = (board[x.row-1][x.column].alive) ? count+1 : count;
                count = (board[x.row-1][x.column-1].alive) ? count+1 : count;
                count = (board[x.row][x.column-1].alive) ? count+1 : count;
                count = (board[x.row-1][x.column+1].alive) ? count+1 : count;
                count = (board[x.row][x.column+1].alive) ? count+1 : count;
            }
        }
        else {
            if(x.column == 0) {
                count = (board[x.row-1][x.column].alive) ? count+1 : count;
                count = (board[x.row-1][x.column+1].alive) ? count+1 : count;
                count = (board[x.row+1][x.column].alive) ? count+1 : count;
                count = (board[x.row+1][x.column+1].alive) ? count+1 : count;
                count = (board[x.row][x.column+1].alive) ? count+1 : count;
            }
            else if(x.column == board[x.row].length-1) {
                count = (board[x.row-1][x.column].alive) ? count+1 : count;
                count = (board[x.row-1][x.column-1].alive) ? count+1 : count;
                count = (board[x.row+1][x.column].alive) ? count+1 : count;
                count = (board[x.row+1][x.column-1].alive) ? count+1 : count;
                count = (board[x.row][x.column-1].alive) ? count+1 : count;
            }
            else {
                count = (board[x.row-1][x.column].alive) ? count+1 : count;
                count = (board[x.row-1][x.column-1].alive) ? count+1 : count;
                count = (board[x.row][x.column-1].alive) ? count+1 : count;
                count = (board[x.row-1][x.column+1].alive) ? count+1 : count;
                count = (board[x.row+1][x.column].alive) ? count+1 : count;
                count = (board[x.row+1][x.column-1].alive) ? count+1 : count;
                count = (board[x.row+1][x.column+1].alive) ? count+1 : count;
                count = (board[x.row][x.column+1].alive) ? count+1 : count;
            }
        }
        x.nextTo = count;
        return count;
    } 
}
