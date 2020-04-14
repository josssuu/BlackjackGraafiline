public class Kaart {
    private String mast;
    private String number;
    private int väärtus;

    public Kaart(String mast, String number) {
        this.mast = mast;
        this.number = number;
        if (number.chars().allMatch(Character::isDigit)) this.väärtus = Integer.parseInt(number);
        else if (number.equals("A")) this.väärtus = 1;
        else this.väärtus = 10;

    }

    public int getVäärtus() {
        return väärtus;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return this.mast + "_" + this.number;
    }
}
