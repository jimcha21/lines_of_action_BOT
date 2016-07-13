package TucLoA;

//Lines of Action BOT
//Chatziparaschis Dimitris
//AM 2011030039
//

import java.util.ArrayList;

public class Moves_and_Checks {
	
	
	void recursiveCheckGroup( GamePosition gamePosition, GamePosition temp, int x, int y, byte playerColor )
	{
		temp.getBoard()[x][y] = 1;

		if( x-1 >= 0 )
		{
			if( y-1 >= 0 )
			{
				if( gamePosition.getBoard()[x-1][y-1] == playerColor )
					if( temp.getBoard()[x-1][y-1] != 1 )
						recursiveCheckGroup( gamePosition, temp, x-1, y-1, playerColor );
			}
			
			if( gamePosition.getBoard()[x-1][y] == playerColor )
				if( temp.getBoard()[x-1][y] != 1 )
					recursiveCheckGroup( gamePosition, temp, x-1, y, playerColor );

			if( y+1 < temp.BOARD_SIZE )
			{
				if( gamePosition.getBoard()[x-1][y+1] == playerColor )
					if( temp.getBoard()[x-1][y+1] != 1 )
						recursiveCheckGroup( gamePosition, temp, x-1, y+1, playerColor );
			}
		}

		if( x+1 < temp.BOARD_SIZE )
		{
			if( y-1 >= 0 )
			{
				if(gamePosition.getBoard()[x+1][y-1] == playerColor )
					if( temp.getBoard()[x+1][y-1] != 1 )
						recursiveCheckGroup( gamePosition, temp, x+1, y-1, playerColor );
			}

			if( gamePosition.getBoard()[x+1][y] == playerColor )
				if( temp.getBoard()[x+1][y] != 1 )
					recursiveCheckGroup( gamePosition, temp, x+1, y, playerColor );

			if( y+1 < temp.BOARD_SIZE )
			{
				if( gamePosition.getBoard()[x+1][y+1] == playerColor )
					if( temp.getBoard()[x+1][y+1] != 1 )
						recursiveCheckGroup( gamePosition, temp, x+1, y+1, playerColor );
			}
		}
		
		if( y-1 >= 0 )
		{
			if( gamePosition.getBoard()[x][y-1] == playerColor )
				if( temp.getBoard()[x][y-1] != 1 )
					recursiveCheckGroup( gamePosition, temp, x, y-1, playerColor );
		}

		if( y+1 < temp.BOARD_SIZE )
		{
			if( gamePosition.getBoard()[x][y+1] == playerColor )
				if( temp.getBoard()[x][y+1] != 1 )
					recursiveCheckGroup( gamePosition, temp, x, y+1, playerColor );
		}

	}

	/**********************************************************/
	// The checkWin code is from the C folder, from board.c file .... Its the code for the server to
	// check if someone wins at the current game status..
	boolean checkWin(GamePosition gamePosition,byte playerColor)  
	{

		int i, j;
		int k = 0, l = 0;
		int pieces = 0;
		int groupMembers = 0;
		//char groupMap[ BOARD_SIZE ][ BOARD_SIZE ] = {{0}};
		GamePosition aaa = new GamePosition();
		
		
		for( i = 0; i < gamePosition.BOARD_SIZE; i++ )
		{
			for( j = 0; j < gamePosition.BOARD_SIZE; j++ )
			{
				if( gamePosition.getBoard()[ i ][ j ] ==  playerColor)
				{
					pieces++;		//count player's pieces
					k = i;
					l = j;			//save x,y for the last piece to start our recursive function there
				}
			}
		}
		
		recursiveCheckGroup( gamePosition, aaa, k, l, playerColor );
		
		for( i = 0; i < gamePosition.BOARD_SIZE; i++ )
		{
			for( j = 0; j < gamePosition.BOARD_SIZE; j++ )
			{
				if( aaa.getBoard()[ i ][ j ] == 1 )
					groupMembers++;		//count group members
			}
		}
		
		//System.out.println("gia na doume ti einai ayto " + groupMembers + " kai ayto " +pieces);
		return (groupMembers==pieces);
		
		
	}
	
