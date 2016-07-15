package TucLoA;

//Lines of Action BOT
//Chatziparaschis Dimitris
//AM 2011030039
//

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;


public class Client {
	private Communication comm;
	private GamePosition gamePosition;
	private byte myColor;
	private Moves_and_Checks mac;

	private final byte NM_NEW_POSITION = 1;
	private final byte NM_COLOR_W = 2;
	private final byte NM_COLOR_B = 3;
	private final byte NM_REQUEST_MOVE = 4;
	private final byte NM_PREPARE_TO_RECEIVE_MOVE = 5;
	private final byte NM_REQUEST_NAME = 6;
	private final byte NM_QUIT = 7;

	public Client(){
		try{
			comm = new Communication("127.0.0.1", 6001);
			gamePosition = new GamePosition();
			mac = new Moves_and_Checks();
		}catch (IOException e) {
			System.out.println("No server found!!");
			System.exit(1);
		}
	}


	public void run() {
		int msg;

		try {
			while (true) {
				msg = comm.recvMsg();
				System.out.println("msg: "+msg);
				switch (msg) {
				case NM_REQUEST_NAME:
					
					//Scanner reader3 = new Scanner(System.in);  // Reading from System.in
					//System.out.println("Enter your name: ");
					//String name = reader3.next(); 
					
					comm.sendName("AgentJim");
					break;

				case NM_NEW_POSITION:
					comm.getPosition(gamePosition);
					mac.printPosition(gamePosition);
					break;

				case NM_COLOR_W:
					myColor = gamePosition.WHITE;
					break;

				case NM_COLOR_B:
					myColor = gamePosition.BLACK;
					break;

				case NM_PREPARE_TO_RECEIVE_MOVE:
					System.out.println("NM_PREPARE_TO_RECEIVE_MOVE");
					byte b[] = comm.getMove();
					Move m=  new Move(b[0],b[1],b[2],b[3]);
					mac.doMove(gamePosition, m);//updating board after opponent movement
					mac.printPosition(gamePosition);
					break;

				case NM_REQUEST_MOVE:
					
					byte myMove[] = new byte[4];

					if( mac.canMove( gamePosition, myColor, false )==0 )
					{
						myMove[ 0 ] = -1;		//null move
					}
					else
					{
						
						AlphaBetaSearch search = new AlphaBetaSearch(gamePosition);
						//inputs: deapth,enable AlphaBeta Pruning, enable Singular extensions, enable Forward Pruning, aggression....
		    	    	
						Move bestmove = search.makeDecision(gamePosition, myColor, 6,true,true,true,true); 
												
						myMove[0]=bestmove.x_start;
						myMove[1]=bestmove.y_start;
						myMove[2]=bestmove.x_end;
						myMove[3]=bestmove.y_end;
						
						System.out.println(search.getMetrics());
						
					}

					comm.sendMove( myMove );			//send our move to the server
					Move bestmove =  new Move(myMove[0],myMove[1],myMove[2],myMove[3]);
					mac.doMove( gamePosition, bestmove );		//play our move on our position
					mac.printPosition( gamePosition );

					break;

				case NM_QUIT:
					System.out.println("The end!!!");
					System.exit(0);
					break;

				default:
					System.exit(1);
					break;
				}
			}
		} catch (IOException e) {
			System.out.println("ERROR : Network problem!!!");
			System.exit(1);
		}
	}
		
	public static void main(String[] args) {
		Client client = new Client();
		client.run();
	}

}
