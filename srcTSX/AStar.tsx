import { Graph } from './Graph';
import { Heuristic } from './Heuristic';
import { MyHeuristic } from './MyHeuristic';
import { Node } from './Node';
import { PriorityQueue } from './PriorityQueue';
import { RushHour } from './RushHour';
import { State } from './State';

interface Equalizable {
  equals(other: any): boolean;
}

export class AStar {
  static findPath(input: string): string {
    const source = new State(input);
    const start = new Graph<State>();
    const h = new MyHeuristic(source);

    return this.aStar(start, new Node<State>(source), h);
  }

  static aStar(graph: Graph<State>, source: Node<State>, h: Heuristic): string {
    const openList = new PriorityQueue<Node<State>>((a, b) => a.getF() - b.getF());
    const closedSet = new Map<string, number>();

    source.setG(0);
    source.setParent(null);

    h = new MyHeuristic(source.getCurrent());
    source.setH(h.getValue());

    graph.addNode(source);
    openList.push(source);

    while (openList.length > 0) {
      const curr = openList.pop()!;
      this.generateNeighbors(graph, curr);

      for (const u of Array.from(graph.getNeighbours(curr))) {
        if (u.getCurrent().isSolved()) {
          u.setG(curr.getG() + 1);
          u.setParent(curr);
          return this.writeSolution(u);
        } else {
          const newG = curr.getG() + 1;
          if (!openList.includes(u) && !closedSet.has(u.getCurrent().getKey())) {
            h = new MyHeuristic(u.getCurrent());
            u.setH(h.getValue());
            u.setG(newG);
            u.setParent(curr);
            openList.push(u);
            closedSet.set(u.getCurrent().getKey(), u.getF());
          } else if (openList.includes(u) && newG < u.getG()) {
            u.setG(newG);
            u.setParent(curr);
            closedSet.set(u.getCurrent().getKey(), u.getF());
          }
        }
      }
      closedSet.set(curr.getCurrent().getKey(), curr.getF());
    }
    return "no solution found";
  }

  static generateNeighbors(graph: Graph<State>, currentNode: Node<State>): void {
    let next: State;
    let moveCode: string;
    const mover = new RushHour();

    mover.readInto(currentNode.getCurrent().get());

    for (const v of mover.vehicles.list) {
      for (const direction of [RushHour.LEFT, RushHour.RIGHT, RushHour.UP, RushHour.DOWN]) {
        let l = 4;
        while (l >= 1) {
          if (mover.tryMove(v.name, direction, l)) {
            next = mover.nextState(v, direction, l);
            moveCode = v.name + (direction === RushHour.LEFT ? "L" : direction === RushHour.RIGHT ? "R" : direction === RushHour.UP ? "U" : "D") + l;
            next.setStep(moveCode);
            const nextNode = new Node(next);
            graph.addNode(nextNode);
            graph.addEdge(currentNode, nextNode);
            if (next.isSolved()) return;
            break;
          }
          l--;
        }
      }
    }
  }

  private static writeSolution<T extends Equalizable>(target: Node<T> | null): string {
    let aS = (target!.getCurrent() as unknown as State).getStep();
    target = target!.getParent();
    while (true) {
      aS = (target!.getCurrent() as unknown as State).getStep() + "\n" + aS;
      target = target!.getParent();
      if (target!.getParent() === null) break;
    }
    return aS;
  }

}