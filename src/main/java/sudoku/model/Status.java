package sudoku.model;

import lombok.Getter;

@Getter
public enum Status {

    NEW(1), IN_PROGRESS(2), FINISHED(3);

    private final int status;

    Status(int status) {
        this.status = status;
    }
}
