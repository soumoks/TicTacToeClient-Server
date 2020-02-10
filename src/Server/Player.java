package Server;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class Player {

	// private String name;
	private char mark;
	private String name;

	public Player(char mark,String name) {
		this.mark = mark;
		this.name = name;
	}

	public char getMark() {
		return mark;
	}

	public void setMark(char mark) {
		this.mark = mark;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
