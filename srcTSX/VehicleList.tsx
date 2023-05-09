import { Vehicle } from './Vehicle';

export class VehicleList {

    public list: Vehicle[];

    constructor() {
        this.list = [];
    }

    public add(name: string, y: number, x: number): void {
        const id = this.contains(name);
        if (id === -1) {
            const newVehicle = new Vehicle(name, y, x);
            this.list.push(newVehicle);
        } else {
            this.list[id].addCoord(y, x);
        }
    }

    public get(name: string): Vehicle | undefined {
        return this.list.find(vehicle => vehicle.name === name);
    }

    public contains(name: string): number {
        let i = 0;
        for (const cur of this.list) {
            if (cur.name === name) return i;
            i++;
        }
        return -1;
    }

    public clear(): void {
        this.list = [];
    }

    copy(): VehicleList {
        const newList = new VehicleList();
        newList.list = this.list.map(vehicle => vehicle.copy());
        return newList;
    }

    updateVehiclePosition(vh: Vehicle, dr: number, ln: number): void {
        const vehicleIndex = this.list.findIndex(vehicle => vehicle.name === vh.name);
        if (vehicleIndex !== -1) {
            this.list[vehicleIndex].move(dr, ln);
        }
    }

    public format(): void {
        for (let i = 0; i < this.list.length; i++) {
            const cur = this.list[i];
            // set type
            if (cur.coord.length === 2) {
                cur.type = 'c';
            } else if (cur.coord.length === 3) {
                cur.type = 't';
            } else {
                throw new Error('invalid length of vehicle');
            }
            // set direction
            if (cur.coord[0][0] === cur.coord[1][0]) {
                cur.direction = 'h';
            } else if (cur.coord[0][1] === cur.coord[1][1]) {
                cur.direction = 'v';
            } else {
                throw new Error('invalid direction of vehicle');
            }
        }
    }

    public display(): void {
        for (let i = 0; i < this.list.length; i++) {
            console.log(`${i + 1}) ${this.list[i]} `);
        }
    }
}