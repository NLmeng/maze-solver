import { Equalizable } from "./types";

export class Node<T extends Equalizable> {
    private h: number;
    private g: number;
    private f: number;
    private current: T;
    private parent: Node<T> | null;

    constructor(name: T) {
        this.current = name;
        this.parent = null;
        this.h = 0;
        this.g = 0;
        this.f = 0;
    }

    getParent(): Node<T> | null {
        return this.parent;
    }

    setParent(parent: Node<T> | null): void {
        this.parent = parent;
    }

    getH(): number {
        return this.h;
    }

    setH(h: number): void {
        this.h = h;
        this.f = this.g + this.h;
    }

    getG(): number {
        return this.g;
    }

    setG(g: number): void {
        this.g = g;
        this.f = this.g + this.h;
    }

    getF(): number {
        return this.f;
    }

    setCurrent(name: T): void {
        this.current = name;
    }

    getCurrent(): T {
        return this.current;
    }

    equals(other: Node<T>): boolean {
        return this.current.equals(other.current);
    }
}
