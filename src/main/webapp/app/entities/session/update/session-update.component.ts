import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { SessionFormService, SessionFormGroup } from './session-form.service';
import { ISession } from '../session.model';
import { SessionService } from '../service/session.service';
import { INiveau } from 'app/entities/niveau/niveau.model';
import { NiveauService } from 'app/entities/niveau/service/niveau.service';

@Component({
  selector: 'jhi-session-update',
  templateUrl: './session-update.component.html',
})
export class SessionUpdateComponent implements OnInit {
  isSaving = false;
  session: ISession | null = null;

  niveausSharedCollection: INiveau[] = [];

  editForm: SessionFormGroup = this.sessionFormService.createSessionFormGroup();

  constructor(
    protected sessionService: SessionService,
    protected sessionFormService: SessionFormService,
    protected niveauService: NiveauService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareNiveau = (o1: INiveau | null, o2: INiveau | null): boolean => this.niveauService.compareNiveau(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ session }) => {
      this.session = session;
      if (session) {
        this.updateForm(session);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const session = this.sessionFormService.getSession(this.editForm);
    if (session.id !== null) {
      this.subscribeToSaveResponse(this.sessionService.update(session));
    } else {
      this.subscribeToSaveResponse(this.sessionService.create(session));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISession>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(session: ISession): void {
    this.session = session;
    this.sessionFormService.resetForm(this.editForm, session);

    this.niveausSharedCollection = this.niveauService.addNiveauToCollectionIfMissing<INiveau>(this.niveausSharedCollection, session.niveau);
  }

  protected loadRelationshipsOptions(): void {
    this.niveauService
      .query()
      .pipe(map((res: HttpResponse<INiveau[]>) => res.body ?? []))
      .pipe(map((niveaus: INiveau[]) => this.niveauService.addNiveauToCollectionIfMissing<INiveau>(niveaus, this.session?.niveau)))
      .subscribe((niveaus: INiveau[]) => (this.niveausSharedCollection = niveaus));
  }
}
