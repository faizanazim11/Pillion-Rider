import { Router } from '@angular/router';
import { environment } from './../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
declare const google: any;

@Component({
  selector: 'app-app-console',
  templateUrl: './app-console.component.html',
  styleUrls: ['./app-console.component.scss']
})
export class AppConsoleComponent implements OnInit {

  location: any;
  map:any;
  mapElement: any;

  constructor(private http: HttpClient, private router: Router) {
  }

  get_location(): void {
    if(navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position: GeolocationPosition) => {
          this.location = {
            lat: position.coords.latitude,
            lng: position.coords.longitude,
          };
          this.map = new google.maps.Map(this.mapElement, {
            center: this.location,
            zoom: 14,
          });
        }
      );
    }
    else
    {
      this.location = {
        lat: 27.2046,
        lng: 77.4977
      };
      this.map = new google.maps.Map(this.mapElement, {
        center: this.location,
        zoom: 14,
      });
    }

  }

  ngOnInit(): void {
    this.mapElement = document.getElementById("mapElement");
    this.get_location();
  }

  logout() :void {
    this.http.get(environment.baseUrl+'/logout');
    this.router.navigate(['/home']);
  }

}
