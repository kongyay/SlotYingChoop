package slotyingchoop;

import javafx.scene.image.ImageView;

class Player {

    private Item[] itemList = new Item[9]; 
    private int itemLength = 0;
    private ImageView[] itemImageView = new ImageView[9];

    public void setItem(Item item) {
        itemList[itemLength++] = item;
    }

    public Item getItem(int i) {
        return itemList[i];
    }
    
    public void clearItem() {
        itemList = new Item[9];
        itemLength = 0;
        itemImageView = new ImageView[9];
    }
    
    public ImageView getItemImageView(int i) {
        return itemImageView[i];
    }
    public void setItemImageView(int i,ImageView img) {
        itemImageView[i] = img;
    }

    public int getItemLength() {
        return itemLength;
    }

    public void shuffleItem(int a, int b) {
        Item temp = itemList[a];
        itemList[a] = itemList[b];
        itemList[b] = temp;
        
        ImageView temp2 = itemImageView[a];
        itemImageView[a] = itemImageView[b];
        itemImageView[b] = temp2;
    }

}

