package sudoku.entity;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import sudoku.entity.util.BoardConverter;
import sudoku.model.Difficulty;
import sudoku.model.Status;
import sudoku.model.Type;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "sudoku")
@Data
public class Sudoku {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Type type;

    private Difficulty difficulty;

    private Status status;

    private String player;

    @Convert(converter = BoardConverter.class)
    private String[][] initialBoard;

    @Convert(converter = BoardConverter.class)
    private String[][] solvingNotes;       // 81 x 9

    @Convert(converter = BoardConverter.class)
    private String[][] solvingSequence;

    @Convert(converter = BoardConverter.class)
    private String[][] board;

    private LocalTime elapsedTime;

    private LocalDateTime created;

    private LocalDateTime updated;
}
