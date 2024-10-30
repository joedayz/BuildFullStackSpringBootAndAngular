import { Component } from '@angular/core';
import {Router, RouterLink, RouterOutlet} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  templateUrl: './app.component.html',
  imports: [
    RouterOutlet,
    RouterLink,
    FormsModule,
    NgIf
  ],
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend';
  public term!: string
  public isTokenThere!: boolean

  constructor(private router: Router) {
    console.log("Token:  " + localStorage.getItem('token'));
    this.isTokenThere = localStorage.getItem('token') != null
  }

  search () {
    this.router.navigate(["/shop", this.term]).then(() => window.location.reload())
  }
}
