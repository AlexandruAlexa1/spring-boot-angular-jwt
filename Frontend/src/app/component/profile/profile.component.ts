import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { User } from 'src/app/domain/user';
import { AuthenticationService } from 'src/app/service/authentication.service';
import { NotificationService } from 'src/app/service/notification.service';
import { UserService } from 'src/app/service/user.service';

import { FormBuilder, FormGroup } from '@angular/forms';
import { Validators } from '@angular/forms';
import { NotificationType } from 'src/app/enum/notification-type';
import { Router } from '@angular/router';
import { ThisReceiver } from '@angular/compiler';

@Component({
  selector: 'user-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit, OnDestroy {

  private subscriptions: Subscription[] = [];
  profileForm: FormGroup;
  loggedUser: User;

  constructor(private authService: AuthenticationService,
              private userService: UserService,
              private fb: FormBuilder,
              private notificationService: NotificationService,
              private router: Router){};

  ngOnInit(): void {
    this.profileForm = this.fb.group({
      id: [''],
      email: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(125)]],
      password: ['', [Validators.minLength(5), Validators.maxLength(64)]],
      firstName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(45)]],
      lastName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(125)]],
      address: this.fb.group({
        city: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(20)]],
        state: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(20)]],
        country: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(20)]],
        postalCode: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(20)]],
        phoneNumber: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(15)]]
      })
    });

    this.getAuthenticatedUser();
  }

  getAuthenticatedUser() {
    this.subscriptions.push(
      this.userService.get(this.authService.getUserId()).subscribe(
        (response: User) => {
          this.loggedUser = response;
        },
  
        (errorResponse: HttpErrorResponse) => {
          this.sendErrorNotification(errorResponse.error.message);
        }
      )
    );
  }

  updateAccount():void {
    console.log(this.profileForm.value);

    this.subscriptions.push(
      this.userService.save(this.profileForm.value).subscribe(

        (response: User) => {
          // Close Modal
          this.closeModal();

          // Redirect to User Page
          this.router.navigateByUrl('/users')

          // Send Notification
          this.notificationService.sendNotification(NotificationType.INFO, 'Your Accountr Has Benn Updated Successfuly!')
        },
  
        (errorResponse: HttpErrorResponse) => {
          this.sendErrorNotification(errorResponse.error.message);
        }
      )
    );
  }

  private sendErrorNotification(message: string) {
    if (message) {
      this.notificationService.sendNotification(NotificationType.INFO, message);
    } else {
      this.notificationService.sendNotification(NotificationType.ERROR, 'An unexpected error occurred');
    }
  }

  showUserProfile():void {
    $('#user-profile-modal').fadeIn('slow');
  }

  closeModal() {
    $('.modal').fadeOut();
  }

  logOut() {
    this.authService.logOut();
  }

  get email() { return this.profileForm.get('email')! }

  get password() { return this.profileForm.get('password')! }

  get firstName() { return this.profileForm.get('firstName')! }

  get lastName() { return this.profileForm.get('lastName')! }

  get city() { return this.profileForm.get('address.city')! }

  get state() { return this.profileForm.get('address.state')! }

  get country() { return this.profileForm.get('address.country')! }

  get postalCode() { return this.profileForm.get('address.postalCode')! }

  get phoneNumber() { return this.profileForm.get('address.phoneNumber')! }

  ngOnDestroy(): void {
    this.subscriptions.forEach(subscription => subscription.unsubscribe());
  }
}



