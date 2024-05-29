package sudoku.service;

import sudoku.entity.Sudoku;

public interface SudokuService {

    Sudoku getSudoku(Long id);

    Sudoku saveSudoku(Sudoku sudoku);

    Sudoku updateSudoku(Long id, Sudoku sudoku);
}
