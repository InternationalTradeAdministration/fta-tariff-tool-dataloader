import { utils } from "xlsx";

export const validate = (sheet) => {
  let headers = [];
  var range = utils.decode_range(sheet['!ref']);
  for(var colNum = range.s.c; colNum < range.e.c; colNum++) {
    headers.push(sheet[utils.encode_cell({r: 0, c: colNum})].v)
  }

  return { valid: true }
}