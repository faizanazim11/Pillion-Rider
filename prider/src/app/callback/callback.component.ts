import { SecurityService } from './../security.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-callback',
  templateUrl: './callback.component.html',
  styleUrls: ['./callback.component.scss']
})
export class CallbackComponent implements OnInit {

  constructor(private route: ActivatedRoute, private router: Router, private securityService: SecurityService) { }

  ngOnInit(): void {

    this.route.queryParams.subscribe(p=> {
      this.securityService.fetchToken(p.code, p.state).subscribe(data=> {
        console.log(data.accessToken);
        this.securityService.updateToken(data.accessToken);
        this.router.navigate(['/console']);
      })
    })

  }

}
