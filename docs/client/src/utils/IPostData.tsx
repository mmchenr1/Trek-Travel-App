/* Interface to store the types of the fields of the post data */
export default interface IData {
    text: string;
    toLocation: string;
    fromLocation: string;
    startDate: string;
    endDate: string;
    type: string;
    toFlights: string;
    returnFlights: string;
    linesToDest: string;
    linesFromDest: string;
    username: string;
  }