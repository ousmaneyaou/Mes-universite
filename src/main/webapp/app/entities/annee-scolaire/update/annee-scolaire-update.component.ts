import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { AnneeScolaireFormService, AnneeScolaireFormGroup } from './annee-scolaire-form.service';
import { IAnneeScolaire } from '../annee-scolaire.model';
import { AnneeScolaireService } from '../service/annee-scolaire.service';
import { ISession } from 'app/entities/session/session.model';
import { SessionService } from 'app/entities/session/service/session.service';

@Component({
  selector: 'jhi-annee-scolaire-update',
  templateUrl: './annee-scolaire-update.component.html',
})
export class AnneeScolaireUpdateComponent implements OnInit {
  isSaving = false;
  anneeScolaire: IAnneeScolaire | null = null;

  sessionsSharedCollection: ISession[] = [];

  editForm: AnneeScolaireFormGroup = this.anneeScolaireFormService.createAnneeScolaireFormGroup();

  constructor(
    protected anneeScolaireService: AnneeScolaireService,
    protected anneeScolaireFormService: AnneeScolaireFormService,
    protected sessionService: SessionService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareSession = (o1: ISession | null, o2: ISession | null): boolean => this.sessionService.compareSession(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ anneeScolaire }) => {
      this.anneeScolaire = anneeScolaire;
      if (anneeScolaire) {
        this.updateForm(anneeScolaire);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const anneeScolaire = this.anneeScolaireFormService.getAnneeScolaire(this.editForm);
    if (anneeScolaire.id !== null) {
      this.subscribeToSaveResponse(this.anneeScolaireService.update(anneeScolaire));
    } else {
      this.subscribeToSaveResponse(this.anneeScolaireService.create(anneeScolaire));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAnneeScolaire>>): void {
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

  protected updateForm(anneeScolaire: IAnneeScolaire): void {
    this.anneeScolaire = anneeScolaire;
    this.anneeScolaireFormService.resetForm(this.editForm, anneeScolaire);

    this.sessionsSharedCollection = this.sessionService.addSessionToCollectionIfMissing<ISession>(
      this.sessionsSharedCollection,
      anneeScolaire.session
    );
  }

  protected loadRelationshipsOptions(): void {
    this.sessionService
      .query()
      .pipe(map((res: HttpResponse<ISession[]>) => res.body ?? []))
      .pipe(
        map((sessions: ISession[]) => this.sessionService.addSessionToCollectionIfMissing<ISession>(sessions, this.anneeScolaire?.session))
      )
      .subscribe((sessions: ISession[]) => (this.sessionsSharedCollection = sessions));
  }
}
