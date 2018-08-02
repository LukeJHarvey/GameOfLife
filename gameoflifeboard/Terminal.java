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
		            if(nextLine[0].equals("Import")) {
		                GameOfLife.frame1.board = new Board(GameOfLife.importBoard("Templates/" + nextLine[1]));
		                GameOfLife.setNewFrame();
		            }
		        }
		    }
		    catch(Exception e) {
		    	System.out.println("Exception: " + e);
		    }
		}
	}
}