 	public int Symmetric_ForForwardPruning(GamePosition gamePosition){
		
		boolean x_axis=true, y_axis=true,diag=true;
		
		for(int j=0;j<8;j++){ //x-axis symmetrical
			for(int i=0;i<4;i++){
				//System.out.println(" "+i+" "+j + " , "+(7-i)+ " " + j + " ," + (7-i) + " "+ (7-j));
				if(gamePosition.getBoard()[i][j] != gamePosition.getBoard()[7-i][j]){
					x_axis=false;
					break;
				}
			}	if (!x_axis) break;
		}
		
		for(int i=0;i<8;i++){
			for(int j=0;j<4;j++){ //y-axis symmetrical or diagonial symmetrical
				//System.out.println(" "+i+" "+j + " , "+i+ " " + (7-j) + " ," + (7-i) + " "+ (7-j));
				if(gamePosition.getBoard()[i][j] != gamePosition.getBoard()[i][7-j]) y_axis=false;
							
				if(gamePosition.getBoard()[i][j] != gamePosition.getBoard()[7-i][7-j]) diag=false;
				
				if (!y_axis&&!diag) break;
			}	if (!y_axis&&!diag) break;
		}
				
		if(x_axis){
			//System.out.println("Forward Prunned (x)");
			return 1;
		}else if (y_axis){
			//System.out.println("Forward Prunned (y)");
			return 2;
		}else if(diag){
			//System.out.println("Forward Prunned (d)");
			return 3;
		}
		
		return -1;
	}
	
	void printBoard( GamePosition gamePosition ){

		// /* Print the upper section 
		System.out.print( "   " );
		for(int i = 0; i < gamePosition.BOARD_SIZE; i++ )
			System.out.print( i + " " );
		System.out.print( "\n +" );
		for(int i = 0; i < 2 * gamePosition.BOARD_SIZE + 1; i++ )
			System.out.print( "-" );
		System.out.print( "+\n" );

	//	/* Print board 
		for(int i = 0; i < gamePosition.BOARD_SIZE; i++ ){
			System.out.print( i + "| ");
			for(int j = 0; j < gamePosition.BOARD_SIZE; j++ )
				switch( gamePosition.getBoard()[ i ][ j ] ){
					case 0:
						System.out.print( "W " );
						break;
					case 1:
						System.out.print( "B " );
						break;
					case 2:
						System.out.print( ". " );
						break;
					case 3:
						System.out.print( "# " );
						break;
					default:
						System.out.print("ERROR: Unknown character in board (printBoard)\n");
						System.exit( 1 );
				}
			System.out.print( "|"+ i +"\n"  );
		}

		// /* Print the lower section 
		System.out.print( " +" );
		for(int i = 0; i < 2 * gamePosition.BOARD_SIZE + 1; i++ )
			System.out.print( "-" );
		System.out.print( "+\n" );
		System.out.print( "   " );
		for(int i = 0; i < gamePosition.BOARD_SIZE; i++ )
			System.out.print( i +" ");
		System.out.print( "\n" );

	}
	
	void printPosition( GamePosition gamePosition ){
		//board
		printBoard( gamePosition );

		//turn
		if( gamePosition.getTurn() == gamePosition.WHITE )
			System.out.print( "Turn: WHITE" );
		else if( gamePosition.getTurn() == gamePosition.BLACK )
			System.out.print( "Turn: BLACK" );
		else
			System.out.print( "Turn: -" );

		System.out.print( "\n" );

	}
	
	public void doMove( GamePosition gamePosition, Move move ){
		

		if( move.x_start != -1 )
		{
			gamePosition.getBoard()[ move.x_start ][  move.y_start ] = gamePosition.EMPTY;	//place piece
			gamePosition.getBoard()[ move.x_end ][  move.y_end  ] = gamePosition.getTurn();	//remove piece 
		
		}
			/* change turn - even in case of a null move */
		if(gamePosition.getTurn() == gamePosition.WHITE)
			gamePosition.setTurn(gamePosition.BLACK);
		else
			gamePosition.setTurn(gamePosition.WHITE);
	
	}

