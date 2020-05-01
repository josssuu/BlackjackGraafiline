import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodRequests;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import javax.swing.plaf.ColorUIResource;

public class MänguHaldur extends Haldur {
    public MänguHaldur() {
        nimeKüsimine();
    }

    private void nimeKüsimine() {
        VBox aken = new VBox();

        Label nimi = new Label("Sisesta oma nimi");
        nimi.setTextFill(Color.WHITE);
        nimi.setStyle("-fx-font-weight: bold");
        nimi.setFont(new Font("Font/EbGaramond12RegularAllSmallcaps-PpOZ.ttf", 25));

        TextField sisend = new TextField("");
        määraMaxTähemärkideArv(sisend, 15);


        AnchorPane kinnitaTagus = new AnchorPane();
        Nupp kinnita = new Nupp("Kinnita", 0, 0);
        kinnitaTagus.getChildren().add(kinnita);

        AnchorPane tagasiTagus = new AnchorPane();
        Nupp tagasi = new Nupp("Tagasi", 0, 0);
        tagasiTagus.getChildren().add(tagasi);

        tagasi.setOnMouseClicked(mouseEvent -> {
            MenüüHaldur haldur = new MenüüHaldur();
            juur.getChildren().setAll(haldur.getJuur());
        });

        kinnita.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY && !sisend.getText().equals("")) {
                juur.getChildren().removeAll(juur.getChildren());
                alustaMängu(sisend.getText());
            }
        });

        aken.setSpacing(50);
        aken.setLayoutX(505);
        aken.setLayoutY(300);

        aken.getChildren().addAll(nimi, sisend, kinnitaTagus, tagasiTagus);
        juur.getChildren().add(aken);
    }

    private void määraMaxTähemärkideArv(final TextField tf, final int maxLength) {
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (tf.getText().length() > maxLength) {
                    String s = tf.getText().substring(0, maxLength);
                    tf.setText(s);
                }
            }
        });
    }

    private void alustaMängu(String mängijaNimi) {
        Mäng mäng = new Mäng(mängijaNimi, this);
        mäng.kontrolliTegevusi();
    }
}
