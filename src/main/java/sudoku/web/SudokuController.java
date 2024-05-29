package sudoku.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sudoku.entity.Sudoku;
import sudoku.service.SudokuService;

@RestController
@RequestMapping("/api/sudoku")
@CrossOrigin(origins = "http://localhost:3000/")
public class SudokuController {

	private static final Logger logger = LogManager.getLogger();

	@Autowired
	private SudokuService sudokuService;

	@GetMapping("/{id}")
	public Sudoku getSudoku(@PathVariable Long id) {
		return sudokuService.getSudoku(id);
	}

	@PostMapping
	public Sudoku createSudoku(@RequestBody Sudoku sudoku) {
		return sudokuService.saveSudoku(sudoku);
	}

	@PutMapping("/{id}")
	public Sudoku updateSudoku(@PathVariable Long id, @RequestBody Sudoku sudoku) {
		return sudokuService.updateSudoku(id, sudoku);
	}

//	@PutMapping("/{id}")
//	public ResponseEntity<Sudoku> updateSudoku(@PathVariable Long id,
//											   @RequestBody Sudoku sudoku) {
//		sudokuService.updateSudoku(id, sudoku);
//		return ResponseEntity.ok(sudoku);
//	}

//	@RequestMapping(value = "/demo", method = { RequestMethod.POST })
//	public ModelAndView reloadAllReports(HttpServletRequest request) {
//		try {
//			return Generator
//			return listAllReports(request.getSession(),
//					messageSource.getMessage("reports.reload", null, RequestContextUtils.getLocale(request)),
//					LEVEL_SUCCESS);
//		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
//			return listAllReports(request.getSession(), message, LEVEL_ERROR);
//		}
//	}
}
