import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Page } from '../domain/page';
import { User } from '../domain/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private host: string = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  findAll(pageNum?: number, pageSize?: number): Observable<Page> {
    return this.http.get<Page>(`${this.host}/api/v1/users?pageNum=${pageNum}&pageSize=${pageSize}`);
  }

  get(id: number): Observable<User> {
    return this.http.get<User>(`${this.host}/api/v1/users/${id}`);
  }

  save(user: User): Observable<User> {
    return this.http.post<User>(`${this.host}/api/v1/users`, user);
  }

  delete(id: number): Observable<any> {
    return this.http.delete<any>(`${this.host}/api/v1/users/${id}`);
  }

  search(keyword: string): Observable<User[]> {
    return this.http.get<User[]>(`${this.host}/api/v1/users/${keyword}`);
  }

   register(user: User): Observable<User> {
    return this.http.post<User>(`${this.host}/auth/register`, user);
  }
}


 