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

  constructor(private http: HttpClient) {
    this.name = "Dummy";
  }

  ngOnInit(): void {
    this.http.get<any>(environment.baseUrl+'/user/name').subscribe(data=>{this.name = data.name});
  }

}
