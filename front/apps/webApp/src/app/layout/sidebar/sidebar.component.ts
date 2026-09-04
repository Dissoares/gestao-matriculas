import { Component, OnInit } from '@angular/core';
import { DrawerModule } from 'primeng/drawer';
@Component({
  standalone: true,
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
  imports: [DrawerModule],
})
export class SidebarComponent implements OnInit {
  constructor() {}

  public ngOnInit() {}
}
