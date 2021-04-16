package ai;

import tzaar.Board;
import tzaar.GamePlay;
import tzaar.GamePlay.Move;

import java.util.ArrayList;

public class Min_Max_Waltz {
    public static class Result{
        double value = 0;
        Move move = null;

        public Result(double value, Move move) {
            this.value = value;
            this.move = move;
        }
    }

    private static Result min_max(Board board, String player, int depth) {
        double value = 0, max = -100000000, min = 100000000;

        if (depth == 0 || GamePlay.isWon(board, false) != null) {
            return new Result(Evaluation.evaluate_state_waltz(board, player), null);
        }
        ArrayList<Move> moves_list = GamePlay.players_all_moves_list(board);

        Move min_move = null;
        Move max_move = null;
        for (Move poss_move : moves_list) {
            Board next = GamePlay.is_allowed(board, poss_move);

            value = min_max(next, player, depth - 1).value;

            if (max < value) {
                max = value;
                max_move = poss_move;
            }

            if(min > value) {
                min = value;
                min_move = poss_move;
            }
        }
        if(board.whose_turn.equals(player))
            return new Result(max, max_move);
        return new Result(min, min_move);
    }

    public static Move min_max(Board board, int depth) {
        Result value_move = min_max(board, board.whose_turn, depth);
        return value_move.move;
    }
}
