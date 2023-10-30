import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBachelier } from '../bachelier.model';

@Component({
  selector: 'jhi-bachelier-detail',
  templateUrl: './bachelier-detail.component.html',
})
export class BachelierDetailComponent implements OnInit {
  bachelier: IBachelier | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bachelier }) => {
      this.bachelier = bachelier;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
