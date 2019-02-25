package com.company;

import java.util.Random;

public class Car {
    //private String number;      //Уникальный номер машины
    private int index;          //Порядковый номер машины

    public Car(int index) {
        this.index = index;
        //numberGenerator();
    }

    public int getIndex() {
        return index;
    }

    /*public String getNumber() {
        return number;
    }*/

    /*private void numberGenerator() {
        StringBuilder string = new StringBuilder();
        string.append(NumberLetterTable.values()[new Random().nextInt(12)]);
        string.append((char) new Random().nextInt(10) + 48);
        string.append((char) new Random().nextInt(10) + 48);
        string.append((char) new Random().nextInt(10) + 48);
        string.append(NumberLetterTable.values()[new Random().nextInt(12)]);
        string.append(NumberLetterTable.values()[new Random().nextInt(12)]);
        int r = new Random().nextInt(2) + 2;
        for (int i = 0; i < r; i++) {
            string.append(new Random().nextInt(10));
        }
        number = string.toString();
    }

    private enum NumberLetterTable {
        A,
        B,
        E,
        K,
        M,
        H,
        O,
        P,
        C,
        T,
        Y,
        X
    }*/
}
