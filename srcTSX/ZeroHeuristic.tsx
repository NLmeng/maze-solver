import { State } from './State';

export class ZeroHeuristic {
    private val: number;

    constructor(state: State) {
        this.val = 0;
    }

    public getValue(): number {
        return this.val;
    }
}