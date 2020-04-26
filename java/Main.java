import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage pealava) {
        laeMenüü(pealava);
        pealava.setTitle("Blackjack");
        pealava.setResizable(false);
        pealava.show();

    }

    static void laeMenüü(Stage pealava) {
        MenüüHaldur haldur = new MenüüHaldur();
        pealava.setScene(haldur.getStseen());
    }
    public static void main(String[] args) {
        launch(args);
    }
}
