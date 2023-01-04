import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Role } from '../domain/role';

@Injectable({
  providedIn: 'root'
})
export class RoleService {

  private host = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  listAll(): Observable<Role[]> {
    return this.http.get<Role[]>(`${this.host}/api/v1/roles`);
  }
}
