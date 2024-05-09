package sudoku.model;

import lombok.Getter;

@Getter
public enum Difficulty {
    EASY(1), MEDIUM(2), HARD(3);

    private final int difficulty;

    Difficulty(int difficulty) {
        this.difficulty = difficulty;
    }
}
