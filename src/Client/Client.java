package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JButton;

public class Client implements Constants {

	private Socket aSocket;
	private PrintWriter socketOut;
	private BufferedReader socketIn;
	private GameView theView;
	private int choice;
	private Boolean isConnected = false;


    /**
     * Constructor
     * @param serverName
     * @param portNumber
     * @param theView
     */
	public Client(String serverName, int portNumber, GameView theView) {
		this.theView = theView;
		try {
			aSocket = new Socket(serverName, portNumber);
			isConnected = true;
			// Socket input Stream
			socketIn = new BufferedReader(new InputStreamReader(aSocket.getInputStream()));

			// Socket output Stream
			socketOut = new PrintWriter(aSocket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    /**
     * Function sends over the player name to the server
     */
	public void sendPlayerName(){
		String name = "";
		name = theView.getName();
		if(name != null){
			System.out.println("Sending player name " + name + " to server.." );
			socketOut.println(name);
			socketOut.flush();
		}
	}

    /**
     * Sends over the row and col values of the button press
     * @param row
     * @param col
     */
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


    /**
     * Sets the mark on the Board after receiving a confirmation from the server
     * We accept server response in the following format.
     * choice,row,col,mark(LETTERX or LETTERO)
     * @param response
     */
	public void setMark(String response) {
		int row, col = 0;
		String mark;
		// Checking to see if the server response is anything other than the expected response
		// containing row,col and mark
		if (response != null) {
			String [] temp = response.split(",");
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

    /**
     * A method which constantly reads server response
     * @throws IOException
     */
	public void getServerResponse() throws IOException {
		while (isConnected) {
			String response = "";
			try {
				response = socketIn.readLine();
				System.out.println("Server response is: " + response);
				String [] temp = response.split(",");
				choice = Integer.parseInt(temp[0]);
			}catch(SocketException e){
				System.out.println("Socket exception!");
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			//Send response to switch board only if non-null
			if(response != null){
				switchBoard(choice, response);
			}
		}
	}


    /**
     * Sets Player Symbols after receiving server response
     * @param response
     */
	public void setPlayer(String response) {
		String [] temp = response.split(",");
			if (temp[1].charAt(0) == LETTER_X || temp[1].charAt(0) == LETTER_O) {
				if (temp[1].charAt(0) == LETTER_X) {
					theView.setPlayerSymbol(LETTER_X);
				} else if (temp[1].charAt(0) == LETTER_O) {
					theView.setPlayerSymbol(LETTER_O);
				}
			}
	}


    /**
     * SwitchBoard on Client side to decide to execute relevant cases based on server response
     * @param choice
     * @param response
     * @throws IOException
     */
	public void switchBoard(int choice, String response) throws IOException {
		switch (choice){
			case 1:
				//Set players
				setPlayer(response);
				//Set name by taking the data from the GUI itself as we have received an ack from server
				theView.setPlayerName(theView.getName());
				break;
			case 2:
				//Set player names
				break;
			case 3:
				//Make move
				String [] message2 = response.split(",");
				theView.setMessageArea(message2[1]);

				ClientController temp = new ClientController(theView);
				while((temp.getRowColData()[0] == -1)){
					System.out.println("Entered case 3 while loop");
				}
				sendButtonPress(temp.getRowColData()[0],temp.getRowColData()[1]);
				break;
			case 4:
			    //Set mark on GUI
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
				break;
			case 7:
				//Send player name to server
				//Ideally, we would be able to send player name without a case but it would be better if the server asks for the name and we send it after that
				sendPlayerName();
				break;
			case 8:
				//Close all connections
				socketOut.close();
				socketIn.close();
				aSocket.close();
				//Set flag to false so as to indicate to the client to stop listening for server response
				isConnected = false;
				break;
			default:
				System.out.println("Invalid choice!");
		}
	}
}
