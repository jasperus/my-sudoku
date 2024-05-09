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
  solvingNotes: string[][][];
  solvingSequence: string[][];
  board: string[][];
  
  elapsedTime: number;
}

export const initializedArray2D: string[][] = Array.from({length: 9},
  () => Array.from({length: 9}, () => ''));
export const initializedArray3D: string[][][] = Array.from({length: 9},
  () => Array.from({length: 9}, () => Array.from({length: 9},  () => '')));

export const sudokuInit : Sudoku = {
  type: Type.MANUAL_ENTRY,
  difficulty: Difficulty.EASY,
  player: "",
  
  initialBoard: initializedArray2D,
  solvingNotes: initializedArray3D,
  solvingSequence: initializedArray2D,
  board: initializedArray2D,
  
  elapsedTime: 0
}