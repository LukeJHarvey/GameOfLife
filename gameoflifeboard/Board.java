
package gameoflifeboard;
public class Board {
	private class Tile {
		int phase = -1;				//-1 = dead, 1 = alive, 2 = special
		//int nextTo = 0;
		Tile(int phase) {
			this.phase = phase;
		}
	}
	Tile board[][];

	Board(int row, int col) {
		board = new Tile[row][col];
	}
	//Changes Tile based on phase
	public void changeTile(int row, int col, int phase) {
		Tile piece = board[row][col];
		piece.phase = phase;
	}
	//Check how many Tiles next to
	public int nextTo(int row, int col) {
		Tile piece = board[row][col];
		int count = 0;

		//Create array for checking around the piece
		//([0][i],[1][i]) points for array
		int inc[][] = 	{-1, -1, 0,  1,  1, 1, 0, -1},
						{0,  -1, -1, -1, 0, 1, 1, 1}
		//Checks if the piece is against the sides of the board
		boolean sideCheck[] = {row == 0, col == 0, row = board.length-1, col = board[row].length-1};
		//takes away conflicting points off of the Increment Array
		for(int i = 0; i < 3; i++) {
			if(sideCheck[i]) {
				if(inc[0].length !=0) {
					int newInc[][] = [2][inc.length-3];
					for(int i = 0; i<inc.length-3; i++) {
						newInc[0][i] = inc[0][2+i];
						newInc[1][i] = inc[1][2+i];
					}
					inc = newInc;
				}
			}
		}

		//checks around piece using inc[][] to move around piece
		for(i = 0; i <inc[0].length; i++) {
			count = (board[ inc[0][i] ][ inc[1][i] ]).phase !=-1 ? count+1 : count;
		}
		
		return count;
	}
	//transitions to next turn;
}