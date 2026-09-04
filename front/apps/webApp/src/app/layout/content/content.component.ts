import { SidebarComponent } from '../sidebar/sidebar.component';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  standalone: true,
  selector: 'app-content',
  templateUrl: './content.component.html',
  styleUrls: ['./content.component.scss'],
  imports: [RouterOutlet, HeaderComponent, SidebarComponent, FooterComponent],
})
export class ContentComponent implements OnInit {
  constructor() {}

  public ngOnInit() {}
}
