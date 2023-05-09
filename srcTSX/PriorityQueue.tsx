export class PriorityQueue<T> {
    private _items: T[];
    private _compare: (a: T, b: T) => number;

    constructor(compare: (a: T, b: T) => number) {
        this._items = [];
        this._compare = compare;
    }

    push(item: T): void {
        this._items.push(item);
        this._items.sort(this._compare);
    }

    pop(): T | undefined {
        return this._items.shift();
    }

    includes(item: T): boolean {
        return this._items.includes(item);
    }

    get length(): number {
        return this._items.length;
    }
}
