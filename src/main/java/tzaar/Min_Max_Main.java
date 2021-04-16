package tzaar;

import ai.*;
import tzaar.GamePlay.Move;

public class Min_Max_Main {
    public static void main(String[] args) {
        String cyan = "\u001B[96m";
        String purple = "\u001B[95m";
        String reset = "\u001B[0m";

        System.out.println("\nWelcome to the game testing! Game name is TZAAR!");
        System.out.println("Player 1 = "+cyan+"Cyan"+reset+", Player 2 = "+purple+"Purple"+reset);
        System.out.println();

        Board old_board;
        int counter;
        int depth = 2;
        int game = 1, p1_wins = 0, p2_wins = 0;

//        for(Move all_first_move: GamePlay.players_all_moves_list(original_board)){
        for(int i = 0; i < 10; i++) {
            counter = 1;

            System.out.printf("Game %d started\n", game++);
            Board board = new Board();
            String player_won = null;

            while (true) {
                board.display_board();
                if(board.no_of_steps_begin > 74){
                    depth = 6;
                }
                else if(board.no_of_steps_begin > 50){
                    depth = 5;
                }
                else if(board.no_of_steps_begin > 34){
                    depth = 4;
                }
                else if(board.no_of_steps_begin > 18){
                    depth = 3;
                }
                else
                    depth = 2;

                player_won = GamePlay.isWon(board, true);
                if (player_won != null) {
                    board.display_board();
                    if ((player_won.equals("p2")))
                        p2_wins++;
                    else
                        p1_wins++;

                    break;
                }

                if (board.whose_turn.charAt(1) == '1') {
                    System.out.println("It is " + cyan + "player1's" + reset + " turn.");
                } else
                    System.out.println("It is " + purple + "player2's" + reset + " turn.");

                if (board.first_move) {
                    System.out.println("You have to make a capture move first!");
                } else {
                    System.out.println("You can chose your second move to be capture or stack or pass!");
                }
                System.out.println("(Your piece's Cell Number, Your opponent's Cell Number)");

                Move move;
                if (board.whose_turn.equals("p2")) {
                    if(counter < 12)
                        move = RandomMoves.play_random_move(board);
                    else
//                        move = Min_Max_New.min_max(board, depth);
                        move = Min_Max_OurAI.play_good_move(board, depth);

                } else {
//                move = RandomMoves.play_random_move(board);
//                    if(board.first_move_of_game)
//                        move = all_first_move;
//                    else
                    if(counter < 12)
                        move = RandomMoves.play_random_move(board);
                    else
//                        move = MinMax.play_good_move(board, depth);
                        move = Min_Max_Waltz.min_max(board, depth);


//                For Human Player
//                Scanner scan = new Scanner(System.in);
////
//                String source = scan.next().toUpperCase();
//
//                if ("EXIT".equals(source)) {
//                    System.out.println("Game is exited!");
//                    System.exit(0);
//                }
//                String destination = scan.next().toUpperCase();
////
//               move = new Move(source, destination);
                }
                old_board = GamePlay.is_allowed(board, move);
                System.out.println(move.source + " " + move.dest+" "+depth);
                counter++;
                if (old_board != null) {
                    board = old_board;
                }
            }
        }
        System.out.println("Player 1 wins: "+p1_wins);
        System.out.println("Player 2 wins: "+p2_wins);
    }
}

