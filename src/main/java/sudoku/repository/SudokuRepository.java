package sudoku.repository;

import sudoku.entity.Sudoku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SudokuRepository extends JpaRepository<Sudoku, Long> {

}