	public void undoMove( GamePosition gamePosition, Move move ){
		
		if(gamePosition.getTurn() == gamePosition.WHITE)
			gamePosition.setTurn(gamePosition.BLACK);
		else
			gamePosition.setTurn(gamePosition.WHITE);
		
		if( move.x_start != -1 )
		{
			gamePosition.getBoard()[ move.x_start ][  move.y_start ] = gamePosition.getTurn();	//replace started piece
			gamePosition.getBoard()[ move.x_end ][  move.y_end  ] = move.capturedchecker;	//replace -if captured- piece
		
		}
			/* change turn - even in case of a null move*/

	 
	}
		
	public boolean isLegalJump( GamePosition gamePosition,  byte player ,byte moveToCheck[] ){

		int i,j;
		int stepX, stepY;
		int pieces = 0;
		int pathLength = 0;
		boolean insidePathFlag = false;

		if( moveToCheck[ 0 ] == -1 )
		{
			return false;
		}

		/* first coordinates must be inside board */
		if( ( moveToCheck[ 0 ] < 0 ) || ( moveToCheck[ 0 ] >= gamePosition.BOARD_SIZE ) )
			return false;
		if( ( moveToCheck[ 1 ] < 0 ) || ( moveToCheck[ 1 ] >= gamePosition.BOARD_SIZE ) )
			return false;

		/* piece must be ours */
		if( gamePosition.getBoard()[ moveToCheck[ 0 ] ][ moveToCheck[ 1 ] ] != player )
			return false;


		/* second coordinates must be inside board */
		if( ( moveToCheck[ 2 ] < 0 ) || ( moveToCheck[ 2] >= gamePosition.BOARD_SIZE ) )
			return false;
		if( ( moveToCheck[ 3 ] < 0 ) || ( moveToCheck[ 3 ] >= gamePosition.BOARD_SIZE ) )
			return false;

		/* square must be empty or enemy - also takes care of the possibility the two coordinates pointing at the same tile */
		if( (gamePosition.getBoard()[ moveToCheck[ 2 ] ][ moveToCheck[ 3 ] ] != gamePosition.EMPTY) && (gamePosition.getBoard()[ moveToCheck[ 2 ] ][ moveToCheck[ 3 ] ] != 1-player ) )
			return false;


		//check all four directions for a match
		if( moveToCheck[ 0 ] == moveToCheck[ 2 ] )
		// if horizontal	(x1 == x2)
		{
			stepX = 0;
			stepY = 1;
			
			i = moveToCheck[ 0 ];
			j = 0;
		}
		else if( moveToCheck[ 1 ] == moveToCheck[ 3 ] )
		//if vertical	(y1 == y2)
		{
			stepX = 1;
			stepY = 0;
			
			i = 0;
			j = moveToCheck[ 1 ];
		}
		else if( (moveToCheck[ 0 ] - moveToCheck[ 1 ]) == (moveToCheck[ 2 ] - moveToCheck[ 3 ]) )
		//if first diagonal	(x1-y1 == x2-y2)
		{
			stepX = 1;
			stepY = 1;

			if( moveToCheck[ 0 ] > moveToCheck[ 1 ] )
			{
				i = moveToCheck[ 0 ] - moveToCheck[ 1 ];
				j = 0;
			}
			else
			{
				i = 0;
				j = moveToCheck[ 1 ] - moveToCheck[ 0 ];
			}
		}
		else if( (moveToCheck[ 0 ] + moveToCheck[ 1 ]) == (moveToCheck[ 2 ] + moveToCheck[ 3 ]) )
		//if second diagonal	(x1+y1 == x2+y2)
		{
			stepX = -1;
			stepY = 1;

			if( (moveToCheck[ 0 ] + moveToCheck[ 1 ]) > (gamePosition.BOARD_SIZE-1) )
			{
				i = gamePosition.BOARD_SIZE - 1;
				j = (moveToCheck[ 0 ] + moveToCheck[ 1 ]) - (gamePosition.BOARD_SIZE - 1);
			}
			else
			{
				i = moveToCheck[ 0 ] + moveToCheck[ 1 ];
				j = 0;
			}
		}
		else	//not in the same line
			return false;


		do	//for all the line of "action" (movement)
		{
			// count pieces
			if( gamePosition.getBoard()[i][j] == gamePosition.WHITE || gamePosition.getBoard()[i][j] == gamePosition.BLACK )
				pieces++;
			
			// if we reached source or destination tile
			if( (i == moveToCheck[ 0 ] && j == moveToCheck[ 1 ]) || (i == moveToCheck[ 2 ] && j == moveToCheck[ 3 ]) )
			{
				if(	insidePathFlag == false )
					insidePathFlag = true;
				else
					insidePathFlag = false;
			}
			
			if( insidePathFlag )
			{
				// if we find enemy inside the path and it's not the destination tile.. then jump is illegal
				if( ( gamePosition.getBoard()[i][j] == 1- player ) && (i != moveToCheck[ 2 ] || j != moveToCheck[ 3 ]) )
					return false;
				pathLength++;
			}
			
			i += stepX;
			j += stepY;
		}
		while( i >= 0 && i < gamePosition.BOARD_SIZE && j >= 0 && j < gamePosition.BOARD_SIZE );	//while still inside board
		
		if( pathLength == pieces )
			return true;
		else
			return false;

	}

