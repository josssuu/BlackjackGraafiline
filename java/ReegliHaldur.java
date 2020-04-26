import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;


public class ReegliHaldur extends Haldur {
    private String reegliFailiTee = "src/main/resources/reeglid.txt";

    private Nupp tagasiNupp;
    private Label reeglid;

    public ReegliHaldur() {
        looReeglid();
        looNupud();
        kontrolliNupud();
        kuvaElemendid();
    }

    private void kuvaElemendid() {
        juur.getChildren().addAll(tagasiNupp, reeglid);
    }

    private void looNupud() {
        looTagasiNupp();
    }

    private void kontrolliNupud() {
        kontrolliTagasi();
    }

    private void looTagasiNupp() {
        tagasiNupp = new Nupp("Tagasi", 900, 700);
    }

    private void kontrolliTagasi() {
        tagasiNupp.setOnMouseClicked(mouseEvent -> {
            MenüüHaldur haldur = new MenüüHaldur();
            juur.getChildren().setAll(haldur.getJuur());
        });
    }

    private void looReeglid() {
        laeReeglid();
        vormistaReeglid();
    }

    private void vormistaReeglid(){
        reeglid.setWrapText(true);
        reeglid.setTextFill(Color.BLACK);
        reeglid.setLayoutX(300);
        reeglid.setLayoutY(100);
        reeglid.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));

    }

    private void laeReeglid() {
        reeglid = new Label();

        try {
            BufferedReader bf = new BufferedReader(new FileReader(reegliFailiTee, StandardCharsets.UTF_8));
            String  rida = bf.readLine();
            while (rida != null) {
                reeglid.setText(reeglid.getText() + rida + "\n");
                rida = bf.readLine();
            }
        }
        catch (Exception e) {
            System.out.println(e + " reeglite viga");
        }
    }
}
