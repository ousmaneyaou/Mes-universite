import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAcceptation } from '../acceptation.model';

@Component({
  selector: 'jhi-acceptation-detail',
  templateUrl: './acceptation-detail.component.html',
})
export class AcceptationDetailComponent implements OnInit {
  acceptation: IAcceptation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ acceptation }) => {
      this.acceptation = acceptation;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
