package model.player;

import model.statistics.GameSession;

public class PlayerSession {
    Player player;
    GameSession gameSession;
    int score;
    boolean isFirst;

    public PlayerSession(Player player, GameSession gameSession) {
        this.player = player;
        this.gameSession = gameSession;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }
}
