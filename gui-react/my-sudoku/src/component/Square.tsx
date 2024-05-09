import classNames from "classnames";

interface SquareProps {
  segmentIndex: number;
  squareIndex: number;
  value: string;
  enabled: boolean;
  onSquareChange: (segmentIndex: number, squareIndex: number, value: string) => void;
}

export default function Square({segmentIndex, squareIndex, value, enabled, onSquareChange}: SquareProps) {
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newValue = e.target.value;
    console.log("[Square.tsx] Square with index [" + segmentIndex + ", " + squareIndex + "], value=" + newValue);
    onSquareChange(segmentIndex, squareIndex, newValue);
  };
  
  const inputClass = classNames(
    'baseStyle',
    'square',
    {
      'editSquare': enabled
    }
  )
  
  return (
    <input className="baseStyle square"
           type="text"
           disabled={!enabled}
           value={value}
           // maxLength={1}
           // onChange={(e) => onSquareChange(segmentIndex, squareIndex, e.target.value)}
           onChange={handleChange}
    />
  );
}