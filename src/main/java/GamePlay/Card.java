package GamePlay;

import javax.swing.*;
import java.io.Serializable;

public class Card implements Serializable {
    private int number;
    private String type;
    private transient ImageIcon rooImage;
    private String rooPath;

    public Card(int number,String type,String rooPath){
        this.number = number;
        this.type = type;
        this.rooPath = rooPath;
    }

    public int getNumber() {
        return number;
    }

    public String getType() {
        return type;
    }

    public ImageIcon getRooImage() {
        if (rooImage == null){
            rooImage = new ImageIcon(rooPath);
        }
        return rooImage;
    }
    public String getRooPath(){
        return rooPath;
    }
}
