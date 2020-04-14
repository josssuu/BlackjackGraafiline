import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Haldur {
    protected int laius = 1200;
    protected int kõrgus = 800;

    protected AnchorPane juur;
    protected Stage pealava;
    protected Scene stseen;

    private String taustapildiTee = "taustapilt.jpg";

    public Haldur() {
        this.juur = new AnchorPane();
        this.stseen = new Scene(juur, laius, kõrgus);
        this.pealava = new Stage();
        laeTaust();
    }

    private void laeTaust() {
        Image pilt = new Image("taustapilt.jpg", laius, kõrgus, false,false);
        Background taustapilt = new Background(new BackgroundImage(pilt, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null));
        juur.setBackground(taustapilt);
    }
    public Stage getPealava() {
        return this.pealava;
    }

    public Scene getStseen() {
        return this.stseen;
    }

    public AnchorPane getJuur() {
        return this.juur;
    }
}
