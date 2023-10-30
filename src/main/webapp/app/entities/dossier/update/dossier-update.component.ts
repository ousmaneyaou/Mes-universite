import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { DossierFormService, DossierFormGroup } from './dossier-form.service';
import { IDossier } from '../dossier.model';
import { DossierService } from '../service/dossier.service';
import { INiveau } from 'app/entities/niveau/niveau.model';
import { NiveauService } from 'app/entities/niveau/service/niveau.service';
import { ICampagne } from 'app/entities/campagne/campagne.model';
import { CampagneService } from 'app/entities/campagne/service/campagne.service';

@Component({
  selector: 'jhi-dossier-update',
  templateUrl: './dossier-update.component.html',
})
export class DossierUpdateComponent implements OnInit {
  isSaving = false;
  dossier: IDossier | null = null;

  niveausSharedCollection: INiveau[] = [];
  campagnesSharedCollection: ICampagne[] = [];

  editForm: DossierFormGroup = this.dossierFormService.createDossierFormGroup();

  constructor(
    protected dossierService: DossierService,
    protected dossierFormService: DossierFormService,
    protected niveauService: NiveauService,
    protected campagneService: CampagneService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareNiveau = (o1: INiveau | null, o2: INiveau | null): boolean => this.niveauService.compareNiveau(o1, o2);

  compareCampagne = (o1: ICampagne | null, o2: ICampagne | null): boolean => this.campagneService.compareCampagne(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dossier }) => {
      this.dossier = dossier;
      if (dossier) {
        this.updateForm(dossier);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dossier = this.dossierFormService.getDossier(this.editForm);
    if (dossier.id !== null) {
      this.subscribeToSaveResponse(this.dossierService.update(dossier));
    } else {
      this.subscribeToSaveResponse(this.dossierService.create(dossier));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDossier>>): void {
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

  protected updateForm(dossier: IDossier): void {
    this.dossier = dossier;
    this.dossierFormService.resetForm(this.editForm, dossier);

    this.niveausSharedCollection = this.niveauService.addNiveauToCollectionIfMissing<INiveau>(this.niveausSharedCollection, dossier.niveau);
    this.campagnesSharedCollection = this.campagneService.addCampagneToCollectionIfMissing<ICampagne>(
      this.campagnesSharedCollection,
      dossier.campagne
    );
  }

  protected loadRelationshipsOptions(): void {
    this.niveauService
      .query()
      .pipe(map((res: HttpResponse<INiveau[]>) => res.body ?? []))
      .pipe(map((niveaus: INiveau[]) => this.niveauService.addNiveauToCollectionIfMissing<INiveau>(niveaus, this.dossier?.niveau)))
      .subscribe((niveaus: INiveau[]) => (this.niveausSharedCollection = niveaus));

    this.campagneService
      .query()
      .pipe(map((res: HttpResponse<ICampagne[]>) => res.body ?? []))
      .pipe(
        map((campagnes: ICampagne[]) => this.campagneService.addCampagneToCollectionIfMissing<ICampagne>(campagnes, this.dossier?.campagne))
      )
      .subscribe((campagnes: ICampagne[]) => (this.campagnesSharedCollection = campagnes));
  }
}
