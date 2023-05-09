import { AStar } from './AStar';

export class Solver {
    static solveFromString(inputGrid: string): string {
        const solnString = AStar.findPath(inputGrid)
        return solnString;
    }
}