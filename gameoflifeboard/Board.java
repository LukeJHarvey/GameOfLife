
package gameoflifeboard;
public class Board {
	private class Tile {
		int phase = -1;				//-1 = dead, 1 = alive, 2 = special
		int nextTo = 0;				//Have to keep track of nextTo for nextMove() to work correctly
		Tile(int phase) {
			this.phase = phase;
		}
	}
	private Tile board[][];

	Board() {
		board = new Tile[16][16];
	}
	Board(int row, int col) {
		board = new Tile[row][col];
	}
	Board(int[][] pBoard) {
		board = new Tile[pBoard.length][pBoard[0].length];
		for(int i = 0; i < pBoard.length; i++) {
			for(int j = 0; j < pBoard.length; j++) {
				board[i][j] = new Tile(pBoard[i][j]);
			}
		}
		checkNextTo();
	}

	//Changes Tile based on phase
	public void changeTile(int row, int col, int phase) {
		Tile piece = board[row][col];
		piece.phase = phase;
		updateNextToAroundTile(row, col);
	}
	public boolean isAlive(int row, int col) {
		return board[row][col].phase != -1;
	}
	public int getPhase(int row, int col) {
		return board[row][col].phase;
	}
	public int getNextTo(int row, int col) {
		board[row][col].nextTo = nextTo(row, col);
		return board[row][col].nextTo;
	}
	public void resetBoard(int row, int col) {
		board = new Tile[row][col];
	}

	//gets Increment for checking around a piece
	private int[][] getIncrement(int row, int col) {
		//Create array for checking around the piece
		//([0][i],[1][i]) points for array
		int inc[][] = new int[][] {
						{-1, -1, 0,  1,  1, 1, 0, -1},
						{0,  -1, -1, -1, 0, 1, 1, 1}
					};
		//Checks if the piece is against the sides of the board
		boolean sideCheck[] = {row == 0, col == 0, row == board.length-1, col == board[row].length-1};
		//takes away conflicting points off of the Increment Array
		for(int i = 0; i < 3; i++) {
			if(sideCheck[i]) {
				if(inc[0].length !=0) {
					int newInc[][] = new int[2][inc.length-3];
					for(int j = 0; j<inc.length-3; j++) {
						newInc[0][i] = inc[0][2+j];
						newInc[1][i] = inc[1][2+j];
					}
					inc = newInc;
				}
			}
		}
		return inc;
	}
	//updates nextTo around piece
	private void updateNextToAroundTile(int row, int col) {
		int[][] inc = getIncrement(row, col);
		for(int i = 0; i < inc[0].length; i++) {
			nextTo(row + inc[0][i], col + inc[1][i]);
		}
	}

	//Check how many Tiles next to
	private int nextTo(int row, int col) {
		int count = 0;

		int inc[][] = getIncrement(row, col);

		//checks around piece using inc[][] to move around piece
		for(int i = 0; i < inc[0].length; i++) {
			count = (board[ row + inc[0][i] ][ col + inc[1][i] ]).phase !=-1 ? count+1 : count;
		}

		board[row][col].nextTo = count;
		return count;
	}
	//Changes nextTo status of each piece on the board, for nextMove()
	public void checkNextTo() {
        for(int i = 0; i < board.length; i++)
        {
            for(int j = 0; j < board[0].length; j++)
            {
                nextTo(i, j);
            }
        }
    }
	//transitions to next turn;
	public void nextMove() {
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				int nextTo = board[i][j].nextTo;
				//if next to less than 2 or more than 3, alive pieces die.
				if(board[i][j].phase != -1 && (nextTo >=4 || nextTo <= 1)){
                	board[i][j].phase = -1;
                }
                //if next to exactly 3 alive squares, a dead one becomes alive
                else if(board[i][j].phase == -1 && nextTo == 3){
                    board[i][j].phase = 1;
                }
			}
		}
		checkNextTo();
	}

}