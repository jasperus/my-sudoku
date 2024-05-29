import React, {JSX} from 'react';
import "../style.css";
import {REGEX_DIGITS, SquareCssClass} from "../model/constants";
import classNames from "classnames";

interface NotesProps {
  segmentIndex: number;
  squareIndex: number;
  cssClasses: SquareCssClass;
  notes: string[];
  onSquareFocus: (segmentIndex: number, squareIndex: number) => void;
  onNotesChange: (segmentIndex: number, squareIndex: number, notes: string[]) => void;
}

export default function Notes({segmentIndex, squareIndex, cssClasses, notes, onSquareFocus, onNotesChange}: NotesProps) {
  
  const squareClass = classNames(
    'base-style',
    'square',
    {
      'square-selected': cssClasses.selected,
      'square-highlighted': cssClasses.highlighted,
      'square-error': cssClasses.invalid
    }
  );
  
  const handleNotesFocus = (e: React.MouseEvent<HTMLDivElement>) => {
    console.log("handleSquareFocus[%s, %s]", segmentIndex, squareIndex);
    onSquareFocus(segmentIndex, squareIndex);
  }
  
  const handleNotesChange = (e: React.KeyboardEvent<HTMLDivElement>) => {
    const keyPressed = e.key;
    if (REGEX_DIGITS.test(keyPressed)) {
      let updatedNotes = [...notes];
      if (updatedNotes[+keyPressed - 1] == keyPressed) {
        updatedNotes[+keyPressed - 1] = '';
      } else {
        updatedNotes[+keyPressed - 1] = keyPressed;
      }
      
      // console.log("[Notes.tsx] square[%s, %s], notes[%s]",
      //   segmentIndex, squareIndex, updatedNotes);
      
      onNotesChange(segmentIndex, squareIndex, updatedNotes);
    }
  };
  
  let notesJsx: JSX.Element[] = [];
  let i = 0;
  
  notesJsx = notes.map((value, notesIndex) => (
    <div key={i++} className="base-style note" contentEditable={false}>
      {value}
    </div>
  ));
  
  return (
    <div
      className={squareClass}
      contentEditable={false}
      tabIndex={segmentIndex * 9 + squareIndex}
      onClick={handleNotesFocus}
      onKeyDown={(e) => handleNotesChange(e)}
    >
      {notesJsx}
    </div>
  );
}
