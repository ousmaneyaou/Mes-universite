import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { InscriptionFormService, InscriptionFormGroup } from './inscription-form.service';
import { IInscription } from '../inscription.model';
import { InscriptionService } from '../service/inscription.service';
import { IPaiement } from 'app/entities/paiement/paiement.model';
import { PaiementService } from 'app/entities/paiement/service/paiement.service';
import { IDossier } from 'app/entities/dossier/dossier.model';
import { DossierService } from 'app/entities/dossier/service/dossier.service';

@Component({
  selector: 'jhi-inscription-update',
  templateUrl: './inscription-update.component.html',
})
export class InscriptionUpdateComponent implements OnInit {
  isSaving = false;
  inscription: IInscription | null = null;

  paiementsSharedCollection: IPaiement[] = [];
  dossiersSharedCollection: IDossier[] = [];

  editForm: InscriptionFormGroup = this.inscriptionFormService.createInscriptionFormGroup();

  constructor(
    protected inscriptionService: InscriptionService,
    protected inscriptionFormService: InscriptionFormService,
    protected paiementService: PaiementService,
    protected dossierService: DossierService,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePaiement = (o1: IPaiement | null, o2: IPaiement | null): boolean => this.paiementService.comparePaiement(o1, o2);

  compareDossier = (o1: IDossier | null, o2: IDossier | null): boolean => this.dossierService.compareDossier(o1, o2);

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

    this.paiementsSharedCollection = this.paiementService.addPaiementToCollectionIfMissing<IPaiement>(
      this.paiementsSharedCollection,
      inscription.paiement
    );
    this.dossiersSharedCollection = this.dossierService.addDossierToCollectionIfMissing<IDossier>(
      this.dossiersSharedCollection,
      ...(inscription.dossiers ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.paiementService
      .query()
      .pipe(map((res: HttpResponse<IPaiement[]>) => res.body ?? []))
      .pipe(
        map((paiements: IPaiement[]) =>
          this.paiementService.addPaiementToCollectionIfMissing<IPaiement>(paiements, this.inscription?.paiement)
        )
      )
      .subscribe((paiements: IPaiement[]) => (this.paiementsSharedCollection = paiements));

    this.dossierService
      .query()
      .pipe(map((res: HttpResponse<IDossier[]>) => res.body ?? []))
      .pipe(
        map((dossiers: IDossier[]) =>
          this.dossierService.addDossierToCollectionIfMissing<IDossier>(dossiers, ...(this.inscription?.dossiers ?? []))
        )
      )
      .subscribe((dossiers: IDossier[]) => (this.dossiersSharedCollection = dossiers));
  }
}
