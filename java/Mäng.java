import javafx.animation.*;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Mäng extends Haldur {
    private int panus;

    private MänguHaldur mänguHaldur;
    private Kaardipakk kaardipakk = new Kaardipakk();
    private Diiler diiler = new Diiler();
    private Mängija mängija = new Mängija();

    private Slider panuseSlaider;
    private Nupp panustaNupp;
    private Nupp hitNupp;
    private Nupp standNupp;
    private Nupp lõpetaNupp;
    private StackPane diileriEsimeneKaart;

    private boolean teadeEkraanil = false;

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
        looKastid();
        looPanustamisKomplekt();
        looTegevusnupud();
        looKaardipakk();
    }

    private void looKastid() {
        Rectangle ristkylik = new Rectangle(850, 70,Color.BLACK);
        ristkylik.setArcHeight(5);
        ristkylik.setArcWidth(5);
        ristkylik.setOpacity(0.5);
        ristkylik.setLayoutX(175);
        ristkylik.setLayoutY(10);

        Circle ring = new Circle(50);
        ring.setOpacity(0.4);
        ring.setLayoutX(120);
        ring.setLayoutY(137);

        this.mänguHaldur.addChildren(ristkylik);
        this.mänguHaldur.addChildren(ring);
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
        komplekt.setLayoutY(120);
        komplekt.setLayoutX(25);
        komplekt.setSpacing(25);
        komplekt.setAlignment(Pos.CENTER);

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
        rahaSumma.setFont(new Font("Font/EbGaramond12RegularAllSmallcaps-PpOZ.ttf", 30));
        rahaSumma.setTextFill(Color.WHITE);

        panuseSlaider.valueProperty().addListener((observableValue, number, t1) -> rahaSumma.setText(Integer.toString((int) panuseSlaider.getValue())));

        komplekt.getChildren().addAll(rahaSumma, painuti, panustaTagus);
        this.mänguHaldur.addChildren(komplekt);
    }

    private void looTegevusnupud() {
        AnchorPane hitTagus = new AnchorPane();
        hitNupp = new Nupp("Võta kaart", 0, 0);
        hitTagus.getChildren().add(hitNupp);

        AnchorPane standTagus = new AnchorPane();
        standNupp = new Nupp("Seisa", 0, 0);
        standTagus.getChildren().add(standNupp);

        AnchorPane lõpetaTagus = new AnchorPane();
        lõpetaNupp = new Nupp("Lõpeta mäng", 0, 0);
        lõpetaTagus.getChildren().add(lõpetaNupp);

        HBox riba = new HBox();
        riba.setLayoutX(387);
        riba.setLayoutY(665);
        riba.setSpacing(100);
        riba.getChildren().addAll(hitTagus, standTagus, lõpetaTagus);
        mänguHaldur.addChildren(riba);
    }

    public void kontrolliTegevusi() {
        kontrolliPanustaNuppu();
        kontrolliHitNuppu();
        kontrolliStandNuppu();
        kontrolliLõpetaNuppu();
    }

    private void kontrolliPanustaNuppu() {
        panustaNupp.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY && !teadeEkraanil) {
                System.out.println("--- PANUSTA ---");

                if (mängija.getKäsi().isEmpty() && mängija.getRaha() > 0) {
                    panus = (int) panuseSlaider.getValue();
                    jagaEsimesedKaardid();
                    kontrolliBlackjacki();
                    diiler.näitaKõikiKaarte();
                }

                if (mängija.getRaha() == 0) {
                    kuvaTeade("SUL ON RAHA OTSAS");
                }

                andmed();
            }
        });
    }

    private void kontrolliLõpetaNuppu() {
        lõpetaNupp.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY && !teadeEkraanil) {
                if (panus == 0) {
                    if (uuendaTop5Faili()) {
                        kuvaTeade("SAID EDETABELISSE!").setOnFinished(e -> {
                            MenüüHaldur haldur = new MenüüHaldur();
                            mänguHaldur.setChildren(haldur.getJuur());
                        });
                    }
                    else {
                        MenüüHaldur haldur = new MenüüHaldur();
                        mänguHaldur.setChildren(haldur.getJuur());
                    }
                }
                else {
                    kuvaTeade("ENNE TULB VOOR LÕPETADA!");
                }
            }
        });
    }

    private void kontrolliHitNuppu() {
        hitNupp.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY && !teadeEkraanil) {
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
            if (mouseEvent.getButton() == MouseButton.PRIMARY && panus != 0 && !teadeEkraanil) {
                System.out.println("--- STAND ---");
                andmed();

                diiler.käik(kaardipakk);
                //Diiler teeb käigu ära ja siis tehakse ekraanile need kaardid järjest nähtavaks.

                String näoTee = "\\Kaardid\\" + diiler.getKäsi().get(0) + ".png";
                Image näoPilt = new Image(näoTee, 93, 126, true,false);
                ImageView pildiVaade = new ImageView(näoPilt);
                diileriEsimeneKaart.getChildren().setAll(pildiVaade);

                SequentialTransition st = new SequentialTransition();
                for (int i = 2; i < diiler.getKäsi().size(); i++) {
                    PauseTransition paus = new PauseTransition(new Duration(500));
                    st.getChildren() .addAll(animKaartKätte(diiler.getKäsi().get(i), i, false,true, false), paus);
                }

                st.setOnFinished(o -> {
                    if (mängija.käeVäärtus() == 21) kontrolliBlackjacki();
                    else if (mängija.käeVäärtus() > diiler.käeVäärtus() || diiler.käeVäärtus() > 21) {   // Mängija võit
                        kuvaTeade("VÕITSID  " + panus + " EUROT!").setOnFinished(e -> {
                            alustaUusVoor(panus);
                            kuvaTeade("TEE OMA PANUS");
                        });
                    } else {      // Mängija kaotus
                        kuvaTeade("KAOTASID  " + panus + " EUROT!").setOnFinished(e -> {
                            alustaUusVoor(-1 * panus);
                            kuvaTeade("TEE OMA PANUS");
                        });
                    }
                });

                st.play();
            }
        });
    }

    private void teeHit() {
        Kaart kaart = kaardipakk.anna_kaart();
        mängija.võtaKaart(kaart);
        animKaartKätte(kaart, mängija.getKäsi().size() - 1, true, false, true);

        if (mängija.käeVäärtus() > 21) {
            kuvaTeade("LÄKSID LÕHKI! KAOTASID " + panus + " EUROT!").setOnFinished(e -> {
                alustaUusVoor(-1 * panus);

                if (mängija.getRaha() > 0) {
                    kuvaTeade("TEE OMA PANUS");
                }
                else {
                    kuvaTeade("SUL ON RAHA OTSAS!");
                }
            });
            System.out.println("LÕHKI!");
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

        System.out.println("---------------------------------------------------");
    }

    private void kontrolliBlackjacki() {
        if (mängija.käeVäärtus() == 21) {
            System.out.println("BLACKJACK!");
            kuvaTeade("BLACKJACK! VÕITSID " + (int) (panus * 1.5) + " EUROT!").setOnFinished(e -> kuvaTeade("TEE OMA PANUS"));
            alustaUusVoor((int) (panus * 1.5));
            andmed();
        }
    }

    private void jagaEsimesedKaardid() {
        for (int i = 0; i < 2; i++) {
            Kaart mängijaKaart = kaardipakk.anna_kaart();
            mängija.võtaKaart(mängijaKaart);
            animKaartKätte(mängijaKaart, i, true, false, true);
            Kaart diileriKaart = kaardipakk.anna_kaart();
            diiler.võtaKaart(diileriKaart);
            animKaartKätte(diileriKaart, i, false, false, true);
        }
    }

    private void animKaardidÄra() {
        ParallelTransition pt = new ParallelTransition();
        pt.setCycleCount(1);
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

        looKaardipakk();
        pt.play();
    }

    private Timeline animKaartKätte(Kaart kaart, int mitmesKaart, boolean mängijale, boolean diileriLõppVoor, boolean mängiKohe) {

        // Kaardi sätted
        int startX = laius - 43 - 90;
        int startY = 40;


        int lõppY = !mängijale ? 200 : 500;
        int esimeseKaardiLõppX = 200;
        int lõppX = esimeseKaardiLõppX + mitmesKaart * 40;

        String kaarditee = "\\Kaardid\\tagakulg.png";
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
        tl.setOnFinished(e -> {
            // Kui pole diileri esimene kaart, siis keeratakse lõpus kaart ümber
            if (!(!mängijale && mitmesKaart == 0 && !diileriLõppVoor)) {
                String näoTee = "\\Kaardid\\" + kaart.toString() + ".png";
                Image näoPilt = new Image(näoTee, 93, 126, true,false);
                ImageView pildiVaade = new ImageView(näoPilt);
                kaardikoht.getChildren().setAll(pildiVaade);
            }
            else {
                diileriEsimeneKaart = kaardikoht;
            }
        });
        if (mängiKohe) tl.play();

        return tl;
    }

    private SequentialTransition kuvaTeade(String sõnum) {
        Label silt = new Label(sõnum);
        silt.setTextFill(Color.WHITE);
        silt.setFont(new Font("Font/EbGaramond12RegularAllSmallcaps-PpOZ.ttf", 50));
        silt.setLayoutX(185);
        silt.setLayoutY(10);

        FadeTransition sisse = new FadeTransition(new Duration(500), silt);
        sisse.setFromValue(0);
        sisse.setToValue(1);
        sisse.setCycleCount(1);
        sisse.setAutoReverse(false);

        PauseTransition paus = new PauseTransition(new Duration(2000));

        FadeTransition välja = new FadeTransition(new Duration(500), silt);
        välja.setFromValue(1);
        välja.setToValue(0);
        välja.setCycleCount(1);
        välja.setAutoReverse(false);

        SequentialTransition teateKuvaja = new SequentialTransition(sisse, paus, välja);

        teadeEkraanil = true;
        teateKuvaja.setOnFinished(e -> teadeEkraanil = false);
        teateKuvaja.play();

        this.mänguHaldur.addChildren(silt);

        return teateKuvaja;
    }

    private boolean uuendaTop5Faili() {
        boolean uuedAndmedSisestatud = false;
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("src/main/resources/edetabel.txt"), StandardCharsets.UTF_8))) {

            List<String> nimed = new ArrayList<>();
            List<Integer> tulemused = new ArrayList<>();
            String rida = br.readLine();
            while (rida != null) {
                String[] tükid = rida.split(",");
                nimed.add(tükid[0]);
                tulemused.add(Integer.parseInt(tükid[1]));
                rida = br.readLine();
            }
            int top5Suurus = 5;
            if (nimed.size() < 5) top5Suurus = nimed.size();
            for (int i = 0; i < top5Suurus; i++) {
                if (mängija.getRaha() > tulemused.get(i) && !uuedAndmedSisestatud) {
                    nimed.add(i, mängija.getNimi());
                    tulemused.add(i, mängija.getRaha());
                    uuedAndmedSisestatud = true;
                }
            }
            if (!uuedAndmedSisestatud && top5Suurus < 5) {
                nimed.add(mängija.getNimi());
                tulemused.add(mängija.getRaha());
                uuedAndmedSisestatud = true;
            }
            try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("src/main/resources/edetabel.txt"), StandardCharsets.UTF_8))) {
                if (nimed.size() <= 5) {
                    for (int i = 0; i < nimed.size(); i++) {
                        bw.write(nimed.get(i) + "," + tulemused.get(i) + "\n");
                    }
                } else {
                    for (int i = 0; i < 5; i++) {
                        bw.write(nimed.get(i) + "," + tulemused.get(i) + "\n");
                    }
                }
            } catch (IOException e) {
                System.out.println("Viga top3 faili kirjutamisel");
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.out.println("Viga top3 failist lugemisel");
            e.printStackTrace();
        }
        return uuedAndmedSisestatud;
    }
}
