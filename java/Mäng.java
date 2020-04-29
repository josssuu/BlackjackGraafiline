import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.w3c.dom.css.Rect;

import javax.naming.TimeLimitExceededException;
import java.awt.*;
import java.security.Key;
import java.sql.Time;
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
    private boolean panustaNuppAktiivne = true;

    public Mäng(String mängijanimi, MänguHaldur mänguHaldur) {
        this.mänguHaldur = mänguHaldur;
        mängija.setNimi(mängijanimi);
        laeMängulaud();

    }

    private void andmed() {
        System.out.println("\nMängija nimi: " + mängija.getNimi());
        System.out.println("Mängija raha: " + mängija.getRaha());
        System.out.println("Panus: " + panus);
        System.out.println("Mängija kaardid: " + mängija.getKäsi() + "   - " + mängija.käeVäärtus());
        System.out.println("Diileri kaardid: " + diiler.getKäsi() + "   - " + diiler.käeVäärtus());
    }

    private void laeMängulaud() {
        looPanustamisKomplekt();
        looTegevusnupud();
        looKaardipakk();
    }

    private void looKaardipakk() {
        AnchorPane kaardipakikoht = new AnchorPane();
        kaardipakikoht.setLayoutY(40);
        kaardipakikoht.setLayoutX(laius - 40 - 93);
        Image kaardipakk = new Image("\\Kaardid\\tagakulg.png", 93, 126, true,false);
        ImageView iv = new ImageView(kaardipakk);
        kaardipakikoht.getChildren().add(iv);
        this.mänguHaldur.addChildren(kaardipakikoht);
    }

    private void looPanustamisKomplekt() {
        kuvaTeade("TEE OMA PANUS");

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
            if (mouseEvent.getButton() == MouseButton.PRIMARY && panustaNuppAktiivne) {
                System.out.println("--- PANUSTA ---");

                if (mängija.getKäsi().isEmpty()) {
                    panus = (int) panuseSlaider.getValue();
                    jagaEsimesedKaardid();
                    kontrolliBlackjacki();
                    diiler.näitaKõikiKaarte();
                }

                andmed();
            }
        });
    }

    private void kontrolliHitNuppu() {
        hitNupp.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                System.out.println("--- HIT ---");

                if (panus != 0) {
                    teeHit();
                }

                andmed();
            }
        });
    }

    private void kontrolliStandNuppu() {
        standNupp.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY && panus != 0) {
                System.out.println("--- STAND ---");
                andmed();

                diiler.käik(kaardipakk);
                //Diiler teeb käigu ära ja siis tehakse ekraanile need kaardid järjest nähtavaks
                //FIXME See teeb siin nii, et diileri kaardid tuleksid uuesti kaardipakist. Tegelt võiks mingi muutuja selle jaoks olla.
                for (int i = 0; i < diiler.getKäsi().size(); i++) {
                    animKaartKätte(diiler.getKäsi().get(i), i, false,true);
                }

                if (mängija.käeVäärtus() > diiler.käeVäärtus() || diiler.käeVäärtus() > 21) {   // Mängija võit
                    kuvaTeade("VÕITSID  " + panus + " EUROT!").setOnFinished(e -> {
                        kuvaTeade("TEE OMA PANUS");
                    });
                    alustaUusVoor(panus);
                }
                else {      // Mängija kaotus
                    kuvaTeade("KAOTASID  " + panus + " EUROT!").setOnFinished(e -> {
                        kuvaTeade("TEE OMA PANUS");
                    });
                    alustaUusVoor(-1 * panus);
                }
            }
        });
    }

    private void teeHit() {
        Kaart kaart = kaardipakk.anna_kaart();
        mängija.võtaKaart(kaart);
        animKaartKätte(kaart, mängija.getKäsi().size() - 1, true, false);

        if (mängija.käeVäärtus() > 21) {
            kuvaTeade("LÄKSID LÕHKI!").setOnFinished(e -> {
                kuvaTeade("TEE OMA PANUS");
            });
            System.out.println("LÕHKI!");
            alustaUusVoor(-1 * panus);
        }
    }

    private void alustaUusVoor(int võiduSumma) {
        andmed();

        Kaardipakk uuspakk = new Kaardipakk();
        kaardipakk.setPakk(uuspakk.getPakk());
        kaardipakk.sega_kaardid();
        diiler.setKäsi(new ArrayList<>());
        mängija.setRaha(mängija.getRaha() + võiduSumma);
        mängija.setKäsi(new ArrayList<>());
        panus = 0;
        //eemaldame kaardid mänguväljalt
        animKaardidÄra();

        if (mängija.getRaha() != 0) {
            panuseSlaider.setMax(mängija.getRaha());
        }
        else {
            mängLäbiEkraan();
        }

        System.out.println("---------------------------------------------------");
    }

    private void kontrolliBlackjacki() {
        if (mängija.käeVäärtus() == 21) {
            System.out.println("BLACKJACK!");
            alustaUusVoor((int) (panus * 1.5));
            andmed();
        }
    }

    private void jagaEsimesedKaardid() {
        SequentialTransition st = new SequentialTransition();
        for (int i = 0; i < 2; i++) {
            Kaart mängijaKaart = kaardipakk.anna_kaart();
            mängija.võtaKaart(mängijaKaart);
            animKaartKätte(mängijaKaart, i, true, false);
            Kaart diileriKaart = kaardipakk.anna_kaart();
            diiler.võtaKaart(diileriKaart);
            animKaartKätte(diileriKaart, i, false, false);
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
        ParallelTransition pt = new ParallelTransition();
        pt.setCycleCount(1);
        pt.setDelay(new Duration(5000));
        for (Node n : this.mänguHaldur.getJuur().getChildren()) {
            if (n instanceof StackPane) {
                Timeline tl = new Timeline();

                KeyValue kvX = new KeyValue(n.layoutXProperty(), laius - 40 - 93);
                KeyValue kvY = new KeyValue(n.layoutYProperty(), 40);
                KeyFrame kf = new KeyFrame(new Duration(500), kvX, kvY);

                tl.getKeyFrames().add(kf);
                pt.getChildren().add(tl);
            }
        }

        panustaNuppAktiivne = false;
        pt.setOnFinished(e -> {
            panustaNuppAktiivne = true;
        });
        pt.play();
    }

    private void animKaartKätte(Kaart kaart, int mitmesKaart, boolean mängijale, boolean diileriLõppVoor) {

        // Kaardi sätted
        int startX = laius - 43 - 90;
        int startY = 40;

        int lõppY = 500;
        if (!mängijale) lõppY = 200;
        int esimeseKaardiLõppX = 200;
        int lõppX = esimeseKaardiLõppX + mitmesKaart * 40;
        String kaarditee = "\\Kaardid\\" + kaart.toString() + ".png";
        //Kui jagatav kaart on diilerile, esimene ja tegemist ei ole lõppvooruga siis kuvame tagurpidi
        if (!mängijale && mitmesKaart == 0 && !diileriLõppVoor) kaarditee = "\\Kaardid\\tagakulg.png";
        Image kaardipilt = new Image(kaarditee, 93, 126, true,false);
        ImageView iv = new ImageView(kaardipilt);
        StackPane kaardikoht = new StackPane();
        kaardikoht.setLayoutX(startX);
        kaardikoht.setLayoutY(startY);
        kaardikoht.getChildren().add(iv);
        this.mänguHaldur.addChildren(kaardikoht);

        // Animatsioon
        Timeline tl = new Timeline();
        KeyValue kv1 = new KeyValue(kaardikoht.layoutXProperty(), lõppX);
        KeyValue kv2 = new KeyValue(kaardikoht.layoutYProperty(), lõppY);
        KeyFrame kf = new KeyFrame(new Duration(500), kv1, kv2);
        tl.getKeyFrames().add(kf);
        tl.play();
    }

    private SequentialTransition kuvaTeade(String sõnum) {

        Label silt = new Label(sõnum);
        silt.setTextFill(Color.WHITE);
        silt.setFont(new Font("Font/EbGaramond12RegularAllSmallcaps-PpOZ.ttf", 50));
        silt.setLayoutX(laius / 2.0 -  150);
        silt.setLayoutY(kõrgus / 2.0 - 100);

        FadeTransition sisse = new FadeTransition(new Duration(500), silt);
        sisse.setFromValue(0);
        sisse.setToValue(1);
        sisse.setCycleCount(1);
        sisse.setAutoReverse(false);

        PauseTransition paus = new PauseTransition(new Duration(5000));

        FadeTransition välja = new FadeTransition(new Duration(500), silt);
        välja.setFromValue(1);
        välja.setToValue(0);
        välja.setCycleCount(1);
        välja.setAutoReverse(false);

        SequentialTransition teateKuvaja = new SequentialTransition(sisse, paus, välja);
        teateKuvaja.play();

        this.mänguHaldur.addChildren(silt);

        return teateKuvaja;
    }
}
