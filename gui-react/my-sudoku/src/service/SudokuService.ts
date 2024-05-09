import axios from "axios";
import {Sudoku} from "../model/Sudoku";

const SUDOKU_API_BASE_URL = "http://localhost:8080/api/sudoku";

// export function getEmployees(query: string, pageNumber: number, pageSize: number, sortColumn: string, sortDirection: SortInfo) {
// export function getSudoku(query: string, pageNumber: number, pageSize: number) {
//   return axios.get(SUDOKU_API_BASE_URL, {
//     params:
//       {
//         query: query,
//         pageNumber: pageNumber,
//         pageSize: pageSize,
//         // sortColumn: sortColumn,
//         // sortDirection: sortDirection
//       }
//   });
// }

export function getAllSudoku() {
  return axios.get(SUDOKU_API_BASE_URL + '/all');
}

export function getSudokuById(id: number) {
  return axios.get(SUDOKU_API_BASE_URL + '/' + id);
}

export function saveSudoku(sudoku: Sudoku) {
  return axios.post(SUDOKU_API_BASE_URL, sudoku);
}

export function updateSudoku(id: number, sudoku: Sudoku) {
  return axios.put(SUDOKU_API_BASE_URL + "/" + id, sudoku);
}

export function deleteSudoku(id: number) {
  return axios.delete(SUDOKU_API_BASE_URL + '/' + id);
}