	public byte horizontal_piecesCount(GamePosition gamePosition, int line){
		
		// horizontal koitaei orizontia posa pionia vlepei..
		int pieces = 0;
		for(int k = 0; k < gamePosition.BOARD_SIZE ; k++ )
		{
			if( gamePosition.getBoard()[line][k] == gamePosition.WHITE || gamePosition.getBoard()[line][k] == gamePosition.BLACK )
				pieces++;
		}
		return (byte) pieces;

    }

	public byte vertical_piecesCount(GamePosition gamePosition, int column){	
		// vertical
		int pieces = 0;
		for(int k = 0; k < gamePosition.BOARD_SIZE ; k++ )
		{
			if( gamePosition.getBoard()[k][column] == gamePosition.WHITE || gamePosition.getBoard()[k][column] == gamePosition.BLACK )
				pieces++;
		}
		return (byte) pieces;
	}
	
	public byte diagonial_piecesCount(GamePosition gamePosition, int line,int column, int diag_type){ // @param diag_type gia ton kathorismo ths katametrhshs twn pioniwn sth mia h sthn allh diagwnio
		
		int pieces=0;
		int k,l = 0;
		
		if (diag_type==1){
			// first diagonal
			if( line > column )
			{
				k = line - column;
				l = 0;
			}
			else
			{
				k = 0;
				l = column - line;
			}

			for( ; k < gamePosition.BOARD_SIZE && l < gamePosition.BOARD_SIZE ; k++, l++ )
			{
				if( gamePosition.getBoard()[k][l] == gamePosition.WHITE || gamePosition.getBoard()[k][l] == gamePosition.BLACK )
					pieces++;
			}
		}else{
			// second diagonal
			if( line + column > (gamePosition.BOARD_SIZE - 1) )
			{
				k = gamePosition.BOARD_SIZE-1;
				l = line + column - (gamePosition.BOARD_SIZE - 1);
			}
			else
			{
				k = line + column;
				l = 0;
			}

			for( ; k >= 0 && l < gamePosition.BOARD_SIZE ; k--, l++ )
			{
				if( gamePosition.getBoard()[k][l] == gamePosition.WHITE || gamePosition.getBoard()[k][l] == gamePosition.BLACK )
					pieces++;
			}
		}
		
		return (byte) pieces;
	}
	
