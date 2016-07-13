package TucLoA;

//Lines of Action BOT
//Chatziparaschis Dimitris
//AM 2011030039
//

import java.io.IOException;
//created by ntaklas
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Communication {

	private PrintWriter out= null;
	Socket sock;
	
	public Communication(String Server_ip, int Server_port) throws IOException {
		final InetAddress server_address = InetAddress.getByName(Server_ip);
		final int server_port = Server_port;
		sock = new Socket(server_address,server_port);

		try {
			out = new PrintWriter(sock.getOutputStream(),true);
			out.flush();
		} catch (IOException e) {
			sock.close();
			System.out.println("ERROR: Network problem!!!");
			System.exit(1);
		}
	}
	
	public int recvMsg() throws IOException {
		return sock.getInputStream().read();
	}
	
	public void sendName(String name) throws IOException {
		char size = (char) name.length();
		
		out.print(size);
		out.flush();
 
		out.print(name);
		out.flush();
	}
	
	public void sendMove(byte[] move) throws IOException {
		byte buffer[] = new byte[4];
		
		for (int i = 0; i < buffer.length; i++) {
			buffer[i] = move[i];
		}
		
		sock.getOutputStream().write(buffer);
	}
	
	public byte[] getMove() throws IOException {
		byte buffer[] =new byte[4];
		byte move[] = new byte[4];
		if(sock.getInputStream().read(buffer) == 4){
			for (int i = 0; i < buffer.length; i++) {
				move[i] = buffer[i];
			}
			return move;
		}else
			throw (new IOException());
	}
	
	public void getPosition(GamePosition gamePosition) throws IOException {
		byte buffer[] = new byte[ 8 * 8 +1 ];
		
		if(sock.getInputStream().read(buffer)==8 * 8 +1){
			//board 
			for(int i = 0; i < 8; i++ )
				for(int j = 0; j < 8; j++ )
					gamePosition.getBoard()[ i ][ j ] = buffer[ i * 8 + j ];

			//turn
			gamePosition.setTurn(buffer[ 64 ]);
		}else
			throw (new IOException());
	}
}
