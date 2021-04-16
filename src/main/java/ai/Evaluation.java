package ai;

import tzaar.Board;
import tzaar.GamePlay;
import tzaar.GamePlay.Move;
import tzaar.Position;

public class Evaluation {

    public static double evaluate_state_waltz(Board next, String player){
        String opp_player;

        double points = 0;
        String player_won = GamePlay.isWon(next, false);

        if(player.equals(next.whose_turn))
            opp_player = GamePlay.next_player(player);
        else
            opp_player = next.whose_turn;

        StateInfo after_curr_pl = new StateInfo(next, player);
        StateInfo after_opp_pl = new StateInfo(next, opp_player);

        // Waltz Start
        //1
        points+=after_curr_pl.material_value(next);
        points-=after_opp_pl.material_value(next);

        //2
        if(player_won!=null) {
            if (player.equals(player_won))
                return 2000000;
            else
                return -2000000;
        }

        //3
        if(!next.whose_turn.equals(player) && after_curr_pl.isIndirectThreat(next, player, after_curr_pl))
            points+=1000;
        if(next.whose_turn.equals(player) && after_opp_pl.isIndirectThreat(next, opp_player, after_opp_pl))
            points-=1000;

        //5
        points+= after_curr_pl.calc_ZOC(next, player).get(0)*(1 - (double)after_opp_pl.getTzaar_count()/6);
        points+= after_curr_pl.calc_ZOC(next, player).get(1)*(1 - (double)after_opp_pl.getTzara_count()/9);
        points+= after_curr_pl.calc_ZOC(next, player).get(2)*(1 - (double)after_opp_pl.getTotts_count()/15);

        points-= after_opp_pl.calc_ZOC(next, opp_player).get(0)*(1 - (double)after_curr_pl.getTzaar_count()/6);
        points-= after_opp_pl.calc_ZOC(next, opp_player).get(1)*(1 - (double)after_curr_pl.getTzara_count()/9);
        points-= after_opp_pl.calc_ZOC(next, opp_player).get(2)*(1 - (double)after_curr_pl.getTotts_count()/15);

        //4
        if(next.first_move && after_opp_pl.getThreatening_opp_pieces().size()<=10 && next.whose_turn.equals(opp_player))
            points+=(10-after_opp_pl.getThreatening_opp_pieces().size())*19900 + 1000;

        if(next.first_move && after_curr_pl.getThreatening_opp_pieces().size()<=10 && next.whose_turn.equals(player))
            points-=(10-after_curr_pl.getThreatening_opp_pieces().size())*19900 + 1000;

        //6
        if(after_curr_pl.getTzaar_max_height() > after_opp_pl.getMax_height())
            points+=25000;

        if(after_curr_pl.getTzara_max_height() > after_opp_pl.getMax_height())
            points+=25000;

        if(after_curr_pl.getTotts_max_height() > after_opp_pl.getMax_height())
            points+=25000;

        if(after_opp_pl.getTzaar_max_height() > after_curr_pl.getMax_height())
            points-=25000;

        if(after_opp_pl.getTzara_max_height() > after_curr_pl.getMax_height())
            points-=25000;

        if(after_opp_pl.getTotts_max_height() > after_curr_pl.getMax_height())
            points-=25000;

        if(after_curr_pl.getSecures_types().size()==3)
            points+=100000;

        if(after_opp_pl.getSecures_types().size()==3)
            points-=100000;

        //7
        if(after_curr_pl.getTzaar_max_height()>=2 && after_curr_pl.getTzara_max_height()>=2 && after_curr_pl.getTotts_max_height()>=2)
            points+=50000;

        if(after_opp_pl.getTzaar_max_height()>=2 && after_opp_pl.getTzara_max_height()>=2 && after_opp_pl.getTotts_max_height()>=2)
            points-=50000;

        //8
        for(String piece: after_curr_pl.getThreatening_opp_pieces()){
            Position temp = next.getBoard().get(piece);
            int height = temp.getHeight();
            if(height == after_opp_pl.getMax_height())
                points+=50000;
        }
        //9 Not Implemented

        //10
        points+=after_curr_pl.calc_non_margin_stack(next);

        points-=after_opp_pl.calc_non_margin_stack(next);

        // Waltz End
        return points;
    }

