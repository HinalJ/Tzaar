package tzaar;

import org.apache.commons.lang3.StringUtils;
import java.util.*;

public class Board {
    public final HashMap<String, Position> board;
    private Set<String> player1_piece = new HashSet<>();
    private Set<String> player2_piece = new HashSet<>();
    public final boolean first_move;
    public final String whose_turn;
    public final boolean first_move_of_game;
    public final int no_of_steps_begin;

    public Board() {
        this.board = initialise_board();
        players_pieces();
        this.whose_turn = "p1";
        this.first_move = true;
        this.first_move_of_game = true;
        this.no_of_steps_begin = 0;
    }

    public Board(Board board){
        this.board = new HashMap<>();
        for(HashMap.Entry<String,Position> entry: board.getBoard().entrySet()) {
            this.board.put(entry.getKey(), new Position(entry.getValue()));
        }
        this.no_of_steps_begin = board.no_of_steps_begin+1;

        if(!board.first_move || board.first_move_of_game) {
            this.whose_turn = board.whose_turn.equals("p1") ? "p2" : "p1";
            this.first_move = true;
        }else {
            this.whose_turn = board.whose_turn;
            this.first_move = false;
        }
        this.first_move_of_game = false;
        players_pieces();
    }

    public void display_board(){
        Map<String, Position> sorted = new TreeMap<>(getBoard());

        String reset = "\u001B[0m";
        String cyan = "\u001B[96m";
        String purple = "\u001B[95m";
        String white = "\u001B[37m";
        String blue = "\u001B[34m";

        int[] arr = {5,6,7,8,9,8,7,6,5};
        HashMap<Integer, Integer> length_width = new HashMap<>();
        length_width.put(5, 172);
        length_width.put(6, 181);
        length_width.put(7, 190);
        length_width.put(8, 199);
        length_width.put(9, 208);

        int counter = 0;
        int index = 0;
        String value ="";
        System.out.println(StringUtils.center(blue+"T Z A A R"+reset, 140)+"\n");

        for(String key: sorted.keySet()){
            Position pos = board.get(key);
            switch (pos.getPlayer()) {
                case "p1":
                    value += (cyan + pos.getPiece()+" {H="+ pos.getHeight()+"}" + reset + "   ");
                    break;
                case "p2":
                    value += (purple + pos.getPiece()+" {H="+ pos.getHeight()+"}"  + reset + "   ");
                    break;
                default:
                    value += (white + pos.getPiece() + reset + "   ");
                    break;
            }
            counter+=1;

            if(counter==arr[index]){
                char letter = key.charAt(0);
                System.out.printf("%s1 %s%s%d%n", letter, StringUtils.center(value, length_width.get(arr[index])), letter, counter);
                System.out.println();
                value = "";
                counter = 0;
                index+=1;
            }
        }
    }

    private void players_pieces(){

        for(String key: board.keySet()){
            if (board.get(key).getPlayer().equals("p1")) {
                this.player1_piece.add(key);
            }
            else if(board.get(key).getPlayer().equals("p2")){
                this.player2_piece.add(key);
            }
        }

    }

