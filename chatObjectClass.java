import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class chatObjectClass {
	
	Scanner in = new Scanner(System.in);
	
	public void initialView() {
		
		boolean notDone = true;
		
		System.out.println("Welcome to our database");
		System.out.println("Please select from the following options: ");
		
		while(notDone) {
			
			System.out.println("  (R) to Register");
			System.out.println("  (L) to login");
			System.out.println("  (Q) to quit");
			System.out.println("-----------------------------------------");
			String answer = in.nextLine();
			
			if(answer.equals("R")) {
				register();
				notDone = false;
			}else if(answer.equals("L")) {
				login();
				notDone = false;

			}else if(answer.equals("Q")){
				quit();
				notDone = false;

			}else {
				System.out.println("Invalid Input...Try again!");
			}
		}
		
		
	}
	
	public void quit() {
		System.out.println("Thank you for using our application !");
		in.close();
	}
	
	public void login() {
		System.out.println();
		System.out.println("Username: ");
		String username = in.nextLine();
		System.out.println("Password: ");
		String password = in.nextLine();
		
		boolean userPassMatch = false;
		
		Connection c = null;
		Statement stmt = null;
		
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/usersdb", "postgres", "team6"
					);
			//System.out.println("Connected to the DB");
			
		}catch(Exception e) {
			e.printStackTrace();
			System.err.print(e.getClass().getName() + ": "  + e.getMessage());
			System.exit(0);
		}
		
		try {
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("select * from userpass");
			
			while(rs.next()) {
				String user = rs.getString("username");
				String pass = rs.getString("password");
				
				if(user.equals(username)) {
					if(pass.equals(password)) {
						userPassMatch = true;
						break;
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.err.print(e.getClass().getName() + ": "  + e.getMessage());
			System.exit(0);
		}
		
		if(userPassMatch) {
			//System.out.println("username and password matched");
			System.out.println("Login successful!");
			System.out.println();
			mainMenu(username);
			
		}else {
			System.out.println("Either the username or password were incorrect");
			System.out.println("Please select from the following options: ");
			System.out.println("  (T) to try again");
			System.out.println("  (R) to register");
			String command = in.nextLine();
			
			if(command.equals("T")) {
				login();
			}else if(command.equals("R")) {
				register();
			}else {
				System.out.println("Please select one of the given options !");
				command = in.nextLine();
				if(command.equals("T")) {
					login();
				}else if(command.equals("R")) {
					register();
				}
			}
		}
	}
	
	public void register() {
		System.out.println("Create your account");
		System.out.println("Username: ");
		String username = in.nextLine();
		String usernameEdit = '\'' + username + '\'';
		System.out.println("Password: ");
		String password = in.nextLine();
		password = '\'' + password + '\'';
		
		Connection c = null;
		Statement stmt = null;
		
		boolean dataInserted = false;
		
		boolean uniqueUser = true;
		
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/usersdb", "postgres", "team6"
					);
			//System.out.println("Connected to the DB");
			
		}catch(Exception e) {
			e.printStackTrace();
			System.err.print(e.getClass().getName() + ": "  + e.getMessage());
			System.exit(0);
		}
		
		
		try {
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("select * from userpass");
			
			while(rs.next()) {
				String existingUsername = rs.getString("username");
				
				if(existingUsername.equals(username)) {
					uniqueUser = false;
					break;
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.err.print(e.getClass().getName() + ": "  + e.getMessage());
			System.exit(0);
		}
		
		if(uniqueUser) {
			try {
				c.setAutoCommit(false);
				stmt = c.createStatement();
				String sql = "INSERT INTO USERPASS("
						+ "username,password) "
						+ "VALUES(" + usernameEdit + "," + password + ");";

				
				stmt.executeUpdate(sql);
				stmt.close();
				c.commit();
				c.close();
				//System.out.println("Data was inserted into the table");
				dataInserted = true;
				
				
			}catch(Exception e) {
				e.printStackTrace();
				System.err.print(e.getClass().getName() + ": "  + e.getMessage());
				System.exit(0);
			}
		}else {
			System.out.println("The chosen username already exists");
			System.out.println("Please choose one of the following options: ");
			System.out.println("  (T) to Try again");
			System.out.println("  (L) to Login");
			
			String command = in.nextLine();
			
			if(command.equals("T")) {
				register();
			}else if(command.equals("L")) {
				login();
			}else {
				System.out.println("Please select one of the given options!");
				command = in.nextLine();
				if(command.equals("T")) {
					register();
				}else if(command.equals("L")) {
					login();
				}
			}

		}
		
		
		if(dataInserted) {
			mainMenu(username);
		}
		
	}
	
	public void mainMenu(String username) {
		
		boolean notDone = true;
		
		while(notDone) {
			
			//System.out.println("username sent in: "+ username);
			System.out.println("Please select from the following options: ");
			System.out.println("  (J) to join an existing chatroom");
			System.out.println("  (C) to create a new chatroom");
			System.out.println("  (A) to modify your account");
			System.out.println("  (L) to logout");
			System.out.println("-------------------------------------------");
			System.out.println();
			String command = in.nextLine();
			
			if(command.equals("J")) {
				join(username);
				notDone = false;
			}else if(command.equals("C")) {
				create(username);
				notDone = false;
			}else if(command.equals("A")) {
				account();
				notDone = false;
			}else if(command.equals("L")) {
				logout();
				notDone = false;
			}
		}
		
		
	}
	
	public void join(String username) {
		System.out.println("Chatroom name: ");
		String roomName = in.nextLine();
		
		Connection c = null;
		Statement stmt = null;
		
		boolean roomExists = false;
		
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/usersdb", "postgres", "team6"
					);
			//System.out.println("Connected to the DB");
			
		}catch(Exception e) {
			e.printStackTrace();
			System.err.print(e.getClass().getName() + ": "  + e.getMessage());
			System.exit(0);
		}
		
		try {
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("select * from roomlist");
			
			while(rs.next()) {
				String room = rs.getString("roomname");
				
				if(room.equals(roomName)) {
					roomExists = true;
					break;
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.err.print(e.getClass().getName() + ": "  + e.getMessage());
			System.exit(0);
		}
		
		if(roomExists) {
			System.out.println('\n' + "Welcome to " + '\"' + roomName + '\"' + " , " + '\"' + username + '\"');
			System.out.println("Type /help for help" + '\n');
			inRoom(roomName, username);
		}else {
			
			boolean notDone = true;
			
			while(notDone) {
				System.out.println("The room " + roomName + " does not exist");
				System.out.println("Please select from the following options");
				System.out.println("  (T) to Try again");
				System.out.println("  (C) to Create a room");
				System.out.println("  (Q) to Quit");
				
				String command = in.nextLine();
				
				if(command.equals("T")) {
					join(username);
					notDone = false;
				}else if(command.equals("Q")) {
					quit();
					notDone = false;
				}else if(command.equals("C")) {
					create(username);
					notDone = false;
				}
			}
			
		}
	}
	
	public void inRoom(String roomName, String username) {
		Connection c = null;
		Statement stmt = null;
		
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/usersdb", "postgres", "team6"
					);
			
		}catch(Exception e) {
			e.printStackTrace();
			System.err.print(e.getClass().getName() + ": "  + e.getMessage());
			System.exit(0);
		}
		String message = in.nextLine();
		if(message.equals("/help")) {
			helpCommand(roomName, username);
		}else if(message.equals("/list")) {
			listCommand(roomName, username);
		}else if(message.equals("/history")) {
			historyCommand(roomName, username);
		}else if(message.equals("/leave")) {
			leave(username);
		}else {
			System.out.println(username + ": " + message);
			
			
			
			try {
				c.setAutoCommit(false);
				stmt = c.createStatement();
				String sql = "INSERT INTO " + roomName + "("
						+ "username,message) "
						+ "VALUES(" + '\'' + username + '\'' + "," + '\'' + message + '\'' + ");";

				
				stmt.executeUpdate(sql);
				stmt.close();
				c.commit();
				c.close();
				//System.out.println("Data was inserted into the table");		
			}catch(Exception e) {
				e.printStackTrace();
				System.err.print(e.getClass().getName() + ": "  + e.getMessage());
				System.exit(0);
			}
			
			//spacing between messages
			System.out.println('\n' + "---------------------------------" + '\n');
			inRoom(roomName, username);
			
		}
	}
	
	public void helpCommand(String roomName, String username) {
		System.out.println("These are the commands you can use: ");
		System.out.println("  /list to see all users in the chatroom");
		System.out.println("  /history to see all chats in this chatroom");
		System.out.println("  /leave to leave this chatroom");
		System.out.println("  /help to see this list");
		
		System.out.println('\n' + "Welcome to " + '\"' + roomName + '\"' + " , " + '\"' + username + '\"');
		System.out.println("Type /help for help" + '\n');
		
		inRoom(roomName, username);


	}
	
	
	public void historyCommand(String roomName, String username) {
		System.out.println("All past messages in the chatroom: ");
		
		Connection c = null;
		Statement stmt = null;
		
		
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/usersdb", "postgres", "team6"
					);			
		}catch(Exception e) {
			e.printStackTrace();
			System.err.print(e.getClass().getName() + ": "  + e.getMessage());
			System.exit(0);
		}
		
		try {
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("select * from " + roomName);
			
			while(rs.next()) {
				String user = rs.getString("username");
				String text = rs.getString("message");
				
				System.out.println(user + ": " + text);
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.err.print(e.getClass().getName() + ": "  + e.getMessage());
			System.exit(0);
		}
		
		System.out.println('\n' + "Welcome to " + '\"' + roomName + '\"' + " , " + '\"' + username + '\"');
		System.out.println("Type /help for help" + '\n');
		
		inRoom(roomName, username);
		
		
	}
	
	public void listCommand(String roomName, String username) {
		System.out.println("List of all users in the chatroom: ");
				
		Connection c = null;
		Statement stmt = null;
		
		
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/usersdb", "postgres", "team6"
					);			
		}catch(Exception e) {
			e.printStackTrace();
			System.err.print(e.getClass().getName() + ": "  + e.getMessage());
			System.exit(0);
		}
		
		ArrayList<String> userList = new ArrayList<String>();
		
		try {
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("select * from " + roomName);
			
			while(rs.next()) {
				String user = rs.getString("username");
				
				if(!userList.contains(user)) {
					userList.add(user);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.err.print(e.getClass().getName() + ": "  + e.getMessage());
			System.exit(0);
		}
		
		for(String u : userList) {
			System.out.println("- " + u);
		}
		
		System.out.println("------------------------------------------------");
		System.out.println('\n' + "Welcome to " + '\"' + roomName + '\"' + " , " + '\"' + username + '\"');
		System.out.println("Type /help for help" + '\n');
		
		inRoom(roomName, username);
	}
	
	public void create(String username) {
		
		System.out.println("What is the name of your Chatroom: ");
		String roomName = in.nextLine();
		
		Connection c = null;
		Statement stmt = null;
		
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/usersdb", "postgres", "team6"
					);			
		}catch(Exception e) {
			e.printStackTrace();
			System.err.print(e.getClass().getName() + ": "  + e.getMessage());
			System.exit(0);
		}
		
		try {
			stmt = c.createStatement();
			String sql = "CREATE TABLE "+ roomName   +
						 "(USERNAME TEXT NOT NULL, " +
						 "MESSAGE TEXT)";
			
			stmt.executeUpdate(sql);
			stmt.close();
			c.close();
		}catch(Exception e) {
			e.printStackTrace();
			System.err.print(e.getClass().getName() + ": "  + e.getMessage());
			System.exit(0);
		}
		
		c = null;
		stmt = null;
		
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/usersdb", "postgres", "team6"
					);			
		}catch(Exception e) {
			e.printStackTrace();
			System.err.print(e.getClass().getName() + ": "  + e.getMessage());
			System.exit(0);
		}

		try {
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "INSERT INTO ROOMLIST("
					+ "ROOMNAME)"
					+ "VALUES("+'\''+roomName+'\''+");";
			
			stmt.executeUpdate(sql);
			stmt.close();
			c.commit();
			c.close();
			//System.out.println("Data was inserted into the table");
			
			
		}catch(Exception e) {
			e.printStackTrace();
			System.err.print(e.getClass().getName() + ": "  + e.getMessage());
			System.exit(0);
		}
		
		System.out.println('\n' + "Welcome to " + '\"' + roomName + '\"' + " , " + '\"' + username + '\"');
		System.out.println("Type /help for help" + '\n');
		System.out.println("-----------------------------------------");
		
		
		
		inRoom(roomName,username);
		
	}

	
	public void account() {
		Connection c = null;
		Statement stmt = null;
		
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/usersdb", "postgres", "team6"
					);			
		}catch(Exception e) {
			e.printStackTrace();
			System.err.print(e.getClass().getName() + ": "  + e.getMessage());
			System.exit(0);
		}
		boolean quit = false;
		while(quit == false) {
			System.out.println("Would you like to change your username?: ");
			System.out.println("(Y) Yes");
			System.out.println("(N) No ");
			System.out.println("(Q) Quit");
			String answer = in.nextLine();
			boolean usernamerun = false;
			boolean passwordrun = false;
			while(usernamerun == false) {
				if(answer.equals("Y")) {
					System.out.println("What is your current username?: ");
					String old_username = in.nextLine();
					String old_usernameEdit = '\''+old_username+'\'';
					System.out.println("What would you like your username to be?: ");
					String username = in.nextLine();
					String usernameEdit = '\''+username+'\'';
					try {
						c.setAutoCommit(false);
						stmt = c.createStatement();
						String sql = "UPDATE USERPASS set USERNAME = " + usernameEdit + " where USERNAME = " + old_usernameEdit + "; ";
						stmt.executeUpdate(sql);
						c.commit();
						
					}catch(Exception e) {
						e.printStackTrace();
						System.err.print(e.getClass().getName() + ": "  + e.getMessage());
						System.exit(0);
					}
					usernamerun = true;
					break;
					
					
					
				}
				if(answer.equals("N")) {
					System.out.println("You have decided to not change the username.");
					usernamerun = true;
					break;
				}
				if(answer.equals("Q")) {
					quit = true;
					passwordrun = true;
					break;
				}
				else {
					System.out.println("Invalid Input...Try again");
					answer = in.nextLine();
				}
				
			
			}
			
		 while(passwordrun == false) {
			System.out.println("Would you like to change your password?: ");
			System.out.println("(Y) Yes");
			System.out.println("(N) No ");
			answer = in.nextLine();
			if(answer.equals("Y")) {
				System.out.println("What is your username?: ");
				String username1 = in.nextLine();
				String usernameEdit1 = '\''+username1+'\'';
				System.out.println("What would you like your password to be?: ");
				String password = in.nextLine();
				String passwordEdit = '\''+password+'\'';
				try {
					c.setAutoCommit(false);
					stmt = c.createStatement();
					String sql = "UPDATE USERPASS set PASSWORD = " + passwordEdit + " where USERNAME = " + usernameEdit1 + "; ";
					stmt.executeUpdate(sql);
					c.commit();
					
				}catch(Exception e) {
					e.printStackTrace();
					System.err.print(e.getClass().getName() + ": "  + e.getMessage());
					System.exit(0);
				}
				passwordrun = true;
				break;
				

			}
			if(answer.equals("N")) {
				System.out.println("You have decided to not change the password.");
				passwordrun = true;
				break;
				
			}
			if(answer.equals("Q")) {
				quit = true;
				break;
			}
			else {
				System.out.println("Invalid Input...Try again");
				answer = in.nextLine();
			}
		break;
		}
		initialView();

		}
	}
	
	
	public void deleteRoom(String username, String roomName) {
		
		Connection c = null;
		Statement stmt = null;
		
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/usersdb", "postgres", "team6"
					);			
		}catch(Exception e) {
			e.printStackTrace();
			System.err.print(e.getClass().getName() + ": "  + e.getMessage());
			System.exit(0);
		}
		
		try {
			stmt  = c.createStatement();
			
		}catch(Exception e) {
			e.printStackTrace();
			System.err.print(e.getClass().getName() + ": "  + e.getMessage());
			System.exit(0);
		}
		
	}





	
	public void logout() {
		System.out.println("Successfully Logged out" + '\n');
		initialView();
	}
	
	public void leave(String username) {
		mainMenu(username);
	}

}




