package kevin.park.egyptianratfun;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class CardTable extends View {

    Context mContext;
    String[] pile_cards = new String[52];
    int[] pile_ids = new int[52];
    int pile_size = 0;
    Rect[] card_images = new Rect[6];
    Card card;
    Paint paint = new Paint();

    public CardTable(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        card = new Card(mContext);
        paint.setColor(Color.BLACK); paint.setTextSize(80); paint.setStyle(Paint.Style.FILL);
        for(int i = 0; i < 6; i++){card_images[i] = new Rect();}
        card_images[0].set(50, 450, 194, 650);
        card_images[1].set(120, 400, 264, 600);
        card_images[2].set(190, 350, 334, 550);
        card_images[3].set(260, 300, 404, 500);
        card_images[4].set(330, 250, 474, 450);
        card_images[5].set(400, 200, 544, 400);
    }

    @Override
    protected void onDraw(Canvas c) {
        if(pile_size > 0 && pile_cards[pile_size-1] != null){
            if(pile_size > 5){c.drawBitmap(card.getCardImage(), card.getCardFrame(pile_ids[pile_size-6]-1), card_images[5], null);}
            if(pile_size > 4){c.drawBitmap(card.getCardImage(), card.getCardFrame(pile_ids[pile_size-5]-1), card_images[4], null);}
            if(pile_size > 3){c.drawBitmap(card.getCardImage(), card.getCardFrame(pile_ids[pile_size-4]-1), card_images[3], null);}
            if(pile_size > 2){c.drawBitmap(card.getCardImage(), card.getCardFrame(pile_ids[pile_size-3]-1), card_images[2], null);}
            if(pile_size > 1){ c.drawBitmap(card.getCardImage(), card.getCardFrame(pile_ids[pile_size-2]-1), card_images[1], null);}
                c.drawBitmap(card.getCardImage(), card.getCardFrame(pile_ids[pile_size-1]-1), card_images[0], null);

            /*c.drawText(pile_cards[pile_size-1],300,300,paint);
                    if(pile_size > 1){c.drawText(pile_cards[pile_size-2],300,300+100,paint);}
                    if(pile_size > 2){c.drawText(pile_cards[pile_size-3],300,300+200,paint);}
                    if(pile_size > 3){c.drawText(pile_cards[pile_size-4],300,300+300,paint);}
                    if(pile_size > 4){c.drawText(pile_cards[pile_size-5],300,300+400,paint);}
                    if(pile_size > 5){c.drawText(pile_cards[pile_size-6],300,300+500,paint);}
                    if(pile_size > 6){c.drawText(pile_cards[pile_size-7],300,300+600,paint);}
            */
    }}

    public void setText(int input){
        Log.d("HELPME", ""+input+" "+pile_cards[pile_size]);
        pile_cards[pile_size] = card.getName(input-1);
        pile_ids[pile_size] = input;
        pile_size++;
        invalidate();
    }

    public String slapLogic(){
        int[] last_three_values = new int[3]; boolean good_slap = false;
        if(pile_size >= 2){
        int[] last_three = new int[3]; //Takes the top of the stack, top card first
        last_three[0] = pile_ids[pile_size-1];
        last_three[1] = pile_ids[pile_size-2];

        if(pile_size == 2){last_three[1] = pile_ids[pile_size-2];
            for(int i = 0; i < 2; i++){
                last_three_values[i] =
                        card.getValue(last_three[i]);
            }
        }
        if(pile_size > 2){last_three[2] = pile_ids[pile_size-3];
            for(int i = 0; i < 3; i++){
                last_three_values[i] =
                        card.getValue(last_three[i]-1);
            }
        }


        //Is it a straight?
            if(last_three_values[2]-last_three_values[1] == 1 && last_three_values[1]-last_three_values[0] == 1 && last_three_values[2]-last_three_values[0] == 2){
                good_slap = true;
                return "Straight";
            }
            if(last_three_values[2]-last_three_values[1] == -1 && last_three_values[1]-last_three_values[0] == -1 && last_three_values[2]-last_three_values[0] == -2){
                good_slap = true;
                return "Straight";
            }
        //Is it a pair?
            if(last_three_values[0] == last_three_values[1]){
                good_slap = true;
                return "Pair";
            }
        //Is it a sandwich?
            if(last_three_values[0] == last_three_values[2] && last_three_values[0] != 0){
                good_slap = true;
                return "Sandwich";
            }
        //Is it a flush?
            if(card.getSuit(last_three[0]) == card.getSuit(last_three[1]) && card.getSuit(last_three[1]) == card.getSuit(last_three[2])
                    && card.getSuit(last_three[0]) == card.getSuit(last_three[2])){
                good_slap = true;
                return "Flush";
            }
        }
        //if(!good_slap){Log.d("LOL YOU STUPID", ":)");}
        return "WRONG";
    }

    public void updateStack(){ //Updates a stack post burn
        pile_ids = getNewStack();
    }

    public int checkforRoyals(){
        if(card.getValue(pile_ids[pile_size-1]-1) == 1 ||card.getValue(pile_ids[pile_size-1]-1) == 11 ||
                card.getValue(pile_ids[pile_size-1]-1) == 12 || card.getValue(pile_ids[pile_size-1]-1) == 13){
                //Log.d("HELPME", "A "+ card.getName(pile_ids[pile_size-1]-1) +" HAS APPEARED");

                return  card.getValue(pile_ids[pile_size-1]-1);
        }
        return 0;
    }

    public int[] getNewStack(){
        int[] swapper_pile = new int[52];
        int swapper = pile_ids[pile_size-1];
        for(int i = 1; i < pile_size; i++){
            swapper_pile[i] = pile_ids[i-1];
        }
        swapper_pile[0] = swapper;
        return swapper_pile;
    }

    public int getPile_size() {
        return pile_size;
    }

    public int[] takeStack(int decksize, int[] deck){
        int[] newDeck = deck;
        for(int i = 0; i < pile_size; i++){
            newDeck[decksize+i] = pile_ids[i];
        }
        pile_size = 0; invalidate();
        return newDeck;
    }
    }

