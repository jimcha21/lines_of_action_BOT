// Lines of Action BOT
// Chatziparaschis Dimitris
// AM 2011030039
//

package TucLoA;

import TucLoA.Metrics;

/**
 * Variant of the search interface. Since players can only control the next
 * move, method <code>makeDecision</code> returns only one action, not a
 * sequence of actions.
 * 
 * @author Ruediger Lunde
 */
public interface AdversarialSearch<STATE, ACTION> {

	/** Returns the action which appears to be the best at the given state. */
	//ACTION makeDecision(STATE state);

	/**
	 * Returns all the metrics of the search.
	 * 
	 * @return all the metrics of the search.
	 */
	Metrics getMetrics();

	Move makeDecision(GamePosition gamePosition, byte myColor, int depth, boolean enable_AB, boolean enable_singular,boolean forward_prun,boolean aggression);
}
