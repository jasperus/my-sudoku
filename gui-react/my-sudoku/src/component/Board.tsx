import React, {JSX, useEffect, useState} from 'react';
import Segment from "./Segment";
import "../style.css";
import {getSudokuById, updateSudoku} from "../service/SudokuService";
import {initializedArray2D, initializedArray3D, initializedArrayCss, Sudoku, sudokuInit} from "../model/Sudoku";
import {
  calculateSquareHighlights,
  checkIsValid,
  transformBoardToSegments,
  transformSegmentsToBoard
} from "../util/functions";
import {SquareCssClass} from "../model/constants";

export default function Board() {
  
  const [gameStarted, setGameStarted] = useState<boolean>(false);
  const [sudoku, setSudoku] = useState<Sudoku>(sudokuInit);
  const [initBoard, setInitBoard] = useState<string[][]>(initializedArray2D());
  const [boardSegments, setBoardSegments] = useState<string[][]>(initializedArray2D);
  const [solvingNotesSegments, setSolvingNotesSegments] = useState<string[][][]>(initializedArray3D());
  const [solvingSequenceSegments, setSolvingSequenceSegments] = useState<string[][]>(initializedArray2D());
  const [solvingSequenceCurrent, setSolvingSequenceCurrent] = useState<number>(0);
  const [highlightedSegments, setHighlightedSegments] = useState<SquareCssClass[][]>(initializedArrayCss());
  const [segments, setSegments] = useState<JSX.Element[]>([]);
  const [notesMode, setNotesMode] = useState<boolean>(false);
  const [message, setMessage] = useState<string>('initial message');
  const [isValid, setIsValid] = useState<boolean>(false);
  
  
  // *** GET SUDOKU
  useEffect(() => {
    const fetchSudoku = async () => {
      try {
        const response = await getSudokuById(5);
        if (response) {
          setSudoku(response.data);
        }
      } catch (error) {
        console.log(error);
      }
    };
    
    fetchSudoku();
    console.log("useEffect - fetchEmployees called");
  }, []);
  
  // *** UPDATE SUDOKU
  useEffect(() => {
    
    let sudokuUpdated = sudoku;
    sudokuUpdated.board = transformSegmentsToBoard(boardSegments);
    sudokuUpdated.solvingSequence = transformSegmentsToBoard(solvingSequenceSegments);
    sudokuUpdated.solvingNotes = transformSegmentsToBoard(solvingNotesSegments)
    
    const update = async () => {
      try {
        const response = await updateSudoku(5, sudokuUpdated);
        if (response) {
          console.log("Sudoku[%s] successfully updated!", 5);
        }
      } catch (error) {
        console.log(error);
      }
    };
    
    update();
    // const response = getSudokuById(3);
    //     if (response) {
    //       setSudoku(response.data);
    //       if (sudoku?.board) {
    //         setBoard(sudoku?.board);
    //       }
    //     }
    
    console.log("useEffect - updateSudoku");
  }, [boardSegments, solvingSequenceSegments, solvingNotesSegments]);
  
  useEffect(() => {
    console.log("useEffect - sudoku changed");
    setInitBoard(sudoku.initialBoard);
    
    const boardTransformed = transformBoardToSegments(sudoku.board);
    setBoardSegments(boardTransformed);
    // console.log("matrix:\n" + JSON.stringify(matrix));
    
    // console.log("solvingSequence:\n" + JSON.stringify(sudoku.solvingSequence));
    const solvingSequenceTransformed = transformBoardToSegments(sudoku.solvingSequence);
    setSolvingSequenceSegments(solvingSequenceTransformed);
    // console.log("solvingSequenceTransformed:\n" + JSON.stringify(solvingSequenceTransformed));
    
    const solvingNotesTransformed = transformBoardToSegments(sudoku.solvingNotes);
    setSolvingNotesSegments(solvingNotesTransformed);
  }, [sudoku]);
  
  const onSquareFocus = (segmentIndex: number, squareIndex: number) => {
    console.log("[Board - onSquareFocus] Square[%s, %s]",
      segmentIndex, squareIndex);
    
    // TODO: za svaki selektirani square, treba highlightati u drugoj boji row/column/segment
    // TODO: kasnije dodati flag helperMode - ako nije uključen, nemoj highliht-ati
    
    const highlightedSegmentsUpdated = calculateSquareHighlights(boardSegments, segmentIndex, squareIndex, highlightedSegments);
    setHighlightedSegments(highlightedSegmentsUpdated);
  };
  
  const onSquareChange = (segmentIndex: number, squareIndex: number, value: string) => {
    console.log("[Board - onSquareChange] Square[%s, %s], Value=%s",
      segmentIndex, squareIndex, value);
    
    // TODO: ovo za sada prebaciti u state i ne dozvoliti daljnji unos nakon jednog pogrešnog...a može tako i ostati za dalje
    const valid = checkIsValid(boardSegments, segmentIndex, squareIndex, value);
    // console.log("valid=%s, isValid=%s", valid.toString(), isValid.toString());
    setIsValid(valid);
    
    if (!isValid) {
      setMessage("You have entered wrong number! Correct it before continuing.")
      return;
    }
    
    let updatedBoardSegments = [...boardSegments];
    updatedBoardSegments[segmentIndex][squareIndex] = value;
    setBoardSegments(updatedBoardSegments);
    
    if (isValid) {
      // TODO: problem - što ako označim nekoliko krivih, pa onda pravi, da li i onda označavam solving sequence
      // TODO: da li da uvedem undo opciju - da li u tom slučaju smanjujem/brišem solving sequence?
      let newSolvingSequenceCurrent = solvingSequenceCurrent + 1;
      let updatedSolvingSequence = [...solvingSequenceSegments];
      updatedSolvingSequence[segmentIndex][squareIndex] = newSolvingSequenceCurrent.toString();
      // FIXME: ovaj state se ne ažurira unutar ove funkcije, pa moram koristiti helper varijablu
      //  a nije baš spretno raditi novi useEffect, zbog ažuriranja solvingSequence matrice
      setSolvingSequenceCurrent(newSolvingSequenceCurrent);
    }
    
    if (!isValid) {
      let updatedHighlightedSegments = [...highlightedSegments];
      updatedHighlightedSegments[segmentIndex][squareIndex].invalid = true;
      setHighlightedSegments(updatedHighlightedSegments);
      // console.log("updatedHighlightedSegments:\n" + JSON.stringify(updatedHighlightedSegments));
      // console.log("highlightedSegments:\n" + JSON.stringify(highlightedSegments));
    }
    
  };
  
  const onNotesChange = (segmentIndex: number, squareIndex: number, notes: string[]) => {
    // console.log("[Board - onNotesChange] Square[%s, %s], Notes[%s]",
    //   segmentIndex, squareIndex, notes);
    
    let updatedSolvingNotes = [...solvingNotesSegments];
    updatedSolvingNotes[segmentIndex][squareIndex] = notes;
    setSolvingNotesSegments(updatedSolvingNotes);
    // console.log("Solving notes:\n" + JSON.stringify(solvingNotes));
  }
  
  useEffect(() => {
    console.log("useEffect - matrix changed:");
    // console.log("Matrix:\n" + JSON.stringify(matrix));
    // console.log("Solving sequence:\n" + JSON.stringify(solvingSequence));
    if (boardSegments) {
      let tempSegments: JSX.Element[] = [];
      
      for (let i = 0; i < 9; i++) {
        tempSegments.push(
          <Segment key={i}
                   segmentIndex={i}
                   values={boardSegments[i]}
                   solvingSequences={solvingSequenceSegments[i]}
                   notesMode={notesMode}
                   solvingNotes={solvingNotesSegments[i]}
                   cssClasses={highlightedSegments[i]}
                   onSquareChange={onSquareChange}
                   onNotesChange={onNotesChange}
                   onSquareFocus={onSquareFocus}
          />
        );
      }
      
      setSegments(tempSegments);
    }
  }, [boardSegments, highlightedSegments, notesMode, solvingNotesSegments]);
  
  return (
    <>
      <div>Test</div>
      <div className="base-style board">
        {segments}
      </div>
      <div>
        <button onClick={() => setNotesMode(!notesMode)}>
          {notesMode ? 'Disable Notes Mode' : 'Enable Notes Mode'}
        </button>
      </div>
      <div className="text-green-600">
        Message: {message}
      </div>
    </>
  );
}
