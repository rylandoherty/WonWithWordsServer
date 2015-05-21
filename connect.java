import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

public class connect extends Thread {
	static ServerSocket s;
	Socket sc = null;
	ObjectOutputStream out;
	ObjectInputStream in;
	static String[] message = new String[50];
	String filename = "";
	Object[] inc = new Object[0];
	Object[] delivery = new Object[0];
	static boolean debugging = true;
	static int[] score = { 2, 2, 2, 2, 2, 3, 2, 3, 2, 4, 3, 2, 2, 2, 2, 2, 5,
			2, 2, 2, 2, 2, 2, 4, 3, 5 };
	int wordcount = 0, areIn = 0, hasLeft = 0, areKnown = 0, pos = 0;
	static String target = "", word = "", friend = "", knowNot = "", know = "",
			unknow = "", repeatOrder = "", repeatCode = "", user = "",
			gameNum = "", gameType = "";
	final static int _portNumber = 54433; // Arbitrary port number

	connect() {

	}

	public static void main(String[] args) throws IOException {
		/*
		 * if(array.length==0) System.out.print("List empty"); for(int i =
		 * 0;i<=array.length-1;i++){ Object[] details = (Object[]) array[i];
		 * for(int b=0;details.length-1>=b;b++){ System.out.print(details[0]); }
		 * System.out.print('\n'); }
		 */
		/*
		 * int abc = 97;
		 * 
		 * String filename = "c:\\test\\"+ "dic.txt"; Object[] ll =
		 * readarray(filename); Object[] details; String word; int scoreT = 0;
		 * for(int i = 0; i<ll.length;i++){
		 * 
		 * details = (Object[]) ll[i]; word = (String) details[0]; scoreT = 0;
		 * for(int a = 0;a<word.length();a++){ final char Letter;
		 * 
		 * Letter = (char) abc; //System.out.print(word.charAt(a) +" " +scoreT+
		 * " "+score[word.charAt(a)-97]);
		 * 
		 * scoreT = scoreT+score[word.charAt(a)-97] ; } System.out.println("");
		 * 
		 * replaceLine(filename,i,details[0]+"~"+scoreT+"~"); }
		 */

		/*
		 * int abc = 97;
		 * 
		 * String filename = "c:\\test\\"+ "dic.txt"; Object[] ll =
		 * readarray(filename); Object[] details;
		 * 
		 * for(int i = 0; i<ll.length;i++){ details = (Object[]) ll[i]; String
		 * word = (String) details[0]; int scoreT = 0; for(int a =
		 * 0;a<word.length();a++){ final char Letter;
		 * 
		 * Letter = (char) abc; scoreT = scoreT+score[word.charAt(a)-97]; }
		 * replaceLine(filename,i,details[0]+"~"+scoreT+"~"); }
		 */

		try {
			new connect().startServer();
		} catch (Exception e) {
			System.out.println("I/O failure: " + e.getMessage());
			e.printStackTrace();
		}

	}

	public void startServer() throws Exception {
		ServerSocket s = null;
		boolean listening = true;

		try {
			s = new ServerSocket(_portNumber);
		} catch (IOException e) {
			System.err.println("Could not listen on port: " + _portNumber);
			System.exit(-1);
		}

		while (listening) {
			handleClientRequest(s);
		}

		s.close();
	}

