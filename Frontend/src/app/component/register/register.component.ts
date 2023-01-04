import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { User } from 'src/app/domain/user';
import { NotificationType } from 'src/app/enum/notification-type';
import { AuthenticationService } from 'src/app/service/authentication.service';
import { NotificationService } from 'src/app/service/notification.service';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit, OnDestroy {

  private subscriptions: Subscription[] = [];
  registerForm: FormGroup;

  constructor(private fb: FormBuilder,
              private userService: UserService,
              private router: Router,
              private notificationService: NotificationService,
              private authSerice: AuthenticationService) {
    this.registerForm = this.fb.group({
      email: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(125)]],
      password: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(64)]],
      firstName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(45)]],
      lastName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(45)]],
      address: this.fb.group({
        city: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(20)]],
        state: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(20)]],
        country: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(20)]],
        postalCode: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(20)]],
        phoneNumber: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(15)]]
      })
    });
  }

  ngOnInit(): void {
   if (this.authSerice.isLoggedIn()) {
    this.router.navigateByUrl('/users');
   }
  }

  register() {
    console.log(this.registerForm.value);

    this.subscriptions.push(
      this.userService.register(this.registerForm.value).subscribe(
        (response: User) => {
          this.router.navigateByUrl('/login');
          this.notificationService.sendNotification(NotificationType.SUCCESS, 'You have been registred successfuly!');
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

  get email() { return this.registerForm.get('email')! }

  get password() { return this.registerForm.get('password')! }

  get firstName() { return this.registerForm.get('firstName')! }

  get lastName() { return this.registerForm.get('lastName')! }

  get city() { return this.registerForm.get('address.city')! }

  get state() { return this.registerForm.get('address.state')! }

  get country() { return this.registerForm.get('address.country')! }

  get postalCode() { return this.registerForm.get('address.postalCode')! }

  get phoneNumber() { return this.registerForm.get('address.phoneNumber')! }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }
}
