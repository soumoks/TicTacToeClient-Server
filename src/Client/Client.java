package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;

public class Client implements Constants {

	private Socket aSocket;
	private PrintWriter socketOut;
	private BufferedReader socketIn;
	private GameView theView;
	private int choice;


	public Client(String serverName, int portNumber, GameView theView) {
		this.theView = theView;
		try {
			aSocket = new Socket(serverName, portNumber);
			// Socket input Stream
			socketIn = new BufferedReader(new InputStreamReader(aSocket.getInputStream()));

			// Socket output Stream
			socketOut = new PrintWriter(aSocket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendPlayerName(){
		String name = "";
		name = theView.getName();
		if(name != null){
			System.out.println("Sending player name " + name + " to server.." );
			socketOut.println(name);
			socketOut.flush();
		}
	}

	public void sendButtonPress(int row, int col) {
		// Convert row and columns into a single string
		String line = "";
		line = row + "," + col + "," + theView.getPlayerSymbol().charAt(0);

		/*
		 * Check if this line is required or can be replaced with something beter
		 */
		if (line != null && !line.isEmpty()) {
			System.out.println("Sending row,col values to server..");
			socketOut.println(line);
			socketOut.flush();
		}
	}


	public void setMark(String response) {
		// We are assuming the response here consists of the row and column of the mark.
		// Parse the response to separate the row and column
		// Place the mark at the correct position on the board.
		int row, col = 0;
		String mark;
//		String response = null;

//		try {
//			response = socketIn.readLine();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		// Checking to see if the server response is anything other than the string
		// containing row,col and mark
		if (response != null) {
			String [] temp = response.split(",");

			// Add an array size check here
			if (temp.length == 4) {
				row = Integer.parseInt(temp[1]);
				col = Integer.parseInt(temp[2]);
				mark = temp[3];
				JButton button[][] = theView.getButton();
				//System.out.println("response row, col, mark: " + row + col + mark);

				if ((row >= 0 && row <= 2) && (col >= 0 && col <= 2)) {

					// Changing mark to lower case letters so that they are visible on the GUI
					if (mark.charAt(0) == LETTER_X) {
						button[row][col].setText("x");
						System.out.println("Mark is equal to X");
					} else {
//						System.out.println("Mark is not equal to X");
					}
					if (mark.charAt(0) == LETTER_O) {
						button[row][col].setText("o");
						System.out.println("Mark is equal to O");
					} else {
//						System.out.println("Mark is not equal to O");
					}
				} else {
					System.out.println("Row and column not in limits!");
				}
			}

		}
	}

	public void getServerResponse() throws IOException {
		while (true) {
			String response = "";
			try {
				response = socketIn.readLine();
				System.out.println("Server response is: " + response);
				String [] temp = response.split(",");
				choice = Integer.parseInt(temp[0]);
			} catch (IOException e) {
				e.printStackTrace();
			}
			//Send response to switch board only if non-null
			if(response != null){
				switchBoard(choice, response);
			}
		}
	}


	public void setPlayer(String response) {
		// Checking if the string actually consists of the player Mark
		// Character c = theView.getPlayerSymbol();
		//System.out.println("Character of JTextField" + theView.getPlayerSymbol());
		String [] temp = response.split(",");
			if (temp[1].charAt(0) == LETTER_X || temp[1].charAt(0) == LETTER_O) {
				if (temp[1].charAt(0) == LETTER_X) {
					theView.setPlayerSymbol(LETTER_X);
				} else if (temp[1].charAt(0) == LETTER_O) {
					theView.setPlayerSymbol(LETTER_O);
				}
			}
	}

	public void switchBoard(int choice, String response) throws IOException {
		switch (choice){
			case 1:
				//Set players
				setPlayer(response);
				break;
			case 2:
				//Set player names
				break;
			case 3:
				//System.out.println("Entered case 3");
				//Waiting for server to tell me to make a move
				String [] message2 = response.split(",");
				theView.setMessageArea(message2[1]);


				ClientController temp = new ClientController(theView,this);
				//System.out.println("Row column values before entering while loop on Controller" + temp.getRowColData()[0]);
				while((temp.getRowColData()[0] == -1)){
					System.out.println("Entered case 3 while loop");
				}
				sendButtonPress(temp.getRowColData()[0],temp.getRowColData()[1]);
				//Reset the button as the stream is a buffered reader and we have to reset all the previous content.
				//Make Move
				//Displays whether you are allowed to make a move.
				//and also registers a button press and sends over the button press to the Server
				break;
			case 4:
				setMark(response);
				//Clear the message area after mark has been set. This clears any previous message on the screen.
				theView.setMessageArea("");
				break;
			case 5:
				//Winner display.
				String [] message = response.split(",");
				theView.setMessageArea(message[1]);
				break;
			case 6:
				//Waiting messages from players
				String [] message3 = response.split(",");
				theView.setMessageArea(message3[1]);
			case 7:
				//Case for sending player name to server.
				//Ideally, we would be able to send player name without a case but it would be better if the server asks for the name and we send it after that
				sendPlayerName();
			default:
				System.out.println("Invalid choice!");
		}
	}
}
