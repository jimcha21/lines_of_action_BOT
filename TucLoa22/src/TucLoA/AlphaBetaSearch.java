package TucLoA;

//Lines of Action BOT
//Chatziparaschis Dimitris
//AM 2011030039
//

import java.util.ArrayList;
import java.util.Random;
import TucLoA.Metrics;

/**
 * Artificial Intelligence A Modern Approach (3rd Ed.): Page 173.<br>
 * 
 * <pre>
 * <code>
 * function ALPHA-BETA-SEARCH(state) returns an action
 *   v = MAX-VALUE(state, -infinity, +infinity)
 *   return the action in ACTIONS(state) with value v
 *   
 * function MAX-VALUE(state, alpha, beta) returns a utility value
 *   if TERMINAL-TEST(state) then return UTILITY(state)
 *   v = -infinity
 *   for each a in ACTIONS(state) do
 *     v = MAX(v, MIN-VALUE(RESULT(s, a), alpha, beta))
 *     if v >= beta then return v
 *     alpha = MAX(alpha, v)
 *   return v
 *   
 * function MIN-VALUE(state, alpha, beta) returns a utility value
 *   if TERMINAL-TEST(state) then return UTILITY(state)
 *   v = infinity
 *   for each a in ACTIONS(state) do
 *     v = MIN(v, MAX-VALUE(RESULT(s,a), alpha, beta))
 *     if v <= alpha then return v
 *     beta = MIN(beta, v)
 *   return v
 * </code>
 * </pre>
 * 
 * Figure 5.7 The alpha-beta search algorithm. Notice that these routines are
 * the same as the MINIMAX functions in Figure 5.3, except for the two lines in
 * each of MIN-VALUE and MAX-VALUE that maintain alpha and beta (and the
 * bookkeeping to pass these parameters along).
 * 
 * @author Ruediger Lunde
 * 
 * @param <STATE>
 *            Type which is used for states in the game.
 * @param <ACTION>
 *            Type which is used for actions in the game.
 * @param <PLAYER>
 *            Type which is used for players in the game.
 */
