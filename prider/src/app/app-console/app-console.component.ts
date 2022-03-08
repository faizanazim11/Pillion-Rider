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
  marker:any;
  profile:any;
  picture:any;
  name:any;

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
          var myicon = {
            url: this.picture,
            scaledSize: new google.maps.Size(32,32),
            origin: new google.maps.Point(0,0),
            anchor: new google.maps.Point(0,0),
          };
          console.log(myicon.url);
          var mlabel = "";
          var namel = this.name.split(" ");
          namel.forEach((element: string) => {
            mlabel=mlabel+element.charAt(0);
          });
          this.marker = new google.maps.Marker({
            position: this.location,
            icon: myicon,
            map: this.map,
            label: {text: mlabel, color:"white"},
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

  get_profile():void {
    var user_name = document.getElementById("username");
    this.profile = this.http.get<any>(environment.baseUrl+"/user/profile").subscribe(data=>{
      user_name!.innerHTML = data.name;
      this.picture = data.picture;
      this.name = data.name;
      this.get_location();

    });
  }

  ngOnInit(): void {
    this.mapElement = document.getElementById("mapElement");
    this.get_profile();
  }

  logout() :void {
    this.http.get(environment.baseUrl+'/logout');
    this.router.navigate(['/home']);
  }

}
