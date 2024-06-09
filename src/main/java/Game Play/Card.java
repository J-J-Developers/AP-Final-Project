import javax.swing.*;

public class Card {
    private int number;
    private String type;
    private ImageIcon roo;
    private ImageIcon posht;
    public Card(int number,String type,String rooPath,String poshtPath){
        this.number = number;
        this.type = type;
        this.roo = new ImageIcon(rooPath);
        this.posht = new ImageIcon(poshtPath);
    }

    public int getNumber() {
        return number;
    }

    public String getType() {
        return type;
    }

    public ImageIcon getRoo() {
        return roo;
    }

    public ImageIcon getPosht() {
        return posht;
    }

}
