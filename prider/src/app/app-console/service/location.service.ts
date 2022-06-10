import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class LocationService {

  constructor(private http:HttpClient) {
  }
  getLocations()
  {
    let url = "http://localhost:8080/stops";
    return this.http.get<any>(url);
  }

  postLocations(postLocation:any) {
    let url = "http://localhost:8080/stops";
    return this.http.post(url,postLocation);
  }
  postRoutes(postRoutes:any) {
    let url = "http://localhost:8080/addRoute";
    return this.http.post(url,postRoutes);
  }
  postRiders(postRiders:any) {
    console.log("Apun postRoutes aayela hai");
    let url = "http://localhost:8080/riders";
    return this.http.post(url,postRiders);
  }
}
