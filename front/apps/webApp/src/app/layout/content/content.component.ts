import { SidebarComponent } from '../sidebar/sidebar.component';
import { FooterComponent } from '../footer/footer.component';
import { HeaderComponent } from '../header/header.component';
import { Component, OnInit } from '@angular/core';

@Component({
  standalone: true,
  selector: 'app-content',
  templateUrl: './content.component.html',
  styleUrls: ['./content.component.scss'],
  imports: [HeaderComponent, SidebarComponent, FooterComponent],
})
export class ContentComponent implements OnInit {
  constructor() {}

  public ngOnInit() {}
}
