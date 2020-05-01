import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;

public class EdetabeliHaldur extends Haldur {
    private String edeTabeliFailiTee = "src/main/resources/edetabel.txt";

    private Nupp okNupp;
    private Label edetabel;
    private VBox kastid;

    public EdetabeliHaldur() {
        looKastid();
        okNupp = new Nupp("OK", 502, 700);
        looEdetabel();
        kontrolliokNuppu();
        juur.getChildren().addAll(kastid, okNupp, edetabel);
    }

    private void looKastid() {
        kastid = new VBox(10);
        Rectangle kast1 = new Rectangle(600,65);
        Rectangle kast2 = new Rectangle(600,65);
        Rectangle kast3 = new Rectangle(600,65);
        Rectangle kast4 = new Rectangle(600,65);
        Rectangle kast5 = new Rectangle(600,65);
        kast1.setOpacity(0.6);
        kast2.setOpacity(0.5);
        kast3.setOpacity(0.4);
        kast4.setOpacity(0.3);
        kast5.setOpacity(0.3);
        kastid.getChildren().addAll(kast1,kast2,kast3,kast4,kast5);
        kastid.setLayoutX(300);
        kastid.setLayoutY(100);
    }

    private void kontrolliokNuppu() {
        okNupp.setOnMouseClicked(mouseEvent -> {
            MenüüHaldur haldur = new MenüüHaldur();
            juur.getChildren().setAll(haldur.getJuur());
        });
    }

    private void looEdetabel() {
        laeEdetabel();
        vormistaEdetabel();
    }

    private void laeEdetabel() {
        edetabel = new Label();

        try {
            BufferedReader bf = new BufferedReader(new FileReader(edeTabeliFailiTee, StandardCharsets.UTF_8));
            String  rida = bf.readLine();
            int kohaLoendur = 1;
            while (rida != null) {
                String[] tükid = rida.split(",");
                String tekst = kohaLoendur + ".  " + tükid[0] + "(" + tükid[1] + ")";
                edetabel.setText(edetabel.getText() + tekst + "\n");
                rida = bf.readLine();
                kohaLoendur++;
            }
        }
        catch (Exception e) {
            System.out.println(e + " Viga edetabeli lugemisega");
        }
    }

    private void vormistaEdetabel() {
        edetabel.setWrapText(true);
        edetabel.setTextFill(Color.WHITE);
        edetabel.setFont(new Font("Font/EbGaramond12RegularAllSmallcaps-PpOZ.ttf", 52));
        edetabel.setLayoutX(320);
        edetabel.setLayoutY(97);
        edetabel.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
    }
}
