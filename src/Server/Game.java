package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Game implements Runnable, Constants {
	// X sockets
	private Socket xSocket;
	private PrintWriter xSocketOut;
	private BufferedReader xSocketIn;

	// O sockets
	private Socket oSocket;
	private PrintWriter oSocketOut;
	private BufferedReader oSocketIn;

	private Board theBoard;
	private Player xPlayer;
	private Player oPlayer;

	String xLine;
	String oLine;

	public Game(Socket xSocket, Socket oSocket, Board theBoard) {
		this.xSocket = xSocket;
		this.oSocket = oSocket;
		this.theBoard = theBoard;
		createPlayers();
	}

	@Override
	public void run() {
		// createPlayers();
		System.out.println("Started game");

		//String xLine = null;
		//String oLine = null;
		//String response = null;
		char checkPlayerMark;
		int row;
		int col;

		// First read in x-Player's move, validate, add to the board, then send back the
		// line to both client objects to display on both boards







		while(true){
			if(!checkWinner()){
////				xPlayerMove();
//				String[] temp = xLine.split(",");
//				row = Integer.parseInt(temp[0]);
//				col = Integer.parseInt(temp[1]);
//
//				if(theBoard.checkMark(row,col)){
//					addMarkX(row,col);
//					if(checkWinner()){
//						break;
//					}
//				}
//				else{
//					System.out.println("Invalid move!");
//					addMarkX(row,col);
//					if(checkWinner()){
//						break;
//					}
//				}
//
//				String[] temp2 = oLine.split(",");
//				row = Integer.parseInt(temp2[0]);
//				col = Integer.parseInt(temp2[1]);
//
//				if(theBoard.checkMark(row,col)){
//					addMarkO(row,col);
//					if(checkWinner()){
//						break;
//					}
//				}else{
//					System.out.println("Invalid move!");
//					addMarkO(row,col);
//					if(checkWinner()){
//						break;
//					}
//				}


				addMarkX();
				if(checkWinner()){
					break;
				}
				addMarkO();
				if(checkWinner()){
					break;
				}

//
//				oPlayerMove();
//				String[] temp2 = oLine.split(",");
//				row = Integer.parseInt(temp2[0]);
//				col = Integer.parseInt(temp2[1]);
//				if(theBoard.checkMark(row,col)){
//					theBoard.addMark(row, col, oPlayer.getMark());
//					theBoard.display();
//					System.out.println("Sending O");
//					xSocketOut.println("4,"+oLine);
//					oSocketOut.println("4,"+oLine);
//					oLine = null;
//					if(checkWinner()){
//						break;
//					}
//				}else{
//					System.out.println("Invalid move!");
//					continue;
//				}

			}
		}

		System.out.println("Game over!");
		}

	public void addMarkX(){
		int row,col;
		xPlayerMove();
		String[] temp = xLine.split(",");
		row = Integer.parseInt(temp[0]);
		col = Integer.parseInt(temp[1]);
		if(theBoard.checkMark(row,col)){
			theBoard.addMark(row, col, xPlayer.getMark());
			theBoard.display();
			System.out.println("Sending X");
			xSocketOut.println("4,"+xLine);
			oSocketOut.println("4,"+xLine);
			//Reset the socket to null
			xLine = null;
		}else{
			System.out.println("Invalid move!");
			addMarkX();
		}


	}

	public void addMarkO(){
		int row,col;
		oPlayerMove();
		String[] temp2 = oLine.split(",");
		row = Integer.parseInt(temp2[0]);
		col = Integer.parseInt(temp2[1]);
		if(theBoard.checkMark(row,col)){
			theBoard.addMark(row, col, oPlayer.getMark());
			theBoard.display();
			System.out.println("Sending O");
			xSocketOut.println("4,"+oLine);
			oSocketOut.println("4,"+oLine);
			oLine = null;
		}else{
			System.out.println("Invalid move!");
			addMarkO();
		}
	}


	public Boolean xPlayerMove(){
		/*
		Block O until X makes a move
		 */
		//Tell X to make a move
		xSocketOut.println("3");
		xSocketOut.flush();
		while(xLine == null){
			try{

				xLine = xSocketIn.readLine();
				System.out.println("Xline" + xLine);
			}catch (IOException e){
				e.printStackTrace();
			}
		}return true;
	}

	public Boolean oPlayerMove(){
		/*
		Block X until O makes a move
		 */
		//Tell O to make a move
		oSocketOut.println("3");
		xSocketOut.flush();
		while(oLine == null){
			try{

				oLine = oSocketIn.readLine();
				System.out.println("oLine" + oLine);
			}catch (IOException e){
				e.printStackTrace();
			}
		}return true;
	}

	/**
	 * Creates the x-Player and o-Player, and initializes their respective sockets
	 * with the input and output streams
	 */
	public void createPlayers() {
		// X-player
		try {
			xSocketIn = new BufferedReader(new InputStreamReader(xSocket.getInputStream()));
			xSocketOut = new PrintWriter(xSocket.getOutputStream(), true);
			xPlayer = new Player(xSocketIn, xSocketOut, LETTER_X);
//			xSocketOut.println( "Setting player" + LETTER_X);
			xSocketOut.println("1,"+LETTER_X);

			oSocketIn = new BufferedReader(new InputStreamReader(oSocket.getInputStream()));
			oSocketOut = new PrintWriter(oSocket.getOutputStream(), true);
			oPlayer = new Player(oSocketIn, oSocketOut, LETTER_O);
//			oSocketOut.println("Setting player" + LETTER_O);
			oSocketOut.println("1,"+LETTER_O);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public boolean validateMove(int row, int col) {
		// Fix this method
		return true;
	}

	public void sendError() {
		// Fix this method
	}

	public boolean checkWinner() {
		Boolean gameEnd = false;
		if (theBoard.xWins()) {
			gameEnd = true;
			// theView.setMessageArea("Game Over!, " + theView.getNameX() + " is the
			// winner!");
			System.out.println("Game Over!, X is the winner!");
			return gameEnd;
		} else if (theBoard.oWins()) {
			gameEnd = true;
			// theView.setMessageArea("Game Over!, " + theView.getNameO() + " is the
			// winner!");
			System.out.println("Game Over!, O is the winner!");
			return gameEnd;
		} else if (theBoard.isFull()) {
			gameEnd = true;
			// theView.setMessageArea("Game ends in Tie");
			System.out.println("Game ends in Tie");
			return gameEnd;
		}
		return gameEnd;
	}

}
