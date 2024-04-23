interface SquareProps {
  segmentIndex: number;
  squareIndex: number;
  value: number;
  onSquareClick: (segmentIndex: number, squareIndex: number, value: number) => void;
}

export default function Square({segmentIndex, squareIndex, value, onSquareClick = () => {}}: SquareProps) {
  
  return (
    // <div className="bg-[#fff] border-[1px] border-[solid] border-[#999] float-left text-[24px] font-bold leading-[34px] h-[34px] -mr-px -mt-px p-0 text-center w-[34px]">
    <div className="baseStyle square" onClick={(e) => onSquareClick(segmentIndex, squareIndex, value)}>
      {value}
    </div>
  );
}