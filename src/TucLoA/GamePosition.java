package TucLoA;

//Lines of Action BOT
//Chatziparaschis Dimitris
//AM 2011030039
//

public class GamePosition {
	private byte board[][];
	private byte turn;
	final byte BOARD_SIZE = 8;
	final byte WHITE = 0;
	final byte BLACK = 1;
	final byte EMPTY = 2;
	
	public GamePosition() {
		board = new byte[BOARD_SIZE][BOARD_SIZE];
	}
	
	public byte[][] getBoard() {
		return board;
	}
	
	public byte getTurn() {
		return turn;
	}
	
	public void setTurn(byte t) {
		turn = t;
	}
}
