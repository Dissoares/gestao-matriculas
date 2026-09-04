import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  standalone: true,
  selector: 'app-conteudo',
  templateUrl: './conteudo.component.html',
  styleUrls: ['./conteudo.component.scss'],
  imports: [RouterOutlet],
})
export class ConteudoComponent implements OnInit {
  constructor() {}

  public ngOnInit() {}
}
