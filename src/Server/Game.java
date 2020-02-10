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


	/**
	 * Constructor
	 * @param xSocket
	 * @param oSocket
	 * @param theBoard
	 */
	public Game(Socket xSocket, Socket oSocket, Board theBoard) {
		this.xSocket = xSocket;
		this.oSocket = oSocket;
		this.theBoard = theBoard;
		createPlayers();
	}

	/**
	 * Runs the game
	 */
	@Override
	public void run() {
		System.out.println("Started game");

		/*
		X player moves first, followed by O player. Winner is checked on each move and before every move
		 */
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
		String[] temp = xPlayerMove().split(",");

		row = Integer.parseInt(temp[0]);
		col = Integer.parseInt(temp[1]);

		//Client expects response in this format - choice,row,col,mark(LETTERX or LETTERO)
		//Constructing a response from server
		String serverResponse = "";
		serverResponse = 4 + "," + row + "," + col + "," + LETTER_X;
		if(theBoard.checkMark(row,col)){
			theBoard.addMark(row, col, xPlayer.getMark());
			theBoard.display();
			System.out.println("Sending X");
			xSocketOut.println(serverResponse);
			oSocketOut.println(serverResponse);
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
		/*
		Wait until O player makes a move
		 */
		String[] temp = oPlayerMove().split(",");
		row = Integer.parseInt(temp[0]);
		col = Integer.parseInt(temp[1]);

		//Client expects response in this format - choice,row,col,mark(LETTERX or LETTERO)
		//Constructing a response from server
		String serverResponse = "";
		serverResponse = 4 + "," + row + "," + col + "," + LETTER_O;

		if(theBoard.checkMark(row,col)){
			theBoard.addMark(row, col, oPlayer.getMark());
			theBoard.display();
			System.out.println("Sending O");
			xSocketOut.println(serverResponse);
			oSocketOut.println(serverResponse);
		}else{
			System.out.println("Invalid move!");
			addMarkO();
		}
	}


	/**
	 * Blocking function for O Player until X Player makes a move
	 * @return
	 */
	public String xPlayerMove(){
		/*
		Block O until X makes a move
		 */
		//Tell X to make a move
		//3 indicates the client to make a move
		xSocketOut.println("3," + "Make your move..");
		xSocketOut.flush();

		String xInput = "";

		//6 indicates the client that it is the other player's turn
		oSocketOut.println("6," + "Waiting for " + xPlayer.getName() + " to play..");
		oSocketOut.flush();
		while(xInput.isEmpty()){
			try{
				xInput = xSocketIn.readLine();
				System.out.println("xInput:" + xInput);
			}catch (IOException e){
				e.printStackTrace();
			}
		}return xInput;
	}

	/**
	 * Blocking function for X Player until O Player makes a move
	 * @return
	 */
	public String oPlayerMove(){
		/*
		Block X until O makes a move
		 */
		//Tell O to make a move
		oSocketOut.println("3," + "Make your move..");
		oSocketOut.flush();

		String oInput = "";
		//Clear X sockets previous output
		xSocketOut.println("6," + "Waiting for " + oPlayer.getName() + " to play..");
		xSocketOut.flush();
		while(oInput.isEmpty()){
			try{
				oInput = oSocketIn.readLine();
				System.out.println("oInput:" + oInput);
			}catch (IOException e){
				e.printStackTrace();
			}
		}return oInput;
	}

	/**
	 * Creates the x-Player and o-Player, and initializes their respective sockets
	 * with the input and output streams
	 */
	public void createPlayers() {
		try {
			// X-player
			xSocketIn = new BufferedReader(new InputStreamReader(xSocket.getInputStream()));
			xSocketOut = new PrintWriter(xSocket.getOutputStream(), true);

			//Create X player object. This accepts the Xplayer name as well
			xPlayer = new Player(LETTER_X,acceptPlayerName(xSocketIn,xSocketOut));

			// O-player
			oSocketIn = new BufferedReader(new InputStreamReader(oSocket.getInputStream()));
			oSocketOut = new PrintWriter(oSocket.getOutputStream(), true);

			//Create O player object. This accepts the Oplayer name as well
			oPlayer = new Player(LETTER_O,acceptPlayerName(oSocketIn,oSocketOut));

			//Set the Player symbols
			xSocketOut.println("1,"+LETTER_X);
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
		System.out.println("Asking client to send name..");
		socketOut.println("7");
		socketOut.flush();
		String name = "";

		//Block until we receive a name from the client.
		while (name.isEmpty()) {
			try {
				name = socketIn.readLine();
				System.out.println("Player name is: " + name);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return name;
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
			xSocketOut.println("5,"+"Game Over! " + xPlayer.getName() + " is the winner!");
			oSocketOut.println("5,"+"Game Over! " + xPlayer.getName() + " is the winner!");
			return gameEnd;
		} else if (theBoard.oWins()) {
			gameEnd = true;
			System.out.println("Game Over! O is the winner!");
			xSocketOut.println("5,"+"Game Over! " + oPlayer.getName() + " is the winner!");
			oSocketOut.println("5,"+"Game Over! " + oPlayer.getName() + " is the winner!");
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
