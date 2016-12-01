
import java.util.ArrayList;

/**
 * Class used to keep track of the cards in a player's possession
 * 
 * @author Brandon Cox
 * 		
 */
public class Deck {
	
	private String[] names;
	private ArrayList<Integer> known;
	private ArrayList<Integer> guessable;
	
	/**
	 * Constructs the Deck object
	 * 
	 * @param names the names of the cards in this deck
	 */
	public Deck(String[] names) {
		this.names = names;
		this.known = new ArrayList<Integer>();
		this.guessable = new ArrayList<Integer>();
	}
	
	/**
	 * Adds a known value to the list of known cards
	 * 
	 * @param val the value to add
	 * @param guess true if val should be added to the guessable ArrayList
	 */
	public void addKnown(int val, boolean guess) {
		known.add(val);
		if(guess)
			guessable.add(val);
	}
	
	/**
	 * Gets a list of the known cards
	 * 
	 * @return a list of the known cards
	 */
	public ArrayList<Integer> getKnown() {
		return known;
	}
	
	/**
	 * Gets a list of the guessable cards
	 * 
	 * @return a list of the guessable cards
	 */
	public ArrayList<Integer> getGuessable() {
		return guessable;
	}
	
	/**
	 * Sets the names of the cards
	 * 
	 * @param names the names of the cards
	 */
	public void setNames(String[] names) {
		this.names = names;
	}
	
	/**
	 * Gets a list of the names of the cards
	 * 
	 * @return the names of the cards
	 */
	public String[] getNames() {
		return names;
	}
	
	public String toString() {
		return known.toString();
	}
}
