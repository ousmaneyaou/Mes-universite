import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { NiveauFormService, NiveauFormGroup } from './niveau-form.service';
import { INiveau } from '../niveau.model';
import { NiveauService } from '../service/niveau.service';
import { IDepartement } from 'app/entities/departement/departement.model';
import { DepartementService } from 'app/entities/departement/service/departement.service';
import { ISession } from 'app/entities/session/session.model';
import { SessionService } from 'app/entities/session/service/session.service';

@Component({
  selector: 'jhi-niveau-update',
  templateUrl: './niveau-update.component.html',
})
export class NiveauUpdateComponent implements OnInit {
  isSaving = false;
  niveau: INiveau | null = null;

  departementsSharedCollection: IDepartement[] = [];
  sessionsSharedCollection: ISession[] = [];

  editForm: NiveauFormGroup = this.niveauFormService.createNiveauFormGroup();

  constructor(
    protected niveauService: NiveauService,
    protected niveauFormService: NiveauFormService,
    protected departementService: DepartementService,
    protected sessionService: SessionService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareDepartement = (o1: IDepartement | null, o2: IDepartement | null): boolean => this.departementService.compareDepartement(o1, o2);

  compareSession = (o1: ISession | null, o2: ISession | null): boolean => this.sessionService.compareSession(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ niveau }) => {
      this.niveau = niveau;
      if (niveau) {
        this.updateForm(niveau);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const niveau = this.niveauFormService.getNiveau(this.editForm);
    if (niveau.id !== null) {
      this.subscribeToSaveResponse(this.niveauService.update(niveau));
    } else {
      this.subscribeToSaveResponse(this.niveauService.create(niveau));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INiveau>>): void {
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

  protected updateForm(niveau: INiveau): void {
    this.niveau = niveau;
    this.niveauFormService.resetForm(this.editForm, niveau);

    this.departementsSharedCollection = this.departementService.addDepartementToCollectionIfMissing<IDepartement>(
      this.departementsSharedCollection,
      niveau.departement
    );
    this.sessionsSharedCollection = this.sessionService.addSessionToCollectionIfMissing<ISession>(
      this.sessionsSharedCollection,
      niveau.session
    );
  }

  protected loadRelationshipsOptions(): void {
    this.departementService
      .query()
      .pipe(map((res: HttpResponse<IDepartement[]>) => res.body ?? []))
      .pipe(
        map((departements: IDepartement[]) =>
          this.departementService.addDepartementToCollectionIfMissing<IDepartement>(departements, this.niveau?.departement)
        )
      )
      .subscribe((departements: IDepartement[]) => (this.departementsSharedCollection = departements));

    this.sessionService
      .query()
      .pipe(map((res: HttpResponse<ISession[]>) => res.body ?? []))
      .pipe(map((sessions: ISession[]) => this.sessionService.addSessionToCollectionIfMissing<ISession>(sessions, this.niveau?.session)))
      .subscribe((sessions: ISession[]) => (this.sessionsSharedCollection = sessions));
  }
}
