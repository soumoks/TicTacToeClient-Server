package Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

public class ClientController implements Constants {

	private GameView theView;
	private int [] rowColData;

	/**
	 * Getter for the matrix consisting of the button press row and column values
	 * @return
	 */
	public int[] getRowColData() {
		return rowColData;
	}

	/**
	 * Constructor
	 * @param theView
	 */
	public ClientController(GameView theView) {
		this.theView = theView;
		theView.addButtonListener(new ButtonListener());
		rowColData = new int[2];

		//Initialise the array to non-matrix value i.e does not belong to 0-2
		rowColData[0] = -1;
		rowColData[1] = -1;
	}

	/**
	 * Action listener for the Game buttons
	 */
	class ButtonListener implements ActionListener {

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
				}
			}
		}
	}

}
