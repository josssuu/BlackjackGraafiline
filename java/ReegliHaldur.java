
import javafx.scene.control.TextArea;
import java.io.*;


public class ReegliHaldur extends Haldur {
    private String reegliFailiTee = "src/main/resources/reeglid.txt";

    private Nupp tagasiNupp;
    private TextArea reeglid;

    public ReegliHaldur() {
        laeReeglid();
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

    private void laeReeglid() {
        reeglid = new TextArea();
        // test
        StringBuilder sisu = new StringBuilder();
        try {
            BufferedReader bf = new BufferedReader(new FileReader("src/main/resources/reeglid.txt"));
            String rida = bf.readLine();
            while (rida != null) {
                sisu.append(rida);
                rida = bf.readLine();
            }
        }
        catch (Exception e) {
            System.out.println(e + " reeglite viga");
        }
        reeglid.setText(sisu.toString());
        reeglid.setEditable(false);
    }
}
