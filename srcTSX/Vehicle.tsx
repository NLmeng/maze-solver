import { RushHour } from './RushHour';

export class Vehicle {
    public name: string;
    public coord: Array<[number, number]> = [];
    public type: string;
    public direction: string;

    constructor(name: string, y: number, x: number) {
        this.name = name;
        this.coord.push([y, x]);
        this.type = '';
        this.direction = '';
    }

    public addCoord(y: number, x: number): void {
        this.coord.push([y, x]);
    }

    public getPos(): Array<[number, number]> {
        return this.coord;
    }

    public getName(): string {
        return this.name;
    }

    public toString(): string {
        return `Car: ${this.name}: ${this.coord.toString()}\n-Type: ${this.type} -Direction: ${this.direction}\n`;
    }

    copy(): Vehicle {
        const newVehicle = new Vehicle(this.name, this.coord[0][1], this.coord[0][0]);
        newVehicle.coord = this.coord.map(([y, x]) => [y, x]);
        newVehicle.type = this.type;
        newVehicle.direction = this.direction;
        return newVehicle;
    }

    move(dr: number, ln: number): void {
        const newPos = this.calculateNewPos(dr, ln);
        this.setPos(newPos);
    }

    calculateNewPos(dr: number, ln: number): Array<[number, number]> {
        const newPos: Array<[number, number]> = this.coord.map(([y, x]) => {
            if (this.direction === 'h') {
                return dr === RushHour.LEFT ? [y, x - ln] : [y, x + ln];
            } else {
                return dr === RushHour.UP ? [y - ln, x] : [y + ln, x];
            }
        });
        return newPos;
    }

    setPos(newPos: Array<[number, number]>): void {
        this.coord = newPos;
    }

    public rightMost(): number {
        if (this.type === 'c') {
            return 1;
        } else if (this.type === 't') {
            return 2;
        } else {
            console.log('unexpected error');
            return this.coord.length - 1;
        }
    }
}
