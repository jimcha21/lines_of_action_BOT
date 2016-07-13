package TucLoA;

//Lines of Action BOT
//Chatziparaschis Dimitris
//AM 2011030039
//

public class Move {

	 public byte x_start, y_start,x_end,y_end;
	 byte capturedchecker;
	public Move next;
	
    public Move(byte x_start, byte y_start, byte x_end, byte y_end) {
        this.x_start = x_start;
        this.y_start = y_start;
        this.x_end = x_end;
        this.y_end = y_end;
    }
	
    public Move(byte x_start, byte y_start, byte x_end, byte y_end, byte capturedchecker) {
        this.x_start = x_start;
        this.y_start = y_start;
        this.x_end = x_end;
        this.y_end = y_end;
        this.capturedchecker = capturedchecker;
    }
	
	
}
