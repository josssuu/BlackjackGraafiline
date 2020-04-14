import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class MänguHaldur extends Haldur {
    public MänguHaldur() {
        nimeKüsimine();
    }

    private void nimeKüsimine() {
        VBox aken = new VBox();
        TextField sisend = new TextField("Sisesta oma nimi");

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
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                juur.getChildren().removeAll(juur.getChildren());
                alustaMängu(sisend.getText());
            }
        });

        aken.setSpacing(50);
        aken.setLayoutX(505);
        aken.setLayoutY(300);

        aken.getChildren().addAll(sisend, kinnitaTagus, tagasiTagus);
        juur.getChildren().add(aken);
    }

    private void alustaMängu(String mängijaNimi) {
        Mäng mäng = new Mäng(mängijaNimi, this);
        mäng.kontrolliTegevusi();
    }
}
