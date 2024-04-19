public class Main {
    public static void main(String[] args) {
        Model model = new Model();
        model.createInitialUserTable();
        model.createInitialLeaderboard();

        model.addLeaderboardScore("bergen", 1000);
    }
}
