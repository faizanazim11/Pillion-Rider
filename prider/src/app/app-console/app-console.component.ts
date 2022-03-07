import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { environment } from './../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-app-console',
  templateUrl: './app-console.component.html',
  styleUrls: ['./app-console.component.scss']
})
export class AppConsoleComponent implements OnInit {

  name;

  constructor(private http: HttpClient, private router: Router) {
    this.name = "Dummy";
  }

  ngOnInit(): void {
    this.http.get<any>(environment.baseUrl+'/user/name').subscribe(data=>{this.name = data.name});
  }

  logout() :void {
    this.http.get(environment.baseUrl+'/logout');
    this.router.navigate(['/home']);
  }

}
