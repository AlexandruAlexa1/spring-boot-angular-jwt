import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ThisReceiver } from '@angular/compiler';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { NotificationType } from 'src/app/enum/notification-type';
import { NotificationService } from 'src/app/service/notification.service';
import { AuthRequest } from '../../domain/auth-request';
import { User } from '../../domain/user';
import { AuthenticationService } from '../../service/authentication.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  private subscriptions: Subscription[] = [];

  constructor(private authService: AuthenticationService,
              private notificationService: NotificationService,
              private router: Router) { }

  ngOnInit(): void {
    if (this.authService.isLoggedIn()) {
      this.router.navigateByUrl('/users');
    } else {
      this.router.navigateByUrl('/login');
    }
  }

  onLogin(authRequest: AuthRequest) {
    console.log(authRequest);

    this.subscriptions.push(
      this.authService.login(authRequest).subscribe(

        (response: HttpResponse<User>) => {
          let token = response.headers.get('JWT');
  
          console.log(`JWT: ${token}`);
  
          this.authService.saveToken(String(token));
          this.authService.saveUser(Object(response.body));
          this.authService.setUserId(Number(response.body?.id));
  
          this.router.navigateByUrl('/users');
        },
        (errorResponse: HttpErrorResponse) => {
          this.sendErrorNotification(errorResponse.error.message);
        }
  
      )
    );
  }

  sendErrorNotification(message: string) {
    if (message) {
      this.notificationService.sendNotification(NotificationType.INFO, message);
    } else {
      this.notificationService.sendNotification(NotificationType.ERROR, 'An error occured. Please try again.');
    }
  }
  
}

