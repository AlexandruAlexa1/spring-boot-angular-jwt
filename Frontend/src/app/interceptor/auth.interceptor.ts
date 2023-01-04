import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthenticationService } from '../service/authentication.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private authService: AuthenticationService) {}

  intercept(httpRequest: HttpRequest<any>, httpHandler: HttpHandler): Observable<HttpEvent<any>> {

    // If Request contains /auth/login or /auth/register just pass the Request
    if (httpRequest.url.includes(`${this.authService.host}/auth/login`) ||
        httpRequest.url.includes(`${this.authService.host}/auth/register`)) {
      return httpHandler.handle(httpRequest);
    }

    // Load Token in Local Cash
    this.authService.loadToken();

    const token = this.authService.getToken(); // Get token from Local Cash
    const request = httpRequest.clone({setHeaders: { Authorization: `Bearer ${token}`}}); // Create a clone from HttpRequest and pass the Authorization Header with Bearer

    // Finally return the custom Request
    // This request will be send every time to the Backend when we call UserService
    return httpHandler.handle(request);
  }
}
