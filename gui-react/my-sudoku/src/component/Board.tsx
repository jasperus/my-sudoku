import React, {JSX, useEffect, useState} from 'react';
import Segment from "./Segment";
import "../style.css";
import {getSudokuById} from "../service/SudokuService";
import {initializedArray2D, initializedArray3D, Sudoku, sudokuInit} from "../model/Sudoku";

export default function Board() {
  
  const [gameStarted, setGameStarted] = useState<boolean>(false);
  const [sudoku, setSudoku] = useState<Sudoku>(sudokuInit);
  const [initBoard, setInitBoard] = useState<string[][]>(initializedArray2D);
  // const [board, setBoard] = useState<string[][]>(initializedArray2D);
  const [solvingNotes, setSolvingNotes] = useState<string[][][]>(initializedArray3D);
  const [solvingSequence, setSolvingSequence] = useState<string[][]>(initializedArray2D);
  const [matrix, setMatrix] = useState<string[][]>(initializedArray2D);
  const [segments, setSegments] = useState<JSX.Element[]>([]);
  
  // TODO: useEffect da se spremi u bazu na svaku promjenu
  
  useEffect(() => {
    const fetchSudoku = async () => {
      try {
        const response = await getSudokuById(4);
        if (response) {
          setSudoku(response.data);
        }
      } catch (error) {
        console.log(error);
      }
    };
    
    fetchSudoku();
    // const response = getSudokuById(3);
    //     if (response) {
    //       setSudoku(response.data);
    //       if (sudoku?.board) {
    //         setBoard(sudoku?.board);
    //       }
    //     }
    
    console.log("useEffect - fetchEmployees called");
  }, []);
  
  useEffect(() => {
    console.log("useEffect - sudoku changed");
    setInitBoard(sudoku.initialBoard);
    // setBoard(sudoku.board);
    // setSolvingNotes(sudoku.solvingNotes);
    // setSolvingSequence(sudoku.solvingSequence);
    
    // console.log(JSON.stringify(initBoard));
    
    const boardSegments = transformBoardToSegments(sudoku.initialBoard);
    setMatrix(boardSegments);
    // console.log("matrix:\n" + JSON.stringify(matrix));
    
    // console.log("solvingSequence:\n" + JSON.stringify(sudoku.solvingSequence));
    const solvingSequenceSegments = transformBoardToSegments(sudoku.solvingSequence);
    setSolvingSequence(solvingSequenceSegments);
    // console.log("solvingSequenceSegments:\n" + JSON.stringify(solvingSequenceSegments));
  }, [sudoku]);
  
  function transformBoardToSegments(board: string[][]): string[][] {
    let copy: string[][] = Array.from({length: 9}, () => Array.from({length: 9}, () => ''));
    for (let x = 0; x < 9; x++) {
      for (let y = 0; y < 9; y++) {
        let s_x = Math.floor(x / 3) * 3 + Math.floor(y / 3);
        let s_y = (x % 3) * 3 + y % 3;
        // console.log(`x=${x}, y=${y}, s_x=${s_x}, s_y=${s_y}`);
        // FIXME: nule ne bi trebale završiti u bazi, ovo je samo za početak
        // if (board[x][y] == '0') {
        //   copy[s_x][s_y] = '';
        // } else {
        copy[s_x][s_y] = board[x][y];
        // }
      }
    }
    return copy;
    // setMatrix(copy);
  }
  
  
  useEffect(() => {
    // console.log("useEffect - matrix changed:\n" + JSON.stringify(matrix));
    console.log("useEffect - matrix changed:");
    console.log(JSON.stringify(matrix));
    if (matrix) {
      
      let tempSegments: JSX.Element[] = [];
      for (let i = 0; i < 9; i++) {
        tempSegments.push(
          <Segment key={i}
                   segmentIndex={i}
                   values={matrix[i]}
                   solvingSequences={solvingSequence[i]}
                   onSquareChange={onSquareChange}/>
        );
      }
      
      setSegments(tempSegments);
    }
  }, [matrix]);
  
  const onSquareChange = (segmentIndex: number, squareIndex: number, value: string) => {
    console.log("[Board.tsx] Square with index [" + segmentIndex + ", " + squareIndex + "], value=" + value);
    
    let updatedMatrix = [...matrix];
    updatedMatrix[segmentIndex][squareIndex] = value;
    setMatrix(updatedMatrix);
  };
  
  return (
    <>
      <div>Test</div>
      <div className="baseStyle board">
        {segments}
      </div>
    </>
  );
}