	public ArrayList<Move> available_moves( GamePosition gamePosition, byte player ){
		byte i, j;
		byte pieces;
		byte tempMove[] = new byte[4];
		//Move m = null ;
		
		ArrayList<Move> m = new ArrayList<Move>();
		
		for( i = 0; i < gamePosition.BOARD_SIZE; i++ )
		{
			for( j = 0; j < gamePosition.BOARD_SIZE; j++ )
			{
				
				//System.out.println(gamePosition.getBoard()[ i ][ j ]);
				if( gamePosition.getBoard()[ i ][ j ] == player )		//when we find a piece of ours
				{
					//System.out.println("eftase");
					tempMove[0] = i;
					tempMove[1] = j;
					
					// check all four directions for a legal move
					
					// horizontal
					pieces=horizontal_piecesCount(gamePosition,i);
					
					tempMove[2] = i;
					tempMove[3] = (byte) (j+pieces);
					
					if( isLegalJump( gamePosition,player, tempMove ) )
				        m.add(new Move(tempMove[0], tempMove[1], tempMove[2],tempMove[3], gamePosition.getBoard()[tempMove[2]][tempMove[3]]));
						//m.next = result;
					
						//moveCount++;
					
					tempMove[2] = i;
					tempMove[3] = (byte) (j-pieces);
					
					if( isLegalJump( gamePosition,player, tempMove ) )
						 m.add(new Move(tempMove[0], tempMove[1], tempMove[2],tempMove[3], gamePosition.getBoard()[tempMove[2]][tempMove[3]]));
					//m.next = result;
						
										
					// vertical
					pieces =  vertical_piecesCount(gamePosition, j);
					
					tempMove[2] = (byte) (i+pieces);
					tempMove[3] = j;
					
					if( isLegalJump( gamePosition,player, tempMove ) )
						 m.add(new Move(tempMove[0], tempMove[1], tempMove[2],tempMove[3], gamePosition.getBoard()[tempMove[2]][tempMove[3]]));
					//m.next = result;
						
					
					tempMove[2] = (byte) (i-pieces);
					tempMove[3] = j;
					
					if( isLegalJump( gamePosition,player, tempMove ) )
						 m.add(new Move(tempMove[0], tempMove[1], tempMove[2],tempMove[3], gamePosition.getBoard()[tempMove[2]][tempMove[3]]));
					//m.next = result;
						
					
					// first diagonal
					pieces = diagonial_piecesCount(gamePosition,i,j,1);
							
					tempMove[2] = (byte) (i+pieces);
					tempMove[3] = (byte) (j+pieces);
					
					if( isLegalJump( gamePosition,player, tempMove ) )
						 m.add(new Move(tempMove[0], tempMove[1], tempMove[2],tempMove[3], gamePosition.getBoard()[tempMove[2]][tempMove[3]]));
					//m.next = result;
						
					tempMove[2] = (byte) (i-pieces);
					tempMove[3] = (byte) (j-pieces);
					
					if( isLegalJump( gamePosition, player,tempMove ) )
						 m.add(new Move(tempMove[0], tempMove[1], tempMove[2],tempMove[3], gamePosition.getBoard()[tempMove[2]][tempMove[3]]));
					//m.next = result;
					
					
					
					// second diagonal
					pieces = diagonial_piecesCount(gamePosition,i,j,2);
					
					tempMove[2] = (byte) (i-pieces);
					tempMove[3] = (byte) (j+pieces);
					
					if( isLegalJump( gamePosition,player, tempMove ) )
						 m.add(new Move(tempMove[0], tempMove[1], tempMove[2],tempMove[3], gamePosition.getBoard()[tempMove[2]][tempMove[3]]));
					//m.next = result;
						
					tempMove[2] = (byte) (i+pieces);
					tempMove[3] = (byte) (j-pieces);
					
					if( isLegalJump( gamePosition,player, tempMove ) )
						 m.add(new Move(tempMove[0], tempMove[1], tempMove[2],tempMove[3], gamePosition.getBoard()[tempMove[2]][tempMove[3]]));
					//m.next = result;
					
					
				}
			}
		}

      return m;
}
	
