import { Address } from "./address";
import { Role } from "./role";

export class User {
    id: number;
	email: string;
	password: string;
	firstName: string;
	lastName: string;
    joinDate: Date;
	lastLoginDate: Date;
	enabled: boolean;
	notLocked: boolean;
	address: Address;
	roles: Role[];
}
