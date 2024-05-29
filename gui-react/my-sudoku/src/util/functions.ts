import {SquareCssClass} from "../model/constants";
import {json} from "stream/consumers";

export function transformBoardToSegments(board: string[][]): string[][];
export function transformBoardToSegments(board: string[][][]): string[][][];
export function transformBoardToSegments(board: SquareCssClass[][]): SquareCssClass[][];

export function transformBoardToSegments(board: any): any {
  let copy: string[][] = Array.from({length: 9}, () => Array.from({length: 9}, () => ''));
  for (let x = 0; x < 9; x++) {
    for (let y = 0; y < 9; y++) {
      let segment_x = Math.floor(x / 3) * 3 + Math.floor(y / 3);
      let segment_y = (x % 3) * 3 + y % 3;
      // console.log(`x=${x}, y=${y}, s_x=${s_x}, s_y=${s_y}`);
      copy[segment_x][segment_y] = board[x][y];
    }
  }
  return copy;
}

export function transformSegmentsToBoard(board: any): any {
  let copy: string[][] = Array.from({length: 9}, () => Array.from({length: 9}, () => ''));
  for (let x = 0; x < 9; x++) {
    for (let y = 0; y < 9; y++) {
      let board_x = Math.floor(x / 3) * 3 + Math.floor(y / 3);
      let board_y = (x % 3) * 3 + y % 3;
      copy[board_x][board_y] = board[x][y];
    }
  }
  return copy;
}

export function checkIsValid(segments: string[][], segmentIndex: number, squareIndex: number, value: string): boolean {
  const board = transformSegmentsToBoard(segments);
  const row = Math.floor(segmentIndex / 3) * 3 + Math.floor(squareIndex / 3);
  const column = (segmentIndex % 3) * 3 + squareIndex % 3;
  
  const segmentContainsValue = segments[segmentIndex].includes(value);
  const rowContainsValue = board[row].includes(value);
  let columnContainsValue = false;
  for (let r = 0; r < 9; r++) {
    if (board[r][column] == value) {
      columnContainsValue = true;
      break;
    }
  }
  console.log("[checkIsValid] segmentContainsValue: %s, rowContainsValue: %s, columnContainsValue=%s",
    segmentContainsValue.toString(), rowContainsValue.toString(), columnContainsValue.toString());
  
  return !(segmentContainsValue || rowContainsValue || columnContainsValue);
}

export function calculateSquareHighlights(boardSegments: string[][], segmentIndex: number, squareIndex: number, highlightedSegments: SquareCssClass[][]): SquareCssClass[][] {
  // Reset all highlights
  for (let i = 0; i < 9; i++) {
    for (let j = 0; j < 9; j++) {
      highlightedSegments[i][j].selected = false;
      highlightedSegments[i][j].highlighted = false;
      highlightedSegments[i][j].highlighted_error = false;
    }
  }
  
  // Return early if the clicked square is not empty
  if (boardSegments[segmentIndex][squareIndex] !== '') {
    return highlightedSegments;
  }
  
  // Highlight all empty squares in the same segment
  for (let i = 0; i < 9; i++) {
    if (boardSegments[segmentIndex][i] === '') {
      highlightedSegments[segmentIndex][i].highlighted = true;
    }
  }
  
  // Transform both arrays, so we can calculate highlights for row and column
  let highlightedSegmentsTransformed: SquareCssClass[][] = transformSegmentsToBoard(highlightedSegments);
  let boardSegmentsTransformed = transformSegmentsToBoard(boardSegments);
  
  // Highlight all empty squares in the same row and column
  let selected_square_x = Math.floor(segmentIndex / 3) * 3 + Math.floor(squareIndex / 3);
  let selected_square_y = (segmentIndex % 3) * 3 + squareIndex % 3;
  
  for (let x = 0; x < 9; x++) {
    for (let y = 0; y < 9; y++) {
      if (boardSegmentsTransformed[x][y] != '') {
        continue;
      }
      if (
        (x == selected_square_x && y != selected_square_y) ||
        (x != selected_square_x && y == selected_square_y)
      ) {
        highlightedSegmentsTransformed[x][y].highlighted = true;
      }
    }
  }
  
  // Mark the clicked square as selected and remove highlighted flag
  highlightedSegments[segmentIndex][squareIndex].selected = true;
  highlightedSegments[segmentIndex][squareIndex].highlighted = false;
  
  // Transform board back to segments and return
  return transformBoardToSegments(highlightedSegmentsTransformed);
}