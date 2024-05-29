package sudoku.entity;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import sudoku.entity.converters.BoardConverter2D;
import sudoku.entity.converters.BoardConverter3D;
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

    @Convert(converter = BoardConverter2D.class)
    private String[][] initialBoard;

    @Convert(converter = BoardConverter2D.class)
    private String[][] board;

    @Convert(converter = BoardConverter2D.class)
    private String[][] solvingSequence;

    @Convert(converter = BoardConverter3D.class)
    private String[][][] solvingNotes;

    @Convert(converter = BoardConverter2D.class)
    private String[][] highlightedFlags;

    private LocalTime elapsedTime;

    private LocalDateTime created;

    private LocalDateTime updated;
}
