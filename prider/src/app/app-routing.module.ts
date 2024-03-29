import { RideeComponent } from './ridee/ridee.component';
import { AdminAppConsoleComponent } from './admin-app-console/admin-app-console.component';
import { CallbackComponent } from './callback/callback.component';
import { AppConsoleComponent } from './app-console/app-console.component';
import { AuthGuard } from './auth.guard';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';

const routes: Routes = [
  {
    path: '', pathMatch: 'full', component: HomeComponent
  },
  {
    path: 'home', component: HomeComponent
  },
  {
    path: 'console', component: AppConsoleComponent, canActivate: [AuthGuard]
  },
  {
    path: 'callback', component: CallbackComponent
  },
  {
    path: 'admin-console', component: AdminAppConsoleComponent
  },
  {
    path: 'ridee-console', component: RideeComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
