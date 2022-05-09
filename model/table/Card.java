package model.table;

import java.util.Locale;
import java.util.Objects;

public class Card {

    public enum Color {
        RED, BLUE, GREEN, YELLOW, ALL
    }


    public enum TypesOfCard {
        DRAW, REVERSE, SKIP, WILD, WILDDRAW, NUMBER
    }

    private String color;
    private final int value;

    //
//    private boolean isSpecial;
    private final String typeOfCard;

    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return String.format("%s_%d_%s", getColor().toLowerCase(Locale.ROOT), getValue(), getTypeOfCard().toLowerCase(Locale.ROOT));
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getValue() {
        return value;
    }
//
//    public boolean isSpecial() {
//        return isSpecial;
//    }
//
//    public void setSpecial(boolean special) {
//        isSpecial = special;
//    }

    public String getTypeOfCard() {
        return typeOfCard;
    }


    public Card(String color, int value, String typeOfCard) {
        this.color = color;
        this.value = value;
//        this.isSpecial = isSpecial;
        this.typeOfCard = typeOfCard;

    }

    @Override
    public boolean equals(Object o) {
        return this == o;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, value, typeOfCard);
    }

}
