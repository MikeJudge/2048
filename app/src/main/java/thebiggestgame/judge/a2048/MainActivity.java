package thebiggestgame.judge.a2048;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class MainActivity extends AppCompatActivity {
    private static final String FILENAME = "out.out";
    private static final String GAME_BUNDLE = "game";


    private TableRow row[];     //rows to add to table, hold cells
    private TextView grid[][];  //represent the cells of the Game
    private TableLayout table;  //holds the rows
    private TextView scoreView;
    private Button newGameButton;
    private ImageButton instructionButton;

    private float oldX, oldY;   //used for detecting swipe direction
    private long highScore;     //high score

    private Game game;


    /**********************************************************************

     Function    : notifyInstruction
     Description : Create and show dialog displaying the rules of the game
     Inputs      :
     Outputs     :

     ***********************************************************************/

    private void notifyInstruction() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.instruction).setTitle(R.string.instruction_title); //set title and message
        builder.setPositiveButton(R.string.ok, null);
        AlertDialog dialog = builder.create(); //create the dialog
        dialog.show();      //show the dialog
    }


    /**********************************************************************

     Function    : writeFile
     Description : Write the high score to storage
     Inputs      :
     Outputs     :

     ***********************************************************************/

    private void writeFile() {
        try {
            DataOutputStream os = new DataOutputStream(openFileOutput(FILENAME, MODE_PRIVATE));
            os.writeLong(highScore);
            os.close();
        } catch (Exception e) {
            //System.out.println("error writing file" + e.getMessage());
        }
    }

    /**********************************************************************

     Function    : readFile
     Description : read high score from storage
     Inputs      :
     Outputs     :

     ***********************************************************************/

    private void readFile() {
        try {
            DataInputStream is = new DataInputStream(openFileInput(FILENAME));
            highScore = is.readLong();
            is.close();
        } catch (Exception e) {
            notifyInstruction();    //first run of app if file is not found, show user how to play
            //System.out.println("error reading file" + e.getMessage());
        }
    }

    /**********************************************************************

     Function    : notifyLose
     Description : Create and Show a Dialog to the user to start a new game
     Inputs      :
     Outputs     :

     ***********************************************************************/

    private void notifyLose() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.new_game).setTitle(R.string.game_over); //set title and message
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() { //set ok button function
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (highScore == game.getScore()) //write new high score to file
                    writeFile();

                game = new Game();  //start a new game
                updateUI();         //update the UI to show this new game
            }
        });
        AlertDialog dialog = builder.create(); //create the dialog
        dialog.show();      //show the dialog
    }

    /**********************************************************************

     Function    : notifyWon
     Description : Create and Show a Dialog to the user to ask to continue or end the game
     Inputs      :
     Outputs     :

     ***********************************************************************/

    private void notifyWon() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.won_game_option).setTitle(R.string.won_game);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) { //do nothing we are continuing
                return;
            }
        });

        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) { //start a new game
                if (highScore == game.getScore())   //write new high score to file
                    writeFile();

                game = new Game();  //start a new game
                updateUI();         //update the UI to show this new game
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**********************************************************************

     Function    : updateUI
     Description : set the UI elements to match the current state of the game
     Inputs      :
     Outputs     :

     ***********************************************************************/

    private void updateUI() {
        for (int i = 0; i < Game.SIZE; i++)
            for (int n = 0; n < Game.SIZE; n++) {
                grid[i][n].setText("" + game.getCell(i, n));    //set the number in the cell
                //Each number represents a different color
                switch (game.getCell(i, n)) {
                    case 0:      grid[i][n].setBackgroundColor(0xffffffff);
                                 break;
                    case 2:      grid[i][n].setBackgroundColor(0xffff0000);
                                 break;
                    case 4:      grid[i][n].setBackgroundColor(0xffff751a);
                                 break;
                    case 8:      grid[i][n].setBackgroundColor(0xffffa64d);
                                 break;
                    case 16:     grid[i][n].setBackgroundColor(0xffffbf00);
                                 break;
                    case 32:     grid[i][n].setBackgroundColor(0xffffff00);
                                 break;
                    case 64:     grid[i][n].setBackgroundColor(0xffd2ff4d);
                                 break;
                    case 128:    grid[i][n].setBackgroundColor(0xff80ff00);
                                 break;
                    case 256:    grid[i][n].setBackgroundColor(0xff33cc00);
                                 break;
                    case 512:    grid[i][n].setBackgroundColor(0xff00ff80);
                                 break;
                    case 1024:   grid[i][n].setBackgroundColor(0xff00ffbf);
                                 break;
                    case 2048:   grid[i][n].setBackgroundColor(0xff00ffff);
                                 break;
                    case 4096:   grid[i][n].setBackgroundColor(0xff00bfff);
                                 break;
                    case 8192:   grid[i][n].setBackgroundColor(0xff0080ff);
                                 break;
                    case 16384:  grid[i][n].setBackgroundColor(0xff0040ff);
                                 break;
                    case 32768:  grid[i][n].setBackgroundColor(0xff8000ff);
                                 break;
                    case 65536:  grid[i][n].setBackgroundColor(0xffbf00ff);
                                 break;
                    case 131072: grid[i][n].setBackgroundColor(0xffff00ff);
                                 break;
                    case 262144: grid[i][n].setBackgroundColor(0xffff00bf);
                                 break;
                }
            }
        scoreView.setText(getString(R.string.score) + " " + game.getScore() + "\n" + getString(R.string.high_score) + " " + highScore); //update the score textView
        if (game.isWon())
            notifyWon();
        if (game.isGameOver())
            notifyLose();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null)
            game = new Game();
        else
            game = (Game)savedInstanceState.getSerializable(GAME_BUNDLE); //reload the game from the bundle

        readFile(); //get high score

        //Link scoreView and table to elements in XML
        scoreView = (TextView)findViewById(R.id.score_bar);
        table = (TableLayout)findViewById(R.id.table);

        table.setStretchAllColumns(true); //table fills screen horizontally, and each column has identical weight

        grid = new TextView[Game.SIZE][Game.SIZE];
        row = new TableRow[Game.SIZE];
        for (int i = 0; i < Game.SIZE; i++) {
            grid[i] = new TextView[Game.SIZE];
            row[i] = new TableRow(this);

            //gives each row 1/4 of the space vertically
            row[i].setLayoutParams(new TableLayout.LayoutParams(0, 0, 1.0f));
            for (int n = 0; n < Game.SIZE; n++) {
                grid[i][n] = new TextView(this);
                //each cell fills itself with content
                TableRow.LayoutParams params = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f); //each cell has equal weight horizontally
                DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
                int px = (int)((getResources().getDimension(R.dimen.border_size) * displayMetrics.density) + 0.5);
                params.setMargins(px,px,px,px); //creates border on textView
                grid[i][n].setLayoutParams(params);
                grid[i][n].setTextSize(getResources().getDimension(R.dimen.text_size));
                grid[i][n].setTypeface(Typeface.DEFAULT_BOLD);
                grid[i][n].setGravity(Gravity.CENTER); //number is in middle of cell

                row[i].addView(grid[i][n]); //add the cell to the current row
            }
            table.addView(row[i]);  //insert the row into the table
        }
        updateUI(); //set the UI colors and score

        table.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    oldX = motionEvent.getX();  //store initial X coordinate on finger down
                    oldY = motionEvent.getY();  //store initial Y coordinate on finger down
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    //subtract final finger location on finger release from screen X,Y from initial
                    float dx = motionEvent.getX() - oldX;
                    float dy = motionEvent.getY() - oldY;

                    //if movement was really small don't do anything
                    if (Math.abs(dx) < 10 && Math.abs(dy) < 10)
                        return true;
                    //check which change in direction was larger and make a decision
                    if (Math.abs(dx) > Math.abs(dy)) {
                        if (dx > 0)
                            game.move(Game.RIGHT);
                        else
                            game.move(Game.LEFT);
                    } else {
                        if (dy > 0)
                            game.move(Game.DOWN);
                        else
                            game.move(Game.UP);
                    }
                    if (game.getScore() > highScore)    //update high score if needed
                        highScore = game.getScore();

                    updateUI();
                }
                return true;
            }
        });

        newGameButton = (Button)findViewById(R.id.new_game_button);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage(getString(R.string.new_game) + "?").setTitle(R.string.game_button);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { //start a new game
                        if (highScore == game.getScore())   //write new high score to file
                            writeFile();

                        game = new Game();  //start a new game
                        updateUI();         //update the UI to show this new game
                    }
                });

                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { //user wants to continue
                        return;
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        instructionButton = (ImageButton)findViewById(R.id.instruction_button);
        instructionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyInstruction();
            }
        });


    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(GAME_BUNDLE, game);    //write the current game state to the bundle
    }
}
