import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  templateUrl: './app.component.html',
  imports: [
    RouterOutlet
  ],
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend';
  // public term : string
  // public isTokenThere : boolean

  constructor() {
    console.log("Hola");
  }

  //TODO escribir la busqueda
}
