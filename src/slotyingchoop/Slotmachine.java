package slotyingchoop;


class Slotmachine {

    private Rune[] allRunes = new Rune[5];
    private Rune[] runeShow = new Rune[3];
    private Player owner;
    private int[] runeIndex = new int[3];

    Slotmachine(Player owner) {
        allRunes[0] = new Rune("Rock");
        allRunes[1] = new Rune("Paper");
        allRunes[2] = new Rune("Scissor");
        allRunes[3] = new Rune("Shield");
        allRunes[4] = new Rune("Grass");

        runeShow[0] = new Rune("Rock");
        runeShow[1] = new Rune("Paper");
        runeShow[2] = new Rune("Scissor");

        runeIndex[0] = 0;
        runeIndex[1] = 1;
        runeIndex[2] = 2;

    }

    public Rune spinStart(int i) {
        runeShow[i] = allRunes[(++runeIndex[i]) % 5];
        return runeShow[i];
    }

    public Rune getRuneShow(int i) {
        return runeShow[i];
    }

    public Player getOwner() {
        return owner;
    }

    public Item getItem() {
        Rune runeItem;
        boolean isBig = false;
        // TRIPLE
        if (runeShow[0].getName().equals(runeShow[1].getName()) && runeShow[0].getName().equals(runeShow[2].getName()) && runeShow[1].getName().equals(runeShow[2].getName())) {
            return new Torpedo();
        }
        // DOUBLE
        else if (runeShow[0].getName().equals(runeShow[1].getName()) || runeShow[0].getName().equals(runeShow[2].getName())) {
            runeItem = runeShow[0];    
            isBig = true;
        } 
        else if (runeShow[1].getName().equals(runeShow[2].getName())) {
            runeItem = runeShow[1]; 
            isBig = true;
        } 
        // SINGLE
        else {
            runeItem = runeShow[1]; 
        }
        
        switch (runeItem.getName()) {
                case "Rock":
                    return new Rock(isBig);
                case "Paper":
                    return new Paper(isBig);
                case "Scissor":
                    return new Scissor(isBig);
                case "Shield":
                    return new Shield(isBig);
                case "Grass":
                    return new Grass();
                default:
                    return null;
        }
    }

    public void giveItem() {
        owner.setItem(this.getItem());
    }

}