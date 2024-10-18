import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend';
  // public term : string
  // public isTokenThere : boolean

  constructor(private router: RouterOutlet) {
    console.log("Hola");
  }

  //TODO escribir la busqueda
}
