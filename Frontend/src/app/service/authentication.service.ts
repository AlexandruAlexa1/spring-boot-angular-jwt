import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { JwtHelperService } from "@auth0/angular-jwt";
import { Observable } from 'rxjs';
import { AuthRequest } from '../domain/auth-request';
import { User } from '../domain/user';
import { NotificationService } from './notification.service';


@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  token: string | null;
  private jwtHelperService = new JwtHelperService();
  private loggedInUser: string | null;
  host = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  loadToken(): void {
    this.token = localStorage.getItem('token');
  }

  isLoggedIn(): boolean {
    this.loadToken();

    // Check if token exist
    if (this.token != null && this.token !== '') {

      // Check if token contains the subject
      if (this.jwtHelperService.decodeToken(this.token).sub != null || '') {

        // Check if token is not expired
        if (!this.jwtHelperService.isTokenExpired(this.token)) {
          this.loggedInUser = this.jwtHelperService.decodeToken(this.token).sub;
          
          return true;
        }
      }
    } 
      
    this.logOut();

    return false;
  }

  logOut() {
    this.token = null;
    this.loggedInUser = null;

    localStorage.removeItem('user');
    localStorage.removeItem('token');
    localStorage.removeItem('users');
    localStorage.removeItem('userId');
  }

  // This method will be call when user press login button
  login(authRequest: AuthRequest): Observable<HttpResponse<User>> {
    return this.http.post<User>(`${this.host}/auth/login`, authRequest, { observe: 'response' });
  }

  // This method will be call when user press login button
  saveToken(token: string) {
    this.token = token;

    localStorage.setItem('token', token);
  }

  // This method will be call when user press login button
  saveUser(user: User) {
    localStorage.setItem('user', JSON.stringify(user));
  }

  deleteUser() {
    localStorage.removeItem('user');
  }

  // This metod will be call when UserCompoent is initialized
  saveUsers(users: User[]) {
    localStorage.setItem('users', JSON.stringify(users));
  }

  setUserId(id: number) {
    localStorage.setItem('userId', JSON.stringify(id));
  }

  getUserId(): number {
    return JSON.parse(localStorage.getItem('userId')!);
  }

  // This method will be call in Interceptor
  getToken(): string {
    return String(this.token);
  }

  // I will use this metod in user-details.component
  getUser(): User {
    return JSON.parse(localStorage.getItem('user')!);
  }
}
