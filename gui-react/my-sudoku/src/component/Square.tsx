import classNames from "classnames";
import React from "react";
import {REGEX_DIGITS, SquareCssClass} from "../model/constants";

// TODO: dodati flag helperMode - ako je uključen onda highlighta sve, a ako nije onda da bude kao na papiru
//  ovo ide u Board komponentu, treba se proslijeđivati u childove, zbog html class
interface SquareProps {
  segmentIndex: number;
  squareIndex: number;
  enabled: boolean;
  cssClasses: SquareCssClass;
  value: string;
  onSquareFocus: (segmentIndex: number, squareIndex: number) => void;
  onSquareChange: (segmentIndex: number, squareIndex: number, value: string) => void;
}

export default function Square({
                                 segmentIndex,
                                 squareIndex,
                                 enabled,
                                 cssClasses,
                                 value,
                                 onSquareChange,
                                 onSquareFocus
                               }: SquareProps) {
  
  const squareClass = classNames(
    'base-style',
    'square',
    {
      'square-edit': enabled,
      'square-selected': cssClasses.selected,
      'square-highlighted': cssClasses.highlighted,
      'square-error': cssClasses.invalid,
      'square-highlighted-error': cssClasses.highlighted_error,
    }
  );
  
  const handleSquareFocus = (e: React.MouseEvent<HTMLDivElement>) => {
    console.log("handleSquareFocus[%s, %s], highlighted=%s", segmentIndex, squareIndex, JSON.stringify(cssClasses));
    onSquareFocus(segmentIndex, squareIndex);
  };
  
  const handleSquareChange = (e: React.KeyboardEvent<HTMLDivElement>) => {
    let keyPressed = e.key;
    if (keyPressed == 'Backspace' || keyPressed == 'Delete') {
      keyPressed = '';
    }
    if (keyPressed == value) {
      keyPressed = '';
    }
    if (keyPressed == '' || REGEX_DIGITS.test(keyPressed)) {
      // console.log("handleSquareChange[%s, %s], value=%", segmentIndex, squareIndex, value);
      onSquareChange(segmentIndex, squareIndex, keyPressed);
    }
  };
  
  return (
    <div className={squareClass}
         contentEditable={false}
         tabIndex={segmentIndex * 9 + squareIndex}
      // tabIndex={value  ? segmentIndex * 9 + squareIndex : undefined}
         onClick={handleSquareFocus}
         onKeyDown={(e) => handleSquareChange(e)}
    >
      {value}
    </div>
  );
}