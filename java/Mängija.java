import java.util.ArrayList;
import java.util.List;

public class Mängija {
    String nimi;
    int raha;
    List<Kaart> käsi;

    public Mängija() {
        this.nimi = "";                 // hiljem võib nime ka muuta
        this.raha = 100;            // algselt 100 peaks okei olema
        this.käsi = new ArrayList<>();
    }

    public Mängija(String nimi, int raha) {
        this.nimi = nimi;
        this.raha = raha;
        this.käsi = new ArrayList<>();
    }

    public void võtaKaart(Kaart kaart) {
        this.käsi.add(kaart);
    }

    public void näitaKaarte() {
        System.out.print(nimi + " kaardid: ");
        for (Kaart kaart : käsi) {
            System.out.print(kaart + " ");
        }
        System.out.print("\n");
    }

    public int käeVäärtus() {
        int summa = 0;
        int ässadeArv = 0;
        for (Kaart kaart : käsi) {
            if (kaart.getNumber().equals("A")) {
                ässadeArv++;
            }
            else {
                summa += kaart.getVäärtus();
            }
        }

        for (int i = 0; i < ässadeArv; i++) {
            if (summa >= 11) {
                summa++;
            }
            else {
                summa += 11;
            }
        }

        return summa;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public void setRaha(int raha) {
        this.raha = raha;
    }

    public void setKäsi(List<Kaart> käsi) {
        this.käsi = käsi;
    }

    public String getNimi() {
        return this.nimi;
    }

    public List<Kaart> getKäsi() {
        return this.käsi;
    }

    public int getRaha() {
        return raha;
    }

    @Override
    public String toString() {
        return "Mängija{" +
                "nimi='" + nimi + '\'' +
                ", raha=" + raha +
                ", käsi=" + käsi +
                '}';
    }
}
