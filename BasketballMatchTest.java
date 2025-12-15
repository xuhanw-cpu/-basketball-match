package finalProject副本;

import java.util.*;
import java.io.*; 

public class BasketballMatchTest {
	private static Scanner input = new Scanner(System.in);

    
    // use BST to store player ID（Integer）
    private static BST<Integer> playerBST = new BST<>();
    private static List<player> playerList = new ArrayList<>();    
    private static List<team> teamList = new ArrayList<>();        
    private static List<match> matchList = new ArrayList<>(); 
    
		 public static void main(String[] args) {
		       
		        boolean running = true;

		        while (running) {
		            printMainMenu();
		            int choice = readInt("Please select an operation:");

		            switch (choice) {
		                case 1:
		                    addTeam();
		                    break;
		                case 2:
		                    addPlayer();
		                    break;
		                case 3:
		                    recordMatch();
		                    break;
		                case 4:
		                    showTopScorers();
		                    break;
		                case 5:
		                    showTeamStandings();
		                    break;
		                case 6:
		                    searchPlayerById();
		                    break;
		                case 7:
		                    //save to files
		                    saveDataToFiles();
		                    break;
		                case 8:
		                    //load from files
		                    loadDataFromFiles();
		                    break;
		                case 9:
		                    System.out.println("Launch the system. Goodbye!");
		                    running = false;
		                    break;
		                default:
		                    System.out.println("Invalid selection. Please re-enter.");
		            }
		        }
		    }
		// the menu
		    private static void printMainMenu() {
		        System.out.println("《《Basketball match management system》》");
		        System.out.println("1. Add team");
		        System.out.println("2. Add player");
		        System.out.println("3. Enter the match results");
		        System.out.println("4. Display the players' scoring rankings");
		        System.out.println("5. Display the team win rate rankings");
		        System.out.println("6. Search player information by ID");
		        System.out.println("7. Save data to file");
		        System.out.println("8. Load data from the file");
		        System.out.println("9. Log out");
		        
		    }
		    
		    private static int readInt(String prompt) {
		        while (true) {
		            System.out.print(prompt);
		            try {
		                String line = input.nextLine();
		                return Integer.parseInt(line.trim());
		            } catch (Exception e) {
		                System.out.println("The input is not a valid integer. Please try again.");
		            }
		        }
		    }

		    private static String readString(String prompt) {
		        System.out.print(prompt);
		        return input.nextLine();
		    }

		    
		    // the first function: Add team 
		    
		    private static void addTeam() {
		        String name = readString("Enter the team name:");
		        // check whether the team is exist 检查是否已存在
		        for (team t : teamList) {
		            if (t.getName().equalsIgnoreCase(name)) {
		                System.out.println("The team is exist.");
		                return;
		            }
		        }
		        team team = new team(name);
		        teamList.add(team);
		        System.out.println("Team added successfully.");
		    }

		 
		    // the second function: Add player 
		  
		    private static void addPlayer() {
		        int id = readInt("Please enter the player ID (integer):");
		        String name = readString("Please enter the player's name:");
		        String teamName = readString("Please enter the name of the team you belong to (it must already exist or be added later):");

		        player player = new player(id, name, teamName);

		        // use the BST to store ID
		        boolean ok = playerBST.insert(id);
		        if (!ok) {
		            System.out.println("There has the same ID in BST.");
		            return;
		        }

		        // Put the complete Player objects in the List to be used for ranking and statistics.
		        playerList.add(player);

		        System.out.println("Player added successfully!");
		    }

		   
		    // the third function: Enter competition results and update statistics 
		  
