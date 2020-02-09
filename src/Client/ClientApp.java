package Client;

import java.io.IOException;

public class ClientApp {
	public static void main(String[] args) throws IOException {
		GameView theView = new GameView();
		Client theClient = new Client("localhost", 9090, theView);
		ClientController theController = new ClientController(theView, theClient);
		theView.setVisible(true);
		theView.pack();
		theClient.getServerResponse();
		//Client object is initiated in ClientController class.
	}
}
