import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.shuffle;

public class Kaardipakk {
    List<Kaart> pakk;

    public Kaardipakk() {
        this.pakk = uus_pakk();
        this.sega_kaardid();
    }

    public String toString() {
        String kaardid = "";
        for (Kaart kaart : pakk) {
            kaardid += kaart + " ";
        }
        return kaardid;
    }

    public List<Kaart> uus_pakk() {
        List<Kaart> tulemus = new ArrayList<>();
        String[] mastid = {"poti","ärtu","risti","ruutu"};
        String[] numbrid = {"2","3","4","5","6","7","8","9","10","J","Q","K","A"};
        for (int i = 0; i < mastid.length; i++) {
            for (int j = 0; j < numbrid.length; j++) {
                tulemus.add(new Kaart(mastid[i],numbrid[j]));
            }
        }
        return tulemus;
    }

    public void sega_kaardid(){
        shuffle(this.pakk);
    }

    public void eemalda_kaart(Kaart kaart) {
        this.pakk.remove(kaart);
    }

    public Kaart anna_kaart() {
        Kaart antav = this.pakk.get(0);
        eemalda_kaart(antav);
        return antav;
    }

    public List<Kaart> getPakk() {
        return pakk;
    }

    public void setPakk(List<Kaart> pakk) {
        this.pakk = pakk;
    }

}
