package finalProject副本;

public class team {
	 private String name;
	    private int wins;
	    private int losses;

	    public team(String name) {
	        this.name = name;
	        this.wins = 0;
	        this.losses = 0;
	    }

	    public void incrementWins() {
	        wins++;
	    }

	    public void incrementLosses() {
	        losses++;
	    }

	    public double getWinRate() {
	        int total = wins + losses;
	        if (total == 0) return 0.0;
	        return (double) wins / total;
	    }

	    public String getName() {
	        return name;
	    }

	    public int getWins() {
	        return wins;
	    }

	    public int getLosses() {
	        return losses;
	    }
}