	private void handleClientRequest(ServerSocket serverSocket) {
		try {
			new ConnectionRequestHandler(serverSocket.accept()).run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public class ConnectionRequestHandler implements Runnable {
		private Socket sc = null;

		public ConnectionRequestHandler(Socket socket) {
			sc = socket;
		}

		public void run() {
			System.out.println("Client connected to socket: " + sc.toString());
			try {
				out = new ObjectOutputStream(sc.getOutputStream());
				out.flush();
				in = new ObjectInputStream(sc.getInputStream());
				message = (String[]) in.readObject();

				for (int x = 0; x < message.length; x++) {
					System.out.print(message[x] + "\n");
				}
				if (message.length == 1) {
					login();
				} else if (message[1].equals("friendlist")) {
					friendList();
				} else if (message[1].equals("addfriend")) {
					addFriend();
				} else if (message[1].equals("acceptFriend")) {
					acceptFriend();
				} else if (message[1].equals("rejectFriend")) {
					rejectFriend();
				} else if (message[1].equals("clearReject")) {
					clearReject();
				} else if (message[1].equals("clearRejectGame")) {
					clearReject();
				} else if (message[1].equals("createGame")) {
					createGame();
				} else if (message[1].equals("inviteFriend")) {
					inviteFriend();
				} else if (message[1].equals("rejectGame")) {
					rejectGame();
				} else if (message[1].equals("enterGuess")) {
					enterGuess();

				} else if (message[1].equals("enterTarget")) {
					enterTarget();

				} else if (message[1].equals("loadGame")) {
					loadGame();

				} else if (message[1].equals("acceptRematch")) {
					acceptRematch();

				} else if (message[1].equals("rejectRematch")) {
					rejectRematch();
				} else if (message[1].equals("pickIcon")) {
					pickIcon();
				} else if (message[1].equals("loadLadder")) {
					getLadder();
				} else if (message[1].equals("enterLadder")) {
					getLadder();
				} else if (message[1].equals("enterLadderGuess")) {
					enterLadderGuess();
				} else {

				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.out.println(e1.getMessage());
				e1.printStackTrace();
			} catch (ClassNotFoundException e) {
				System.out.println(e.getMessage());
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally { // In case anything goes wrong we need to close our I/O
						// streams and sockets.
				try {
					delivery = new Object[0];
					out.close();
					in.close();
					sc.close();
				} catch (Exception e) {
					System.out.println("Couldn't close I/O streams");
				}

			}
		}
	}

	// Incoming commands

	public void login() throws IOException {
		filename = "c:\\test\\" + "login" + ".txt";
		if (findString(filename, message[0], 0) == true) {
			getGameList();
			getProfile();
		} else {
			addUser();
			getGameList();
			getProfile();
		}
	}

	public void getProfile() throws IOException {
		String filename = "c:\\test\\profiles\\" + message[0] + ".txt";
		System.out.print("PROFILE -- ");
		sendMessage(getLine(filename, 0));

		Object[] ext = getLine(filename, 1);
		ext = expand(ext);
		filename = "c:\\test\\login.txt";
		ext[ext.length - 1] = getSector(filename,
				findStringLoc(filename, message[0], 0), 1);
		sendMessage(ext);
		ext[ext.length - 1] = getSector(filename,
				findStringLoc(filename, message[0], 0), 2);
		sendMessage(ext);
		filename = "c:\\test\\wordfreq\\" + message[0] + ".txt";
		Object[] freqtemp = readarray(filename);
		int freqlen = 10;
		if (freqtemp.length < 10) {
			freqlen = freqtemp.length;
		}
		Object[] wordFreq = new Object[freqlen];
		for (int a = 0; a < freqlen; a++) {
			wordFreq[a] = getLine(filename, a);
		}
		sendMessage(wordFreq);
		
	}
	public void sortWordFreq() throws IOException {
		String filename = "c:\\test\\wordfreq\\" + message[0] + ".txt";
		Object[] freqtemp = readarray(filename);
		Object[] details = (Object[]) freqtemp[0];
		double score = 0;
		double tempScore = 0;
		int count = 0;
		for (int a = 0; a < freqtemp.length; a++) {
			freqtemp = readarray(filename);
			tempScore = Double.parseDouble((String) details[1]);
			details = (Object[]) freqtemp[a];

			score = Double.parseDouble((String) details[1]);

			if (score > tempScore) {

				while (score > tempScore) {
					count++;
					if (a - count == 0) {

						break;
					}

					details = (Object[]) freqtemp[a - count - 1];
					tempScore = Double.parseDouble((String) details[1]);

				}
				details = (Object[]) freqtemp[a];

				insertLine(filename, a - count, details[0] + "~" + details[1]
						+ "~");
				deleteLine(filename, a + 1);

				count = 0;
				details = (Object[]) freqtemp[a - 1];
				tempScore = Double.parseDouble((String) details[1]);

			}
		}
		freqtemp = readarray(filename);
		for (int i = 0; i < freqtemp.length; i++) {
			replaceSector(filename, i, 2, (i + 1) + "");
		}
	}

	// Game Commands

	public void rejectGame() throws IOException {
		filename = "c:\\test\\messages\\" + message[0] + ".txt";
		int loc = findStringLoc(filename, message[2], 2, "game", 0);

		
		deleteLine(filename, loc);
		filename = "c:\\test\\messages\\" + message[2] + ".txt";
		loc = findStringLoc(filename, message[0], 2, "game", 0);
	
		replaceSector(filename, loc, 1, "rejected");
	}

	public void createGame() throws IOException {
		int gamenum = 0;
		// Get Game Number and update
		if (!message[6].equals("ladder")) {
			filename = "c:\\test\\servergames\\" + "config" + ".txt";

		} else {
			filename = "c:\\test\\laddergames\\" + "config" + ".txt";
		}
		java.io.File file = new java.io.File(filename);
		Scanner inFile = new Scanner(file);
		String line;
		line = inFile.nextLine();
		gamenum = Integer.parseInt(line);
		gamenum = gamenum + 1;
		createFile(filename, gamenum + "");
		inFile.close();

		// System.out.println(message);

		if (!message[6].equals("ladder")) {
			filename = "c:\\test\\usergames\\" + message[0] + ".txt";
			addToFile(filename, gamenum + "~");
			filename = "c:\\test\\usergames\\" + message[2] + ".txt";
			addToFile(filename, gamenum + "~");
		} else {
			filename = "c:\\test\\ladder\\" + message[0] + ".txt";
			addToFile(filename, gamenum + "~0~nothing~0~");
		}
		Random generator = new Random();
		int roll;
		filename = "c:\\test\\dic.txt";
		file = new java.io.File(filename);
		inFile = new Scanner(file);
		int count = 0;
		boolean five = true;
		while (five){
			
			generator = new Random();
			roll = generator.nextInt(6915) + 1;
			five = false;
			count = 0;
			inFile.reset();
			inFile = new Scanner(file);
			System.out.println(roll+"");
		while (inFile.hasNext()) {
			line = inFile.next();
			count++;
			
			if (count == roll) {
				for (int i = 0; i < 5; i++) {
						char x = line.charAt(i);
						System.out.println(x+"");
						for (int a = 0; a < 5; a++) {
							if (i == a)
								continue;
							else if (x == line.charAt(a)) {
								System.out.println("repeat " + line);
								five = true;
								
							}
						}

					}
				
				
				break;
				}
				
			}
		
		}
		inFile.close();
		
		
		
		
		Date date = new Date();
		String name = new SimpleDateFormat("ddMMyyyyHHmm").format(date);

		if (!message[6].equals("ladder")) {
			filename = "c:\\test\\servergames\\" + gamenum + ".txt";
			// Game Type User 1 User 2 Turn Status Status 2 Last Acted
			addToFile(filename, message[3] + "~" + message[0] + "~"
					+ message[2] + "~0~0~0~" + name + "~" + gamenum + "~0~");
		} else {
			filename = "c:\\test\\laddergames\\" + gamenum + ".txt";
			// Game Type User 1 User 2 Turn Status Status 2 Last Acted
			addToFile(filename, "ladder~" + message[0] + "~0~" + name
					+ "~" + gamenum + "~");
		}
		// Target Word
		if (message[3].equals("comp") || message[6].equals("ladder"))
			addToFile(filename, line);
		else if (message[3].equals("versus") || message[3].equals("mastermind")) {
			addToFile(filename, "nothing~nothing~");

		}

		// Extra ingame info

		// now i know my ABC's
		addToFile(filename, message[0] + "~abcdefghijklmnopqrstuvwxyz~0~0~");
		if (!message[6].equals("ladder")) {
			addToFile(filename, message[2] + "~abcdefghijklmnopqrstuvwxyz~0~0~");
		}

		if (!message[6].equals("ladder")) {
			filename = "c:\\test\\messages\\" + message[0] + ".txt";
			deleteLine(filename,
					findStringLoc(filename, message[2], 2, "game", 0));
			filename = "c:\\test\\messages\\" + message[2] + ".txt";
			deleteLine(filename,
					findStringLoc(filename, message[0], 2, "game", 0));
		}
	}

	public void getGameList() throws IOException {
		Object[] gameStats = new Object[0];
		System.out.println("getting game list");
		Object[] array = new Object[0];
		Object[] details = new Object[0];
		int count = 0;
		String filename = "c:\\test\\messages\\" + message[0] + ".txt";
		array = readarray(filename);
		System.out.println(array.length);
		System.out.println(fileEmpty(filename));

		while (count < array.length) {

			details = (Object[]) array[count];

			if (details[0].toString().equals("game")) {
				if (details[1].equals("pending")
						|| details[1].equals("incoming")
						|| details[1].equals("rejected")) {
					delivery = expand(delivery);
					delivery[delivery.length - 1] = details;
				}

			}
			count++;
		}
		filename = "c:\\test\\usergames\\" + message[0] + ".txt";
		String serverGames = "";
		array = readarray(filename);
		String friendProf;
		count = 0;
		while (count < array.length) {
			details = (Object[]) array[count];

			delivery = expand(delivery);
			delivery[delivery.length - 1] = getGameListInfo(details[0]
					.toString());
			friendProf = "c:\\test\\friendfiles\\" + message[0] + ".txt";
			serverGames = "c:\\test\\servergames\\" + details[0] + ".txt";
			if (getSector(serverGames, 0, 1).equals(message[0])) {
				gameStats = expand(gameStats);
				Object[] temp = getLine(
						friendProf,
						findStringLoc(friendProf, getSector(serverGames, 0, 2),
								0));
				temp = expand(temp);
				temp[temp.length - 1] = getSector("c:\\test\\profiles\\"
						+ getSector(serverGames, 0, 2) + ".txt", 0, 5);
				gameStats[gameStats.length - 1] = temp;
				System.out.println(temp[4]);
			} else {
				gameStats = expand(gameStats);
				Object[] temp = getLine(
						friendProf,
						findStringLoc(friendProf, getSector(serverGames, 0, 1),
								0));
				temp = expand(temp);
				temp[temp.length - 1] = getSector("c:\\test\\profiles\\"
						+ getSector(serverGames, 0, 1) + ".txt", 0, 5);
				gameStats[gameStats.length - 1] = temp;
				System.out.println(temp[4]);
			}

			count++;
		}

		System.out.println(delivery.length);
		if (delivery.length == 0) {
			delivery = expand(delivery);
			delivery[0] = "0";
			gameStats = expand(gameStats);
			gameStats[0] = "0";
		}
		sendMessage(delivery);
		sendMessage(gameStats);
	}

	public void loadLadder(String gameNum) throws FileNotFoundException {

		Object[] array = new Object[0];
		Object[] details = new Object[0];
		Object[] transfer = new Object[0];

		filename = "c:\\test\\laddergames\\" + gameNum + ".txt";
		array = readarray(filename);

		int count = 0;
		int tcount = 0;

		details = (Object[]) array[count];

		String gametype = details[0].toString();

		transfer = expand(transfer);
		transfer[tcount] = details;
		tcount++;

		String user = message[0];
		count++;
		count++;
		details = (Object[]) array[count];
		transfer = expand(transfer);
		transfer[tcount] = details;
		tcount++;
		count++;

		while (count < array.length) {

			details = (Object[]) array[count];
			transfer = expand(transfer);
			transfer[tcount] = details;
			tcount++;
			count++;

		}

		sendMessage(transfer);
		returnLadderInfo();
	}

	public void returnLadderInfo() throws FileNotFoundException{
		filename = "c:\\test\\ladder\\" + message[0] + ".txt";
		Object[] array =(Object[])  readarray(filename);
		Object[] details;
		Object[] transfer = new Object[0];
		Object[] addIn;
		int x = array.length;
		if(x<6){
			
		}
		else
			x=6;
		for(int a = array.length-x;a<array.length-1;a++){
			
			details = (Object[]) array[a];
			System.out.println("array info "+ details[2]);
			transfer = expand(transfer);
			transfer[transfer.length-1]=(Object[]) array[a];
		}
		sendMessage(transfer);
		filename = "c:\\test\\login.txt";
		array = readarray(filename);
		sendMessage(array);
		
	}
	
	public void loadGame() throws FileNotFoundException {

		Object[] array = new Object[0];
		Object[] details = new Object[0];
		Object[] transfer = new Object[0];

		filename = "c:\\test\\servergames\\" + message[2] + ".txt";
		array = readarray(filename);

		int count = 0;
		int tcount = 0;

		details = (Object[]) array[count];

		String gametype = details[0].toString();
		String user = "";
		String other = "";
		transfer = expand(transfer);
		transfer[tcount] = details;
		tcount++;
		int pos = 0;
		if (details[1].toString().equals(message[0])) {
			user = message[0];
			other = details[2].toString();
			pos = 1;
		} else {
			user = details[2].toString();
			other = details[1].toString();
			pos = 2;
		}
		count++;

		while (count < array.length) {

			details = (Object[]) array[count];

			if (gametype.equals("comp")) {
				if (count == 1) {

				} else if (details[0].toString().equals(user)) {

					transfer = expand(transfer);
					transfer[tcount] = details;

					tcount++;
				} else if (details[0].toString().equals(other) && count > 3) {

					transfer = expand(transfer);
					Object[] shortcut = new Object[2];
					shortcut[0] = details[0];
					shortcut[1] = details[1];
					transfer[tcount] = shortcut;

					tcount++;
				}
				count++;
			}

			else if (gametype.equals("versus") || gametype.equals("mastermind")) {

				if (count == 1) {
					transfer = expand(transfer);
					if (pos == 1) {
						Object[] shortcut = new Object[2];
						shortcut[0] = details[0];
						transfer[tcount] = shortcut;
					} else if (pos == 2) {
						Object[] shortcut = new Object[2];
						shortcut[0] = details[1];
						transfer[tcount] = shortcut;
					}
					count++;
					tcount++;
				} else if (count == 2) {
					transfer = expand(transfer);
					if (pos == 1) {
						transfer[tcount] = details;
						count++;
						tcount++;
						details = expand(details);
						details = (Object[]) array[count];
						transfer = expand(transfer);
						transfer[tcount] = details;
						count++;
						tcount++;
					} else if (pos == 2) {
						count++;
						details = expand(details);
						details = (Object[]) array[count];
						transfer[tcount] = details;
						tcount++;
						details = (Object[]) array[count - 1];
						transfer = expand(transfer);
						transfer[tcount] = details;
						count++;
						tcount++;
					}
				}

				else if (details[0].toString().equals(user)) {

					transfer = expand(transfer);
					transfer[tcount] = details;
					count++;
					tcount++;
				} else if (details[0].toString().equals(other) && count > 3) {

					transfer = expand(transfer);
					transfer[tcount] = details;
					count++;
					tcount++;
				}
			}
		}

		sendMessage(transfer);
	}

	public void printFile(String file) throws FileNotFoundException {

		java.io.File filex = new java.io.File(file);
		Scanner inFilex = new Scanner(filex);

		String line;
		int count = 0;
		String editLine = "";
		while (inFilex.hasNext()) {
			line = inFilex.nextLine();
			editLine = "";

			count++;

			int count1 = 0;
			int count2 = 0;
			while (count1 < line.length()) {

				if (line.substring(count1, count1 + 1).equals("~")) {
					String word = line.substring(0, count1);
					System.out.println(word);

					line = line.substring(count1 + 1, line.length());
					count1 = 0;
					count2++;

				}
				count1++;
			}

		}
		inFilex.close();
	}

	public void printArray(Object[] msg) {

		for (int a = 0; a < msg.length; a++) {
			Object[] details = (Object[]) msg[a];
			for (int b = 0; b < details.length; b++) {
				System.out.print(b + "--" + details[b] + "    ");
			}
			System.out.println("");
		}
	}

	// Support

	public Object getGameListInfo(String gamenum) throws FileNotFoundException {
		filename = "c:\\test\\servergames\\" + gamenum + ".txt";
		Object[] array = readarray(filename);
		Object[] details = (Object[]) array[0];
		return details;
	}

	// Friend List

	public void inviteFriend() throws IOException {
		filename = "c:\\test\\messages\\" + message[0] + ".txt";
		delivery = expand(delivery);
		if (!findString(filename, message[2], 2)) {
			filename = "c:\\test\\messages\\" + message[0] + ".txt";
			addToFile(filename, "game~pending~" + message[2] + "~" + message[3]
					+ "~");
			filename = "c:\\test\\messages\\" + message[2] + ".txt";
			addToFile(filename, "game~incoming~" + message[0] + "~"
					+ message[3] + "~");
			delivery[0] = "1";
		} else {
			delivery[0] = "0";
		}
		sendMessage(delivery);
	}

	public void acceptFriend() throws IOException {
		// delivery = expand(delivery);
		filename = "c:\\test\\messages\\" + message[0] + ".txt";
		int loc = findStringLoc(filename, message[2], 2);
		if (debugging)
			System.out.println(loc);
		deleteLine(filename, loc);
		filename = "c:\\test\\messages\\" + message[2] + ".txt";
		loc = findStringLoc(filename, message[0], 2);
		if (debugging)
			System.out.println(loc);
		deleteLine(filename, loc);
		filename = "c:\\test\\friendfiles\\" + message[2] + ".txt";
		addToFile(filename, message[0] + "~0~0~0~0~0~");
		filename = "c:\\test\\friendfiles\\" + message[0] + ".txt";
		addToFile(filename, message[2] + "~0~0~0~0~0~");
	}

	public void rejectFriend() throws IOException {
		filename = "c:\\test\\messages\\" + message[0] + ".txt";
		int loc = findStringLoc(filename, message[2], 2, "friend", 0);
		if (debugging)
			System.out.println(loc);
		deleteLine(filename, loc);
		filename = "c:\\test\\messages\\" + message[2] + ".txt";
		loc = findStringLoc(filename, message[0], 2, "friend", 0);
		if (debugging)
			System.out.println(loc);
		replaceSector(filename, loc, 1, "rejected");
	}

	public void clearReject() throws IOException {
		filename = "c:\\test\\messages\\" + message[0] + ".txt";
		int loc = 0;

		if (message[1].equals("clearRejectGame")) {
			loc = findStringLoc(filename, message[2], 2, "game", 0);
		} else if (message[1].equals("clearReject")) {
			loc = findStringLoc(filename, message[2], 2, "friend", 0);
		}
		if (debugging)
			System.out.println(loc);
		deleteLine(filename, loc);
	}

	public void addFriend() throws IOException {
		delivery = expand(delivery);
		filename = "c:\\test\\" + "login" + ".txt";
		if (findString(filename, message[2], 0)) {
			filename = "c:\\test\\messages\\" + message[0] + ".txt";

			if (!findString(filename, message[2], 2)) {
				filename = "c:\\test\\friendfiles\\" + message[0] + ".txt";
				if (!findString(filename, message[2], 0)) {
					filename = "c:\\test\\messages\\" + message[0] + ".txt";
					addToFile(filename, "friend~pending~" + message[2] + "~");
					filename = "c:\\test\\messages\\" + message[2] + ".txt";
					addToFile(filename, "friend~incoming~" + message[0] + "~");
					delivery[0] = "requestsent";
				} else
					delivery[0] = "Friend already on your list";
			} else {
				Object[] reader = readarray(filename);
				int count = 0;
				while (count < reader.length) {
					Object[] details = (Object[]) reader[count];
					if (details[2].equals(message[2])
							&& details[1].equals("pending")) {
						delivery[0] = "User has been invited already";
					} else if (details[2].equals(message[2])
							&& details[1].equals("incoming")) {
						delivery[0] = "User has invited you already";

					}
					count++;
				}

			}
		}

		else
			delivery[0] = "User not found";

		System.out.println(delivery[0]);
		sendMessage(delivery);

	}

	public void friendList() throws FileNotFoundException {
		System.out.println("getting friend list");
		Object[] array = new Object[0];
		Object[] details;
		int count = 0;
		String filename = "c:\\test\\messages\\" + message[0] + ".txt";
		boolean bb = fileEmpty(filename);
		array = readarray(filename);

		while (count < array.length) {

			details = (Object[]) array[count];
			System.out.println(details[0]);
			if (details[0].equals("friend")) {
				if (details[1].equals("pending")
						|| details[1].equals("incoming")
						|| details[1].equals("rejected")) {
					delivery = expand(delivery);
					delivery[delivery.length - 1] = details;
				}

			}
			count++;
		}

		filename = "c:\\test\\friendfiles\\" + message[0] + ".txt";
		array = readarray(filename);
		count = 0;
		while (count < array.length) {
			details = (Object[]) array[count];

			System.out.println(details[0]);
			delivery = expand(delivery);
			delivery[delivery.length - 1] = details;
			count++;

		}

		sendMessage(delivery);

	}

	// Save Stats // Profile
	public void recordStats() throws IOException {

		recordRecord();

	}

	public void pickIcon() throws IOException {
		filename = "c:\\test\\profiles\\" + message[0] + ".txt";
		replaceSector(filename, 0, 5, message[2]);
	}

	public void recordWordsUsed() throws NumberFormatException, IOException {
		user = message[0];
		word = message[3];
		String game = "c:\\test\\wordfreq\\" + user + ".txt";
		int loc = findStringLoc(game, word, 0);
		if (loc > 0) {
			int x = Integer.parseInt(getSector(game, loc, 1)) + 1;
			replaceSector(game, loc, 1, x + "");
		} else {
			addToFile(game, word + "~1~");
		}

	}

	public void recordRecord() throws IOException {
		user = message[0];
		gameNum = message[2];
		word = message[3];
		String game = "c:\\test\\servergames\\" + gameNum + ".txt";
		Object[] array = (Object[]) readarray(game);
		Object[] reader = (Object[]) array[0];
		getPos(game);
		String other;
		int turns, turnsOth;

		if (pos == 1) {
			turns = Integer.parseInt(reader[3].toString());
			turnsOth = Integer.parseInt(reader[8].toString());
			other = reader[2].toString();
		} else {
			turns = Integer.parseInt(reader[8].toString());
			turnsOth = Integer.parseInt(reader[3].toString());
			other = reader[1].toString();
		}
		String userProf = "c:\\test\\profiles\\" + user + ".txt";
		String otherProf = "c:\\test\\profiles\\" + other + ".txt";
		String friendFile = "c:\\test\\friendfiles\\" + user + ".txt";
		String otherFriendFile = "c:\\test\\friendfiles\\" + other + ".txt";
		addNumberToSector(userProf, 0, 3, turns);
		addNumberToSector(userProf, 0, 4, turnsOth);
		addNumberToSector(otherProf, 0, 4, turns);
		addNumberToSector(otherProf, 0, 3, turnsOth);
		addNumberToSector(friendFile, 0, 4, turns);
		addNumberToSector(friendFile, 0, 5, turnsOth);
		addNumberToSector(otherFriendFile, 0, 5, turns);
		addNumberToSector(otherFriendFile, 0, 4, turnsOth);

		if (turns > turnsOth) {

			addNumberToSector(userProf, 0, 1, 1);
			addNumberToSector(otherProf, 0, 0, 1);

			addNumberToSector(friendFile, findStringLoc(friendFile, other, 0),
					2, 1);
			addNumberToSector(friendFile,
					findStringLoc(otherFriendFile, user, 0), 1, 1);

		} else if (turns < turnsOth) {
			addNumberToSector(userProf, 0, 0, 1);
			addNumberToSector(otherProf, 0, 1, 1);
			addNumberToSector(friendFile, findStringLoc(friendFile, other, 0),
					1, 1);
			addNumberToSector(friendFile,
					findStringLoc(otherFriendFile, user, 0), 2, 1);
		} else if (turns == turnsOth) {
			addNumberToSector(userProf, 0, 2, 1);
			addNumberToSector(otherProf, 0, 2, 1);
			addNumberToSector(friendFile, findStringLoc(friendFile, other, 0),
					3, 1);
			addNumberToSector(friendFile,
					findStringLoc(otherFriendFile, user, 0), 3, 1);
		}
	}

	// Ladder Settings

	public void getLadder() throws FileNotFoundException {
		filename = "c:\\test\\ladder\\" + message[0] + ".txt";
		Object[] reader = (Object[]) readarray(filename);
		Object[] details;
		details = (Object[]) reader[reader.length - 1];
		loadLadder((String) details[0]);

	}


	public void sortLadder() throws IOException {
		String ladderFile = "c:\\test\\login.txt";
		Object[] read = readarray(ladderFile);
		Object[] details = (Object[]) read[0];
		double score = 0;
		double tempScore = 0;
		int count = 0;
		for (int a = 0; a < read.length; a++) {
			read = readarray(ladderFile);
			tempScore = Double.parseDouble((String) details[1]);
			details = (Object[]) read[a];
			
			score = Double.parseDouble((String) details[1]);

			if (score < tempScore) {

				while (score < tempScore) {
					count++;
					if (a - count == 0) {

						break;
					}

					details = (Object[]) read[a - count - 1];
					tempScore = Double.parseDouble((String) details[1]);

				}
				details = (Object[]) read[a];

				insertLine(ladderFile, a - count, details[0] + "~" + details[1]
						+ "~" + details[2] + "~");
				deleteLine(ladderFile, a + 1);

				count = 0;
				details = (Object[]) read[a - 1];
				tempScore = Double.parseDouble((String) details[1]);

			}
		}
		read = readarray(ladderFile);
		for(int i = 0; i<read.length;i++){
			replaceSector(ladderFile,i,2,(i+1)+"");
		}
	}

	// File CommandsFile Commands File Commands File Commands

	public void insertLine(String file, int line, String oldLine)
			throws IOException {

		java.io.File filex = new java.io.File(file);
		Scanner inFilex = new Scanner(filex);
		File tempx = File.createTempFile("temp", ".txt", filex.getParentFile());
		FileWriter textx = new FileWriter(tempx, true);
		BufferedWriter outTextx = new BufferedWriter(textx);
		String linein;
		int count = 0;
		while (inFilex.hasNext()) {

			if (count != line) {
				linein = inFilex.nextLine();
				outTextx.write(linein);
				outTextx.newLine();
				count++;
			} else {
				outTextx.write(oldLine);
				outTextx.newLine();
				count++;

			}
		}
		inFilex.close();
		outTextx.close();
		textx.close();
		filex.delete();
		tempx.renameTo(filex);
	}

	public void addNumberToSector(String file, int line, int loc, int quant)
			throws IOException {
		int x = Integer.parseInt(getSector(file, line, loc)) + quant;
		replaceSector(file, line, loc, x + "");
	}

	public boolean fileEmpty(String file) throws FileNotFoundException {
		Object[] array = readarray(file);
		System.out.println(array.length);
		if (array.length == 0)
			return true;

		else
			return false;

	}

	public boolean findString(String file, String string, int loc)
			throws FileNotFoundException {

		Object[] array = readarray(file);
		if (array.length == 0) {
			return false;
		}
		Object[] details = (Object[]) array[0];
		if (details.length == 0) {
			return false;
		}

		for (int i = 0; i <= array.length - 1; i++) {
			details = (Object[]) array[i];

			if (details[loc].equals(string)) {
				if (debugging)
					System.out.println(details[loc] + " String found");
				return true;
			}

		}
		return false;
	}

	public String getSector(String file, int lineN, int loc) throws IOException {

		java.io.File filex = new java.io.File(file);
		Scanner inFilex = new Scanner(filex);

		String line;
		int count = 0;
		String editLine = "";
		while (inFilex.hasNext()) {
			line = inFilex.nextLine();
			editLine = "";
			if (count != lineN) {
				count++;

			} else {

				int count1 = 0;
				int count2 = 0;
				while (count1 < line.length()) {

					if (line.substring(count1, count1 + 1).equals("~")) {
						if (count2 != loc) {

							line = line.substring(count1 + 1, line.length());
							count1 = 0;
							count2++;
						} else {
							String word = line.substring(0, count1);
							inFilex.close();
							return word;

						}
					}

					count1++;

				}

			}

		}
		inFilex.close();

		return null;
	}

	public Object[] getLine(String file, int lineN) throws IOException {

		java.io.File filex = new java.io.File(file);
		Scanner inFilex = new Scanner(filex);
		Object[] bb = (Object[]) readarray(file);
		Object[] ret = (Object[]) bb[lineN];
		inFilex.close();

		return ret;
	}

	public void replaceLine(String file, int lineN, String newLine)
			throws IOException {

		java.io.File filex = new java.io.File(file);
		Scanner inFilex = new Scanner(filex);
		File tempx = File.createTempFile("temp", ".txt", filex.getParentFile());
		FileWriter textx = new FileWriter(tempx, true);
		BufferedWriter outTextx = new BufferedWriter(textx);
		String line;
		int count = 0;
		while (inFilex.hasNext()) {
			line = inFilex.nextLine();

			if (count != lineN) {

				outTextx.write(line);
				outTextx.newLine();
				count++;
			} else {
				outTextx.write(newLine);
				outTextx.newLine();
				count++;
			}
		}
		inFilex.close();
		outTextx.close();
		textx.close();
		filex.delete();
		tempx.renameTo(filex);
	}

	public void replaceSector(String file, int lineN, int loc, String newLine)
			throws IOException {

		java.io.File filex = new java.io.File(file);
		Scanner inFilex = new Scanner(filex);
		File tempx = File.createTempFile("temp", ".txt", filex.getParentFile());
		FileWriter textx = new FileWriter(tempx, true);
		BufferedWriter outTextx = new BufferedWriter(textx);
		String line;
		int count = 0;
		String editLine = "";
		while (inFilex.hasNext()) {
			line = inFilex.nextLine();
			editLine = "";
			if (count != lineN) {

				outTextx.write(line);
				outTextx.newLine();
				count++;
			} else {

				int count1 = 0;
				int count2 = 0;
				while (count1 < line.length()) {

					if (line.substring(count1, count1 + 1).equals("~")) {
						if (count2 != loc) {

							String word = line.substring(0, count1);

							editLine = editLine + word + "~";
							line = line.substring(count1 + 1, line.length());
							count1 = 0;
							count2++;
						} else {
							editLine = editLine + newLine + "~";
							line = line.substring(count1 + 1, line.length());
							count1 = 0;
							count2++;
						}
					}

					count1++;

				}
				outTextx.write(editLine);
				outTextx.newLine();
				count++;
			}

		}
		inFilex.close();
		outTextx.close();
		textx.close();
		filex.delete();
		tempx.renameTo(filex);
	}

	public int findStringLoc(String file, String string, int loc,
			String string2, int loc2) throws FileNotFoundException {
		Object[] array = readarray(file);
		for (int i = 0; i <= array.length - 1; i++) {
			Object[] details = (Object[]) array[i];

			if (details[loc].equals(string) && details[loc2].equals(string2)) {
				return i;
			}

		}
		return 0;

	}

	public String getLineString(String file, int loc)
			throws FileNotFoundException {
		Object[] array = readarray(file);
		Object[] details;
		for (int i = 0; i <= array.length - 1; i++) {
			details = (Object[]) array[i];
			String ret = "";
			if (loc == i) {
				for (int a = 0; a <= details.length - 1; a++) {
					ret = ret + details[a] + "~";
				}
				return ret;
			}

		}
		return "";

	}

	public int findStringLoc(String file, String string, int loc)
			throws FileNotFoundException {
		Object[] array = readarray(file);
		for (int i = 0; i <= array.length - 1; i++) {
			Object[] details = (Object[]) array[i];

			if (details[loc].equals(string)) {
				return i;
			}

		}
		return 0;

	}

	public void deleteLine(String file, int lineN) throws IOException {

		java.io.File filex = new java.io.File(file);
		Scanner inFilex = new Scanner(filex);
		File tempx = File.createTempFile("temp", ".txt", filex.getParentFile());
		FileWriter textx = new FileWriter(tempx, true);
		BufferedWriter outTextx = new BufferedWriter(textx);
		String line;
		int count = 0;
		while (inFilex.hasNext()) {
			line = inFilex.nextLine();

			if (count != lineN) {

				outTextx.write(line);
				outTextx.newLine();
				count++;
			} else {
				count++;
			}
		}
		inFilex.close();
		outTextx.close();
		textx.close();

		filex.delete();
		tempx.renameTo(filex);
	}

	public void addToFile2(String filename, String msg) throws IOException {
		FileWriter text = new FileWriter(filename, false);
		BufferedWriter outText = new BufferedWriter(text);
		outText.write(msg);

		outText.close();
		text.close();
	}

	public void createFile(String filename, String msg) throws IOException {
		FileWriter text = new FileWriter(filename, false);
		BufferedWriter outText = new BufferedWriter(text);
		outText.write(msg);
		outText.newLine();
		outText.close();
		text.close();
	}

	public void addToFile(String filename, String msg) throws IOException {
		FileWriter text = new FileWriter(filename, true);
		BufferedWriter outText = new BufferedWriter(text);
		outText.write(msg);
		outText.newLine();
		outText.close();
		text.close();
	}

	public Object[] readarray(String filename) throws FileNotFoundException {
		java.io.File file = new java.io.File(filename);
		Scanner inFile = new Scanner(file);
		String line;

		Object[] array = new Object[0];

		while (inFile.hasNext()) {
			line = inFile.nextLine();
			array = expand(array);

			Object[] details = new Object[0];

			int count1 = 0;

			while (count1 < line.length()) {

				if (line.substring(count1, count1 + 1).equals("~")) {
					details = expand(details);
					String word = line.substring(0, count1);

					details[details.length - 1] = word;
					line = line.substring(count1 + 1, line.length());
					count1 = 0;
				}

				count1++;

			}
			// For Making Fake Dictionary global scores lol
			// array[array.length - 1] = new Object []{line};
			array[array.length - 1] = details;
		}
		inFile.close();
		return array;
	}

	// Support Commands for functions
	public void addUser() throws IOException {
		filename = "c:\\test\\login.txt";
		String msgOut = "";
		String user = message[0];
		msgOut = user + "~0~0~";
		addToFile(filename, msgOut);

		filename = "c:\\test\\profiles\\" + user + ".txt";

		msgOut = ("0~0~0~0~0~0~");

		addToFile(filename, msgOut);
		msgOut = ("0~0~");

		addToFile(filename, msgOut);

		filename = "c:\\test\\messages\\" + user + ".txt";
		msgOut = ("");
		addToFile2(filename, msgOut);
		filename = "c:\\test\\ladder\\" + user + ".txt";
		msgOut = ("");
		addToFile2(filename, msgOut);
		filename = "c:\\test\\usergames\\" + user + ".txt";
		msgOut = ("");
		addToFile2(filename, msgOut);

		filename = "c:\\test\\friendfiles\\" + user + ".txt";
		msgOut = ("");
		addToFile2(filename, msgOut);
		delivery = expand(delivery);
		delivery[0] = "0";
		sendMessage(delivery);

		message = new String[] { message[0], "", "", "", "", "", "ladder" };

		createGame();

	}

	// Misc

	public Object[] expand(Object[] array) {
		Object[] newArray = new Object[array.length + 1];
		System.arraycopy(array, 0, newArray, 0, array.length);

		// an alternative to using System.arraycopy would be a for-loop:
		// for(int i = 0; i < OrigArray.length; i++)
		// newArray[i] = OrigArray[i];

		return newArray;
	}

	public void sendMessage(Object[] msg) {
		/*
		 * for(int a = 0; a<msg.length;a++) { Object[] details =(Object[])
		 * msg[a]; for(int b = 0; b<details.length;b++) { System.out.println(b
		 * +" "+ a +" : sending = "+details[b]); }
		 * 
		 * }
		 */
		try {
			out.writeObject(msg);
			out.flush();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	// Rematch
	public void acceptRematch() throws IOException {
		String sgame = "c:\\test\\servergames\\" + message[4] + ".txt";

		getPos(sgame);

		if (pos == 1) {

			replaceSector(sgame, 0, 4, "4");
			if (getSector(sgame, 0, 5).equals("4")) {
				replaceSector(sgame, 0, 5, "5");
				replaceSector(sgame, 0, 4, "5");
				createGame();
			}
		} else {

			replaceSector(sgame, 0, 5, "4");
			if (getSector(sgame, 0, 4).equals("4")) {
				replaceSector(sgame, 0, 4, "5");
				replaceSector(sgame, 0, 5, "5");
				createGame();
			}
		}

	}

	public void rejectRematch() throws IOException {
		String sgame = "c:\\test\\servergames\\" + message[2] + ".txt";

		getPos(sgame);

		if (pos == 1) {

			replaceSector(sgame, 0, 4, "5");

			replaceSector(sgame, 0, 5, "5");
			addToFile(sgame, "game~rejected~" + message[0]);
		} else {

			replaceSector(sgame, 0, 4, "5");

			replaceSector(sgame, 0, 5, "5");
			addToFile(sgame, "game~rejected~" + message[0]);
		}

	}

	// KeyWord

	public void enterTarget() throws IOException {

		String line;
		int found = 0;
		user = message[0];
		word = message[3];
		gameNum = message[2];
		String sgame = "c:\\test\\servergames\\" + gameNum + ".txt";
		getPos(sgame);
		found = checkDicInput();

		if (found == 1) {

			if (pos == 1) {
				replaceSector(sgame, 1, 0, word);
				replaceSector(sgame, 0, 4, "1");
				if (getSector(sgame, 0, 5).equals("1")) {
					replaceSector(sgame, 0, 4, "2");
					replaceSector(sgame, 0, 5, "2");
				}
			} else {
				replaceSector(sgame, 1, 1, word);
				replaceSector(sgame, 0, 5, "1");
				if (getSector(sgame, 0, 4).equals("1")) {
					replaceSector(sgame, 0, 4, "2");
					replaceSector(sgame, 0, 5, "2");
				}
			}
		}

		sendMessage(new Object[] { found + "" });
	}

	public int checkDicInput() throws FileNotFoundException {

		String word = message[3];
		String user = message[0];
		if (word.length() != 5) {
			return 2;
		}
		for (int i = 0; i < 5; i++) {
			char x = word.charAt(i);
			for (int a = 0; a < 5; a++) {
				if (i == a)
					continue;
				else if (x == word.charAt(a)) {
					return 3;
				}
			}

		}

		String filename = "c:\\test\\" + "dic" + ".txt";
		java.io.File file = new java.io.File(filename);
		Scanner inFile = new Scanner(file);
		String line;

		while (inFile.hasNext()) {
			line = inFile.nextLine();

			if (line.equals(word)) {

				return 1;
			}
		}
		return 0;
	}

	// LADDER ADD TURNS ENTER GUESS
	public void recordLadder() throws IOException {
		word = message[3];
		user = message[0];
		gameNum = message[2];
		String dic = "c:\\test\\dic.txt";
		int wordLoc = findStringLoc(dic, word, 0);
		Object[] words = getLine(dic, wordLoc);
		String game = "c:\\test\\laddergames\\" + gameNum + ".txt";

		Object[] reader = readarray(game);
		Object[] details;
		details = (Object[]) reader[0];
		int yourTurns = Integer.parseInt((String) details[2]);
		int turns = Integer.parseInt((String) details[2])+Integer.parseInt((String) words[1]);
		System.out.println(turns+"   turns");
		int newTurn = Integer.parseInt((String) words[2]) + 1;
		System.out.println(newTurn+"   newTurn");
		replaceSector(dic, wordLoc, 1, turns + "");
		replaceSector(dic, wordLoc, 2, newTurn + "");
		replaceLine(dic, wordLoc, getLineString(dic, wordLoc) + user + "~");
		
		words = getLine(dic, wordLoc);
		double average = turns / newTurn;
		System.out.println(average+"   average");
		average = Double.parseDouble(String.format("%.1f", average));
		System.out.println(average+"   average");
		String lad = "c:\\test\\ladder\\" + user + ".txt";
		replaceSector(lad,findStringLoc(lad,gameNum,0),1,yourTurns+"");
		replaceSector(lad,findStringLoc(lad,gameNum,0),2,word);
		for (int a = 3; a < words.length ; a++) {
			updateLadderUser((String) words[a], word, average);
			
		}
		sortLadder();
	}

	public void updateLadderUser(String user, String word, double avg)
			throws IOException {
		String game = "c:\\test\\ladder\\" + user + ".txt";
		String login = "c:\\test\\login.txt";
		System.out.println (user + "   "+word +"   "+avg);
		Object[] reader = (Object[]) readarray(game);
		Object[] details;
		double score = 0;
		for (int a = 0; a < reader.length; a++) {
			details = (Object[]) reader[a];
			if (details[2].equals(word)) {
				double temp = (Double.parseDouble((String) details[1]) - avg);
				replaceSector(game, a, 3,
						temp + "");
				score = score + (Double.parseDouble((String) details[1]) - avg);
			} else {
				score = score + (Double.parseDouble((String) details[3]));
			}
		}
		replaceSector(login, findStringLoc(login, user, 0), 1, score + "");
	}

	public void enterLadderGuess() throws NumberFormatException, IOException {
		word = message[3];
		user = message[0];
		gameNum = message[2];
		int found = 0;
		String game = "c:\\test\\laddergames\\" + gameNum + ".txt";

		found = checkDic(Integer.parseInt(gameNum));
		System.out.println(getSector(game,1,0));
		if (found == 1) {
			sendMessage(new Object[] { 1 });
			
			if(getSector(game,1,0).equals(word)){
				sendMessage(new Object[] { 6 });
				
			}
			else
				sendMessage(new Object[] { 0 });
			addLadderTurn();
			processWords();
			recordWordsUsed();
			
		} else {
			sendMessage(new Object[] { found });
		}

	}

	public void enterGuess() throws IOException {
		word = message[3];
		user = message[0];
		gameNum = message[2];
		int found = 0;
		String game = "c:\\test\\servergames\\" + gameNum + ".txt";
		getPos(game);
		found = checkDic(Integer.parseInt(gameNum));

		if (found == 1) {

			if (getSector(game, 0, 0).equals("comp")) {
				if (pos == 1 && getSector(game, 0, pos + 4).equals("1")) {
					replaceSector(game, 0, 4, "0");
					replaceSector(game, 0, 5, "0");
				} else if (pos == 2 && getSector(game, 0, pos + 2).equals("1")) {
					replaceSector(game, 0, 4, "0");
					replaceSector(game, 0, 5, "0");

				} else
					replaceSector(game, 0, pos + 3, "1");
			}

			sendMessage(new Object[] { found });
			addTurn();
			processWords();

			recordWordsUsed();
			// changeStatus("2","3");
		} else {
			sendMessage(new Object[] { found });
		}

	}

	public void addLadderTurn() throws IOException {
		user = message[0];
		gameNum = message[2];
		String game = "c:\\test\\laddergames\\" + gameNum + ".txt";

		String turn = getSector(game, 0, 2).toString();
		int newturn = Integer.parseInt(turn) + 1;
		replaceSector(game, 0, 2, newturn + "");

	}

	public void addTurn() throws IOException {
		user = message[0];
		gameNum = message[2];
		String game = "c:\\test\\servergames\\" + gameNum + ".txt";
		getPos(game);
		if (pos == 1) {
			String turn = getSector(game, 0, 3).toString();
			int newturn = Integer.parseInt(turn) + 1;
			replaceSector(game, 0, 3, newturn + "");
		} else {
			String turn = getSector(game, 0, 8).toString();
			int newturn = Integer.parseInt(turn) + 1;
			replaceSector(game, 0, 8, newturn + "");
		}
	}

	// Game

	public int inCommon() {
		int inCommon = 0;
		for (int i = 0; i < 5; i++) {
			if (target.indexOf(word.charAt(i)) >= 0)
				inCommon++;
		}
		return inCommon;
	}

	public void processWords() throws IOException {

		user = message[0];
		gameNum = message[2];
		word = message[3];
		boolean ladder = false;
		if (message[1].equals("enterLadderGuess")) {
			ladder = true;
		}

		String game;
		String line;
		Object[] reader;
		Object[] details;
		if (ladder) {
			game = "c:\\test\\laddergames\\" + message[2] + ".txt";
			reader = readarray(game);
			details = (Object[]) reader[0];

			details = (Object[]) reader[1];
			target = details[0].toString();

			knowNot = getSector(game, 2, 3);
			know = getSector(game, 2, 2);

		} else {
			game = "c:\\test\\servergames\\" + message[2] + ".txt";

			reader = readarray(game);
			details = (Object[]) reader[0];
			getPos(game);
			if (details[0].equals("comp") && pos == 1) {
				friend = details[2].toString();

				details = (Object[]) reader[1];
				target = details[0].toString();

				knowNot = getSector(game, 2, 3);
				know = getSector(game, 2, 2);
			} else if (details[0].equals("comp") && pos == 2) {
				friend = details[2].toString();

				details = (Object[]) reader[1];
				target = details[0].toString();

				knowNot = getSector(game, 3, 3);
				know = getSector(game, 3, 2);
			} else if (pos == 1) {
				friend = details[2].toString();

				details = (Object[]) reader[1];
				target = details[1].toString();

				knowNot = getSector(game, 2, 3);
				know = getSector(game, 2, 2);

			} else {
				friend = details[1].toString();

				details = (Object[]) reader[1];
				target = details[0].toString();

				knowNot = getSector(game, 3, 3);
				know = getSector(game, 3, 2);

			}
		}

		if (knowNot.equals("0")) {
			knowNot = "";
		}
		if (know.equals("0")) {
			know = "";
		}
		addToFile(game, user + "~" + word + "~" + inCommon() + "~");
		
		wordcount = 0;
		reader = readarray(game);

		String knownNottemp = "", knowntemp = "";
		do {
			knownNottemp = knowNot;
			knowntemp = know;
			int count = 4;
			if (ladder){
				count = 3;
			}

			while (reader.length > count) {

				details = (Object[]) reader[count];

				if (details[0].equals(user)) {
					word = details[1].toString();
					System.out.println(wordcount + "........................");

					repeatOrder = orderRepeat();
					areIn = inCommon();

					if (areIn == 0) {
						System.out.print("Type 1" + "\n");
						noMatch(word);
					}
					hasLeft = lettersUnknown();

					if ((hasLeft) - (areIn - areKnown) == 0) {
						System.out.print("Type 2" + "\n");
						addIn();
					}
					areKnown = proven();
					if (areIn == areKnown) {
						System.out.print("Type 3" + "\n");
						System.out.print((areIn) + "\n");
						System.out.print((areKnown) + "\n");
						remove();
					}
					if ((hasLeft + areKnown) == areIn) {
						System.out.print("Type 4" + "\n");
						System.out.print((hasLeft) + "\n");
						System.out.print((areIn) + "\n");
						System.out.print((areKnown) + "\n");
						addIn();
						remove();
					}
					repeatCode = checkRepeat(word);

					repeatProcess();
					System.out.print((word));
					System.out.print((hasLeft) + "\n");
					System.out.print((areIn) + "\n");
					System.out.print((areKnown) + "\n");

					wordcount++;
				}
				count++;
				System.out.println("kn" + know + " kn" + knowNot);
			}
			System.out.print("KN.T= " + knownNottemp.length() + "\n" + "KN= "
					+ knowNot.length() + "\n");
			System.out.print("K.T= " + knowntemp.length() + "\n" + "K= "
					+ know.length() + "\n");

		} while (knownNottemp.length() != knowNot.length()
				|| knowntemp.length() != know.length());
		if (ladder && word.equals(target)) {
			message = new String[] { message[0], message[1], message[2],
					message[3], "", "", "ladder" };
			createGame();
			recordLadder();
			pos = 1;
		} else if (word.equals(target)) {
			if (pos == 1) {

				replaceSector(game, 0, 4, "3");
				if (getSector(game, 0, 5).equals("3")
						|| getSector(game, 0, 5).equals("4")) {
					recordRecord();
				}

			} else {

				replaceSector(game, 0, 5, "3");
				if (getSector(game, 0, 4).equals("3")
						|| getSector(game, 0, 4).equals("4")) {
					recordRecord();
				}
			}
		}
		findUnknown();

		if (know.equals("")) {
			know = "0";
		}
		if (knowNot.equals("")) {
			knowNot = "0";
		}
		pos = 1;
		if (pos == 1) {

			replaceSector(game, 2, 1, unknow);
			replaceSector(game, 2, 2, know);
			replaceSector(game, 2, 3, knowNot);

		} else {
			replaceSector(game, 3, 1, unknow);
			replaceSector(game, 3, 2, know);
			replaceSector(game, 3, 3, knowNot);
		}
		if (ladder) {
			loadLadder(gameNum);
		} else
			loadGame();

	}

	public String orderRepeat() {
		int count = 0, code = 0;
		String holdcode = "";
		int intcode[] = new int[6];
		for (int i = 0; i < 5; i++) {
			char x = word.charAt(i);
			count = 1;
			for (int a = 0; a < 5; a++) {
				if (i == a)
					continue;
				else if (x == word.charAt(a))
					count++;
			}
			intcode[i] = count;

		}

		for (int f = 0; f < 5; f++) {

			holdcode = holdcode + "" + Integer.toString(intcode[f]);
		}
		code = Integer.parseInt(holdcode);

		return code + "";
	}

	public int checkDic(int gameNum) throws IOException {
		String game = "";
		game = "c:\\test\\laddergames\\" + gameNum + ".txt";
		if (!message[1].equals("enterLadderGuess"))
			game = "c:\\test\\servergames\\" + gameNum + ".txt";
		word = message[3];
		user = message[0];
		if (word.length() != 5) {
			return 2;
		}
		if (getSector(game, 0, 0).equals("mastermind")) {
			String abcnot = getSector(game, pos + 1, 2);
			for (int i = 0; i < abcnot.length(); i++) {
				System.out.println(word.indexOf(abcnot.charAt(i)) < 0);
				if (word.indexOf(abcnot.charAt(i)) < 0 && !abcnot.equals("0"))
					return 3;
			}

		}

		String filename = "c:\\test\\" + "dic" + ".txt";
		java.io.File file = new java.io.File(filename);
		Scanner inFile = new Scanner(file);
		String line;

		while (inFile.hasNext()) {
			line = inFile.nextLine();

			if (line.substring(0, 5).equals(word)) {

				return 1;
			}
		}
		return 0;
	}

	public void noMatch(String s) {

		for (int i = 0; i < 5; i++) {
			if (knowNot.indexOf(s.charAt(i)) < 0)
				knowNot = knowNot + s.charAt(i);
			System.out.print("No Match At All" + "\n");
		}
	}

	public int lettersUnknown() {

		int hasLeftCount = 0;
		for (int i = 0; i < 5; i++) {
			if (knowNot.indexOf(word.charAt(i)) < 0
					&& know.indexOf(word.charAt(i)) < 0)
				hasLeftCount++;
		}

		return hasLeftCount;
	}

	public int proven() {

		int areKnownCount = 0;
		for (int i = 0; i < 5; i++) {
			if (know.indexOf(word.charAt(i)) >= 0)
				areKnownCount++;

		}
		return areKnownCount;
	}

	public void addIn() {

		for (int i = 0; i < 5; i++) {
			if (target.indexOf(word.charAt(i)) >= 0) {
				if (know.indexOf(word.charAt(i)) < 0) {
					know = know + word.charAt(i);
					System.out.print(know + " ++++" + word.charAt(i) + "\n");
				}
			}
		}
	}

	public void remove() {

		for (int i = 0; i < 5; i++) {
			if (target.indexOf(word.charAt(i)) < 0) {
				if (knowNot.indexOf(word.charAt(i)) < 0) {
					knowNot = knowNot + word.charAt(i);
					System.out.print(knowNot + " ----" + word.charAt(i) + "\n");
				}
			}
		}
	}

	public String checkRepeat(String s) {

		int count = 0, code = 0;
		String holdcode = "";
		int loc = 0;
		int intcode[] = new int[6];
		for (int i = 0; i < 5; i++) {

			char x = s.charAt(i);
			count = 1;
			for (int a = 0; a < 5; a++) {
				if (i == a)
					continue;
				else if (x == s.charAt(a))
					count++;
			}
			if (know.indexOf(x) < 0 && knowNot.indexOf(x) < 0) {
				intcode[loc] = count;
				loc++;
			}

		}
		if (loc == 0) {
			return "0";
		}

		for (int a = 1; a < 6; a++) {
			for (int f = 0; f < loc; f++) {
				if (intcode[f] == a)
					holdcode = holdcode + "" + Integer.toString(a);

			}
		}
		code = Integer.parseInt(holdcode);

		return code + "";
	}

	public void repeatProcess() {
		int differenceknown = areIn - areKnown;
		System.out.print("Difference (incommon-green)=" + differenceknown
				+ "\n");
		if (repeatCode.equals("122")) {
			if (differenceknown == 1 || differenceknown == 2) {
				System.out.print("122--1" + "\n");
				addIn();
				remove();
			}

		} else if (repeatCode.equals("1122")) {
			if (differenceknown == 1) {
				for (int a = 0; a < 5; a++) {
					if ('2' == (repeatOrder).charAt(a)) {
						if (knowNot.indexOf(word.charAt(a)) < 0) {
							System.out.print("1122--1" + "\n");
							knowNot = knowNot + word.charAt(a);
						}
					}
				}
			} else if (differenceknown == 3) {
				for (int a = 0; a < 5; a++) {
					if ('2' == ((repeatOrder)).charAt(a)) {
						if (know.indexOf(word.charAt(a)) < 0) {
							System.out.print("1122--3" + "\n");
							know = know + word.charAt(a);
						}
					}
				}
			}
		} else if (repeatCode.equals("1333")) {
			if (differenceknown == 3 || differenceknown == 1) {
				System.out.print("1333--13" + "\n");
				addIn();
				remove();
			}

		} else if (repeatCode.equals("11122")) {
			if (differenceknown == 1) {
				for (int a = 0; a < 5; a++) {
					if ('2' == ((repeatOrder)).charAt(a)) {
						if (knowNot.indexOf(word.charAt(a)) < 0) {
							System.out.print("11122--1" + "\n");
							knowNot = knowNot + word.charAt(a);
						}
					}
				}
			} else if (areIn == 4) {
				for (int a = 0; a < 5; a++) {
					if ('2' == ((repeatOrder)).charAt(a)) {
						if (know.indexOf(word.charAt(a)) < 0) {
							System.out.print("11122--4" + "\n");
							know = know + word.charAt(a);
						}
					}
				}
			}

		} else if (repeatCode.equals("11333")) {
			if (differenceknown == 3 || differenceknown == 2) {
				System.out.print("11333--23" + "\n");
				addIn();
				remove();
			} else if (differenceknown == 4) {
				for (int a = 0; a < 5; a++) {
					if ('3' == ((repeatOrder)).charAt(a)) {
						if (know.indexOf(word.charAt(a)) < 0) {
							System.out.print("11333--4" + "\n");
							know = know + word.charAt(a);
						}
					}
				}
			} else if (differenceknown == 1) {
				for (int a = 0; a < 5; a++) {
					if ('3' == ((repeatOrder)).charAt(a)) {
						if (knowNot.indexOf(word.charAt(a)) < 0) {
							System.out.print("11333--1" + "\n");
							knowNot = knowNot + word.charAt(a);
						}
					}
				}
			}
		} else if (repeatCode.equals("12222")) {
			if (differenceknown == 4 || differenceknown == 1) {
				System.out.print("12222--14" + "\n");
				addIn();
				remove();
			} else if (differenceknown == 2) {
				for (int a = 0; a < 5; a++) {
					if ('1' == ((repeatOrder)).charAt(a)) {
						if (knowNot.indexOf(word.charAt(a)) < 0) {
							System.out.print("12222--2" + "\n");
							knowNot = knowNot + word.charAt(a);
						}
					}
				}
			} else if (differenceknown == 3) {
				for (int a = 0; a < 5; a++) {
					if ('1' == ((repeatOrder)).charAt(a)) {
						if (know.indexOf(word.charAt(a)) < 0) {
							System.out.print("12222--3" + "\n");
							know = know + word.charAt(a);
						}
					}
				}
			}
		} else if (repeatCode.equals("14444")) {
			if (differenceknown == 4 || differenceknown == 1) {
				System.out.print("14444--14" + "\n");
				addIn();
				remove();
			}
		} else if (repeatCode.equals("22333")) {
			if (differenceknown == 2 || differenceknown == 3) {
				System.out.print("22333--23" + "\n");
				addIn();
				remove();
			}
		}
		hasLeft = lettersUnknown();
		areKnown = proven();
	}

	public void findUnknown() throws IOException {

		String x = "";
		unknow = "";
		for (int a = 0; a < 26; a++) {
			final char Letter;
			int abc = a + 97;
			Letter = (char) abc;

			if (know.indexOf(Letter) < 0 && knowNot.indexOf(Letter) < 0) {
				unknow = unknow + Letter;
			}
		}

	}

	public void getPos(String file) throws FileNotFoundException {
		Object[] reader = readarray(file);

		Object[] details = (Object[]) reader[0];
		if (details[1].equals(message[0])) {
			pos = 1;
		} else
			pos = 2;
	}

}

/*
 * 0 = enter word1 = waiting 2 = enter guess3 = waiting for other4 = both done 5
 * = rematch
 */

