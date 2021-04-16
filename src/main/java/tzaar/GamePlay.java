package tzaar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class GamePlay {
    public static class Move{
        public String source;
        public String dest;

        public Move(String source, String dest){
            this.source = source;
            this.dest = dest;
        }
    }

    private static boolean capture_move(Board board, Position pos_current, Position pos_dest, String current, String destination){
        if (pos_current.getHeight() >= pos_dest.getHeight()) {
            pos_dest.setPlayer(pos_current.getPlayer());
            pos_dest.setPiece(pos_current.getPiece());
            pos_dest.setHeight(pos_current.getHeight());
            pos_current.setPlayer("*");
            pos_current.setPiece("     *     ");
            pos_current.setHeight(0);
            update_board(board, pos_current);
            return true;
        }

        System.err.printf("Your stack size at %s is smaller than opposition at %s.\n", current, destination);
        System.err.println("Illegal Move!");
        return false;
    }

    private static boolean stack_move(Board board, Position pos_current, Position pos_dest){
        pos_dest.setPiece(pos_current.getPiece());
        pos_dest.setHeight(pos_current.getHeight() + pos_dest.getHeight());
        pos_current.setPlayer("*");
        pos_current.setPiece("     *     ");
        pos_current.setHeight(0);
        update_board(board, pos_current);
        return true;
    }

    private static void update_board(Board board, Position pos_current){
        String right = pos_current.getRight();
        String left = pos_current.getLeft();
        String upleft = pos_current.getUpLeft();
        String downleft = pos_current.getDownLeft();
        String upright = pos_current.getUpRight();
        String downright = pos_current.getDownRight();

        if(right!=null && left!=null){
            board.getBoard().get(right).setLeft(left);
            board.getBoard().get(left).setRight(right);
        }
        else if(right==null && left!=null)
            board.getBoard().get(left).setRight(null);
        else if(right!=null) //&& left==null
            board.getBoard().get(right).setLeft(null);

        if(upleft!=null && downright!=null){
            board.getBoard().get(upleft).setDownRight(downright);
            board.getBoard().get(downright).setUpLeft(upleft);
        }
        else if(upleft==null && downright!=null){
            board.getBoard().get(downright).setUpLeft(null);
        }
        else if(upleft!=null) // && downright==null
            board.getBoard().get(upleft).setDownRight(null);

        if(downleft!=null && upright!=null){
            board.getBoard().get(downleft).setUpRight(upright);
            board.getBoard().get(upright).setDownLeft(downleft);
        }
        else if(downleft==null && upright!=null){
            board.getBoard().get(upright).setDownLeft(null);
        }
        else if(downleft!=null) // && upright==null
            board.getBoard().get(downleft).setUpRight(null);
    }

    public static Board is_allowed(Board old_board, Move move){
        Board board = new Board(old_board);
        boolean source_in_board = false;
        boolean dest_in_board = false;
        String source, destination;
        if(move!=null) {
            source = move.source;
            destination = move.dest;
        }else {
            System.out.println(move);
            return null;
        }

        if(source.equals("PASS") && destination.equals("MOVE")) {
            if(old_board.first_move){
                System.err.println("First move must be a capture move!");
                return null;
            }
            return board;
        }

        if(source.equals("E5") || destination.equals("E5")){
            System.err.println("Central space is not part of the board!");
            return null;
        }

        if (source.equals(destination)) {
            System.err.println("Please choose two different cells!");
            return null;
        }

        for(String key: board.getBoard().keySet()){
            if(source.equals(key))
                source_in_board = true;
            if(destination.equals(key))
                dest_in_board = true;
            if(source_in_board && dest_in_board)
                break;
        }

        if(source_in_board && dest_in_board){

            Position pos_source = board.getBoard().get(source);
            Position pos_destination = board.getBoard().get(destination);

            if (pos_source.getPlayer().equals("*")) {
                System.err.printf("The cell %s you have chosen is empty.\n", source);
                return null;
            }

            if (pos_destination.getPlayer().equals("*")) {
                System.err.printf("You cannot move to an empty cell %s.\n", destination);
                return null;
            }

            if(!old_board.whose_turn.equals(pos_source.getPlayer())){
                System.err.println("You cannot move opposition piece! Chose your own piece!");
                return null;
            }

            if(old_board.first_move && old_board.whose_turn.equals(pos_destination.getPlayer())){
                System.err.println("Your first move have to be capture move!");
                return null;
            }

            boolean dest_is_neighbour = false;

            ArrayList<String> neighbours = Position.neighbours(pos_source);

            for(String neighbour: neighbours){
                if(neighbour.equals(destination)){
                    dest_is_neighbour = true;
                    break;
                }
            }

            if(dest_is_neighbour){
                if(!old_board.whose_turn.equals(pos_destination.getPlayer())) {
                    if(GamePlay.capture_move(board, pos_source, pos_destination, source, destination)){
                        Set<String> temp_source_player;
                        Set<String> temp_destination_player;

                        if(old_board.whose_turn.equals("p1")){
                            temp_source_player = board.getPlayer1_piece();
                            temp_destination_player = board.getPlayer2_piece();

                            temp_source_player.remove(source);
                            temp_destination_player.remove(destination);
                            temp_source_player.add(destination);

                            board.setPlayer1_piece(temp_source_player);
                            board.setPlayer2_piece(temp_destination_player);
                        }
                        else{
                            temp_source_player = board.getPlayer2_piece();
                            temp_destination_player = board.getPlayer1_piece();

                            temp_source_player.remove(source);
                            temp_destination_player.remove(destination);
                            temp_source_player.add(destination);

                            board.setPlayer2_piece(temp_source_player);
                            board.setPlayer1_piece(temp_destination_player);
                        }

                        return board;
                    }
                }
                else {
                    if(GamePlay.stack_move(board, pos_source, pos_destination)){
                        Set<String> temp_source_player;

                        if(old_board.whose_turn.equals("p1")){
                            temp_source_player = board.getPlayer1_piece();
                            temp_source_player.remove(source);

                            board.setPlayer1_piece(temp_source_player);
                        }else{
                            temp_source_player = board.getPlayer2_piece();
                            temp_source_player.remove(source);

                            board.setPlayer2_piece(temp_source_player);
                        }

                        return board;
                    }
                }
                return null;
            }
            else
                System.err.printf("You cannot move from %s to %s.\n", source, destination);

        }
        else {
            System.err.println("Either one/both position(s) is/are not part of the board!");
        }

        return null;
    }

    public static ArrayList<Move> players_all_moves_list(Board board){
        Set<String> c_players_pieces_list;
        ArrayList<Move> c_players_moves_list = new ArrayList<>();

        if(board.whose_turn.equals("p1"))
            c_players_pieces_list = board.getPlayer1_piece();
        else
            c_players_pieces_list = board.getPlayer2_piece();

        // For Passing move
        if(!board.first_move){
            c_players_moves_list.add(new Move("PASS", "MOVE"));
        }

        for(String piece: c_players_pieces_list){
            Position pos_piece = board.getBoard().get(piece);

            for(String move: Position.neighbours(pos_piece)) {
                Position pos_move = board.getBoard().get(move);
                if(!board.first_move){
                    if(pos_piece.getPlayer().equals(pos_move.getPlayer()))
                        c_players_moves_list.add(new Move(piece, move));
                }
                if(!pos_piece.getPlayer().equals(pos_move.getPlayer())){
                    if(pos_piece.getHeight() >= pos_move.getHeight())
                        c_players_moves_list.add(new Move(piece, move));
                }

            }
        }
        return c_players_moves_list;
    }

    public static ArrayList<Move> players_all_moves_list(Board board, String player, boolean first_move){
        Set<String> c_players_pieces_list;
        ArrayList<Move> c_players_moves_list = new ArrayList<>();

        if(player.equals("p1"))
            c_players_pieces_list = board.getPlayer1_piece();
        else
            c_players_pieces_list = board.getPlayer2_piece();

        if(!first_move)
            c_players_moves_list.add(new Move("PASS", "MOVE"));

        for(String piece: c_players_pieces_list){
            Position pos_piece = board.getBoard().get(piece);

            for(String move: Position.neighbours(pos_piece)) {
                Position pos_move = board.getBoard().get(move);
                if(!first_move){
                    if(pos_piece.getPlayer().equals(pos_move.getPlayer()))
                        c_players_moves_list.add(new Move(piece, move));
                }
                if(!pos_piece.getPlayer().equals(pos_move.getPlayer())){
                    if(pos_piece.getHeight() >= pos_move.getHeight())
                        c_players_moves_list.add(new Move(piece, move));
                }
            }
        }
        return c_players_moves_list;
    }

    public static String isWon(Board board, boolean print){
        Set<String> player1 = new HashSet<>();
        Set<String> player2 = new HashSet<>();

        String reset = "\u001B[0m";
        String cyan = "\u001B[96m";
        String purple = "\u001B[95m";

        for(String piece: board.getPlayer1_piece()){
            player1.add(board.getBoard().get(piece).getPiece());
        }

        if (player1.size() != 3) {
            if(print) {
                System.out.println("\n" + purple + "Player 2 (Purple) won!" + reset);
                System.out.println("Due to " + cyan + "Player 1 (Cyan)" + reset + " lost entirely one of its type!");
            }
            return "p2";
        }

        for(String piece: board.getPlayer2_piece()){
            player2.add(board.getBoard().get(piece).getPiece());
        }

        if (player2.size() != 3) {
            if(print) {
                System.out.println("\n" + cyan + "Player 1 (Cyan) won!" + reset);
                System.out.println("Due to " + purple + "Player 2 (Purple)" + reset + " lost entirely one of its type!");
            }
            return "p1";
        }

        if(board.first_move){
            ArrayList<Move> moves_list = players_all_moves_list(board);
            if(moves_list.size() == 0){
                if(board.whose_turn.equals("p1")){
                    if(print) {
                        System.out.println("\n" + purple + "Player 2 (Purple) won!" + reset);
                        System.out.println("As " + cyan + "Player 1 (Cyan)" + reset + " doesn't have any capture move left!");
                    }
                    return "p2";
                }
                else{
                    if(print) {
                        System.out.println("\n" + cyan + "Player 1 (Cyan) won!" + reset);
                        System.out.println("As " + purple + "Player 2 (Purple)" + reset + " doesn't have any capture move left!");
                    }
                    return "p1";
                }
            }
        }
        return null;
    }

    public static String next_player(String player){
        return player.equals("p1") ? "p2" : "p1";
    }
}