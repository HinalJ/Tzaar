package tzaar;

import tzaar.GamePlay.Move;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        String cyan = "\u001B[96m";
        String purple = "\u001B[95m";
        String reset = "\u001B[0m";
        Board board = new Board();

        rules();

        System.out.println("\nWelcome to the game testing! Game name is TZAAR!");
        System.out.println("Player 1 = "+cyan+"Cyan"+reset+", Player 2 = "+purple+"Purple"+reset);
        System.out.println();

        Board old_board;

        while(true){
            board.display_board();
            if(GamePlay.isWon(board, true)!=null){
                System.exit(0);
            }

            if(board.whose_turn.charAt(1)=='1'){
                System.out.println("It is "+cyan+"player1's"+reset+" turn.");
            }
            else
                System.out.println("It is "+purple+"player2's"+reset+" turn.");

            if(board.first_move) {
                System.out.println("You have to make a capture move first!");
            }else{
                System.out.println("You can chose your second move to be capture or stack or pass!");
            }
            System.out.println("(Your piece's Cell Number, Your opponent's Cell Number)");

            Scanner scan = new Scanner(System.in);
            String source = scan.next().toUpperCase();

            switch (source) {
                case "EXIT":
                    System.out.println("Game is exited!");
                    System.exit(0);
                case "RULES":
                    rules();
                    continue;
                default:
                    break;
            }
            String destination = scan.next().toUpperCase();

            old_board = GamePlay.is_allowed(board, new Move(source, destination));

            if(old_board!=null)
            {
                if(board.first_move) {
                    System.out.println("First move placed!");
                }
                else
                    System.out.println("Second move placed!");
                board = old_board;
            }
        }
    }

    public static void rules(){
        System.out.println("How to Play TZAAR?");

        String rules = "“Shall I make myself stronger or my opponent weaker?” This is the tricky question you’ll have to ask yourself on each of your turns.\n" +
                "\nThere are 2 ways to win:\n" +

                "\n1. Players have 3 types of pieces: Tzaars, Tzaras and Totts.\n" +
                "These 3 types represent a trinity: they cannot exist without each other. " +
                "You must keep at least one piece of each type on the board at all times. " +
                "In other words, the first way to win is to make your opponent run out of either Tzaars, Tzarras or Totts.\n" +

                "\n2. Each turn, players are obligated to capture at least one opponent’s piece." +
                "So the second way to win is to put your opponent in a position where he cannot capture any of your " +
                "remaining pieces on the board.\n" +
                "After Cyan has started the game with only one move, players always have two moves per turn.\n" +

                "\nTHE FIRST MOVE OF A TURN:\n" +
                "“Forced Capture!”\n" +

                "\n1. You must capture! You can capture a piece in an adjacent space, " +
                "but during the course of the game more and more spaces will become vacant. You may also capture by moving a piece " +
                "in a straight line over any number of vacant spaces, to the first space occupied by an opponent’s piece.\n" +

                "\n2. As far as capturing goes, there’s no difference between Tzaars, Tzarras and Totts. Each piece can capture " +
                "any other piece, as long as the piece is at least equally strong as the piece it wants to capture. The strength of " +
                "a piece is not determined by its type, but by its height. In the beginning there are only single pieces, which " +
                "means that all pieces on the board are equally strong. All pieces on the board move the same way.\n" +

                "\nTHE SECOND MOVE OF A TURN:\n" +
                "For the second move, you must choose between 3 possibilities: \n1. make a second capture,\n2. make one of your pieces " +
                "(or stacks) stronger, or\n3. pass.\n" +

                "\n1. A second capture\n" +
                "- To make a second capture, the rules are the same as for the capture with your first move.\n- You may make the " +
                "second capture with the same piece (or stack) that made the first capture, or with a different piece (or stack).\n" +

                "\n2. Making a piece stronger\n" +
                "- To make a piece (or stack) stronger, you just have to jump with it on top of another piece (or stack) of your color. " +
                "You may jump on one of your pieces (or stacks) in an adjacent space or in a space that you can reach by moving " +
                "in a straight line over any number of vacant spaces. So to make a piece stronger, you have exactly the same movement " +
                "possibilities as for capturing.\n- Any of your pieces or stacks can jump on any of your other pieces or stacks. " +
                "For example, a single Tzaar can jump onto a stack with a Tott on top and vice versa.\n- Only the top piece of a " +
                "stack counts for the different types of pieces in play. For example, if you put a Tott on top of a Tzaar, the stack " +
                "counts only as a Tott (although the Tzaar is still in play as part of the stack).\n" +

                "\n3. Pass the move.\n" +
                "You are not obligated to use your second move. " +
                "If you decide to pass, you just tell your opponent that it is her/his turn again.\n" +

                "\nFOR THE SAKE OF CLARITY:\n" +
                "1. A piece (or stack) can never be moved to an empty space. Once a space is empty, it remains empty until the end of the game.\n" +
                "2. A piece (or stack) can never jump over one or more other pieces (or stacks). It can only be moved over vacant spaces.\n" +
                "3. The board has no central space. Pieces may not be moved across the center.\n" +
                "4. Stacks can only consist of pieces of one and the same color.\n" +
                "5. You must respect the order of moves: always the forced capture first, then a choice between three possibilities for the second move.";

        System.out.println(rules);
    }
}