import { Equalizable } from "./types";
import { VehicleList } from './VehicleList';

export class State implements Equalizable {
    private cur: string[][];
    private step: string;
    private hash: number;
    public vehicles: VehicleList;

    constructor(c: string[][] | string, vehicles?: VehicleList) {
        this.step = '';
        this.hash = -31;
        if (typeof c === 'string') {
            this.cur = this.parseFromString(c);
        } else {
            this.cur = [];
            for (let i = 0; i < 6; i++) {
                this.cur[i] = [];
                for (let j = 0; j < 6; j++) {
                    this.cur[i][j] = c[i][j];
                }
            }
            this.hash = this.computeHash();
        }
        this.vehicles = vehicles || new VehicleList();
    }

    private parseFromString(input: string): string[][] {
        const lines = input.split('\n');

        const cur: string[][] = [];
        for (let i = 0; i < 6; i++) {
            const data = lines[i];
            if (data.length !== 6) {
                throw new Error('Bad board');
            }

            cur[i] = [];
            for (let j = 0; j < 6; j++) {
                cur[i][j] = data.charAt(j);
            }
        }

        return cur;
    }

    setStep(s: string): void {
        this.step = s;
    }

    getStep(): string {
        return this.step;
    }

    get(): string[][] {
        return this.cur;
    }

    display(): void {
        for (let i = 0; i < 6; i++) {
            for (let j = 0; j < 6; j++) {
                process.stdout.write(this.cur[i][j]);
            }
            console.log();
        }
        console.log();
    }

    isSolved(): boolean {
        for (let i = 0; i < 6; i++) {
            if (this.cur[i][5] === 'X') {
                return true;
            }
        }
        return false;
    }

    private computeHash(): number {
        let hash = 0;
        for (let i = 0; i < 6; i++) {
            for (let j = 0; j < 6; j++) {
                hash = hash ^ (Math.pow(31, i) + Math.pow(31, j)) * this.cur[i][j].charCodeAt(0);
            }
        }
        return hash;
    }

    hashCode(): number {
        return this.hash;
    }

    getKey(): string {
        return this.cur.map(row => row.join("")).join("-");
    }

    equals(obj: any): boolean {
        if (this === obj) {
            return true;
        }

        if (!(obj instanceof State)) {
            return false;
        }

        if (this.hashCode() !== obj.hashCode()) {
            return false;
        }

        for (let i = 0; i < 6; i++) {
            for (let j = 0; j < 6; j++) {
                if (this.cur[i][j] !== obj.cur[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

}