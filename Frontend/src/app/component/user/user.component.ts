import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Route, Router } from '@angular/router';
import { User } from 'src/app/domain/user';
import { AuthenticationService } from 'src/app/service/authentication.service';
import { UserService } from 'src/app/service/user.service';

import * as $ from 'jquery';
import { AuthorizationService } from 'src/app/service/authorization.service';
import { Subscription } from 'rxjs';
import { ThisReceiver } from '@angular/compiler';
import { NotificationService } from 'src/app/service/notification.service';
import { NotificationType } from 'src/app/enum/notification-type';
import { Address } from 'src/app/domain/address';
import { Page } from 'src/app/domain/page';
import { readyException } from 'jquery';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit, OnDestroy {

  private subscriptions: Subscription[] = [];

  users: User[];
  userId: number;
  message: string;
  selectedUser: User;
  
  pageNum: number = 0;
  pageSize: number = 2;
  totalPages: Array<number>;

  totalElements: number;

  constructor(public userService: UserService,
              private authService: AuthenticationService,
              private authorizationService: AuthorizationService,
              private activatedRoute: ActivatedRoute,
              private router: Router,
              private notificationService: NotificationService) { }

  ngOnInit(): void {
    this.listUsers();
    this.getMessage();
  }

  getMessage(): void {
    this.message = this.activatedRoute.snapshot.params['message'];

    setTimeout(() => {
      $('.message').fadeOut();
    }, 5000);
  }

  listUsers(): void {
    this.subscriptions.push(
      this.userService.findAll(this.pageNum, this.pageSize).subscribe(
        (response: Page) => {
          this.users = response.content;
          this.authService.saveUsers(response.content);
          
          this.totalPages = new Array(response.totalPages);
          this.totalElements = response.totalElements;
        },
        (errorResponse: HttpErrorResponse) => {
          this.sendErrorNotification(errorResponse.error.message);
        }
      )
    );
  }

  setPage(pageNum: number, event: any) {
    event.preventDefault();

    this.pageNum = pageNum;
    this.listUsers();
  }

  showConfirmModal(id: number) {
    this.userId = id;
    $('.confirm-modal-message').text(`You are sure? you want to delet User with ID: ${id}`);
    $('#confirm-modal').fadeIn();;
  }

  deleteUser() {
    this.subscriptions.push(
      this.userService.delete(this.userId).subscribe(
        (response: any) => {
          this.closeModal();
          let message: string = `User with ID: ${this.userId} has been deleted successfuly!`;
          this.router.navigateByUrl(`/users/${message}`);
        },
        (errorResponse: HttpErrorResponse) => {
          this.sendErrorNotification(errorResponse.error.message);
        }
        )
    );
  }

  viewUserDetails(user: User) {
    $('#user-details-modal').fadeIn();
    this.selectedUser = user;
  }

  closeModal() {
    $('.modal').fadeOut();
  }

  public get isAdmin(): boolean {
    return this.authorizationService.isAdmin();
  }

  public get isManager(): boolean {
   return this.authorizationService.isManager();
  }

  public get isEditor(): boolean {
    return this.authorizationService.isEditor();
  }

  public get isUser(): boolean {
   return this.authorizationService.isUser();
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe);
  }

  sendErrorNotification(message: string) {
    if (message) {
      this.notificationService.sendNotification(NotificationType.INFO, message);
    } else {
      this.notificationService.sendNotification(NotificationType.ERROR, 'An error occured. Please try again.');
    }
  }
} 
