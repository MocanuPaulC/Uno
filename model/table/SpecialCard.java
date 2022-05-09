package model.table;

public class SpecialCard extends Card{
    private final int nrToDraw;

    public int getNrToDraw() {
        return nrToDraw;
    }

    public SpecialCard(String color, int value, boolean isSpecial, String typeOfCard) {
        super(color, value, typeOfCard);
        if(typeOfCard.equals("DRAW"))nrToDraw=2;
        else if(typeOfCard.equals("WILDDRAW"))nrToDraw=4;
        else nrToDraw=0;
    }
}