		    private static void recordMatch() {
		        String teamAName = readString("Please enter the name of Team A:");
		        String teamBName = readString("Please enter the name of Team B:");

		        team teamA = findTeamByName(teamAName);
		        team teamB = findTeamByName(teamBName);

		        if (teamA == null || teamB == null) {
		            System.out.println("The team does not exist. Please create a team first.");
		            return;
		        }

		        int scoreA = readInt("Please enter the score of Team A:");
		        int scoreB = readInt("Please enter the score of Team B:");

		        // Create a match object
		        match match = new match(teamAName, teamBName, scoreA, scoreB);
		        matchList.add(match);

		        // Update the win/loss status.
		        if (scoreA > scoreB) {
		            teamA.incrementWins();
		            teamB.incrementLosses();
		        } else if (scoreB > scoreA) {
		            teamB.incrementWins();
		            teamA.incrementLosses();
		        } else {
		        //draw (平局)   
		            System.out.println("draw");
		        }

		        // Update player's personal data 
		        System.out.println("Should the personal data of the players be entered?（y/n）");
		        String ans = input.nextLine();
		        if (ans.equalsIgnoreCase("y")) {
		            recordPlayerStats();
		        }

		        System.out.println("The match results have been entered!");
		    }

		   
		    // Enter players' single-game statistics
		    private static void recordPlayerStats() {
		        while (true) {
		            System.out.println("Enter the players' match data:");
		            int playerId = readInt("Player ID (Enter -1 to end):");
		            if (playerId == -1) {
		                break;
		            }

		            // Use BST to determine whether this ID is in the tree
		            boolean exists = playerBST.search(playerId);
		            if (!exists) {
		                System.out.println("The player ID is not found in the BST.");
		                continue;
		            }

		            // Find the actual Player object from playerList
		            player player = null;
		            for (player p : playerList) {
		                if (p.getId() == playerId) {
		                    player = p;
		                    break;
		                }
		            }

		            if (player == null) {
		                System.out.println("The ID exists in the BST, but the player was not found in the list.");
		                continue;
		            }

		            // Enter the data of this session
		            int points = readInt("Score of this game:");
		            int rebounds = readInt("Rebounds in this game:");
		            int assists = readInt("Assists in this game:");

		            player.addGameStats(points, rebounds, assists);
		            System.out.println("Player data updated:" + player.getName());
		        }
		    }

		    private static team findTeamByName(String name) {
		        for (team t : teamList) {
		            if (t.getName().equalsIgnoreCase(name)) {
		                return t;
		            }
		        }
		        return null;
		    }

		    
		    // the forth function: display the player score ranking (Bubble Sort)
		   
		    private static void showTopScorers() {
		        if (playerList.isEmpty()) {
		            System.out.println("No player data available.");
		            return;
		        }

		        // Make a copy of the list to avoid changing the original order.
		        List<player> sorted = new ArrayList<>(playerList);

		        // calculate the average points per match
		        // Use bubble sort to sort from high to low according to the average score.
		        for (int i = 0; i < sorted.size() - 1; i++) {
		            for (int j = 0; j < sorted.size() - 1 - i; j++) {
		                if (sorted.get(j).getAvgPoints() < sorted.get(j + 1).getAvgPoints()) {
		                    
		                    player temp = sorted.get(j);
		                    sorted.set(j, sorted.get(j + 1));
		                    sorted.set(j + 1, temp);
		                }
		            }
		        }

		        System.out.println("《《Player scoring rankings》》");
		        for (player p : sorted) {
		            System.out.printf("ID: %d, name: %s, average points: %.2f, appearance: %d%n",
		                    p.getId(), p.getName(), p.getAvgPoints(), p.getGamesPlayed());
		        }
		    }

		    
		    // the fifth function: Display the ranking of team win rates 
		 
		    private static void showTeamStandings() {
		        if (teamList.isEmpty()) {
		            System.out.println("There is no team data.");
		            return;
		        }

		        List<team> sorted = new ArrayList<>(teamList);
		        // Bubble sort, from the highest to the lowest win rate.
		        for (int i = 0; i < sorted.size() - 1; i++) {
		            for (int j = 0; j < sorted.size() - 1 - i; j++) {
		                if (sorted.get(j).getWinRate() < sorted.get(j + 1).getWinRate()) {
		                    team tmp = sorted.get(j);
		                    sorted.set(j, sorted.get(j + 1));
		                    sorted.set(j + 1, tmp);
		                }
		            }
		        }

		        System.out.println("《Team win rate ranking list》");
		        for (team t : sorted) {
		            System.out.printf("team name: %s, win: %d, defeat: %d, win rate: %.2f%n",
		                    t.getName(), t.getWins(), t.getLosses(), t.getWinRate());
		        }
		    }

		   
		    // the sixth function: use BST search to query players by ID
		   
