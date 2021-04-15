package tzaar;

import java.util.ArrayList;

public class Position {
    private String left, upLeft, upRight, right, downRight, downLeft, player, piece;
    private Integer height;

    public Position(String left, String upLeft, String upRight, String right, String downRight,
                    String downLeft, String player, String piece, Integer height) {
        this.upLeft = upLeft;
        this.upRight = upRight;
        this.left = left;
        this.right = right;
        this.downRight = downRight;
        this.downLeft = downLeft;
        this.player = player;
        this.piece = piece;
        this.height = height;
    }

    public Position(Position position) {
        this.upLeft = position.upLeft;
        this.upRight = position.upRight;
        this.left = position.left;
        this.right = position.right;
        this.downRight = position.downRight;
        this.downLeft = position.downLeft;
        this.player = position.player;
        this.piece = position.piece;
        this.height = position.height;
    }

    public static ArrayList<String> neighbours(Position current) {
        ArrayList<String> neighbours_list = new ArrayList<>();

        if (current.getLeft() != null)
            neighbours_list.add(current.getLeft());

        if (current.getUpLeft() != null)
            neighbours_list.add(current.getUpLeft());

        if (current.getUpRight() != null)
            neighbours_list.add(current.getUpRight());

        if (current.getRight() != null)
            neighbours_list.add(current.getRight());

        if (current.getDownRight() != null)
            neighbours_list.add(current.getDownRight());

        if (current.getDownLeft() != null)
            neighbours_list.add(current.getDownLeft());

        return neighbours_list;
    }

    //ALL Getters/Setters below here.

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getUpLeft() {
        return upLeft;
    }

    public void setUpLeft(String upLeft) {
        this.upLeft = upLeft;
    }

    public String getUpRight() {
        return upRight;
    }

    public void setUpRight(String upRight) {
        this.upRight = upRight;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public String getDownLeft() {
        return downLeft;
    }

    public void setDownLeft(String downLeft) {
        this.downLeft = downLeft;
    }

    public String getDownRight() {
        return downRight;
    }

    public void setDownRight(String downRight) {
        this.downRight = downRight;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getPiece() {
        return piece;
    }

    public void setPiece(String piece) {
        this.piece = piece;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
}
