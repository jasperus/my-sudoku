package sudoku.service;

import sudoku.entity.Sudoku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sudoku.repository.SudokuRepository;
import sudoku.util.SudokuMapper;

import java.time.LocalDateTime;

@Service
public class SudokuServiceImpl implements SudokuService {

    @Autowired
    private SudokuRepository sudokuRepository;

    @Override
    public Sudoku getSudoku(Long id) {
        return sudokuRepository.findById(id).get();
    }

    @Override
    public Sudoku saveSudoku(Sudoku sudoku) {
        // FIXME: ovo je temporary, dok na ruke spremam kroz Postman
        //  - ulazni parametar ne treba biti Sudoku objekt, trebao bih izdvojiti board u poseban model (ideja)
        Sudoku newSudoku = SudokuMapper.prepareBoardForSaving(sudoku.getBoard());
        newSudoku.setCreated(LocalDateTime.now());
        sudokuRepository.save(newSudoku);
        return newSudoku;
    }

//    @Override
//    public Sudoku updateSudoku(Long id, Sudoku sudoku) {
//        Sudoku existingSudoku = sudokuRepository.findById(id).get();
//
//        existingSudoku.setInitialBoard(sudoku.getInitialBoard());
//        existingSudoku.setBoard(sudoku.getBoard());
//
//        sudokuRepository.save(existingSudoku);
//        return sudoku;
//    }
}