    private static HashMap<String, Position> initialise_board() {
        HashMap<String, Position> board = new HashMap<>();

        //A
        board.put("A1", new Position(null, null, null, "A2", "B2", "B1", "p2", "TOTTS", 1));
        board.put("A2", new Position("A1", null, null, "A3", "B3", "B2", "p2", "TOTTS", 1));
        board.put("A3", new Position("A2", null, null, "A4", "B4", "B3", "p2", "TOTTS", 1));
        board.put("A4", new Position("A3", null, null, "A5", "B5", "B4", "p2", "TOTTS", 1));
        board.put("A5", new Position("A4", null, null, null, "B6", "B5", "p1", "TOTTS", 1));

        //B
        board.put("B1", new Position(null, null, "A1", "B2", "C2", "C1", "p1", "TOTTS", 1));
        board.put("B2", new Position("B1", "A1", "A2", "B3", "C3", "C2", "p2", "TZARA", 1));
        board.put("B3", new Position("B2", "A2", "A3", "B4", "C4", "C3", "p2", "TZARA", 1));
        board.put("B4", new Position("B3", "A3", "A4", "B5", "C5", "C4", "p2", "TZARA", 1));
        board.put("B5", new Position("B4", "A4", "A5", "B6", "C6", "C5", "p1", "TZARA", 1));
        board.put("B6", new Position("B5", "A5", null, null, "C7", "C6", "p1", "TOTTS", 1));

        //C
        board.put("C1", new Position(null, null, "B1", "C2", "D2", "D1", "p1", "TOTTS", 1));
        board.put("C2", new Position("C1", "B1", "B2", "C3", "D3", "D2", "p1", "TZARA", 1));
        board.put("C3", new Position("C2", "B2", "B3", "C4", "D4", "D3", "p2", "TZAAR", 1));
        board.put("C4", new Position("C3", "B3", "B4", "C5", "D5", "D4", "p2", "TZAAR", 1));
        board.put("C5", new Position("C4", "B4", "B5", "C6", "D6", "D5", "p1", "TZAAR", 1));
        board.put("C6", new Position("C5", "B5", "B6", "C7", "D7", "D6", "p1", "TZARA", 1));
        board.put("C7", new Position("C6", "B6", null, null, "D8", "D7", "p1", "TOTTS", 1));

        //D
        board.put("D1", new Position(null, null, "C1", "D2", "E2", "E1", "p1", "TOTTS", 1));
        board.put("D2", new Position("D1", "C1", "C2", "D3", "E3", "E2", "p1", "TZARA", 1));
        board.put("D3", new Position("D2", "C2", "C3", "D4", "E4", "E3", "p1", "TZAAR", 1));
        board.put("D4", new Position("D3", "C3", "C4", "D5", null, "E4", "p2", "TOTTS", 1));
        board.put("D5", new Position("D4", "C4", "C5", "D6", "E6", null, "p1", "TOTTS", 1));
        board.put("D6", new Position("D5", "C5", "C6", "D7", "E7", "E6", "p1", "TZAAR", 1));
        board.put("D7", new Position("D6", "C6", "C7", "D8", "E8", "E7", "p1", "TZARA", 1));
        board.put("D8", new Position("D7", "C7", null, null, "E9", "E8", "p1", "TOTTS", 1));

        //E
        board.put("E1", new Position(null, null, "D1", "E2", "F1", null, "p1", "TOTTS", 1));
        board.put("E2", new Position("E1", "D1", "D2", "E3", "F2", "F1", "p1", "TZARA", 1));
        board.put("E3", new Position("E2", "D2", "D3", "E4", "F3", "F2", "p1", "TZAAR", 1));
        board.put("E4", new Position("E3", "D3", "D4", null, "F4", "F3", "p1", "TOTTS", 1));
        board.put("E5", new Position(null, null, null, null, null, null, "", "           ", 0));          //EMPTY
        board.put("E6", new Position(null, "D5", "D6", "E7", "F6", "F5", "p2", "TOTTS", 1));
        board.put("E7", new Position("E6", "D6", "D7", "E8", "F7", "F6", "p2", "TZAAR", 1));
        board.put("E8", new Position("E7", "D7", "D8", "E9", "F8", "F7", "p2", "TZARA", 1));
        board.put("E9", new Position("E8", "D8", null, null, null, "F8", "p2", "TOTTS", 1));

        //F
        board.put("F1", new Position(null, "E1", "E2", "F2", "G1", null, "p2", "TOTTS", 1));
        board.put("F2", new Position("F1", "E2", "E3", "F3", "G2", "G1", "p2", "TZARA", 1));
        board.put("F3", new Position("F2", "E3", "E4", "F4", "G3", "G2", "p2", "TZAAR", 1));
        board.put("F4", new Position("F3", "E4", null, "F5", "G4", "G3", "p2", "TOTTS", 1));
        board.put("F5", new Position("F4", null, "E6", "F6", "G5", "G4", "p1", "TOTTS", 1));
        board.put("F6", new Position("F5", "E6", "E7", "F7", "G6", "G5", "p2", "TZAAR", 1));
        board.put("F7", new Position("F6", "E7", "E8", "F8", "G7", "G6", "p2", "TZARA", 1));
        board.put("F8", new Position("F7", "E8", "E9", null, null, "G7", "p2", "TOTTS", 1));

        //G
        board.put("G1", new Position(null, "F1", "F2", "G2", "H1", null, "p2", "TOTTS", 1));
        board.put("G2", new Position("G1", "F2", "F3", "G3", "H2", "H1", "p2", "TZARA", 1));
        board.put("G3", new Position("G2", "F3", "F4", "G4", "H3", "H2", "p2", "TZAAR", 1));
        board.put("G4", new Position("G3", "F4", "F5", "G5", "H4", "H3", "p1", "TZAAR", 1));
        board.put("G5", new Position("G4", "F5", "F6", "G6", "H5", "H4", "p1", "TZAAR", 1));
        board.put("G6", new Position("G5", "F6", "F7", "G7", "H6", "H5", "p2", "TZARA", 1));
        board.put("G7", new Position("G6", "F7", "F8", null, null, "H6", "p2", "TOTTS", 1));

        //H
        board.put("H1", new Position(null, "G1", "G2", "H2", "I1", null, "p2", "TOTTS", 1));
        board.put("H2", new Position("H1", "G2", "G3", "H3", "I2", "I1", "p2", "TZARA", 1));
        board.put("H3", new Position("H2", "G3", "G4", "H4", "I3", "I2", "p1", "TZARA", 1));
        board.put("H4", new Position("H3", "G4", "G5", "H5", "I4", "I3", "p1", "TZARA", 1));
        board.put("H5", new Position("H4", "G5", "G6", "H6", "I5", "I4", "p1", "TZARA", 1));
        board.put("H6", new Position("H5", "G6", "G7", null, null, "I5", "p2", "TOTTS", 1));

        //I
        board.put("I1", new Position(null, "H1", "H2", "I2", null, null, "p2", "TOTTS", 1));
        board.put("I2", new Position("I1", "H2", "H3", "I3", null, null, "p1", "TOTTS", 1));
        board.put("I3", new Position("I2", "H3", "H4", "I4", null, null, "p1", "TOTTS", 1));
        board.put("I4", new Position("I3", "H4", "H5", "I5", null, null, "p1", "TOTTS", 1));
        board.put("I5", new Position("I4", "H5", "H6", null, null, null, "p1", "TOTTS", 1));

        return board;
    }

    public HashMap<String, Position> getBoard() {
        return board;
    }

    public Set<String> getPlayer1_piece() {
        players_pieces();
        return this.player1_piece;
    }

    public Set<String> getPlayer2_piece() {
        players_pieces();
        return this.player2_piece;
    }

    public void setPlayer1_piece(Set<String> player1_piece) {
        this.player1_piece = player1_piece;
    }

    public void setPlayer2_piece(Set<String> player2_piece) {
        this.player2_piece = player2_piece;
    }
}