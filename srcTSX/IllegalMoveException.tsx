export class IllegalMoveException extends Error {
    constructor(message: string) {
        super(message);
        this.name = 'IllegalMoveException';
    }
}
