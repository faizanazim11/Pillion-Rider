import { SecurityService } from './../security.service';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  constructor(private securityService: SecurityService) { }

  ngOnInit(): void {
  }

  login() {
    this.securityService.login();
  }

}
