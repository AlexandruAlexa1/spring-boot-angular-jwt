 import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { NotificationType } from '../enum/notification-type';
import { AuthenticationService } from '../service/authentication.service';
import { NotificationService } from '../service/notification.service';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationGuard implements CanActivate {

  constructor(private authService: AuthenticationService,
              private router: Router,
              private notificationService: NotificationService) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    return this.isUserLoggedIn();
  }
  
  private isUserLoggedIn(): boolean {
    if (this.authService.isLoggedIn()) {
      return true;
    }

    this.notificationService.sendNotification(NotificationType.WARNING, 'You need to LogIn to access this page.')

    this.router.navigate(['/login']);

    return false;
  } 
}
