package kevin.park.egyptianratfun;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;

public class Card {
    String[] names = new String[52];
    int[] values = new int[52];
    Rect[] cardFrames = new Rect[52];
    BitmapDrawable cardImage;
    Bitmap cardBitmap;
    Context mContext;

    public Card(Context context){
        mContext = context;
        setCardValues();
        initializeImages();
    }

    private void setCardValues() {
        names[0] = "Ace of Spades"; values[0] = 1;
        names[1] = "Two of Spades"; values[1] = 2;
        names[2] = "Three of Spades"; values[2] = 3;
        names[3] = "Four of Spades"; values[3] = 4;
        names[4] = "Five of Spades"; values[4] = 5;
        names[5] = "Six of Spades"; values[5] = 6;
        names[6] = "Seven of Spades"; values[6] = 7;
        names[7] = "Eight of Spades"; values[7] = 8;
        names[8] = "Nine of Spades"; values[8] = 9;
        names[9] = "Ten of Spades"; values[9] = 10;
        names[10] = "Jack of Spades"; values[10] = 11;
        names[11] = "Queen of Spades"; values[11] = 12;
        names[12] = "King of Spades"; values[12] = 13;
        names[13] = "Ace of Hearts"; values[13] = 1;
        names[14] = "Two of Hearts"; values[14] = 2;
        names[15] = "Three of Hearts"; values[15] = 3;
        names[16] = "Four of Hearts"; values[16] = 4;
        names[17] = "Five of Hearts"; values[17] = 5;
        names[18] = "Six of Hearts"; values[18] = 6;
        names[19] = "Seven of Hearts"; values[19] = 7;
        names[20] = "Eight of Hearts"; values[20] = 8;
        names[21] = "Nine of Hearts"; values[21] = 9;
        names[22] = "Ten of Hearts"; values[22] = 10;
        names[23] = "Jack of Hearts"; values[23] = 11;
        names[24] = "Queen of Hearts"; values[24] = 12;
        names[25] = "King of Hearts"; values[25] = 13;
        names[26] = "Ace of Clubs"; values[26] = 1;
        names[27] = "Two of Clubs"; values[27] = 2;
        names[28] = "Three of Clubs"; values[28] = 3;
        names[29] = "Four of Clubs"; values[29] = 4;
        names[30] = "Five of Clubs"; values[30] = 5;
        names[31] = "Six of Clubs"; values[31] = 6;
        names[32] = "Seven of Clubs"; values[32] = 7;
        names[33] = "Eight of Clubs"; values[33] = 8;
        names[34] = "Nine of Clubs"; values[34] = 9;
        names[35] = "Ten of Clubs"; values[35] = 10;
        names[36] = "Jack of Clubs"; values[36] = 11;
        names[37] = "Queen of Clubs"; values[37] = 12;
        names[38] = "King of Clubs"; values[38] = 13;
        names[39] = "Ace of Diamonds"; values[39] = 1;
        names[40] = "Two of Diamonds"; values[40] = 2;
        names[41] = "Three of Diamonds"; values[41] = 3;
        names[42] = "Four of Diamonds"; values[42] = 4;
        names[43] = "Five of Diamonds"; values[43] = 5;
        names[44] = "Six of Diamonds"; values[44] = 6;
        names[45] = "Seven of Diamonds"; values[45] = 7;
        names[46] = "Eight of Diamonds"; values[46] = 8;
        names[47] = "Nine of Diamonds"; values[47] = 9;
        names[48] = "Ten of Diamonds"; values[48] = 10;
        names[49] = "Jack of Diamonds"; values[49] = 11;
        names[50] = "Queen of Diamonds"; values[50] = 12;
        names[51] = "King of Diamonds"; values[51] = 13;
        
    }

    public Rect getCardFrame(int i){
        return cardFrames[i];
    }

    public Bitmap getCardImage(){
        return cardImage.getBitmap();
    }

    public void initializeImages(){
        for(int i = 0; i < 52; i++){cardFrames[i] = new Rect();}
        //Note: Width = 44, Height = 60
        //Goes from A - K left to right, clubs, diamonds, hearts, spades
        cardImage = (BitmapDrawable)mContext.getResources().getDrawable(R.drawable.cards, null);
        for(int i = 0; i < 13; i++){ //Spades
            cardFrames[i].set(0+i*44, 180, 44*(i+1), 240);
        }
        for(int i = 13; i < 26; i++){ //Hearts
            cardFrames[i].set(0+(i-13)*44,120,44*(i-12),180);
        }
        for(int i = 26; i < 39; i++){ //Clubs
            cardFrames[i].set(0+(i-26)*44,0,44*(i-25),60);
        }
        for(int i = 39; i < 52; i++){ //Diamonds
            cardFrames[i].set(0+(i-39)*44,60,44*(i-38),120);
        }
    }

    public String getColor(int value){
        if(value >= 0 && value < 13 && value >= 26 && value < 39){return "black";}
        else{return "red";}
    }

    public String getSuit(int value){
        if(value > 1 && value <= 13){return "Spades";}
        if(value > 13 && value <= 26){return "Hearts";}
        if(value > 26 && value <= 39){return "Clubs";}
        if(value > 39 && value <= 52){return "Diamonds";}
        return "None";
    }

    public String getName(int i){return names[i];}

    public int getValue(int i){return values[i];}
}
