package TucLoA;

//Lines of Action BOT
//Chatziparaschis Dimitris
//AM 2011030039
//

public class Checker {

	  public int x;
	  public int y;
	  public int owner;

  Checker(int X, int Y, int own) {
      x = X;
      y = Y;
      owner = own;
  }

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
	