public class AlphaBetaSearch<STATE, ACTION, PLAYER> implements
		AdversarialSearch<STATE, ACTION> {

	public final static String METRICS_NODES_EXPANDED = "nodesExpanded";
	
	GamePosition game;
	static final int MAX_VALUE = 100000;
	static final int MIN_VALUE = -100000;
	static final int aggression_factor = 30;
	static int maxxed = 0;
	private Metrics metrics = new Metrics();
	private Moves_and_Checks mac;	
	
	private byte myColor;
	private boolean enable_AB;
	private boolean forward_prun;

	/** Creates a new search object for a given game. */
	public static <STATE, ACTION, PLAYER> AlphaBetaSearch<STATE, ACTION, PLAYER> createFor(
			GamePosition game) {
		return new AlphaBetaSearch<STATE, ACTION, PLAYER>(game);
	}

	public AlphaBetaSearch(GamePosition game) {
		this.game = game;
	}
	
	public Move makeDecision(GamePosition gamePosition,byte myColor ,int depth, boolean enable_AB, boolean enable_singular, boolean forward_prun, boolean aggression) {
		
		this.enable_AB=enable_AB;
		this.myColor=myColor;
		this.forward_prun=forward_prun;
		
		metrics = new Metrics();
		Move bestmove = null;
		Move topmove = null;
		mac = new Moves_and_Checks();
		
		//double resultValue = depth*MIN_VALUE;  
		double value = depth*MIN_VALUE;  
		
		ArrayList<Move> moves = mac.available_moves(gamePosition,gamePosition.getTurn());	
					
		//storing the movement eval values on an array, sized by moves.size()... oses einai dld kai oi
		//diathesimes kinhseis...
		double[][] movement_values = new double[moves.size()][3];
		int symmetrical_board=-1;
		if(forward_prun){
			symmetrical_board=mac.Symmetric_ForForwardPruning(gamePosition);		
		}
		
		ArrayList<Checker> b = obtainAllPlayerPieces(gamePosition,(byte) (1-myColor));	
		boolean aggressor_mode=false;
		int numof_oppeggles=b.size();
		if(numof_oppeggles>9&&aggression){
			aggressor_mode=true;
		}
		
		
		for(int i=0;i<moves.size();i++){
			
			//System.out.println("synolikes kiniseis ="+a.size()+ " kai eimai sth " + i);
			//System.out.println("xarakthristika kinhshs " + a.get(i).x_start + " "+ a.get(i).y_start + " "+ a.get(i).x_end + " "+ a.get(i).y_end);
			
			if(forward_prun){ // For the Forward Pruning process... skipping movements if the board is symmetric in some way...
				if(symmetrical_board==1){
					if (moves.get(i).x_start>3)	continue; //movement on the symmetrical down part (x-axis symmetric), so continue to the next movement...
				}else if(symmetrical_board==2){
					if (moves.get(i).y_start>3)	continue;  //movement on the symmetrical left part (y-axis symmetric), so continue to the next movement...
				}else if(symmetrical_board==3){
					if (moves.get(i).x_start+moves.get(i).y_start>7) continue;  //movement on the diagonal symmetrical part (diagonal symmetric), so continue to the next movement...
				}//else continue the search with this movement....
			}
			
	
			mac.doMove( gamePosition, moves.get(i));		
			//System.out.println("kinhsh tou pebble "+a.get(i).x_start+" ystart"+a.get(i).y_start+" xend"+a.get(i).x_end +" yend"+a.get(i).y_end );
			
			value = minValue(gamePosition,depth-1,
					Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
			
			//saving every movement cost in an array..
			movement_values[i][0]=value;
			movement_values[i][1]=i; //movement id number
						
			b = obtainAllPlayerPieces(gamePosition,(byte) (1-myColor));	
			
			if(aggressor_mode&&b.size()<numof_oppeggles) movement_values[i][2]=1;
			else movement_values[i][2]=0;
						
			mac.undoMove( gamePosition, moves.get(i));
			
			//System.out.println("res="+resultValue + " value="+value);
			
//			if (value > resultValue) {
//				bestmove = moves.get(i);
//				resultValue = value;
//			}		
			
		}
				
		movement_values=D_BubbleSort(movement_values); //the best solution is in the first slot of the array (biggest value)
		bestmove=moves.get((int) movement_values[0][1]); //best movement , in the searching depth 5 ...
		
		//for the aggression character...
		if(aggressor_mode){
			for(int i=0;i<movement_values.length;i++){
				if(movement_values[i][2]==1&&i<movement_values.length/4) {
					return moves.get((int) movement_values[i][1]);
				}
			}
		}
		
		if (bestmove == null){ //in this case , there is not any good choice, because probably every movement leads to a future loss.. so the agent chooses a random movement
			//to perform...
			//throw new Error("Couldn't find best move, cause he loses in the next round!");
			moves = mac.available_moves(gamePosition,gamePosition.getTurn());
			Random rand = new Random(System.currentTimeMillis());
			return moves.get(rand.nextInt(moves.size())); //returning the movement...
		}
		
		//for the singular extensions... the agent extends his search for the top five movements that has choosed... after a more deaply search he determines which is the best in the deep end..
		else if(enable_singular){

			double temporarybestmove = movement_values[0][1];
			for(int bi=0;bi<5;bi++){
			
				topmove=moves.get((int) movement_values[bi][1]);
				
				mac.doMove(gamePosition, topmove);
				//RESEARCHIN THE MOVE : ORISMOS https://chessprogramming.wikispaces.com/Singular+Extensions

				value = minValue(gamePosition,depth+1,
						Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

				System.out.println("Movement "+bi+ " evaluation completed with more deeply search..");
				
				//updating the movement value...
				movement_values[bi][0]=value;

				mac.undoMove( gamePosition, topmove);
				

			
			}		
			
			D_BubbleSort(movement_values);

			bestmove=moves.get((int) movement_values[0][1]);
			
			if(temporarybestmove!=movement_values[0][1]){
				System.out.println("Changed his mind about the best movement...");
			}else{
				System.out.println("Still believes that this movement is the best...");
			}
			
		}
		
		return bestmove;
	}
	
	public double maxValue(GamePosition gamePosition, int depth, double alpha, double beta) {
		
		metrics.incrementInt(METRICS_NODES_EXPANDED);
		
		if (depth==1 || mac.checkWin(gamePosition,(byte) (1-myColor)) ||  mac.checkWin(gamePosition,myColor))
			return evaluation_func(gamePosition,depth); // return the heuristic value
		
		ArrayList<Move> a = mac.available_moves(gamePosition,gamePosition.getTurn());
		
		double value = Double.NEGATIVE_INFINITY;
		int symmetrical_board=-1;
		
		if(forward_prun){
			symmetrical_board=mac.Symmetric_ForForwardPruning(gamePosition);		
		}
		
     	for(int i=0;i<a.size();i++){
			
			if(forward_prun){ // For the Forward Pruning process... skipping movements if the board is symmetric in some way...
				if(symmetrical_board==1){
					if (a.get(i).x_start>3)	continue; //movement on the symmetrical down part (x-axis symmetric), so continue to the next movement...
				}else if(symmetrical_board==2){
					if (a.get(i).y_start>3)	continue;  //movement on the symmetrical left part (y-axis symmetric), so continue to the next movement...
				}else if(symmetrical_board==3){
					if (a.get(i).x_start+a.get(i).y_start>7) continue;  //movement on the diagonal symmetrical part (diagonal symmetric), so continue to the next movement...
				}//else continue the search with this movement....
			}
			    		
     		mac.doMove( gamePosition, a.get(i));
     		
			value = Math.max(value, minValue( //
					gamePosition, depth-1, alpha, beta));
			mac.undoMove(gamePosition, a.get(i));
			if(enable_AB){
				if (value >= beta)
					return value;
				alpha = Math.max(alpha, value);
			}
		}
     	
		return value;
	}
	
	public double minValue(GamePosition gamePosition, int depth, double alpha, double beta) {
		
		metrics.incrementInt(METRICS_NODES_EXPANDED);
		
		if (depth==1 || mac.checkWin(gamePosition,(byte) (1-myColor)) ||  mac.checkWin(gamePosition,myColor))
				return evaluation_func(gamePosition,depth); // return the heuristic value
		
		double value = Double.POSITIVE_INFINITY;
		ArrayList<Move> a = mac.available_moves(gamePosition,gamePosition.getTurn());
		int symmetrical_board=-1;
		
		if(forward_prun){
			symmetrical_board=mac.Symmetric_ForForwardPruning(gamePosition);		
		}
		
		for(int i=0;i<a.size();i++){
			
			if(forward_prun){ // For the Forward Pruning process... skipping movements if the board is symmetric in some way...
				if(symmetrical_board==1){
					if (a.get(i).x_start>3)	continue; //movement on the symmetrical down part (x-axis symmetric), so continue to the next movement...
				}else if(symmetrical_board==2){
					if (a.get(i).y_start>3)	continue;  //movement on the symmetrical left part (y-axis symmetric), so continue to the next movement...
				}else if(symmetrical_board==3){
					if (a.get(i).x_start+a.get(i).y_start>7) continue;  //movement on the diagonal symmetrical part (diagonal symmetric), so continue to the next movement...
				}//else continue the search with this movement....
			}
			
			mac.doMove( gamePosition, a.get(i));
			
			value = Math.min(value, maxValue( //
					gamePosition, depth-1, alpha, beta));
			mac.undoMove(gamePosition, a.get(i));
			if(enable_AB){
				if (value <= alpha)
					return value;
				beta = Math.min(beta, value);
			}
		}
		
		return value;
	} 

//The Evaluation function, which computes the heuristic value depending in which case the
	//minimax search has reached.. (opponent winning, me winning or maximum depth searched) 
	int evaluation_func(GamePosition gamePosition,int depth) {
		
		metrics.incrementInt(METRICS_NODES_EXPANDED);
    	int value=0;
    	
    	if(mac.checkWin(gamePosition,(byte) (1-myColor))){  //opponent win
    		value = MIN_VALUE*depth; // *** depth needs to be added here, to inform us how far is the win or the lose.. I have more details in my report..
    	}else if(mac.checkWin(gamePosition,myColor)){ ////my win
    		value = MAX_VALUE*depth;
    	}else{
    		
    		// I want to intone the Board positions in which I block more than one opponent's peggle
    		//to move...
    		int bl=mac.canMove(gamePosition, (byte) (1-myColor), true);
    		
    		ArrayList<Checker> myCheckers = obtainAllPlayerPieces(gamePosition,myColor); 
        	ArrayList<Checker> opponentCheckers = obtainAllPlayerPieces(gamePosition,(byte) (1-myColor));
    		int myDist = getSumDistance_betweenCheckers(myCheckers);
    		int opDist = getSumDistance_betweenCheckers(opponentCheckers);
    		
    		//The main goal is to maximize the value 
    		//(To bring my peggles more closer to each other
    		// than opponent's, which i want to separate them off)
    		value = opDist - myDist + bl*10; 
    	}
  	
    	return value;
    }
	
    //////////////////////////FUNCTIONS//////////////////////////////////////// 
	public static double[][] D_BubbleSort( double [][] num ) //DESCEDING ORDER
	{
	     int j;
	     boolean flag = true;   // set flag to true to begin first pass
	     double temp;   //holding variable

	     while ( flag )
	     {
	            flag= false;    //set flag to false awaiting a possible swap
	            for( j=0;  j < num.length -1;  j++ )
	            {
	                   if ( num[ j ][0] < num[j+1][0] )   // change to > for ascending sort
	                   {
	                           temp = num[ j ][0];                //swap elements
	                           num[ j ][0] = num[ j+1 ][0];
	                           num[ j+1 ][0] = temp;
	                           temp = num[ j ][1];                //swap elements
	                           num[ j ][1] = num[ j+1 ][1];
	                           num[ j+1 ][1] = temp;
	                           temp = num[ j ][2];                //swap elements
	                           num[ j ][2] = num[ j+1 ][2];
	                           num[ j+1 ][2] = temp;
	                          flag = true;              //shows a swap occurred  
	                  } 
	            } 
	      }
	     return num;
	} 
	
//	public static double[][] A_BubbleSort(double[][] num) //ASCENDING ORDER
//{
//        double temp;
//        for(int i=0; i < num.length-1; i++){
// 
//            for(int j=1; j < num.length-i; j++){
//                if(num[j-1][0] > num[j][0]){
//                    temp=num[j-1][0];
//                    num[j-1][0] = num[j][0];
//                    num[j][0] = temp;
//                    temp=num[j-1][1];
//                    num[j-1][1] = num[j][1];
//                    num[j][1] = temp;
//                }
//            }
//            //System.out.println((i+1)+"th iteration result: "+Arrays.toString(arr));
//        }
//        return num;
//    }
//	
	
	ArrayList<Checker> obtainAllPlayerPieces(GamePosition gamePosition,byte player){
    	
		ArrayList<Checker> pieces = new ArrayList<Checker>();
    	for(int i=0;i<8;i++){
    		for(int j=0;j<8;j++){
    			if( gamePosition.getBoard()[i][j] == player){
    				pieces.add(new Checker(i,j,player));
    			}
    		}
    		
    	}
    	//System.out.println("Tosa " + myColor + " kai # checkers - > " + pieces.size());
		return pieces;
    }
        
    int getSumDistance_betweenCheckers(ArrayList<Checker> pieces){
    	int sumof_distances = 0;
    	for(int i = 0; i < pieces.size();i++){
    		for(int j = 0; j < i;j++){
    			// distance calculation between 2 checkers , with Pythagorean Theorem
    			// Distance = a^2 (line distance) + b^2 (row distance)
    			int a = (int) Math.pow(pieces.get(i).x-pieces.get(j).x,2);
    	    	int b = (int) Math.pow(pieces.get(i).y-pieces.get(j).y,2);
    	    	sumof_distances += Math.sqrt(a+b);
    		}
    	}
    	return sumof_distances;
    }
    
    //info for the Node creation and expansion..
	public Metrics getMetrics() {
		return metrics;
	}

}
