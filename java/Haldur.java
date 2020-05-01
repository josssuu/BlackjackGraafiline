import javafx.scene.Node;
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

    private String taustapildiTee = "taustapilt.png";

    public Haldur() {
        this.juur = new AnchorPane();
        this.stseen = new Scene(juur, laius, kõrgus);
        this.pealava = new Stage();
        laeTaust();
    }

    private void laeTaust() {
        Image pilt = new Image(taustapildiTee, laius, kõrgus, false,false);
        Background taustapilt = new Background(new BackgroundImage(pilt, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null));
        juur.setBackground(taustapilt);
    }

    /*
    public Stage getPealava() {
        return this.pealava;
    }
     */

    public Scene getStseen() {
        return this.stseen;
    }

    public AnchorPane getJuur() {
        return this.juur;
    }

    public void addChildren(Node laps) {
        this.juur.getChildren().add(laps);
    }

    public void setChildren(Node laps) {
        this.juur.getChildren().setAll(laps);
    }
}
