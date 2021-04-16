package ai;

import tzaar.Board;
import tzaar.GamePlay;
import tzaar.GamePlay.Move;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class RandomMoves {
    public static class Result{
        double value = 0;
        Move move = null;

        public Result(double value, Move move) {
            this.value = value;
            this.move = move;
        }
    }
    public static Result play_random_move(Board board, Move move, String player){
        String player_won = GamePlay.isWon(board,  false);
        if(player_won!=null){
            if(player_won.equals(player))
                return new Result(2000000, move);
            return new Result(-2000000, move);
        }
        else{
            Random random = new Random();
            ArrayList<Move> list = GamePlay.players_all_moves_list(board);
            Move random_move = list.get(random.nextInt(list.size()));
            Board next = GamePlay.is_allowed(board, random_move);
            return play_random_move(next, move, player);
        }
    }

    public static Result random_move(Board board) {
        Random random = new Random();

        ArrayList<Move> list = GamePlay.players_all_moves_list(board);
        Move random_move = list.get(random.nextInt(list.size()));

        Board next = GamePlay.is_allowed(board, random_move);
        return play_random_move(next, random_move, board.whose_turn);
    }

    public static Move play_random_move(Board board){
        HashMap<Move, Double> map = new HashMap<>();
        for(int i = 0; i<100; i++){
            Result random_result = random_move(board);
            if(map.containsKey(random_result.move)){
                double temp = map.get(random_result.move);
                map.replace(random_result.move, random_result.value  + temp);
            }
            else
                map.put(random_result.move, random_result.value);
        }
        Double max = null;
        Move random_move= null;
        double value;
        for(Move temp: map.keySet()){
            value = map.get(temp);
            if(max==null || max < value){
                max = value;
                random_move = temp;
            }
        }

        return random_move;
    }

}
