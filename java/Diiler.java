import java.util.ArrayList;
import java.util.List;

public class Diiler {
    private List<Kaart> käsi;

    public Diiler() {
        this.käsi = new ArrayList<>();
    }

    public void võtaKaart(Kaart kaart) {
        this.käsi.add(kaart);
    }

    public int käeVäärtus() {
        int summa = 0;
        int ässadeArv = 0;
        for (Kaart kaart : käsi) {
            if (kaart.getNumber().equals("A")) {
                ässadeArv++;
            } else {
                summa += kaart.getVäärtus();
            }
        }

        for (int i = 0; i < ässadeArv; i++) {
            if (summa >= 11) {
                summa++;
            } else {
                summa += 11;
            }
        }

        return summa;
    }

    public void käik(Kaardipakk k) {
        System.out.println("Diiler teeb oma käigu...");

        System.out.print("Diileri kaardid: ");
        Kaart võetav;

        while (käeVäärtus() < 17) {
            võetav = k.anna_kaart();
            võtaKaart(võetav);
            System.out.print(võetav + " ");
        }
        System.out.println("   - " + this.käeVäärtus());
    }

    public void setKäsi(List<Kaart> käsi) {
        this.käsi = käsi;
    }

    public List<Kaart> getKäsi() {
        return käsi;
    }
}
