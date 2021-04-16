package ai;

import tzaar.Board;
import tzaar.GamePlay;
import tzaar.GamePlay.Move;
import tzaar.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StateInfo {

    public String opponent;
    private final ArrayList<String> corners = new ArrayList<String>(Arrays.asList("A1", "A5", "E1", "E9", "I1", "I5"));
    private final ArrayList<String> margin = new ArrayList<String>(Arrays.asList("A2", "A3", "A4", "B1", "C1", "D1", "F1", "G1",
                                            "H1", "I2", "I3", "I4", "H6", "G7", "F8", "D8", "C7", "B6"));
    private Set<String> pieces = new HashSet<>();
    private final ArrayList<String> less_types = new ArrayList<>();
    private int max_height;
    private final ArrayList<String> max_height_types = new ArrayList<>();
    private int tzaar_max_height = 0;
    private int tzara_max_height = 0;
    private int totts_max_height = 0;
    private int tzaar_count = 0;
    private int tzara_count = 0;
    private int totts_count = 0;

    private final Set<String> threatening_opp_pieces = new HashSet<>();
    private final Set<String> secures_types = new HashSet<>();
    private int secures_count;

    public StateInfo(Board board, String player) {
        calculate_info(board, player);
    }

    private void calculate_info(Board board, String player){
        Set<String> current_players_pieces;
        if(player.equals("p1")) {
            this.opponent = "p2";
            this.pieces = board.getPlayer1_piece();
        }
        else {
            this.opponent = "p1";
            this.pieces = board.getPlayer2_piece();
        }
        current_players_pieces = this.pieces;
        Position temp;
        int height = 0;
        String type;
        for(String piece: current_players_pieces){
            temp = board.getBoard().get(piece);
            type = temp.getPiece();
            height = temp.getHeight();

            if(type.equals("TZAAR") && height > this.tzaar_max_height){
                this.tzaar_max_height = height;
                this.tzaar_count++;
            }else if(type.equals("TZARA") && height > this.tzara_max_height){
                this.tzara_max_height = height;
                this.tzara_count++;
            } else if(type.equals("TOTTS") && height > this.totts_max_height){
                this.totts_max_height = height;
                this.totts_count++;
            }

            if(this.max_height < height)
                this.max_height = height;
        }
        if(this.max_height == this.tzaar_max_height)
            max_height_types.add("TZAAR");

        if(this.max_height == this.tzara_max_height)
            max_height_types.add("TZARA");

        if(this.max_height == this.totts_max_height)
            max_height_types.add("TOTTS");

        calc_less_type();
        count_curr_secures(board, player, current_players_pieces);
    }

    private void calc_less_type(){
        if(compare(this.tzaar_count, this.tzara_count,
                this.tzaar_max_height, this.tzara_max_height)<0){
            this.less_types.add(0, "TZARA");
            this.less_types.add(1, "TZAAR");

            if(compare(this.tzaar_count, this.totts_count,
                    this.tzaar_max_height, this.totts_max_height)>=0){
                this.less_types.add(2, "TOTTS");
            } else if(compare(this.tzara_count, this.totts_count,
                    this.tzara_max_height, this.totts_max_height)>=0){
                    this.less_types.add(1, "TOTTS");
            }else
                this.less_types.add(0, "TOTTS");

        }
        else{
            this.less_types.add(0,"TZAAR");
            this.less_types.add(1,"TZARA");

            if(compare(this.tzara_count, this.totts_count,
                    this.tzara_max_height, this.totts_max_height)>=0){
                this.less_types.add(2, "TOTTS");
            }
            else if(compare(this.tzaar_count, this.totts_count,
                    this.tzaar_max_height, this.totts_max_height)>=0){
                this.less_types.add(1, "TOTTS");
            }else
                this.less_types.add(0, "TOTTS");
        }
    }

    private int compare(int a , int b, int c, int d){
        if(a>b)
            return -1;
        else if(a<b)
            return 1;
        else
            return Integer.compare(d, c);
    }

    private void count_curr_secures(Board board, String player, Set<String> current_players_pieces){
        ArrayList<Move> moves_list = GamePlay.players_all_moves_list(board, opponent, true);
        Position temp;

        for(Move move: moves_list){
            current_players_pieces.remove(move.dest);
        }
        this.secures_count = current_players_pieces.size();

        for(String piece: current_players_pieces){
            temp = board.getBoard().get(piece);
            this.secures_types.add(temp.getPiece());
        }
    }

    public int material_value(Board board){
        Position temp;
        String type;
        int value = 0;
        for(String piece: this.pieces){
            temp = board.getBoard().get(piece);
            int piece_height = temp.getHeight();
            type = temp.getPiece();

            if(type.equals("TZAAR"))
                value+= calc_height_value(piece_height)*calc_count_value(this.tzaar_count);
            else if(type.equals("TZARA"))
                value+= calc_height_value(piece_height)*calc_count_value(this.tzara_count);
            else
                value+= calc_height_value(piece_height)*calc_count_value(this.totts_count);

        }
        return value;
    }

    // For Material Value
    private int calc_height_value(int height){
        int value = 0;
        switch(height){
            case 1:
                value+=10;
                break;
            case 2: case 12:
                value+=100;
                break;
            case 3: case 9:
                value+=150;
                break;
            case 4:
                value+=155;
                break;
            case 5: case 6: case 7: case 8:
                value+=160;
                break;
            case 10:
                value+=140;
                break;
            case 11:
                value+=130;
                break;
            case 13:
                value+=60;
                break;
            case 14:
                value+=30;
                break;
            default:
                break;
        }
        return value;
    }

    // For Material Value
    private int calc_count_value(int type_count){
        switch (type_count){
            case 1:
                return 100;
            case 2:
                return 90;
            case 3:
                return 40;
            case 4:
                return 18;
            case 5:
                return 10;
            case 6:
                return 8;
            case 7:
                return 6;
            case 8:
                return 5;
            case 9:
                return 4;
            case 10:
                return 3;
            case 11:
                return 2;
            case 12: case 13: case 14: case 15:
                return 1;
            default:
                return 0;
        }
    }

    public boolean isIndirectThreat(Board board, String player, StateInfo opposition){
        ArrayList<Move> moves_list = GamePlay.players_all_moves_list(board, player, true);
        Position temp;
        String type;
        for(Move move: moves_list){
            temp = board.getBoard().get(move.dest);
            type = temp.getPiece();

            if(type.equals("TZAAR"))
                if(opposition.getTzaar_count()==1)
                    return true;
            if(type.equals("TZARA"))
                if(opposition.getTzara_count()==1)
                    return true;
            if(type.equals("TOTTS"))
                if(opposition.getTotts_count()==1)
                    return true;

        }

        return false;
    }

    public ArrayList<Double> calc_ZOC(Board board, String player){
        ArrayList<Move> moves_list = GamePlay.players_all_moves_list(board, player, true);
        Position temp;
        String type;
        double tzaar_zoc = 0, tzara_zoc = 0, totts_zoc = 0;
        ArrayList<Double> zoc_values = new ArrayList<>();

        for(Move move: moves_list){
            temp = board.getBoard().get(move.dest);
            type = temp.getPiece();

            if(type.equals("TZAAR")){
                tzaar_zoc+=1;
            }else if(type.equals("TZARA"))
                tzara_zoc+=1;
            else
                totts_zoc+=1;
            this.threatening_opp_pieces.add(move.dest);

        }
        zoc_values.add(0, tzaar_zoc);
        zoc_values.add(1, tzara_zoc);
        zoc_values.add(2, totts_zoc);

        return zoc_values;
    }

    // For WALTZ
    public double calc_non_margin_stack(Board board){
        Position temp;
        int height;
        double value = 0;
        for(String piece: this.pieces){
            temp = board.getBoard().get(piece);
            height = temp.getHeight();
            if(height!=1 && !corners.contains(piece) && !margin.contains(piece)){
                switch(height){
                    case 5: case 6: case 7: case 8:
                        value+= 25;
                        break;
                    case 4:
                        value+= 22;
                        break;
                    case 3: case 9:
                        value+= 19;
                        break;
                    case 10: case 11:
                        value+= 16;
                        break;
                    case 2: case 12:
                        value+= 13;
                        break;
                    case 13: case 14:
                        value+= 10;
                        break;
                    default:
                        break;
                }
            }
            else if(height !=1 && corners.contains(piece))
                value-=30;
        }
        return value;
    }

    //For our AI
    public double calc_non_margin_stack_another_version(Board board){
        Position temp;
        int height;
        double value = 0;
        for(String piece: this.pieces){
            temp = board.getBoard().get(piece);
            height = temp.getHeight();
            if(height!=1 && !corners.contains(piece) && !margin.contains(piece)){
                switch(height){
                    case 5: case 6: case 7: case 8:
                        value+= 2500;
                        break;
                    case 4:
                        value+= 2200;
                        break;
                    case 3: case 9:
                        value+= 1900;
                        break;
                    case 10: case 11:
                        value+= 1600;
                        break;
                    case 2: case 12:
                        value+= 1300;
                        break;
                    case 13: case 14:
                        value+= 1000;
                        break;
                    default:
                        break;
                }
            }
            else if(height !=1 && corners.contains(piece))
                value-=3000;
            else if(height !=1 && margin.contains(piece))
                value-=1500;
        }
        return value;
    };

    //All getters

    public ArrayList<String> getLess_types() {
        return less_types;
    }

    public int getMax_height() {
        return max_height;
    }

    public ArrayList<String> getMax_height_types() {
        return max_height_types;
    }

    public int getTzaar_max_height() {
        return tzaar_max_height;
    }

    public int getTzara_max_height() {
        return tzara_max_height;
    }

    public int getTotts_max_height() {
        return totts_max_height;
    }

    public int getTzaar_count() {
        return tzaar_count;
    }

    public int getTzara_count() {
        return tzara_count;
    }

    public int getTotts_count() {
        return totts_count;
    }

    public Set<String> getThreatening_opp_pieces() {
        return threatening_opp_pieces;
    }

    public Set<String> getSecures_types() {
        return secures_types;
    }

    public int getSecures_count() {
        return secures_count;
    }
}
