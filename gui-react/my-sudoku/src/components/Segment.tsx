import React, {JSX} from "react";
import Square from "./Square";
import "../style.css"

const onSquareClick = (segmentIndex: number, squareIndex: number, value: number) => {
  alert("Clicked on square with index [" + segmentIndex + ", " + squareIndex + "], value=" + value);
};

interface SegmentProps {
  segmentIndex: number;
  values: number[];
}

export default function Segment({segmentIndex, values}: SegmentProps) {
  
  let rows: JSX.Element[] = [];
  let segment: JSX.Element[] = [];
  
  values.forEach((value, squareIndex) => {
    rows.push(
      <Square segmentIndex={segmentIndex} squareIndex={squareIndex} value={value} onSquareClick={() => onSquareClick(segmentIndex, squareIndex, value)}/>
    );
    // if (index % 2 === 0) {
    
    // row = [];
  });
  
  segment.push(
    <div className="baseStyle segment">
      {rows}
    </div>
  );
  
  return (
    // <div className="bg-[#fff] border-[1px] border-[solid] border-[#999] float-left text-[24px] font-bold leading-[34px] h-[34px] -mr-px -mt-px p-0 text-center w-[34px]">
    <div>
      {segment}
    </div>
  );
}