import { Component, OnInit } from '@angular/core';
import { ToolbarModule } from 'primeng/toolbar';
@Component({
  standalone: true,
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
  imports: [ToolbarModule],
})
export class HeaderComponent implements OnInit {
  constructor() {}

  public ngOnInit() {}
}
