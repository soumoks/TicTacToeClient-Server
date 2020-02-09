package Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import Client.ClientController.TestButtonListener;

public class ClientController implements Constants {

	private GameView theView;
	private Client aClient;
	private int [] rowColData;


	public int[] getRowColData() {
		return rowColData;
	}

	public ClientController(GameView v, Client c) {
		theView = v;
		// theView.setPlayerName(theView.getNameX());
		// theView.setPlayerSymbol(LETTER_X);
		// theView.setMessageArea(theView.getNameX() + ", Please make your move");
		theView.addButtonListener(new TestButtonListener());
		aClient = c;
		rowColData = new int[2];
		rowColData[0] = -1;
		rowColData[1] = -1;
	}

	class TestButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			for (int row = 0; row < 3; row++) {
				for (int col = 0; col < 3; col++) {
					JButton[][] temp = theView.getButton();
					if(temp[row][col] == e.getSource()){
						System.out.println("Setting row and column values based on button press");
						rowColData[0] = row;
						rowColData[1] = col;
					}

//					if (temp[row][col] == e.getSource() && aClient.getChoice() == 3) {
//						System.out.println("Row:" + row);
//						System.out.println("Column:" + col);

						// TODO
						/*
						 * This logic should be taken away from the client. Instead, we should set
						 * whatever is being sent by the server in the GameView. This would ensure that
						 * we don't set conflicting marks on different client screens. Implementation of
						 * this method would be done as part of the Client class.
						 * 
						 */
						//if(aClient.listenForPermission()){
						//aClient.sendButtonPress(row, col);
						//}
//						else{
//							System.out.println("No permission");
//						}
//

						//aClient.setMark();
//                        if(theView.getPlayerSymbol() == LETTER_X){
//                            aClient.sendButtonPress(row,col);
//                            temp[row][col].setText("x");
//                        }
//                        else if(theView.getPlayerSymbol() == LETTER_O){
//                            aClient.sendButtonPress(row,col);
//                            temp[row][col].setText("o");
					//}

				}
			}
		}
	}


}
