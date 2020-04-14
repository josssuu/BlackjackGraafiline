import javafx.scene.input.MouseButton;

public class MenüüHaldur extends Haldur{
    private Nupp väljuNupp;
    private Nupp startNupp;
    private Nupp reegliNupp;

    public MenüüHaldur() {
        looNupud();
        kontrolliNuppe();
    }

    private void looNupud() {
        looVälju();
        looReeglid();
        looStart();
    }

    private void kontrolliNuppe() {
        kontrolliVälju();
        kontrolliReeglid();
        kontrolliStart();
    }

    private void kontrolliVälju() {
        väljuNupp.setOnMouseClicked(mouseEvent -> System.exit(0));
    }

    private void kontrolliReeglid() {
        reegliNupp.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                ReegliHaldur haldur = new ReegliHaldur();
                juur.getChildren().setAll(haldur.getJuur());
            }
        });
    }

    private void kontrolliStart() {
        startNupp.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                MänguHaldur haldur = new MänguHaldur();
                juur.getChildren().setAll(haldur.getJuur());
            }
        });
    }

    private void looVälju() {
        väljuNupp = new Nupp("Välju", 505, 550);
        juur.getChildren().add(väljuNupp);
    }

    private void looReeglid() {
        reegliNupp = new Nupp("Reeglid", 505, 450);
        juur.getChildren().add(reegliNupp);
    }

    private void looStart() {
        startNupp = new Nupp("Alusta", 505, 350);
        juur.getChildren().add(startNupp);
    }
}
