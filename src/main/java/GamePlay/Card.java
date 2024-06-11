package GamePlay;

import javax.swing.*;

public class Card {
    private int number;
    private String type;
    private ImageIcon rooImage;
    private String rooPath;
    private ImageIcon poshtImage;
    private String poshtPath;
    public Card(int number,String type,String rooPath,String poshtPath){
        this.number = number;
        this.type = type;
        this.rooImage = new ImageIcon(rooPath);
        this.rooPath = rooPath;
        this.poshtImage = new ImageIcon(poshtPath);
        this.poshtPath = poshtPath;
    }

    public int getNumber() {
        return number;
    }

    public String getType() {
        return type;
    }

    public ImageIcon getRoo() {
        return rooImage;
    }
    public String getRooPath(){
        return rooPath;
    }

    public ImageIcon getPosht() {
        return poshtImage;
    }
    public String getPoshtPath(){
        return poshtPath;
    }
}
