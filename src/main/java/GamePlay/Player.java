package GamePlay;

import Client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Player extends Client {
    private int winSets;
    private boolean turn;


    public Player(String name,String id){
        super(name,id);
        this.winSets = 0;
        this.turn = false;

//        myHandPanelSetting();

    }




    public int getWinSets() {
        return winSets;
    }
    public boolean isTurn(){
        return turn;
    }

    public void changeTurn() {
        this.turn = ! this.turn;
    }

    public void addWinSets() {
        this.winSets ++;
    }



    }


