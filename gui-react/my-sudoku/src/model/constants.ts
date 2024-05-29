export const REGEX_DIGITS = /^[1-9\b]/;

// export const SQUARE_SELECTED = "S";
// export const SQUARE_HIGHLIGHTED = "H";

// export const VALUE_VALID = "VALUE_VALID ";
// export const VALUE_INVALID = "X";

export type SquareCssClass = {
  selected: boolean;
  highlighted: boolean;
  highlighted_error: boolean;
  invalid: boolean;
};
