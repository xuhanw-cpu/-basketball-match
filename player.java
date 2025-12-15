package finalProject副本;

public class player {
	private int id;
    private String name;
    private String teamName;

    private int totalPoints;
    private int totalRebounds;
    private int totalAssists;
    private int gamesPlayed;

    public player(int id, String name, String teamName) {
        this.id = id;
        this.name = name;
        this.teamName = teamName;
        this.totalPoints = 0;
        this.totalRebounds = 0;
        this.totalAssists = 0;
        this.gamesPlayed = 0;
    }

    public void addGameStats(int points, int rebounds, int assists) {
        this.totalPoints += points;
        this.totalRebounds += rebounds;
        this.totalAssists += assists;
        this.gamesPlayed += 1;
    }

    public double getAvgPoints() {
        if (gamesPlayed == 0) return 0.0;
        return (double) totalPoints / gamesPlayed;
    }

    // getter / setter / toString

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public int getTotalRebounds() {
        return totalRebounds;
    }

    public int getTotalAssists() {
        return totalAssists;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    @Override
    public String toString() {
        return String.format("ID: %d, name: %s, team: %s, total score: %d, average score: %.2f, appearances: %d",
                id, name, teamName, totalPoints, getAvgPoints(), gamesPlayed);
}
    
    public void setStats(int totalPoints, int totalReb, int totalAst, int gamesPlayed) {
        this.totalPoints = totalPoints;
        this.totalRebounds = totalReb;
        this.totalAssists = totalAst;
        this.gamesPlayed = gamesPlayed;
    }
}