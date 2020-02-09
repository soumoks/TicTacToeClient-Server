package Server;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class Player {

	// private String name;
	private char mark;
	private PrintWriter socketOut;
	private BufferedReader socketIn;

	public Player(BufferedReader in, PrintWriter out, char mark) {
		// this.name = name;
		this.setMark(mark);
		this.socketIn = in;
		this.socketOut = out;
	}

	public char getMark() {
		return mark;
	}

	public void setMark(char mark) {
		this.mark = mark;
	}

}
