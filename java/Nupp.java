import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Nupp extends Button {
    private int xkord;
    private int ykord;

    private Background tavaTaust;
    private Background klikiTaust;

    private String fondiTee = "Font/EbGaramond12RegularAllSmallcaps-PpOZ.ttf";
    private int fondiSuurus = 20;

    public Nupp(String tekst, int xkord, int ykord) {
        this.xkord = xkord;
        this.ykord = ykord;
        this.setPrefHeight(49);
        määraFont();
        this.setText(tekst);

        laeTavaTaust();
        kontrolliTegevusi();
    }

    private void määraFont() {
        try {
            Font font = new Font(fondiTee, fondiSuurus);
            this.setFont(font);
        }
        catch (Exception e) {
            System.out.println(e + " font ei laadinud ära");
        }
    }

    private void laeTavaTaust() {
        try {
            tavaTaust = new Background(new BackgroundImage(new Image("nupp_üleval.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null));
            this.setBackground(tavaTaust);
            this.setPrefHeight(49);
            this.setPrefWidth(190);

            this.setLayoutX(xkord);
            this.setLayoutY(ykord);
        }
        catch (Exception e) {
            System.out.println(e + " ei laadinud nupu pilti ära");
        }
    }

    private void laeKlikiTaust() {
        try {
            klikiTaust = new Background(new BackgroundImage(new Image("nupp_all.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null));
            this.setBackground(klikiTaust);
            this.setPrefHeight(45);
            this.setPrefWidth(190);

            this.setLayoutY(ykord + 4);
        }
        catch (Exception e) {
            System.out.println(e + "ei laadinud nupu pilti ära");
        }
    }
    private void kontrolliTegevusi() {
        hiirPeal();
        hiirMaas();
        hiireKlikk();
    }

    private void hiireKlikk() {
        this.setOnMousePressed(mouseEvent -> {
            if (mouseEvent.isPrimaryButtonDown()) {
                this.laeKlikiTaust();
            }
        });

        this.setOnMouseReleased(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                this.laeTavaTaust();
            }
        });
    }

    private void hiirPeal() {
        this.setOnMouseEntered(mouseEvent -> {
            DropShadow vari = new DropShadow();
            vari.setColor(Color.BLACK);
            this.setEffect(vari);
        });
    }

    private void hiirMaas() {
        this.setOnMouseExited(MouseEvent -> this.setEffect(null));
    }
}
