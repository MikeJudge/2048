package thebiggestgame.judge.a2048;

import java.io.Serializable;

/**
 * Created by Judge on 7/1/16.
 */


public class Game implements Serializable {
    public static final int SIZE = 4;

    public static final int LEFT = 1;
    public static final int UP = 2;
    public static final int RIGHT = 3;
    public static final int DOWN = 4;

    private int board[][];
    private int oldBoard[][];
    private int freeSpaces;
    private long score;
    private boolean alreadyWon;


    /**********************************************************************

     Function    : Game
     Description : initialize the 2048 Game
     Inputs      :
     Outputs     :

     ***********************************************************************/

    public Game() {
        board = new int[SIZE][SIZE];
        oldBoard = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            board[i] = new int[SIZE];
            oldBoard[i] = new int[SIZE];
        }
        int rand = (int)(Math.random()*(SIZE*SIZE));
        board[rand/SIZE][rand%SIZE] = (rand % 2 == 0) ? 2 : 4; //sets the initial cell
        freeSpaces = SIZE * SIZE - 1;
        score = 0;
        alreadyWon = false;
    }

    /**********************************************************************

     Function    : getCell
     Description : return cell of board
     Inputs      :  i - row of board
                    n - column of board
     Outputs     : number in board[i][n]

     ***********************************************************************/

    public int getCell(int i, int n) {
        return board[i][n];
    }

    /**********************************************************************

     Function    : getScore
     Description : get score of game
     Inputs      :
     Outputs     : score of game

     ***********************************************************************/

    public long getScore() {
        return score;
    }


    /**********************************************************************

     Function    : isGameOver
     Description : Checks and Returns whether game is finished or not
     Inputs      :
     Outputs     : Return true if no possible moves are left, return false otherwise

     ***********************************************************************/

    public boolean isGameOver() {
        if (freeSpaces > 0) //if there is a free space there is a move to be made
            return false;

        //check adjacent cells for like values
        for (int i = 0; i < SIZE; i++)
            for (int n = 0; n < SIZE - 1; n++)
                if (board[i][n] == board[i][n+1])
                    return false;

        for (int i = 0; i < SIZE; i++)
            for (int n = 0; n < SIZE - 1; n++)
                if (board[n][i] == board[n+1][i])
                    return false;
        //if no open spaces and no cells to combine, then game is over
        return true;
    }

    /**********************************************************************

     Function    : isWon
     Description : Checks and Returns whether user has reached 2048
     Inputs      :
     Outputs     : Return true if user has just populated board with 2048, false otherwise

     ***********************************************************************/

    public boolean isWon() {
        if (alreadyWon)
            return false;
        for (int i = 0; i < SIZE; i++)
            for (int n = 0; n < SIZE; n++)
                if (board[i][n] == 2048) {
                    alreadyWon = true;
                    return alreadyWon;
                }
        return false;
    }

    /**********************************************************************

     Function    : isOldNew
     Description : Checks and Returns whether the last move made a change to the board
     Inputs      :
     Outputs     : Return true if oldBoard == board

     ***********************************************************************/

    public boolean isOldNew() {
        for (int i = 0; i < SIZE; i++)
            for (int n = 0; n < SIZE; n++)
                if (board[i][n] != oldBoard[i][n])
                    return false;
        return true;
    }

    /**********************************************************************

     Function    : insertNum
     Description : Insert a 2 or 4 into a randomly picked open cell
     Inputs      :
     Outputs     :

     ***********************************************************************/

    private void insertNum() {
        if (freeSpaces == 0) //return if no block open
            return;
        if (isOldNew()) //return if the board didn't change with the last move
            return;

        int num;
        int rand = (int)(Math.random()*2);
        //50/50 shot of being a 2
        if (rand == 0)
            num = 2;
        else
            num = 4;

        rand = (int)(Math.random()*freeSpaces); //randomly pick an empty cell
        int count = 0;
        //find and fill in the empty cell with the random number
        for (int i = 0; i < SIZE; i++)
            for (int n = 0; n < SIZE; n++) {
                if (board[i][n] == 0) {
                    if (count == rand) {
                        board[i][n] = num;
                        freeSpaces--;	//reduce number of free spaces by one
                        return;
                    } else {
                        count++; //count number of empty cells passed
                    }
                }
            }
    }

    /**********************************************************************

     Function    : rowRight
     Description : Move elements in positive direction to the wall
     Inputs      : arr - elements to move
     Outputs     :

     ***********************************************************************/

    private void rowRight(int arr[]) {
        int temp[] = new int[SIZE]; //temporary storage
        int index = SIZE - 1;	 //index for temp array
        for (int i = SIZE - 1; i >= 0; i--)
            if (arr[i] > 0) //zeros aren't interesting
                if (temp[index] == arr[i]) { 	//case 1: merge like cells
                    temp[index]*=2;
                    score += temp[index];
                    freeSpaces++;	//new cell space
                    index--;		//the merged cell cannot be merged again in the same move
                } else if (temp[index] == 0) {	//case 2: hitting a wall or an already merged cell
                    temp[index] = arr[i];
                } else {						//case 3: hitting a different numbered cell
                    index--;
                    temp[index] = arr[i];
                }
        for (int i = 0; i < SIZE; i++)	//copy temp result to the input array
            arr[i] = temp[i];
    }

    /**********************************************************************

     Function    : rowLeft
     Description : Move elements in negative direction to the wall
     Inputs      : arr - elements to move
     Outputs     :

     ***********************************************************************/

    private void rowLeft(int arr[]) {
        int temp[] = new int[SIZE]; //temporary storage
        int index = 0;			 //index for temp array
        for (int i = 0; i < SIZE; i++)
            if (arr[i] > 0) //zeros aren't interesting
                if (temp[index] == arr[i]) {  	//case 1: merge like cells
                    temp[index]*=2;
                    score += temp[index];
                    freeSpaces++;	//new cell space
                    index++;		//the merged cell cannot be merged again in the same move
                } else if (temp[index] == 0) {	//case 2: hitting a wall or an already merged cell
                    temp[index] = arr[i];
                } else {						//case 3: hitting a different numbered cell
                    index++;
                    temp[index] = arr[i];
                }
        for (int i = 0; i < SIZE; i++)	//copy temp result to the input array
            arr[i] = temp[i];
    }


    /**********************************************************************

     Function    : moveUp
     Description : Move elements on the board upward
     Inputs      :
     Outputs     :

     ***********************************************************************/

    private void moveUp() {
        int temp[] = new int[SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int n = 0; n < SIZE; n++) //copies elements in a column to a horizontal array
                temp[n] = board[n][i];
            rowLeft(temp);
            for (int n = 0; n < SIZE; n++) //copy result of rowLeft function to the board
                board[n][i] = temp[n];
        }
    }

    /**********************************************************************

     Function    : moveDown
     Description : Move elements on the board Downward
     Inputs      :
     Outputs     :

     ***********************************************************************/

    private void moveDown() {
        int temp[] = new int[SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int n = 0; n < SIZE; n++) //copies elements in a column to a horizontal array
                temp[n] = board[n][i];
            rowRight(temp);
            for (int n = 0; n < SIZE; n++) //copy result of rowRight function to the board
                board[n][i] = temp[n];
        }
    }

    /**********************************************************************

     Function    : moveLeft
     Description : Move elements on the board Leftward
     Inputs      :
     Outputs     :

     ***********************************************************************/

    private void moveLeft() {
        for (int i = 0; i < SIZE; i++)
            rowLeft(board[i]); //performed on each row of the board
    }

    /**********************************************************************

     Function    : moveRight
     Description : Move elements on the board Rightward
     Inputs      :
     Outputs     :

     ***********************************************************************/

    private void moveRight() {
        for (int i = 0; i < SIZE; i++)
            rowRight(board[i]); //performed on each row of the board
    }



    /**********************************************************************

     Function    : move
     Description : Move elements on the board in the direction Up, Down, Left, Right
     Inputs      : integer representing up, down, left, right
     Outputs     :

     ***********************************************************************/

    public void move(int direction) {
        //copy the board to the oldBoard to note changes
        for (int i = 0; i < SIZE; i++)
            for (int n = 0; n < SIZE; n++)
                oldBoard[i][n] = board[i][n];

        //checks input against constants
        switch (direction) {
            case LEFT: 	moveLeft();
                        break;
            case RIGHT: moveRight();
                        break;
            case UP:	moveUp();
                        break;
            case DOWN:	moveDown();
                        break;
        }
        insertNum();
    }

    /**********************************************************************

     Function    : printGame
     Description : Print Information about the Game
     Inputs      :
     Outputs     :

     ***********************************************************************/

    public void printGame() {
        System.out.println("Score: " + score + "  Free Spaces: " + freeSpaces);
        for (int i = 0; i < SIZE; i++) {
            for (int n = 0; n < SIZE; n++)
                System.out.print("" + board[i][n] + " ");
            System.out.println();
        }
    }

}