		    private static void searchPlayerById() {
		        int id = readInt("Please enter the player ID:");

		        // check if the ID exists in the BST.
		        boolean exists = playerBST.search(id);
		        if (!exists) {
		            System.out.println("There is no player with this ID in the BST.");
		            return;
		        }

		        // find the corresponding Player object in the ArrayList<Player>.
		        player target = null;
		        for (player p : playerList) {
		            if (p.getId() == id) {
		                target = p;
		                break;
		            }
		        }

		        if (target == null) {
		           
		            System.out.println("The ID exists in the BST, but the player was not found in the list.");
		        } else {
		            System.out.println("Find the player:");
		            System.out.println(target);
		        }
		    }

		   
		    // the seventh function: save data to file 
		 
		    private static void saveDataToFiles() {
		    	try {
		            // store player data
		            PrintWriter pw = new PrintWriter(new FileWriter("players.csv"));
		            pw.println("id,name,team,totalPoints,totalReb,totalAst,gamesPlayed");
		            for (player p : playerList) {
		                pw.printf("%d,%s,%s,%d,%d,%d,%d\n",
		                        p.getId(),
		                        p.getName(),
		                        p.getTeamName(),
		                        p.getTotalPoints(),
		                        p.getTotalRebounds(),
		                        p.getTotalAssists(),
		                        p.getGamesPlayed());
		            }
		            pw.close();

		            // store team data
		            PrintWriter tw = new PrintWriter(new FileWriter("teams.csv"));
		            tw.println("name,wins,losses");
		            for (team t : teamList) {
		                tw.printf("%s,%d,%d\n",
		                        t.getName(),
		                        t.getWins(),
		                        t.getLosses());
		            }
		            tw.close();

		            System.out.println("The data has been successfully saved to the CSV file!");
		        } catch (Exception e) {
		            System.out.println("Failed to save data:" + e.getMessage());
		        }
		    }

		    //the eighth function: load data from files
		    private static void loadDataFromFiles() {try {
		        playerList.clear();
		        teamList.clear();
		        playerBST.clear();   // clear BST

		        // Loading player data
		        BufferedReader br = new BufferedReader(new FileReader("players.csv"));
		        String line = br.readLine();
		        while ((line = br.readLine()) != null) {
		            String[] data = line.split(",");
		            int id = Integer.parseInt(data[0]);
		            String name = data[1];
		            String team = data[2];
		            int totalPoints = Integer.parseInt(data[3]);
		            int totalReb = Integer.parseInt(data[4]);
		            int totalAst = Integer.parseInt(data[5]);
		            int gamesPlayed = Integer.parseInt(data[6]);

		            player p = new player(id, name, team);

		            
		            p.setStats(totalPoints, totalReb, totalAst, gamesPlayed);

		            playerList.add(p);
		            playerBST.insert(id); // rebuild BST
		        }
		        br.close();

		        // Load team data
		        BufferedReader tr = new BufferedReader(new FileReader("teams.csv"));
		        tr.readLine(); 
		        while ((line = tr.readLine()) != null) {
		            String[] data = line.split(",");
		            String name = data[0];
		            int wins = Integer.parseInt(data[1]);
		            int losses = Integer.parseInt(data[2]);

		            team t = new team(name);
		            for (int i = 0; i < wins; i++) t.incrementWins();
		            for (int i = 0; i < losses; i++) t.incrementLosses();

		            teamList.add(t);
		        }
		        tr.close();

		        System.out.println("The data has been successfully loaded from the CSV file!");
		    } catch (Exception e) {
		        System.out.println("Failed to load data:" + e.getMessage());
		    }
		    }
}
