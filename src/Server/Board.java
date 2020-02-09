package Server;

//STUDENTS SHOULD ADD CLASS COMMENTS, METHOD COMMENTS, FIELD COMMENTS 


import java.io.Serializable;

/**
 * The type Board.
 */
public class Board implements Constants, Serializable {
	private static final long serialVersionUID = 1L;

	private char theBoard[][];
	private int markCount;

	/**
	 * Instantiates a new Board.
	 */
	public Board() {
		markCount = 0;
		theBoard = new char[3][];
		for (int i = 0; i < 3; i++) {
			theBoard[i] = new char[3];
			for (int j = 0; j < 3; j++)
				theBoard[i][j] = SPACE_CHAR;
		}
	}

	/**
	 * Gets the marks in each position of the board.
	 *
	 * @param row the row
	 * @param col the col
	 * @return mark
	 */
	public char getMark(int row, int col) {
		return theBoard[row][col];
	}

	/**
	 * Returns true if markCount becomes 9
	 *
	 * @return boolean
	 */
	public boolean isFull() {
		return markCount == 9;
	}

	/**
	 * Returns true if X wins the game.
	 *
	 * @return boolean
	 */
	public boolean xWins() {
		if (checkWinner(LETTER_X) == 1)
			return true;
		else
			return false;
	}

	/**
	 * Returns true if O wins the game.
	 *
	 * @return boolean
	 */
	public boolean oWins() {
		if (checkWinner(LETTER_O) == 1)
			return true;
		else
			return false;
	}

	/**
	 * Prints the Board on the console.
	 */
	public void display() {
		displayColumnHeaders();
		addHyphens();
		for (int row = 0; row < 3; row++) {
			addSpaces();
			System.out.print("    row " + row + ' ');
			for (int col = 0; col < 3; col++)
				System.out.print("|  " + getMark(row, col) + "  ");
			System.out.println("|");
			addSpaces();
			addHyphens();
		}
	}

	/**
	 * Adds the mark on the board as long as check mark returns true. Adds either an X or an O on the board.
	 *
	 * @param row  the row
	 * @param col  the col
	 * @param mark the mark
	 * @return the boolean
	 */
	public synchronized boolean addMark(int row, int col, char mark) {
		if(checkMark(row,col)){
			theBoard[row][col] = mark;
			markCount++;
			return true;
		}
		else{
			return false;
		}
	}

	/**
	 * Remove mark.
	 *
	 * @param row the row
	 * @param col the col
	 */
	public void removeMark(int row, int col){
		theBoard[row][col] = SPACE_CHAR;
		markCount--;
	}

	/**
	 * Checks if the row, column entered by the user is valid.
	 * It checks the following:
	 * If the row, column are in range (0-2)
	 * If the row,column does not have a mark.
	 * Returns true if the above conditions are met.
	 * Returns false otherwise.
	 *
	 * @param row the row
	 * @param col the col
	 * @return boolean
	 */
	public boolean checkMark(int row, int col){
		if((row>=0 && row<=2) && (col>=0 && col<=2)){
			if((getMark(row,col) == 'X' || getMark(row,col) == 'O')){
				return false;
			}
			else{
				return true;
			}
		}
		else{
			return false;
		}
	}

	/**
	 * Clears the Board. Removes any marks on the board with Spaces.
	 */
	public void clear() {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				theBoard[i][j] = SPACE_CHAR;
		markCount = 0;
	}

	/**
	 * Checks the possible win scenarios.
	 * Row complete.
	 * Column complete.
	 * Diagonal complete.
	 * Opposite Diagonal complete.
	 *
	 * @param mark the mark
	 * @return int
	 */
	int checkWinner(char mark) {
		int row, col;
		int result = 0;

		for (row = 0; result == 0 && row < 3; row++) {
			int row_result = 1;
			for (col = 0; row_result == 1 && col < 3; col++)
				if (theBoard[row][col] != mark)
					row_result = 0;
			if (row_result != 0)
				result = 1;
		}

		
		for (col = 0; result == 0 && col < 3; col++) {
			int col_result = 1;
			for (row = 0; col_result != 0 && row < 3; row++)
				if (theBoard[row][col] != mark)
					col_result = 0;
			if (col_result != 0)
				result = 1;
		}

		if (result == 0) {
			int diag1Result = 1;
			for (row = 0; diag1Result != 0 && row < 3; row++)
				if (theBoard[row][row] != mark)
					diag1Result = 0;
			if (diag1Result != 0)
				result = 1;
		}
		if (result == 0) {
			int diag2Result = 1;
			for (row = 0; diag2Result != 0 && row < 3; row++)
				if (theBoard[row][3 - 1 - row] != mark)
					diag2Result = 0;
			if (diag2Result != 0)
				result = 1;
		}
		return result;
	}

	/**
	 * Prints the following on the console.
	 * col 0, col 1, col2
	 */
	void displayColumnHeaders() {
		System.out.print("          ");
		for (int j = 0; j < 3; j++)
			System.out.print("|col " + j);
		System.out.println();
	}

	/**
	 * Prints Hyphens
	 */
	void addHyphens() {
		System.out.print("          ");
		for (int j = 0; j < 3; j++)
			System.out.print("+-----");
		System.out.println("+");
	}

	/**
	 * Prints spaces
	 */
	void addSpaces() {
		System.out.print("          ");
		for (int j = 0; j < 3; j++)
			System.out.print("|     ");
		System.out.println("|");
	}


}
