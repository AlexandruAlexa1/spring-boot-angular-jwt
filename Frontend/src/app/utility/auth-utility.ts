import { Injectable } from "@angular/core";
import { Role } from "../domain/role";
import { AuthenticationService } from "../service/authentication.service";

@Injectable()
export class AuthUtility {

    static  authService: AuthenticationService;

    static  get isAdmin(): boolean {
    return this.checkRole('Admin');
    }

    static  get isManager(): boolean {
    return this.checkRole('Manager');
    }

    static  get isEditor(): boolean {
    return this.checkRole('Editor');
    }

    static  get isUser(): boolean {
    return this.checkRole('User');
    }

    static  checkRole(roleName: string): boolean {
    let roles = this.getUserRoles();

    for (let role of roles) {
        if (role.name == roleName){
        return true;
        }
    }

    return false;
    }

    static  getUserRoles(): Role[] {
    return this.authService.getUser().roles;
    }
}
