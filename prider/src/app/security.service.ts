import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { environment } from './../environments/environment';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class SecurityService {

  private authorizeEndpoint = '/oauth2/authorization/google';
  private tokenEndpoint = '/login/oauth2/code/google';
  private baseUrl = environment.baseUrl;
  private tokenKey = 'token';

  constructor(private http: HttpClient) { }

  login() {
    window.open(this.baseUrl+this.authorizeEndpoint, '_self');
  }

  logout() {
    this.http.get(this.baseUrl+"/logout");
  }

  updateToken(token:string) {
    localStorage.setItem(this.tokenKey, token);
  }

  fetchToken(code:string, state:string): Observable<any> {
    return this.http.get(this.baseUrl+this.tokenEndpoint+'?code='+code+'&state='+state);
  }

  getToken() {
    return localStorage.getItem(this.tokenKey);
  }

  isLoggedIn(): boolean {
    const token = this.getToken();
    return token!=null;
  }

}
