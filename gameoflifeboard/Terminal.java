package gameoflifeboard;
import java.io.*;
import java.util.Scanner;
public class Terminal extends Thread {
	static Scanner terminalScan;
	public void run() {
		terminalScan = new Scanner(System.in);
		while(true) {
			try {
				while(terminalScan.hasNextLine()) {
		            String[] nextLine = terminalScan.nextLine().split(" ");
		            if(nextLine[0].equals("import")) {
		                GameOfLife.board = new Board(GameOfLife.importBoard("Templates/" + nextLine[1]));
		                GameOfLife.setNewFrame();
		            }
		            else if(nextLine[0].equals("export")) {
		            	GameOfLife.exportBoard("Templates/" + nextLine[1]);
		            }
		            else if(nextLine[0].equals("change")) {
		            	GameOfLife.board = nextLine.length == 2 ? new Board(Integer.parseInt(nextLine[1]), Integer.parseInt(nextLine[1])) : new Board(Integer.parseInt(nextLine[1]), Integer.parseInt(nextLine[2]));
		            	GameOfLife.numRows = GameOfLife.board.getRows();
		            	GameOfLife.numColumns = GameOfLife.board.getColumns();
		            	GameOfLife.setNewFrame();
		            }
		            else if(nextLine[0].equals("exit")) {
		            	GameOfLife.frame1.exit();
		            }
		            else {
		            	System.out.println("Import filename \nExport filename \nChange size [column size]");
		            }
		        }
		    }
		    catch(Exception e) {
		    	System.out.println("Exception: " + e);
		    }
		}
	}
}