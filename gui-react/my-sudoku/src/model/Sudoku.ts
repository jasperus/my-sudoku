import {SquareCssClass} from "./constants";

export type SudokuForm = {
  id: number;
  firstName: string;
  lastName: string;
  fullName: string;
}

// export const types: TypeKeys[] = [
//   {name: "EASY"},
//   {name: "MEDIUM"},
//   {name: "HARD"}
// ];

export const Type= {
  MANUAL_ENTRY: 1,
  GENERATED: 2
};

export type TypeKeys = (typeof Type)[keyof typeof Type];

export const Difficulty= {
  EASY: 1,
  MEDIUM: 2,
  HARD: 3
};

export type DifficultyKeys = (typeof Difficulty)[keyof typeof Difficulty];

export type Sudoku = {
  id?: number;
  type: TypeKeys;
  difficulty: DifficultyKeys;
  // status: Status;
  player: string;
  
  initialBoard: string[][];
  board: string[][];
  solvingSequence: string[][];
  solvingNotes: string[][][];
  highlightedFlags: string[][];
  
  elapsedTime: number;
}

// export const initializedArray1D: string[] = Array.from({length: 9}, () => '');

export const initializedArray2D = (): string[][] => {
  return Array.from({length: 9}, () => Array.from({length: 9}, () => ''));
}

export const initializedArray3D = (): string[][][] => {
  return Array.from({length: 9}, () => Array.from({length: 9}, () => Array.from({length: 9},  () => '')));
}

export const initializedArray3Da = (): string[][][] => {
  return Array.from({length: 9}, () => Array.from({length: 9}, () => Array.from({length: 2},  () => '')));
}

export const initializedArrayCss = (): SquareCssClass[][] => {
  return Array.from({length: 9}, () =>
    Array.from({length: 9}, () => ({
      selected: false,
      highlighted: false,
      highlighted_error: false,
      invalid: false
    })));
}

export const sudokuInit : Sudoku = {
  type: Type.MANUAL_ENTRY,
  difficulty: Difficulty.EASY,
  player: "",
  
  board: initializedArray2D(),
  initialBoard: initializedArray2D(),
  solvingSequence: initializedArray2D(),
  solvingNotes: initializedArray3D(),
  highlightedFlags: initializedArray2D(),
  
  elapsedTime: 0
}