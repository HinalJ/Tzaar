package ai;

import tzaar.Board;
import tzaar.GamePlay;
import tzaar.GamePlay.Move;

import java.util.ArrayList;

public class Min_Max_OurAI {

    public static class Result{
        double value = 0;
        Move move = null;

        public Result(double value, Move move) {
            this.value = value;
            this.move = move;
        }
    }

    public static Move play_good_move(Board board, String player, int depth) {
        double max = -1000000000, value = 0;
        Move good_move = null;
        ArrayList<Move> moves_list = GamePlay.players_all_moves_list(board);
        Board next;

        for (Move poss_move : moves_list) {
            next = GamePlay.is_allowed(board, poss_move);

            value = min_max(next, player, depth-1).value;
            if (max < value) {
                max = value;
                good_move = poss_move;
            }
        }

        return good_move;
    }

    private static Result min_max(Board board, String player, int depth) {
        double value = 0, max = -100000000, min = 100000000, bias = 0;

        if (depth == 0 || GamePlay.isWon(board, false) != null) {
            return new Result(Evaluation.evaluate_state(board, player)
                    -Evaluation.evaluate_state(board, GamePlay.next_player(player)), null);
        }
        ArrayList<Move> moves_list = GamePlay.players_all_moves_list(board);
        Board next;

        Move min_move = null;
        Move max_move = null;
        for (Move poss_move : moves_list) {

            next = GamePlay.is_allowed(board, poss_move);
            if(depth-1 == 0 || GamePlay.isWon(next, false) != null)
                bias = Evaluation.evaluate_move(board, player, poss_move);

            value = min_max(next, player, depth - 1).value + bias;

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

    public static Move play_good_move(Board board, int depth) {
        return play_good_move(board, board.whose_turn, depth);
    }

}
