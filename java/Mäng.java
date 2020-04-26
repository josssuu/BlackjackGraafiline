import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.w3c.dom.css.Rect;

import java.awt.*;
import java.util.ArrayList;

public class Mäng extends Haldur {

    private int panus;
    //private boolean rahaOtsas = false;

    private MänguHaldur mänguHaldur;
    private Kaardipakk kaardipakk = new Kaardipakk();
    private Diiler diiler = new Diiler();
    private Mängija mängija = new Mängija();

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
        looPanustamisKomplekt();
        looTegevusnupud();
        looKaardipakk();
    }

    private void looKaardipakk() {
        //Tegin selle praegu Stackpane'iga, ei tea kas see on kõige parem variant, aga eks paistab
        StackPane kaardipakikoht = new StackPane();
        kaardipakikoht.setLayoutY(40);
        kaardipakikoht.setLayoutX(laius - 40 - 93);
        Image kaardipakk = new Image("\\Kaardid\\tagakulg.png", 93, 126, true,false);
        ImageView iv = new ImageView(kaardipakk);
        kaardipakikoht.getChildren().add(iv);
        this.mänguHaldur.addChildren(kaardipakikoht);
    }

    private void looPanustamisKomplekt() {
        VBox komplekt = new VBox();
        //komplekt.setStyle("-fx-background-color: #DC143C");
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

        Label rahaSumma = new Label(Integer.toString((int) panuseSlaider.getValue()));
        rahaSumma.setMinHeight(25);
        rahaSumma.setTranslateX(88);
        rahaSumma.setFont(new Font("Font/EbGaramond12RegularAllSmallcaps-PpOZ.ttf", 30));
        rahaSumma.setTextFill(Color.WHITE);

        panuseSlaider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                rahaSumma.setText(Integer.toString((int) panuseSlaider.getValue()));
            }
        });

        komplekt.getChildren().addAll(rahaSumma, painuti, panustaTagus);
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
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                if (mängija.getKäsi().isEmpty()) {
                    panus = (int) panuseSlaider.getValue();
                    System.out.println("Panus: " + panus);
                    jagaEsimesedKaardid();
                    kontrolliBlackjacki();
                    diiler.näitaKõikiKaarte();
                    System.out.println("\nMängija käsi: "  + mängija.getKäsi());
                }
            }
        });
    }

    private void kontrolliHitNuppu() {
        hitNupp.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                if (panus != 0) {
                    teeHit();
                }
            }
        });
    }

    private void kontrolliStandNuppu() {
        standNupp.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY && panus != 0) {
                diiler.käik(kaardipakk);
                System.out.println("Mängija käeväärtus: " + mängija.käeVäärtus());
                System.out.println("Diileri käeväärtus: " + diiler.käeVäärtus());
                if (mängija.käeVäärtus() > diiler.käeVäärtus() || diiler.käeVäärtus() > 21) {
                    alustaUusVoor(panus);
                }
                else {
                    alustaUusVoor(-1 * panus);
                }
            }
        });
    }

    private void teeHit() {
        Kaart kaart = kaardipakk.anna_kaart();
        mängija.võtaKaart(kaart);
        animKaartKätte(kaart, mängija.getKäsi().size() - 1);

        System.out.println(mängija.getKäsi());
        if (mängija.käeVäärtus() > 21) {
            alustaUusVoor(-1 * panus);
            System.out.println("lõhki");
        }
    }

    private void alustaUusVoor(int võiduSumma) {
        Kaardipakk uuspakk = new Kaardipakk();
        kaardipakk.setPakk(uuspakk.getPakk());
        kaardipakk.sega_kaardid();
        diiler.setKäsi(new ArrayList<>());
        mängija.setRaha(mängija.getRaha() + võiduSumma);
        mängija.setKäsi(new ArrayList<>());
        panus = 0;
        //eemaldame kaardid mänguväljalt
        for (Node n : this.mänguHaldur.getJuur().getChildren()) {
            System.out.println(n);
            if (n instanceof StackPane) {
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        mänguHaldur.getJuur().getChildren().remove(n);
                        System.out.println("eemaldatud");
                    }
                });
            }
        }
        System.out.println();
        for (Node n : this.mänguHaldur.getJuur().getChildren()) {
            System.out.println(n);
        }

        if (mängija.getRaha() != 0) {
            panuseSlaider.setMax(mängija.getRaha());
            System.out.println("Mängija raha: " + mängija.getRaha());
        }
        else {
            mängLäbiEkraan();
        }
        System.out.println();
    }

    private void kontrolliBlackjacki() {
        if (mängija.käeVäärtus() == 21) {
            System.out.println("BLACKJACK!");
            alustaUusVoor((int) (panus * 1.5));
        }
    }

    private void jagaEsimesedKaardid() {
        for (int i = 0; i < 2; i++) {
            Kaart mängijaKaart = kaardipakk.anna_kaart();
            mängija.võtaKaart(mängijaKaart);
            animKaartKätte(mängijaKaart, i);
            Kaart diileriKaart = kaardipakk.anna_kaart();
            diiler.võtaKaart(diileriKaart);
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

    private void animKaartKätte(Kaart kaart, int mitmesKaart) {
        int y = 500;
        int esimeseKaardiX = 200;
        int x = esimeseKaardiX + mitmesKaart * 40;
        String kaarditee = "\\Kaardid\\" + kaart.toString() + ".png";
        System.out.println(kaarditee);
        Image kaardipilt = new Image(kaarditee, 93, 126, true,false);
        ImageView iv = new ImageView(kaardipilt);
        StackPane kaardikoht = new StackPane();
        kaardikoht.setLayoutY(y);
        kaardikoht.setLayoutX(x);
        kaardikoht.getChildren().add(iv);
        this.mänguHaldur.addChildren(kaardikoht);
    }
}