	public int canMove( GamePosition gamePosition, byte player, boolean search_blockedPieces ){
		byte i, j;
		byte k, l;
		byte pieces;
		byte tempMove[] = new byte[4];
		int numof_blockedPieces=0;
		boolean unblocked=false;
		
		
		for( i = 0; i < gamePosition.BOARD_SIZE; i++ )
		{
			for( j = 0; j < gamePosition.BOARD_SIZE; j++ )
			{
				if( gamePosition.getBoard()[ i ][ j ] == player )		//when we find a piece of ours
				{
					tempMove[0] = i;
					tempMove[1] = j;
					unblocked=false;
					// check all four directions for a legal move
					
					// horizontal
					pieces = 0;
					for( k = 0; k < gamePosition.BOARD_SIZE ; k++ )
					{
						if( gamePosition.getBoard()[i][k] == gamePosition.WHITE || gamePosition.getBoard()[i][k] == gamePosition.BLACK )
							pieces++;
					}
					
					tempMove[2] = i;
					tempMove[3] = (byte) (j+pieces);
					
					if( isLegalJump( gamePosition,player, tempMove ))
						if(search_blockedPieces)
							 unblocked=true;
						else return 13;
					
					tempMove[2] = i;
					tempMove[3] = (byte) (j-pieces);
					
					if( isLegalJump( gamePosition,player, tempMove ) )
						if(search_blockedPieces)
							 unblocked=true;
						else return 13;
					
					
					// vertical
					pieces = 0;
					for( k = 0; k < gamePosition.BOARD_SIZE ; k++ )
					{
						if( gamePosition.getBoard()[k][j] == gamePosition.WHITE || gamePosition.getBoard()[k][j] == gamePosition.BLACK )
							pieces++;
					}
					
					tempMove[2] = (byte) (i+pieces);
					tempMove[3] = j;
					
					if( isLegalJump( gamePosition,player, tempMove ) )
						if(search_blockedPieces)
							 unblocked=true;
						else return 13;
					
					tempMove[2] = (byte) (i-pieces);
					tempMove[3] = j;
					
					if( isLegalJump( gamePosition,player, tempMove ) )
						if(search_blockedPieces)
							 unblocked=true;
						else return 13;
					
					
					// first diagonal
					pieces = 0;
					if( i > j )
					{
						k = (byte) (i - j);
						l = 0;
					}
					else
					{
						k = 0;
						l = (byte) (j - i);
					}
					
					for( ; k < gamePosition.BOARD_SIZE && l < gamePosition.BOARD_SIZE ; k++, l++ )
					{
						if( gamePosition.getBoard()[k][l] == gamePosition.WHITE || gamePosition.getBoard()[k][l] == gamePosition.BLACK )
							pieces++;
					}
					
					tempMove[2] = (byte) (i+pieces);
					tempMove[3] = (byte) (j+pieces);
					
					if( isLegalJump( gamePosition,player, tempMove ) )
						if(search_blockedPieces)
							 unblocked=true;
						else return 13;
					
					tempMove[2] = (byte) (i-pieces);
					tempMove[3] = (byte) (j-pieces);
					
					if( isLegalJump( gamePosition,player, tempMove ) )
						if(search_blockedPieces)
							 unblocked=true;
						else return 13;
					
					
					// second diagonal
					pieces = 0;
					if( i + j > (gamePosition.BOARD_SIZE - 1) )
					{
						k = (byte) (gamePosition.BOARD_SIZE-1);
						l = (byte) (i + j - (gamePosition.BOARD_SIZE - 1));
					}
					else
					{
						k = (byte) (i + j);
						l = 0;
					}
					
					for( ; k >= 0 && l < gamePosition.BOARD_SIZE ; k--, l++ )
					{
						if( gamePosition.getBoard()[k][l] == gamePosition.WHITE || gamePosition.getBoard()[k][l] == gamePosition.BLACK )
							pieces++;
					}
					
					tempMove[2] = (byte) (i-pieces);
					tempMove[3] = (byte) (j+pieces);
					
					if( isLegalJump( gamePosition,player, tempMove ) )
						if(search_blockedPieces)
							 unblocked=true;
						else return 13;
					
					tempMove[2] = (byte) (i+pieces);
					tempMove[3] = (byte) (j-pieces);
					
					if( isLegalJump( gamePosition, player,tempMove ) )
						if(search_blockedPieces)
							 unblocked=true;
						else return 13;
					
					if(search_blockedPieces&&!unblocked){
						numof_blockedPieces++;
					} 
				}
			}
		}

		return numof_blockedPieces; //returns 0 if there is no available move, or 13 if there is.. if the variable search_blockedPieces is enabled this function return the number of blocked pieces..

	}
	
	public boolean isLegal(GamePosition gamePosition,byte player, byte moveToCheck[] )
	{

		if( canMove( gamePosition, player, false )==0 )	//if that player cannot move, the only legal move is null
		{
			if( moveToCheck[ 0 ] == -1 )
				return true;
			else
				return false;
		}
		else
			return isLegalJump( gamePosition, player,moveToCheck );

	}
	
}
