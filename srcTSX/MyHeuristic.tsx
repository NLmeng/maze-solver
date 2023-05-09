import { Heuristic } from './Heuristic';
import { State } from './State';

export class MyHeuristic implements Heuristic {
    private val: number;

    constructor(state: State) {
        const b = state.get();

        let curBlock = 0;
        let fromExit = 4;
        let found = false;

        for (let i = 0; i < 6; i++) {
            if (b[i][5] === 'X') {
                this.val = 0;
                return;
            }

            for (let j = 0; j < 6; j++) {
                if (b[i][j] !== 'X' && !found) {
                    continue;
                } else if (b[i][j] === 'X') {
                    found = true;
                    fromExit -= j;
                    j += 2;
                }
                if (b[i][j] === '.') {
                    fromExit--;
                }
                if (b[i][j] !== '.') {
                    curBlock += j;
                    for (let col = 0; col < 6; col++) {
                        if (b[col][j] !== b[i][j]) {
                            curBlock += 2;
                        }
                        if (col > 0) {
                            if (b[col - 1][j] === b[col][j]) {
                                curBlock--;
                            }
                        }
                    }
                }
            }
            if (found) {
                break;
            }
        }

        this.val = curBlock + fromExit;
    }

    getValue(): number {
        return this.val;
    }
}