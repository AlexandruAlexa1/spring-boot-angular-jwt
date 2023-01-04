import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Role } from 'src/app/domain/role';
import { User } from 'src/app/domain/user';
import { NotificationType } from 'src/app/enum/notification-type';
import { AuthorizationService } from 'src/app/service/authorization.service';
import { NotificationService } from 'src/app/service/notification.service';
import { RoleService } from 'src/app/service/role.service';
import { UserService } from 'src/app/service/user.service';
import { __param } from 'tslib';

import { FormBuilder, FormGroup } from '@angular/forms';

import { Validators } from '@angular/forms';

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.css']
})
export class UserFormComponent implements OnInit, OnDestroy {

  private subscriptions: Subscription[] = [];

  userForm: FormGroup;

  title: string;
  listRoles: Role[];

  userId: number;
  isEditMode: boolean;
  selectedUser = new User();
  
  constructor(private userService: UserService,
              private roleService: RoleService,
              private router: Router,
              private activatedRoute: ActivatedRoute,
              private authorizationService: AuthorizationService,
              private notificationService: NotificationService,
              private fb: FormBuilder) {}

  ngOnInit(): void {
    this.userForm = this.fb.group({
      id: [''],
      email: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(125)]],
      password: ['', [Validators.minLength(5), Validators.maxLength(65)]],
      firstName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(45)]],
      lastName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(45)]],
      address: this.fb.group(
        {
          city: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(20)]],
          state: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(20)]],
          country: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(20)]],
          postalCode: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(20)]],
          phoneNumber: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(15)]]
        }
      ),
      enabled: [''],
      notLocked: [''],
      roles: ['']
      });

    this.checkAuthorization();
    
    this.loadRoles();

    this.userId = this.activatedRoute.snapshot.params['id'];

    this.isEditMode = this.userId != null;

    if (this.isEditMode) {
      this.title = `Manage users edit user (ID: ${this.userId})`;

      // Get selected User
      this.userService.get(this.userId).subscribe(

        (response: User) => {
          this.selectedUser = response;
        },

        (errorResponse: HttpErrorResponse) => {
          this.sendErrorNotification(errorResponse.error.message);
        }
        
      );
    } else {
      this.title = 'Manage users add new user';
    }
  }

  private checkAuthorization() {
    if (!this.authorizationService.isAdmin() && !this.authorizationService.isManager()) {
      alert("You have no permision to access this page!");
      this.router.navigateByUrl('/users');
    }
  }
  
  saveUser() {
    console.log(this.userForm.value);

    this.subscriptions.push(
      this.userService.save(this.userForm.value).subscribe(
        (response: User) => {
          let message: string = '';
  
          if (this.isEditMode) {
            message = `The user with ID: ${this.userId} has been updated successfuly!`;
            this.notificationService.sendNotification(NotificationType.SUCCESS, message);
          } else {
            message = 'The user has been saved successfuly!';
            this.notificationService.sendNotification(NotificationType.SUCCESS, message);
          }
          
          this.router.navigateByUrl(`/users/${message}`);
        },
        (errorResponse: HttpErrorResponse) => {
          console.log(errorResponse)
          this.sendErrorNotification(errorResponse.error.message);
        }
      )
    );
  }

  loadRoles(): void {
    this.subscriptions.push(
      this.roleService.listAll().subscribe(
        (response: Role[]) => {
          this.listRoles = response;
        },
        (errorResponse: HttpErrorResponse) => {
          this.sendErrorNotification(errorResponse.error.message);
        }
      )
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(subscription => subscription.unsubscribe());
  }

  sendErrorNotification(message: string) {
    if (message) {
      this.notificationService.sendNotification(NotificationType.INFO, message);
    } else {
      this.notificationService.sendNotification(NotificationType.ERROR, 'An error occured. Please try again.');
    }
  }

  get email() { return this.userForm.get('email')! }

  get password() { return this.userForm.get('password')! }

  get firstName() { return this.userForm.get('firstName')! }

  get lastName() { return this.userForm.get('lastName')! }

  get city() { return this.userForm.get('address.city')! }

  get state() { return this.userForm.get('address.state')! }

  get country() { return this.userForm.get('address.country')! }

  get postalCode() { return this.userForm.get('address.postalCode')! }

  get phoneNumber() { return this.userForm.get('address.phoneNumber')! }
}



