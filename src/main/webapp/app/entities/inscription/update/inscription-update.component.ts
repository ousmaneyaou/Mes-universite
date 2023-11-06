import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { InscriptionFormService, InscriptionFormGroup } from './inscription-form.service';
import { IInscription } from '../inscription.model';
import { InscriptionService } from '../service/inscription.service';
import { ISession } from 'app/entities/session/session.model';
import { SessionService } from 'app/entities/session/service/session.service';

@Component({
  selector: 'jhi-inscription-update',
  templateUrl: './inscription-update.component.html',
})
export class InscriptionUpdateComponent implements OnInit {
  isSaving = false;
  inscription: IInscription | null = null;

  sessionsSharedCollection: ISession[] = [];

  editForm: InscriptionFormGroup = this.inscriptionFormService.createInscriptionFormGroup();

  constructor(
    protected inscriptionService: InscriptionService,
    protected inscriptionFormService: InscriptionFormService,
    protected sessionService: SessionService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareSession = (o1: ISession | null, o2: ISession | null): boolean => this.sessionService.compareSession(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inscription }) => {
      this.inscription = inscription;
      if (inscription) {
        this.updateForm(inscription);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const inscription = this.inscriptionFormService.getInscription(this.editForm);
    if (inscription.id !== null) {
      this.subscribeToSaveResponse(this.inscriptionService.update(inscription));
    } else {
      this.subscribeToSaveResponse(this.inscriptionService.create(inscription));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInscription>>): void {
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

  protected updateForm(inscription: IInscription): void {
    this.inscription = inscription;
    this.inscriptionFormService.resetForm(this.editForm, inscription);

    this.sessionsSharedCollection = this.sessionService.addSessionToCollectionIfMissing<ISession>(
      this.sessionsSharedCollection,
      inscription.session
    );
  }

  protected loadRelationshipsOptions(): void {
    this.sessionService
      .query()
      .pipe(map((res: HttpResponse<ISession[]>) => res.body ?? []))
      .pipe(
        map((sessions: ISession[]) => this.sessionService.addSessionToCollectionIfMissing<ISession>(sessions, this.inscription?.session))
      )
      .subscribe((sessions: ISession[]) => (this.sessionsSharedCollection = sessions));
  }
}
