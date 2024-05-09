package sudoku.model;

import lombok.Getter;

@Getter
public enum Type {

    MANUAL_ENTRY(1), GENERATED(2);

    private final int type;

    Type(int type) {
        this.type = type;
    }
}
