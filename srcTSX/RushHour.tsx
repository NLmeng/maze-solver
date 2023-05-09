import { IllegalMoveException } from './IllegalMoveException';
import { State } from './State';
import { Vehicle } from './Vehicle';
import { VehicleList } from './VehicleList';

export class RushHour {
    public static readonly UP = 0;
    public static readonly DOWN = 1;
    public static readonly LEFT = 2;
    public static readonly RIGHT = 3;

    public static readonly SIZE = 6;

    public board: string[][] = Array.from({ length: RushHour.SIZE }, () =>
        Array(RushHour.SIZE).fill('.')
    );
    public vehicles = new VehicleList();

    printBoard(): void {
        for (let i = 0; i < RushHour.SIZE; i++) {
            for (let j = 0; j < RushHour.SIZE; j++) {
                process.stdout.write(this.board[i][j]);
            }
            console.log();
        }
        console.log();
    }

    getBoard(): string[][] {
        return this.board;
    }

    readInto(c: string[][]): void {
        this.vehicles.clear();
        for (let i = 0; i < RushHour.SIZE; i++) {
            for (let j = 0; j < RushHour.SIZE; j++) {
                this.board[i][j] = c[i][j];
                if (this.board[i][j] !== '.') {
                    this.vehicles.add(this.board[i][j], i, j);
                }
            }
        }
        this.vehicles.format();
    }

    constructor();
    constructor(c: string[][]);
    constructor(fileName: string);
    constructor(arg?: string[][] | string) {
        if (typeof arg === 'string') {
            // this.readFromFile(arg);
            console.error('readFromFile not implemented');
        } else if (Array.isArray(arg)) {
            this.readInto(arg);
        }
    }

    nextState(vh: Vehicle, dr: number, ln: number): State {
        this.validate(vh, dr, ln);
        this.move(vh, dr, ln);
        const nextStateBoard = this.board.map(row => row.slice());
        const nextStateVehicles = this.vehicles.copy();
        const nextState = new State(nextStateBoard, nextStateVehicles);
        nextStateVehicles.updateVehiclePosition(vh, dr, ln);

        // Revert the move on the original board
        switch (dr) {
            case RushHour.UP:
                this.move(vh, RushHour.DOWN, ln);
                break;
            case RushHour.DOWN:
                this.move(vh, RushHour.UP, ln);
                break;
            case RushHour.LEFT:
                this.move(vh, RushHour.RIGHT, ln);
                break;
            case RushHour.RIGHT:
                this.move(vh, RushHour.LEFT, ln);
                break;
        }

        return nextState;
    }


    tryMove(carName: string, dir: number, length: number): boolean {
        const vh = this.vehicles.get(carName);
        try {
            this.validate(vh!, dir, length);
            return true;
        } catch (e) {
            return false;
        }
    }

    makeMove(carName: string, dir: number, length: number): void {
        const vh = this.vehicles.get(carName);
        this.validate(vh!, dir, length);
        this.move(vh!, dir, length);
    }

    private validate(vh: Vehicle, dr: number, ln: number): void {
        if (vh.direction == 'h') {
            if (dr == RushHour.UP || dr == RushHour.DOWN)
                throw new IllegalMoveException("Invalid Direction");
        } else {
            if (dr == RushHour.LEFT || dr == RushHour.RIGHT)
                throw new IllegalMoveException("Invalid Direction");
        }
        const newPos = vh.calculateNewPos(dr, ln);
        if (!this.isInsideBoard(newPos) || !this.isPathClear(vh, dr, ln)) {
            throw new IllegalMoveException('Invalid move');
        }
    }

    private move(vh: Vehicle, dr: number, ln: number): void {
        const oldPos = vh.getPos();
        const newPos = vh.calculateNewPos(dr, ln);
        vh.setPos(newPos);

        for (const [y, x] of oldPos) {
            this.board[y][x] = '.';
        }
        for (const [y, x] of newPos) {
            this.board[y][x] = vh.getName();
        }
    }

    private isInsideBoard(posList: Array<[number, number]>): boolean {
        return posList.every(
            ([y, x]) => x >= 0 && x < RushHour.SIZE && y >= 0 && y < RushHour.SIZE
        );
    }

    private isPathClear(vh: Vehicle, dr: number, ln: number): boolean {
        const oldPos = vh.getPos();
        const newPos = vh.calculateNewPos(dr, ln);

        if (dr === RushHour.UP || dr === RushHour.DOWN) {
            const x = oldPos[0][1];
            const minY = Math.min(...oldPos.map(p => p[0]), ...newPos.map(p => p[0]));
            const maxY = Math.max(...oldPos.map(p => p[0]), ...newPos.map(p => p[0]));

            for (let y = minY; y <= maxY; y++) {
                if (this.board[y][x] !== '.' && !oldPos.some(([oy, ox]) => oy === y && ox === x)) {
                    return false;
                }
            }
        } else if (dr === RushHour.LEFT || dr === RushHour.RIGHT) {
            const y = oldPos[0][0];
            const minX = Math.min(...oldPos.map(p => p[1]), ...newPos.map(p => p[1]));
            const maxX = Math.max(...oldPos.map(p => p[1]), ...newPos.map(p => p[1]));

            for (let x = minX; x <= maxX; x++) {
                if (this.board[y][x] !== '.' && !oldPos.some(([oy, ox]) => oy === y && ox === x)) {
                    return false;
                }
            }
        }

        return true;
    }

    isSolved(): boolean {
        const exitX = RushHour.SIZE - 1;
        const redCar = this.vehicles.get('X');
        const redCarPos = redCar!.getPos();
        for (const [y, x] of redCarPos) {
            if (x === exitX && y === 2) {
                return true;
            }
        }
        return false;
    }
}                
