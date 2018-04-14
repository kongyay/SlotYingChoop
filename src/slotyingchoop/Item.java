package slotyingchoop;

import javafx.scene.image.Image;

// Item is abstract class that can be polymorphed into Rock, Paper, Scissor, Shield, Grass, Torpedo
public abstract class Item {
    private String name;
    private String picture;
    private Image image;
    private String winList = "";
    private String lostList = "";
    private int damage = 0;
    private static final int SMALL_DAMAGE = 1;
    private static final int BIG_DAMAGE = 2;

    public Item() {
        this("Grass");
    }

    public Item(String name) {
        this(name, false);
    }

    public Item(String name, boolean isBig) {
        this.name = name;
        if (!isBig) {
            picture = "image/small";
            damage = SMALL_DAMAGE;
        } else {
            picture = "image/big";
            damage = BIG_DAMAGE;
        }

        switch (name) {
            case "Rock":
                picture += "Rock.png";
                winList = "Scissor";
                lostList = "Paper";
                break;
            case "Paper":
                picture += "Paper.png";
                winList = "Rock";
                lostList = "Scissor";
                break;
            case "Scissor":
                picture += "Scissor.png";
                winList = "Paper";
                lostList = "Rock";
                break;
            case "Shield":
                picture += "Shield.png";
                break;
            case "Grass":
                damage = 0;
                picture = "image/Grass.png";
                break;
            case "Torpedo":
                damage = BIG_DAMAGE;
                picture = "image/Torpedo.png";
                break;
        }

        image = new Image(picture, 55, 55, true, true);
    }

    public String getName() {
        return name;
    }

    public Image getImage() {
        return image;
    }

    public String getWinList() {
        return winList;
    }

    public String getLostList() {
        return lostList;
    }

    public int getDamage() {
        return damage;
    }    
}

// Item's childs for polymorphism
class Rock extends Item {
    public Rock(boolean isBig) {
        super("Rock",isBig);
    }
}

class Paper extends Item {
    public Paper(boolean isBig) {
        super("Paper",isBig);
    }
}

class Scissor extends Item {
    public Scissor(boolean isBig) {
        super("Scissor",isBig);
    }
}

class Shield extends Item {
    public Shield(boolean isBig) {
        super("Shield",isBig);
    }
}

class Torpedo extends Item {
    public Torpedo() {
        super("Torpedo");
    }
}

class Grass extends Item {
    public Grass() {
        super();
    }
}