import { Node } from './Node';
import { Equalizable } from "./types";

export class Graph<T extends Equalizable> {
    private v: Set<Node<T>>;
    private e: Map<Node<T>, Set<Node<T>>>;

    constructor() {
        this.v = new Set<Node<T>>();
        this.e = new Map<Node<T>, Set<Node<T>>>();
    }

    getv(): Set<Node<T>> {
        return this.v;
    }

    getNodeByName(name: T): Node<T> | null {
        for (const v of this.v) {
            if (v.getCurrent() === name) {
                return v;
            }
        }
        return null;
    }

    getNeighbours(v: Node<T>): Set<Node<T>> {
        return this.e.get(v) as Set<Node<T>>;
    }

    addNode(v: Node<T>): boolean {
        const newNode = !this.v.has(v);
        if (newNode) {
            this.v.add(v);
            this.e.set(v, new Set<Node<T>>());
        }
        return newNode;
    }

    addEdge(v: Node<T>, u: Node<T>): boolean {
        if (this.v.has(v) && this.v.has(u)) {
            const vEdges = this.e.get(v);
            const uEdges = this.e.get(u);

            if (vEdges && uEdges) {
                vEdges.add(u);
                uEdges.add(v);
                return true;
            }
        }
        return false;
    }

    removeEdge(v: Node<T>, u: Node<T>): boolean {
        if (this.v.has(v) && this.v.has(u)) {
            const vEdges = this.e.get(v);
            const uEdges = this.e.get(u);

            if (vEdges && uEdges) {
                vEdges.delete(u);
                uEdges.delete(v);
                return true;
            }
        }
        return false;
    }

}