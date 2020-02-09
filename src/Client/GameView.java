package Client;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * View class that is responsible for the GUI
 */
public class GameView extends JFrame {
    private JPanel mainPanel; // main Panel which contains additional sub-panels
    private JPanel boardPanel; // Panel containing the Board
    private JPanel textPanel; // Panel containing the messageArea, PlayerSymbol and PlayerName
    private JTextArea messageArea;//This is for displaying the winner of the game.
    private JTextField playerSymbol; //Indicates whether the player is X or O. This is done by the program and the user does not get a choice
    private JTextField playerName; // This is for displaying the playerName.
    private JButton[][] button;
    private JPanel PlayerNamePanel;
    private JPanel PlayerFieldPanel;
    private JLabel messageWindow;
    private JLabel playerSymbolLabel;
    private JLabel userNameLabel;
    private String nameX;
    private String nameO;
    private String name;


    /**
     * Returns the name of the player to the client class
     * @return
     */
    public String getName(){
        return name;
    }
    /**
     * Returns the name of the X player
     * @return
     */
    public String getNameX() {
        return nameX;
    }

    /**
     * Returns the name of the O player
     * @return
     */
    public String getNameO() {
        return nameO;
    }

    /**
     * View Constructor that is responsible for accepting user input and creates the frame.
     */
    public GameView(){
        //playerNameInput();
        createGameWindow();
    }

    /**
     * Returns the Button
     * @return
     */
    public JButton[][] getButton() {
        return button;
    }

    /**
     * Returns the Player Symbol
     * @return
     */
    public String getPlayerSymbol() {
        String temp = playerSymbol.getText();
        return temp;
    }

    /**
     * Creates the Game Window
     */
    public void createGameWindow(){
        boardPanel = new JPanel();
        mainPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(3,3,3,3));
        boardPanel.setPreferredSize(new Dimension(150,150));
        button = new JButton[3][3];

        // Initialises and adds the button to the panel
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++){
                button[i][j] = new JButton();
                button[i][j].setPreferredSize(new Dimension(60,60));
                button[i][j].setBorder(null);
                boardPanel.add(button[i][j]);
            }
        }

        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weighty = 0.1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        mainPanel.add(boardPanel,constraints);

        textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());
        messageArea = new JTextArea(50,10);
        messageWindow = new JLabel("Message Window");
        //messageArea.setPreferredSize(new Dimension(200,200));
        textPanel.add(messageArea,BorderLayout.CENTER);
        textPanel.add(messageWindow,BorderLayout.NORTH);
        textPanel.setPreferredSize(new Dimension(210,50));

        constraints.weightx = 0.5;
        constraints.gridx = 1;
        constraints.gridy = 0;
        mainPanel.add(textPanel,constraints);

        PlayerNamePanel = new JPanel();
        PlayerFieldPanel = new JPanel();
        PlayerNamePanel.setLayout(new BorderLayout());
        PlayerFieldPanel.setLayout(new BorderLayout());
        playerSymbol = new JTextField(20);
        playerName = new JTextField(20);
        playerSymbolLabel = new JLabel("Player: ");
        userNameLabel = new JLabel("Player Name: ");
        PlayerNamePanel.add(playerName,BorderLayout.CENTER);
        PlayerNamePanel.add(userNameLabel,BorderLayout.WEST);
        PlayerNamePanel.setPreferredSize(new Dimension(130,25));

        PlayerFieldPanel.add(playerSymbol,BorderLayout.CENTER);
        PlayerFieldPanel.add(playerSymbolLabel,BorderLayout.WEST);
        PlayerFieldPanel.setPreferredSize(new Dimension(60,25));

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weighty = 0.1;
        mainPanel.add(PlayerFieldPanel,constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.weighty = 0.1;
        mainPanel.add(PlayerNamePanel,constraints);


        setPreferredSize(new Dimension(550,350));
        getContentPane().add(mainPanel);
    }

    /**
     * Adds the Action listener present in the Controller class to all buttons
     * @param listenForButton
     */
    public void addButtonListener(ActionListener listenForButton){
        for(int i =0;i<3;i++){
            for (int j=0;j<3;j++){
                button[i][j].addActionListener(listenForButton);
            }
        }
    }

    /**
     * Function is responsible for accepting Player Name.
     */
    public void playerNameInput(){
        name = JOptionPane.showInputDialog("Enter your name");
        while (name == null) {
            JOptionPane.showMessageDialog(null, "Please Try again!", " Warning",JOptionPane.PLAIN_MESSAGE);
            name = JOptionPane.showInputDialog("Enter your name");
        }
//        nameO = JOptionPane.showInputDialog("Enter the name of the O player");
//        while (nameO == null) {
//            JOptionPane.showMessageDialog(null, "Please Try again!", " Warning",JOptionPane.PLAIN_MESSAGE);
//            nameO = JOptionPane.showInputDialog("Enter the name of the O player");
//        }
    }

    /**
     * Sets the playerName on the GUI
     * @param s
     */
    public void setPlayerName(String s){
        playerName.setText(s);
    }

    /**
     * Sets the playerSymbol on the GUI
     * @param s
     */
    public void setPlayerSymbol(char s){
        playerSymbol.setText(String.valueOf(s));
    }

    /**
     * Sets the messageAre on the GUI
     * @param s
     */
    public void setMessageArea(String s){
        messageArea.setText(s);
    }

    /**
     * Updates the board after reading board object from server
     * @param theBoard
     */
//    public void updateBoard(Board theBoard){
//        for(int i=0;i<3;i++){
//            for(int j=0;j<3;j++){
//                if(theBoard.getMark(i,j) == Constants.LETTER_X){
//                    button[i][j].setText("x");
//                }
//                else if(theBoard.getMark(i,j) == Constants.LETTER_O){
//                    button[i][j].setText("o");
//                }
//            }
//        }
//}
}
