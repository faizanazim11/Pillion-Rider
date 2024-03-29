import { Component, OnInit } from '@angular/core';
// import { LocationService } from './service/location.service';
import { NavigationCancel, Router } from '@angular/router';
import { environment } from './../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { LocationService } from '../app-console/service/location.service';
declare const google: any;

let previousCoord: number;
let locationAPI: any[] = [];
let markersArray = [];
var myKeys: any;
var dataMain: any;

@Component({
  selector: 'app-admin-app-console',
  templateUrl: './admin-app-console.component.html',
  styleUrls: ['./admin-app-console.component.scss']
})
export class AdminAppConsoleComponent implements OnInit {

  location: any;
  showLatLong: any;
  floatLatLong: any[] = [];
  map: any;
  mapElement: any;
  marker: any;
  profile: any;
  picture: any;
  name: any;
  markerList: any;
  locname!: string;
  constructor(private http: HttpClient, private router: Router, private locationService: LocationService) {
    this.locationService.getLocations().subscribe(data => {
      dataMain = data
      myKeys = Object.keys(data);
    });
  }

  get_distance(origin: any, destination: any): any {
    const service = new google.maps.DistanceMatrixService();
    const request = {
      origins: [origin],
      destinations: [destination],
      travelMode: google.maps.TravelMode.DRIVING,
      unitSystem: google.maps.UnitSystem.METRIC,
      avoidHighways: false,
      avoidTolls: false,
    };

    service.getDistanceMatrix(request).then((response: any) => {
      console.log("Distance", response);
    }
    );
  }

  place_marker(locationClick: any): void {
    this.marker = new google.maps.Marker({
      position: locationClick,
      map: this.map
    });
    markersArray.push(this.marker);
  }

  addMarker(): void {
    var infowindow = new google.maps.InfoWindow();
    for (var i = 0; i <= myKeys.length; i++) {

      this.marker = new google.maps.Marker({
        position: new google.maps.LatLng(dataMain[myKeys[i]][0], dataMain[myKeys[i]][1]),
        map: this.map,
        animation: google.maps.Animation.BOUNCE,
        title: myKeys[i],
      });
      google.maps.event.addListener(this.marker, 'mouseover', ((marker, i) => {
        var infowindow = new google.maps.InfoWindow({
          content: myKeys[i],
          map: this.map
        });
        return () => {
          infowindow.open(this.map, marker);
        }
      })(this.marker, i));
    }
  }

  get_location(): void {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position: GeolocationPosition) => {
          this.location = {
            lat: position.coords.latitude,
            lng: position.coords.longitude,
          };
          this.map = new google.maps.Map(this.mapElement, {
            // center: this.location,
            center: new google.maps.LatLng(19.19823705853763, 84.74513731923355),
            zoom: 18,
            mapTypeId: google.maps.MapTypeId.HYBRID,
          });


          this.map.addListener("click", (clickEvent: { latLng: any; }) => {
            if (markersArray.length <= 0) {
              console.log("Mouse Event", clickEvent);
              this.place_marker(clickEvent.latLng);

              console.log("Latitude & Longitude", clickEvent.latLng.toJSON());
              this.floatLatLong.push(clickEvent.latLng.lat());
              this.floatLatLong.push(clickEvent.latLng.lng());
              // this.floatLatLong = clickEvent.latLng.lat();
              this.showLatLong = JSON.stringify(this.floatLatLong);

              // this.arr.push(JSON.stringify(clickEvent.latLng.toJSON()));
              // console.log("Array", this.arr);
              // if (this.arr.length == 2) {
              //   previousCoord = 1;
              //   this.get_distance(this.arr[0], this.arr[1]);
              // }
              // if (this.arr.length > 2) {
              //   this.get_distance(this.arr[previousCoord], this.arr[previousCoord + 1]);
              //   previousCoord = previousCoord + 1;
              // }
            }
          });
          var myicon = {
            url: this.picture,
            scaledSize: new google.maps.Size(32, 32),
            origin: new google.maps.Point(0, 0),
            anchor: new google.maps.Point(0, 0),
          };
          console.log(myicon.url);
          var mlabel = "";
          var namel = this.name.split(" ");
          namel.forEach((element: string) => {
            mlabel = mlabel + element.charAt(0);
          });
          this.marker = new google.maps.Marker({
            position: this.location,
            icon: myicon,
            map: this.map,
            label: { text: mlabel, color: "white" },
          });
          this.addMarker();
        }
      );
    }

    else {
      this.location = {
        lat: 27.2046,
        lng: 77.4977
      };
      this.map = new google.maps.Map(this.mapElement, {
        center: this.location,
        zoom: 17,
      });
    }
  }

  get_profile(): void {
    var user_name = document.getElementById("username");
    this.profile = this.http.get<any>(environment.baseUrl + "/user/profile").subscribe(data => {
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

  logout(): void {
    this.http.get(environment.baseUrl + '/logout');
    this.router.navigate(['/home']);
  }

  reset(): void {
    window.location.reload();
  }

  saveForm(): void {
    this.locname = (<HTMLInputElement>document.getElementById('Coordinate_Place')).value;
    var postDict = { "streetName": this.locname, "coords": this.floatLatLong };
    this.locationService.postLocations(postDict).subscribe(res => console.log(res));
    window.location.reload();
  }

}
