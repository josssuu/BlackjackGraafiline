import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.awt.*;
import java.util.ArrayList;

public class Mäng extends Haldur {
    private int panus;
    private boolean rahaOtsas = false;

    private MänguHaldur mänguHaldur;
    private static Kaardipakk kaardipakk = new Kaardipakk();
    private static Diiler diiler = new Diiler();
    private static Mängija mängija = new Mängija();

    private Slider panuseSlaider;
    private Nupp panustaNupp;
    private Nupp hitNupp;
    private Nupp standNupp;

    public Mäng(String mängijanimi, MänguHaldur mänguHaldur) {
        this.mänguHaldur = mänguHaldur;
        mängija.setNimi(mängijanimi);
        laeMängulaud();
    }

    private void laeMängulaud() {
        looKaardipakk();
        looPanustamisKomplekt();
        looTegevusnupud();
    }

    private void looKaardipakk() {

    }

    private void looPanustamisKomplekt() {
        VBox komplekt = new VBox();
        komplekt.setLayoutY(100);
        komplekt.setLayoutX(25);
        komplekt.setSpacing(25);

        panuseSlaider = new Slider(1, mängija.getRaha(), 3);
        panuseSlaider.setOrientation(Orientation.VERTICAL);
        panuseSlaider.setPrefWidth(190);
        panuseSlaider.setPrefHeight(450);

        GridPane painuti = new GridPane();
        GridPane.setHgrow(panuseSlaider, Priority.ALWAYS);
        painuti.add(panuseSlaider, 1, 0);

        AnchorPane panustaTagus = new AnchorPane();
        panustaNupp = new Nupp("Panusta", 0, 0);
        panustaTagus.getChildren().add(panustaNupp);

        komplekt.getChildren().addAll(painuti, panustaTagus);
        this.mänguHaldur.addChildren(komplekt);
    }

    private void looTegevusnupud() {
        AnchorPane hitTagus = new AnchorPane();
        hitNupp = new Nupp("Võta kaart", 0, 0);
        hitTagus.getChildren().add(hitNupp);

        AnchorPane standTagus = new AnchorPane();
        standNupp = new Nupp("Stand", 0, 0);
        standTagus.getChildren().add(standNupp);

        HBox riba = new HBox();
        riba.setLayoutX(150);
        riba.setLayoutY(700);
        riba.setSpacing(500);
        riba.getChildren().addAll(hitTagus, standTagus);
        mänguHaldur.addChildren(riba);
    }

    public void kontrolliTegevusi() {
        kontrolliPanustaNuppu();
        kontrolliHitNuppu();
        kontrolliStandNuppu();
    }

    private void kontrolliPanustaNuppu() {
        panustaNupp.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY && !rahaOtsas) {
                if (mängija.getKäsi().isEmpty()) {
                    panus = (int) panuseSlaider.getValue();
                    jagaEsimesedKaardid();
                    kontrolliBlackjacki();
                    diiler.näitaKõikiKaarte();
                    System.out.println(mängija.getKäsi());
                }
            }
        });
    }

    private void kontrolliHitNuppu() {
        hitNupp.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY && !rahaOtsas) {
                if (panus != 0) {
                    teeHit();
                }
            }
        });
    }

    private void kontrolliStandNuppu() {
        standNupp.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY && !rahaOtsas) {
                diiler.käik(kaardipakk);
                if (mängija.käeVäärtus() > diiler.käeVäärtus()) {
                    mängija.setRaha(mängija.getRaha() + panus);
                }
                else if (mängija.käeVäärtus() == diiler.käeVäärtus()) {
                    alustaUusVoor();
                }
                else {
                    mängija.setRaha(mängija.getRaha() - panus);
                }
            }
        });
    }

    private void teeHit() {
        Kaart kaart = kaardipakk.anna_kaart();
        mängija.võtaKaart(kaart);

        animKaartKätte(kaart);
        System.out.println(mängija.getKäsi());
        if (mängija.käeVäärtus() > 21) {
            teeLõhki();
        }
    }

    private void teeLõhki() {
        alustaUusVoor();
        System.out.println("lõhki");
    }

    private void alustaUusVoor() {
        Kaardipakk uuspakk = new Kaardipakk();
        kaardipakk.setPakk(uuspakk.getPakk());
        kaardipakk.sega_kaardid();
        diiler.setKäsi(new ArrayList<>());
        mängija.setRaha(mängija.getRaha() - panus);
        mängija.setKäsi(new ArrayList<>());
        panus = 0;

        if (mängija.getRaha() != 0) {
            panuseSlaider.setMax(mängija.getRaha());
            System.out.println(mängija.getRaha());
        }
        else {
            rahaOtsas = true;
            mängLäbiEkraan();
        }
    }

    private void kontrolliBlackjacki() {
        if (mängija.käeVäärtus() == 21) {
            mängija.setRaha(mängija.getRaha() + (int) (panus * 1.5));
            alustaUusVoor();
        }
    }

    private void jagaEsimesedKaardid() {
        for (int i = 0; i < 2; i++) {
            mängija.võtaKaart(kaardipakk.anna_kaart());
            diiler.võtaKaart(kaardipakk.anna_kaart());
        }
    }

    private void mängLäbiEkraan() {
        VBox lõpupaneel = new VBox();
        lõpupaneel.setPrefWidth(450);
        lõpupaneel.setPrefHeight(200);
        lõpupaneel.setAlignment(Pos.CENTER);
        lõpupaneel.setSpacing(30);
        lõpupaneel.setLayoutX(375);
        lõpupaneel.setLayoutY(250);

        BackgroundSize taustaSuurus = new BackgroundSize(450, 200, false, false, false, false);
        Image paneeliPilt = new Image("paneel.png");
        BackgroundImage paneeliTaustapilt = new BackgroundImage(paneeliPilt, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, taustaSuurus);
        Background paneeliTaust = new Background(paneeliTaustapilt);
        lõpupaneel.setBackground(paneeliTaust);

        Label teavitus = new Label("Kahjuks sai sul raha otsa!");
        try {
            javafx.scene.text.Font font = new Font("Font/EbGaramond12RegularAllSmallcaps-PpOZ.ttf", 25);
            teavitus.setFont(font);
        } catch (Exception e) {
            System.out.println(e + " font ei laadinud ära");
        }

        Nupp okNupp = new Nupp("OK",0,0);
        okNupp.setOnMouseClicked(mouseEvent -> {
            MenüüHaldur haldur = new MenüüHaldur();
            mänguHaldur.setChildren(haldur.getJuur());
        });

        lõpupaneel.getChildren().addAll(teavitus, okNupp);
        mänguHaldur.addChildren(lõpupaneel);
    }

    private void animKaardidÄra() {

    }

    private void animKaartKätte(Kaart kaart) {

    }
}
