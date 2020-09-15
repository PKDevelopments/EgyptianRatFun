package kevin.park.egyptianratfun;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //GUI Stuff
    CardTable tableView;
    Button playButton;
    GestureDetector gestureDetector;
    TextView judgeView;
    TextView p1score;
    TextView p2score;
    TextView stackView;
    //Reference vars
    //Game vars
    boolean testing = false;
    boolean game_started = false;
    boolean game_over = false;
    int players = 2;
    int player_turn;
    int[] deck1 = new int[52]; int deck1size;
    int[] deck2 = new int[52]; int deck2size;
    int[] basedeck = new int[52];
    int[] tablestack = new int[52]; int stacksize = 0;
    int[] shuffledDeck = new int[52];
    Random shuffler = new Random();

    //AI Logic
    private static int FRAME_RATE = 50;
    int turnwait_count; int slapwait_count;
    int turnwaitlimit = 50; int slapwait_limit = 20;
    boolean waiting = false;
    Handler h = new Handler();

    //Royal Card Logic
    int royal_countdown; int royal_limit;
    boolean royal_active;
    boolean pile_open;

    // each card will have an int number assigned to them, A-K. Order will be Spades, Hearts, Clubs, Diamonds
    /**
     * 1: Ace of Spades
     * 2: 2 of Spades
     * 3: 3 of Spades
     * 4: 4
     * 5: 5
     * 6: 6
     * 7: 7
     * 8: 8
     * 9: 9
     * 10: 10
     * 11: J
     * 12: Q
     * 13: K
     * and etc.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Initialize();
    }

    private void addCards(int player){
        int pile_size = 0;
        switch(player){
            case 0:
                pile_size = tableView.getPile_size();
                deck1 = tableView.takeStack(deck1size, deck1);
                deck1size+=pile_size;
                p1score.setText("P1 Cards Remaining: "+deck1size);
                deck1 = ShuffleDeck(0);
                break;
            case 1:
                pile_size = tableView.getPile_size();
                deck2 = tableView.takeStack(deck2size, deck2);
                deck2size+=pile_size;
                deck2 = ShuffleDeck(1);
                p2score.setText("P2 Cards Remaining: "+deck2size);
                break;
        }
        checkDecksforDuplicates();
        if(player == 0){
            for(int i = 0; i < deck1size; i++){
                }
            }
        if(player == 1){
            for(int i = 0; i < deck2size; i++){
            }
        }
        updateStack();
    }

    private void burnCard(){ //This function applies only if Player 1 (you) was wrong
        if(!game_over){
            PlayCard(0);
            MoveTopCardtoBottom();
        }
    }
    //This is only intended for troubleshooting use, return a log whether there are duplicates in a deck or not
    private boolean checkDecksforDuplicates(){
        for(int i = 0; i < deck1size; i++){
            for(int j = 0; j < deck2size; j++){
                if(deck1[i] == deck2[j]){
                    return true;
                }
            }
        }
        return false;
    }

    //This is just for the base deck
    private boolean checkForDuplicates(int input){
        int matchcount = 0;
        for(int i = 0; i < 52; i++){
            if(shuffledDeck[input] == shuffledDeck[i]){
                matchcount++;
            }
            if(matchcount > 1){return true;}
        }
        return false;
    }

    //This is for player decks, inputs are the new deck and its index number
    //new card = input[index]
    private boolean checkForDuplicates(int[] input, int index){
        int matchcount = 0;
                for(int i = 0; i <= index; i++) {
                    if (input[index] == input[i]){
                        matchcount++;
                        if(matchcount > 1){return true;}
                    }
                }
        return false;
    }

    public void checkforPlay(){
        switch(player_turn){
            case 0:
                break;
            case 1:
                if(!pile_open){turnwait_count++;}
                if(turnwait_count > turnwaitlimit){
                PlayCard(player_turn);
                turnwait_count = 0;
                }
                break;
        }
    }

    public void checkforSlap(){
        String result = tableView.slapLogic();
        if(result != "WRONG"){
            waiting = true;
            if(waiting){
                slapwait_count++;
                if(slapwait_count >= slapwait_limit){
                    judgeView.setText(result);
                    addCards(1);
                    slapwait_count = 0; royal_active = false; royal_countdown = 0; royal_limit = 0;
                    //Note: This code ONLY works because it is TWO PLAYER!!!
                    if(player_turn != 1){updateTurn(1);}
                }
            }
        }
        else{waiting = false;
        }
    }

    //Deals cards evenly amongst players. NOTE: WILL NOT WORK FOR NUMBERS INDIVISIBLE BY 52
    private void Deal(int player_num){
        int decksizes = 52/player_num;
        deck1size = deck2size = decksizes;

        switch(players){
            case 2: //2 Players
                for(int i = 0; i < deck1size; i++){
                    deck1[i] = shuffledDeck[i];
            }
               for(int i = 0; i < deck2size; i++){
                   deck2[i] = shuffledDeck[i+decksizes];
            }
            checkDecksforDuplicates();
            break;
        }
    }

    private void Initialize(){
        tableView = findViewById(R.id.cardTable);
        judgeView = findViewById(R.id.judgeView);
        p1score = findViewById(R.id.p1score);
        p2score = findViewById(R.id.p2score);
        stackView = findViewById(R.id.stackView);
        playButton = findViewById(R.id.playbutton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pile_open){
                    if(player_turn == 0){addCards(1);}
                    if(player_turn == 1){addCards(0);}
                    pile_open = false; royal_active = false; royal_countdown = 0; royal_limit = 0;
                }

                if((player_turn == 0 || testing) && !pile_open){
                PlayCard(player_turn);}
                if(!game_started){game_started = true;}



            }
        });
        for(int i = 0; i < 52; i++){
            basedeck[i] = i+1; //Each card is assigned a value
        }
        Shuffle();
        Deal(players);
        gestureDetector = new GestureDetector(this, gestureListener);
        r.run();
    }
    GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onDown(MotionEvent e) {
            if(!game_over && game_started){ SlapLogic(); }
            return super.onDown(e);
        }
    };

    private void MoveTopCardtoBottom(){
        tableView.updateStack();
    }

    private void PlayCard(int player){
        judgeView.setText(" ");
        switch(player){
            case 0: //P1
                if(deck1size >= 1
                        && stacksize < 52){
                tablestack[stacksize] = deck1[deck1size-1];
                    tableView.setText(deck1[deck1size-1]);
                deck1size--;
                deck1[deck1size] = 0;
                stacksize++;}
                checkforLoss();
                break;
            case 1:
                if(deck2size >= 1  && stacksize < 52){
                    //Update the Stack with P2's top card
                tablestack[stacksize] = deck2[deck2size-1];
                    //Update the Table with the new card
                    tableView.setText(deck2[deck2size-1]);
                deck2size--;
                deck2[deck2size] = 0;
                stacksize++;}
                checkforLoss();
                break;
        }
        p1score.setText("P1 Cards Remaining: "+deck1size);
        p2score.setText("P2 Cards Remaining: "+deck2size);
        stackView.setText("Stack Size: "+stacksize);
        checkforRoyals();
        updateTurn();

    }

    private void checkforLoss() {
        if(deck1size == 0){judgeView.setText("P2 WINS"); game_over = true; pile_open = false; resetGame();}
        if(deck2size == 0){judgeView.setText("P1 WINS"); game_over = true; pile_open = false; resetGame();}
    }

    private void resetGame(){

    }

    private void checkforRoyals(){ //Returns true if there is currently a royal at the top of the stack

        if(tableView.checkforRoyals() != 0){
           switch(tableView.checkforRoyals()) {
               case 1: //Ace
                   royal_limit = 4; royal_countdown = 0;
                   royal_active = true;
                   break;
               case 11: //Jack
                   royal_limit = 1; royal_countdown = 0;
                   royal_active = true;
                   break;
               case 12: //Queen
                   royal_limit = 2; royal_countdown = 0;
                   royal_active = true;
                   break;
               case 13: //King
                   royal_limit = 3; royal_countdown = 0;
                   royal_active = true;
                   break;
           }
        }

        if(royal_active){
            if(royal_countdown > 0){ //If other player didn't play a royal, make it his turn again
                updateTurn();
            }
            royal_countdown++;
            if(royal_countdown > royal_limit){
                royal_active = false; royal_limit = 0; royal_countdown = 0;
                //Have the opposite player take the pile
                pile_open = true; //Player can take the pile or slap as desired

                if(pile_open){
                    if(player_turn == 0){judgeView.setText("P1 can take the Stack.");}
                    if(player_turn == 1){judgeView.setText("P2 can take the Stack.");}
                    }
            }
        }
    }

    private void resetbaseDeck(){
        for(int i = 0; i < 52; i++){
            basedeck[i] = 0;
        }
    }

    final Runnable r = new Runnable() {
        public void run() {
            h.postDelayed(this, FRAME_RATE);
            if(!testing){
                checkforSlap();
                checkforPlay();}
        }
    };

    private void Shuffle(){ //FOR BASE DECK ONLY
        for(int i = 0; i < 52; i++){
            //Assign a card to the deck shuffler
                shuffledDeck[i] = basedeck[shuffler.nextInt(52)];
            //Pick a different card if there's a duplicate found
                while(checkForDuplicates(i)){
                    shuffledDeck[i] = basedeck[shuffler.nextInt(52)];
                }
            }
        resetbaseDeck();
        }

    private int[] ShuffleDeck(int player){
        int[] shuffled = new int[52];
        switch(player){
            case 0:
                for(int i = 0; i < deck1size; i++){
                    shuffled[i] = deck1[shuffler.nextInt(deck1size)];
                    while(checkForDuplicates(shuffled, i)) {
                        shuffled[i] = deck1[shuffler.nextInt(deck1size)];
                    }
                }
                break;
            case 1:
                for(int i = 0; i < deck2size; i++){
                    shuffled[i] = deck2[shuffler.nextInt(deck2size)];
                    while(checkForDuplicates(shuffled, i)) {
                        shuffled[i] = deck2[shuffler.nextInt(deck2size)];
                    }
                }
                break;
        }
        return shuffled;
    }

    private void SlapLogic(){ //This is Player 1's slap logic
        String result = tableView.slapLogic();
        String message = "";
        switch(result){
            case "WRONG": message = "LOL YOU\'RE WRONG";
                burnCard(); //Assumes player screwed this up
            break;
            default: //This means that Player 1 slapped correctly
                message = result;
                addCards(0);
                slapwait_count = 0; royal_active = false; royal_countdown = 0; royal_limit = 0;
                if(player_turn != 0){updateTurn(0);}
            break;
        }
        judgeView.setText(message);
        checkforLoss();
    }

    private void updateStack(){
        stacksize = 0; stackView.setText("Stack Size: "+stacksize);
    }

    private void updateTurn(){
        player_turn++;
        if(player_turn >= players){player_turn = 0;}
        if(player_turn == 0){playButton.setText("It is your turn.");}
        else{playButton.setText("It is Player "+(player_turn+1)+"\'s turn.");}}

    private void updateTurn(int player){
        player_turn = player;
        if(player_turn == 0){playButton.setText("It is your turn.");}
        else{playButton.setText("It is Player "+(player_turn+1)+"\'s turn.");}

    }

    @Override
        public boolean onTouchEvent(MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }
}
