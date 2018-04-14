package slotyingchoop;

import javafx.scene.image.Image;

class Rune {

    private String name;
    private String picture;
    private Image image;

    public Rune(String name) {
        this.name = name;
        picture = "image/rune" + name + ".png";
        image = new Image(picture, 93, 131, true, true);
    }

    public String getName() {
        return name;
    }

    public Image getImage() {
        return image;
    }

}