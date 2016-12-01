import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Starter AI bot for the Clue AI competition.
 * 
 * @author Brandon Cox
 * 		
 */
public class StarterBot {
	
	private int[][] rooms;
	private int[][] players;
	private int id;
	private String name;
	
	/**
	 * Constructs the starter bot.
	 */
	public StarterBot() {
		name = "Starter bot";
	}
	
	/**
	 * Plays the game.
	 * 
	 * @return true if win
	 */
	public boolean play() {
		Scanner scan = new Scanner(System.in);
		int roomCount = 0;
		int playerCount = 0;
		String[] roomNames = null;
		String[] charNames = null;
		String[] weaponNames = null;
		Deck knownChars = new Deck(charNames);
		Deck knownWeaps = new Deck(weaponNames);
		Deck knownRooms = new Deck(roomNames);
		int[] key = new int[3];
		boolean wasGuess = false;
		while(scan.hasNextLine()) {
			String in = scan.nextLine();
			String[] data = in.split(" ");
			if(data[0].equals("ID"))
				id = Integer.parseInt(data[1]);
			else if(data[0].equals("Board")) {
				int h = Integer.parseInt(data[1]);
				int w = Integer.parseInt(data[2]);
				rooms = new int[h][w];
				int x = 0;
				int y = 0;
				for(int i = 0; i < h; i++) {
					for(int j = 0; j < w; j++) {
						rooms[i][j] = scan.nextInt();
						if(rooms[i][j] == 1) {
							x = j;
							y = i;
						}
						if(rooms[i][j] != 0)
							roomCount++;
					}
				}
				roomNames = new String[roomCount];
				knownRooms.setNames(roomNames);
				players = new int[h][w];
				players[y][x] = (int) (Math.pow(2, playerCount) - 1);
			} else if(data[0].equals("Name")) {
				System.out.println(name);
			} else if(data[0].equals("Playercount")) {
				playerCount = Integer.parseInt(data[1]);
			} else if(data[0].equals("Roomname")) {
				int idx = Integer.parseInt(data[1]);
				String name = "";
				for(int i = 2; i < data.length; i++)
					name += data[i] + (i == data.length - 1 ? "": " ");
				roomNames[idx] = name;
			} else if(data[0].equals("Personname")) {
				int len = Integer.parseInt(data[1]);
				if(charNames == null) {
					charNames = new String[len];
					knownChars.setNames(charNames);
				}
				int idx = Integer.parseInt(data[2]);
				String name = "";
				for(int i = 3; i < data.length; i++)
					name += data[i] + (i == data.length - 1 ? "": " ");
				charNames[idx] = name;
			} else if(data[0].equals("Weaponname")) {
				int len = Integer.parseInt(data[1]);
				if(weaponNames == null) {
					weaponNames = new String[len];
					knownWeaps.setNames(weaponNames);
				}
				int idx = Integer.parseInt(data[2]);
				String name = "";
				for(int i = 3; i < data.length; i++)
					name += data[i] + (i == data.length - 1 ? "": " ");
				weaponNames[idx] = name;
			} else if(data[0].equals("Card")) {
				if(wasGuess)
					wasGuess = false;
				if(!data[1].equals("From")) {
					int type = Integer.parseInt(data[1]);
					int val = Integer.parseInt(data[2]);
					if(type == 0)
						knownChars.addKnown(val, true);
					else if(type == 1)
						knownWeaps.addKnown(val, true);
					else
						knownRooms.addKnown(val, true);
				} else {
					int type = Integer.parseInt(data[2]);
					int val = Integer.parseInt(data[3]);
					if(type == 0)
						knownChars.addKnown(val, false);
					else if(type == 1)
						knownWeaps.addKnown(val, false);
					else
						knownRooms.addKnown(val, false);
				}
			} else if(data[0].equals("Opponent")) {
				int id = Integer.parseInt(data[1]);
				int y = Integer.parseInt(data[2]);
				int x = Integer.parseInt(data[3]);
				moveToPos(id, x, y);
			} else if(data[0].equals("Move")) {
				if(wasGuess) {
					System.out.println("Accusation " + key[0] + " " + key[1] + " " + key[2]);
				} else {
					long time = Long.parseLong(data[1]);
					int range = Integer.parseInt(data[2]);
					int[] cur = getCurrentPos(id, players);
					// TODO THIS IS WHERE YOU MAKE YOUR BOT CALCULATE MOVES
					int x = (int) (Math.random() * range);
					int y = (int) (Math.random() * (range - Math.abs(x))) * (Math.random() >= 0.5 ? -1: 1) + cur[0]; // move to a random pos within range
					x = x * (Math.random() >= 0.5 ? -1: 1) + cur[1];
					if(y < 0)
						y = 0;
					else if(y > rooms.length - 1)
						y = rooms.length - 1;
					if(x < 0)
						x = 0;
					else if(x > rooms.length - 1)
						x = rooms.length - 1;
					moveToPos(id, x, y);
					if(rooms[y][x] != 0) { // make a random guess
						int r = rooms[y][x] - 1;
						int p = (int) (Math.random() * charNames.length);
						int w = (int) (Math.random() * weaponNames.length);
						System.out.println("Move Guess " + y + " " + x + " " + p + " " + w + " " + r);
						wasGuess = true;
						key[0] = p;
						key[1] = w;
						key[2] = r;
					} else {
						System.out.println("Move " + y + " " + x);
					}
				}
			} else if(data[0].equals("Disprove")) {
				int p = Integer.parseInt(data[2]);
				int w = Integer.parseInt(data[3]);
				int r = Integer.parseInt(data[4]);
				// TODO THIS IS WHERE YOU MAKE YOUR BOT DETERMINE WHICH CARD TO TELL THE ENGINE THAT IT HAS
				if(knownChars.getGuessable().contains(p))
					System.out.println(0);
				else if(knownWeaps.getGuessable().contains(w))
					System.out.println(1);
				else if(knownRooms.getGuessable().contains(r))
					System.out.println(2);
				else
					System.out.println(-1);
			} else if(data[0].equals("Win")) {
				int id = Integer.parseInt(data[1]);
				scan.close();
				return id == this.id;
			} else if(data[0].equals("Accusation")) {
				int p = Integer.parseInt(data[2]);
				int w = Integer.parseInt(data[3]);
				int r = Integer.parseInt(data[4]);
				// TODO THIS IS WHERE YOU MAKE YOUR BOT DETERMINE WHAT TO DO WHEN A FALSE ACCUSATION IS MADE
			}
		}
		scan.close();
		return false;
	}
	
