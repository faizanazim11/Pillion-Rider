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
    let url = "https://jsonplaceholder.typicode.com/todos";
    return this.http.get(url);
  }
}
