export class Address {

    id: number
    city: string;
	state: string;
	country: string;
	postalCode: string;
	phoneNumber: string;

    constructor() {
        this.city = '';
		this.state = '';
		this.country = '';
		this.postalCode = '';
		this.phoneNumber = '';
    }
}
