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
		System.out.println("Started game");
		// First read in x-Player's move, validate, add to the board, then send back the
		// line to both client objects to display on both boards
		while(true){
			if(!checkWinner()){
				addMarkX();
				if(checkWinner()){
					break;
				}
				addMarkO();
				if(checkWinner()){
					break;
				}
			}
		}
		System.out.println("Game over!");
	}

	/**
	 * Method waits until X player makes a move and adds the X player mark on the Board
	 */
	public void addMarkX(){
		int row,col;
		/*
		Wait until X player makes a move
		 */
		xPlayerMove();
		/*
		Capture the X player move and add a mark on the Board.
		 */
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

	/**
	 * Method waits O player makes a move and adds the O player mark on the Board
	 */
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


	/**
	 * Blocking function for O Player until X Player makes a move
	 * @return
	 */
	public Boolean xPlayerMove(){
		/*
		Block O until X makes a move
		 */
		//Tell X to make a move
		//3 indicates the client to make a move
		xSocketOut.println("3," + "Make your move..");
		xSocketOut.flush();

		//Clear O sockets previous output
		//6 indicates the client that it is the other player's turn
		oSocketOut.println("6," + "Waiting for other player to play");
		oSocketOut.flush();
		while(xLine == null){
			try{
				xLine = xSocketIn.readLine();
				System.out.println("Xline" + xLine);
			}catch (IOException e){
				e.printStackTrace();
			}
		}return true;
	}

	/**
	 * Blocking function for X Player until O Player makes a move
	 * @return
	 */
	public Boolean oPlayerMove(){
		/*
		Block X until O makes a move
		 */
		//Tell O to make a move
		oSocketOut.println("3," + "Make your move..");
		oSocketOut.flush();

		//Clear X sockets previous output
		xSocketOut.println("6," + "Waiting for other player to play");
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

			//Add code here for accepting User name from xSocketIn and oSocketIn.
			//Create Player objects once the player names have been accepted.
			//We could create blocking logic here as well to accept Player Names.
			//Modify Player class's constructor to include player name.

			//Call acceptPlayerName here twice. Once for xPlayer and once for oPlayer
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

	/**
	 * function accepts the player name on each socket.
	 * Intended use is to accept player names on xSocket and oSocket
	 * @param socketIn
	 * @return
	 */
	public String acceptPlayerName(BufferedReader socketIn,PrintWriter socketOut) {
		//Tell client to send name
		socketOut.println("7");
		socketOut.flush();
		String name = "";

		//Block until we receive a name from the client.
		while (name == null) {
			try {
				name = socketIn.readLine();
				System.out.println("Player name is: " + name);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}return name;
	}

	/**
	 * Method to check Winner
	 * @return
	 */
	public boolean checkWinner() {
		Boolean gameEnd = false;
		if (theBoard.xWins()) {
			gameEnd = true;
			System.out.println("Game Over!, X is the winner!");
			//Send winner messages to both X and O sockets
			xSocketOut.println("5,"+"Game Over! X is the winner!");
			oSocketOut.println("5,"+"Game Over! X is the winner!");
			return gameEnd;
		} else if (theBoard.oWins()) {
			gameEnd = true;
			System.out.println("Game Over! O is the winner!");
			xSocketOut.println("5,"+"Game Over! O is the winner!");
			oSocketOut.println("5,"+"Game Over! O is the winner!");
			return gameEnd;
		} else if (theBoard.isFull()) {
			gameEnd = true;
			System.out.println("Game ends in Tie");
			xSocketOut.println("5,"+"Game ends in Tie");
			oSocketOut.println("5,"+"Game ends in Tie");
			return gameEnd;
		}
		return gameEnd;
	}
}
