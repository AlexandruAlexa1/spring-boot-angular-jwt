import { Injectable } from '@angular/core';
import { Role } from '../domain/role';
import { AuthenticationService } from './authentication.service';

@Injectable({
  providedIn: 'root'
})
export class AuthorizationService {

  constructor(private authService: AuthenticationService) { }

  isAdmin(): boolean {
    return this.checkRole('Admin');
  }

  isManager(): boolean {
   return this.checkRole('Manager');
  }

  isEditor(): boolean {
    return this.checkRole('Editor');
  }

  isUser(): boolean {
   return this.checkRole('User');
  }

  private checkRole(roleName: string): boolean {
    let roles = this.getUserRoles();

    for (let role of roles) {
      if (role.name == roleName){
        return true;
      }
    }

    return false;
  }

  private getUserRoles(): Role[] {
    return this.authService.getUser().roles;
  }
}