    public static double evaluate_state(Board next, String player){
        String opp_player = GamePlay.next_player(player);
        double points = 0;
        String player_won;

        player_won = GamePlay.isWon(next, false);

        if(player_won!=null){
            if(player_won.equals(player)) {
                return 4000000;
            }
            return -4000000;
        }

        StateInfo after_curr_pl = new StateInfo(next, player);
        StateInfo after_opp_pl = new StateInfo(next, opp_player);

        if(after_curr_pl.getMax_height() > after_opp_pl.getMax_height())
            points+=10000;

        if(after_curr_pl.getTzaar_max_height() > after_opp_pl.getMax_height()
        && after_curr_pl.getTzara_max_height() > after_opp_pl.getMax_height()
        && after_curr_pl.getTotts_max_height() > after_opp_pl.getMax_height())
            points+=50000;

        if(after_curr_pl.getSecures_count()==0)
            points-=100000;

        if(after_curr_pl.getSecures_types().containsAll(after_curr_pl.getLess_types()))
            points+=10000;
        else if(after_curr_pl.getSecures_types().containsAll(after_curr_pl.getLess_types().subList(0, 2)))
            points+=5000;
        else if(after_curr_pl.getSecures_types().size()==2)
            points+=3000;
        else if(after_curr_pl.getSecures_types().size()==1 &&
                after_curr_pl.getSecures_types().contains(after_curr_pl.getLess_types().get(0)))
            points+=1200;
        else if(after_curr_pl.getSecures_types().size()==1 &&
                after_curr_pl.getSecures_types().contains(after_curr_pl.getLess_types().get(1)))
            points+=800;
        else if(after_curr_pl.getSecures_types().size()==1)
            points+=400;

        points+=after_curr_pl.calc_non_margin_stack_another_version(next);
        points+=after_curr_pl.material_value(next);

        points+= after_curr_pl.calc_ZOC(next, player).get(0)*(1 - (double)after_opp_pl.getTzaar_count()/6);
        points+= after_curr_pl.calc_ZOC(next, player).get(1)*(1 - (double)after_opp_pl.getTzara_count()/9);
        points+= after_curr_pl.calc_ZOC(next, player).get(2)*(1 - (double)after_opp_pl.getTotts_count()/15);

        if(after_opp_pl.getThreatening_opp_pieces().size()<=10)
            points+=(10-after_opp_pl.getThreatening_opp_pieces().size())*20000 + 1000;

        return points;
    }

    public static double evaluate_capture_move(Board board, Board next, String player, Position dest){
        String opp_player = GamePlay.next_player(player);
        StateInfo before_opp_pl = new StateInfo(board, opp_player);
        StateInfo after_opp_pl = new StateInfo(next, opp_player);

        double points = 0;
        if(after_opp_pl.getMax_height() < before_opp_pl.getMax_height())
            points+=50000;
        else if (before_opp_pl.getMax_height() == dest.getHeight() && before_opp_pl.getMax_height()!=1)
            points += 5000;

        if (dest.getPiece().equals(before_opp_pl.getLess_types().get(0)))
            points += 4000;
        if (dest.getPiece().equals(before_opp_pl.getLess_types().get(1)))
            points += 3000;
        points+=(dest.getHeight()-1)*7000;

        return points;
    }

    public static double evaluate_stack_move(Board board, Board next, String player, Position curr, Position dest){
        String opp_player = GamePlay.next_player(player);
        StateInfo before_curr_pl = new StateInfo(board, player);
        StateInfo after_curr_pl = new StateInfo(next, player);
        StateInfo after_opp_pl = new StateInfo(next, opp_player);
        double points = 0;
        String t;

        t = before_curr_pl.getLess_types().get(0);
        if(dest.getPiece().equals(t))
            points -= 10000;

        if(dest.getPiece().equals(before_curr_pl.getLess_types().get(1)))
            points -= 8000;

        if(dest.getPiece().equals("TZAAR") && before_curr_pl.getTzaar_count()==1)
            points-=500000;
        if(dest.getPiece().equals("TZARA") && before_curr_pl.getTzara_count()==1)
            points-=500000;
        if(dest.getPiece().equals("TOTTS") && before_curr_pl.getTotts_count()==1)
            points-=500000;

        if (after_curr_pl.getMax_height() > after_opp_pl.getMax_height()
                && after_curr_pl.getMax_height_types().contains(after_curr_pl.getLess_types().get(0)))
            points += 70000;

        if(before_curr_pl.getTzaar_count()<=2)
            if(curr.getPiece().equals("TZAAR") && curr.getPiece().equals(before_curr_pl.getLess_types().get(0)))
                points+=10000;
            else
                points+=5000;
        if(before_curr_pl.getTzara_count()<=2)
            if(curr.getPiece().equals("TZARA") && curr.getPiece().equals(before_curr_pl.getLess_types().get(0)))
                points+=10000;
            else
                points+=5000;
        if(before_curr_pl.getTotts_count()<=2)
            if(curr.getPiece().equals("TOTTS") && curr.getPiece().equals(before_curr_pl.getLess_types().get(0)))
                points+=10000;
            else
                points+=5000;

        return points;
    }

    public static double evaluate_move(Board board, String player, Move move){
        String opp_player = GamePlay.next_player(player);
        Board next = GamePlay.is_allowed(board, move);
        double points = 0;

        if(next==null)
            return 0;

        if(GamePlay.isWon(next, false)!=null || move.source.equals("PASS"))
            return 0;

        Position source = board.getBoard().get(move.source);
        Position dest = board.getBoard().get(move.dest);

        if(player.equals(board.whose_turn)) {
            if (!dest.getPlayer().equals(source.getPlayer()))
                points += evaluate_capture_move(board, next, player, dest);
            else
                points += evaluate_stack_move(board, next, player, source, dest);
        }
        else{
            if (!dest.getPlayer().equals(source.getPlayer()))
                points -= evaluate_capture_move(board, next, opp_player, dest);
            else
                points -= evaluate_stack_move(board, next, opp_player, source, dest);
        }

        return points;
    }
}
