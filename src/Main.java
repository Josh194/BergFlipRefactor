public class Main {
    public static void main(String[] args) {

        System.out.println("Launching the Program...");

        new Controller();

        Model model = new Model();
        model.getUsers();
    }
}
