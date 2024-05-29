import React, {JSX} from "react";
import Square from "./Square";
import "../style.css";
import Notes from "./Notes";
import {SquareCssClass} from "../model/constants";

interface SegmentProps {
  segmentIndex: number;
  values: string[];
  solvingSequences: string[];
  notesMode: boolean;
  solvingNotes: string[][];
  cssClasses: SquareCssClass[];
  onSquareChange: (segmentIndex: number, squareIndex: number, value: string) => void;
  onNotesChange: (segmentIndex: number, squareIndex: number, notes: string[]) => void;
  onSquareFocus: (segmentIndex: number, squareIndex: number) => void;
}

export default function Segment({
                                  segmentIndex,
                                  values,
                                  solvingSequences,
                                  notesMode,
                                  solvingNotes,
                                  cssClasses,
                                  onSquareChange,
                                  onNotesChange,
                                  onSquareFocus
                                }: SegmentProps) {
  
  let enabled;
  let rows: JSX.Element[] = [];
  
  for (let i = 0; i < 9; i++) {
    enabled = solvingSequences[i] != "0";
    
    if (solvingSequences[i] != '' || !notesMode) {
      rows.push(
        <Square key={i}
                segmentIndex={segmentIndex}
                squareIndex={i}
                enabled={enabled}
                cssClasses={cssClasses[i]}
                value={values[i]}
                onSquareChange={(segmentIndex, squareIndex, value) => onSquareChange(segmentIndex, squareIndex, value)}
                onSquareFocus={(segmentIndex, squareIndex) => onSquareFocus(segmentIndex, squareIndex)}
        />
      );
    } else {
      rows.push(
        <Notes key={i}
               segmentIndex={segmentIndex}
               squareIndex={i}
               cssClasses={cssClasses[i]}
               notes={solvingNotes[i]}
               onSquareFocus={(segmentIndex, squareIndex) => onSquareFocus(segmentIndex, squareIndex)}
               onNotesChange={(segmentIndex, squareIndex, notes) => onNotesChange(segmentIndex, squareIndex, notes)}
        />
      );
    }
  }
  
  return (
    <div className="base-style segment">
      {rows}
    </div>
  );
}