	/**
	 * Moves a player to a given position.
	 * 
	 * @param id the id of the player to move
	 * @param x the x-coordinate to move the player to
	 * @param y the y-coordinate to move the player to
	 */
	public void moveToPos(int id, int x, int y) {
		int[] cur = getCurrentPos(id, players);
		players[cur[0]][cur[1]] -= Math.pow(2, id);
		players[y][x] += Math.pow(2, id);
	}
	
	/**
	 * Gets the current position of a given player
	 * 
	 * @param id the id of the player to find
	 * @param pLocs the 2D array representing the player locations
	 * @return a new int array representing the coordinates of the player [y, x]
	 */
	public static int[] getCurrentPos(int id, int[][] pLocs) {
		int curX = 0;
		int curY = 0;
		for(int i = 0; i < pLocs.length; i++) {
			for(int j = 0; j < pLocs.length; j++) {
				String b = Integer.toBinaryString(pLocs[i][j]);
				if(b.length() > id && b.charAt(b.length() - id - 1) == '1') {
					curX = j;
					curY = i;
					break;
				}
			}
		}
		return new int[] { curY, curX };
	}
	
	/**
	 * Main method.
	 * 
	 * @param args unused command line arguments
	 */
	public static void main(String[] args) {
		if(args.length > 0) {
			try {
				System.setErr(new PrintStream(args[0])); // Use the System.err printstream for debugging since the engine captures System.out.
			} catch(FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		StarterBot bot = new StarterBot();
		if(bot.play()) {
			System.err.println("WIN");
		} else {
			System.err.println("LOSE");
		}
	}
}
