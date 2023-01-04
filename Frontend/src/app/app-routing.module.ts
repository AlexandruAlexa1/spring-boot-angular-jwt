import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './component/login/login.component';
import { RegisterComponent } from './component/register/register.component';
import { UserFormComponent } from './component/user-form/user-form.component';
import { UserComponent } from './component/user/user.component';
import { AuthenticationGuard } from './guard/authentication.guard';

const routes: Routes = [
  { path: 'login', component: LoginComponent},
  { path: 'users', component: UserComponent, canActivate: [AuthenticationGuard]},
  { path: 'users/new', component: UserFormComponent, canActivate: [AuthenticationGuard]},
  { path: 'users/edit/:id', component: UserFormComponent, canActivate: [AuthenticationGuard]},
  { path: 'users/delete/:id', component: UserComponent, canActivate: [AuthenticationGuard]},
  { path: 'users/:message', component: UserComponent},
  { path: 'register', component: RegisterComponent},
  { path: '', redirectTo: '/login', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
