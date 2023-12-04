import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { FiliereFormService, FiliereFormGroup } from './filiere-form.service';
import { IFiliere } from '../filiere.model';
import { FiliereService } from '../service/filiere.service';
import { IDepartement } from 'app/entities/departement/departement.model';
import { DepartementService } from 'app/entities/departement/service/departement.service';

@Component({
  selector: 'jhi-filiere-update',
  templateUrl: './filiere-update.component.html',
})
export class FiliereUpdateComponent implements OnInit {
  isSaving = false;
  filiere: IFiliere | null = null;

  departementsSharedCollection: IDepartement[] = [];

  editForm: FiliereFormGroup = this.filiereFormService.createFiliereFormGroup();

  constructor(
    protected filiereService: FiliereService,
    protected filiereFormService: FiliereFormService,
    protected departementService: DepartementService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareDepartement = (o1: IDepartement | null, o2: IDepartement | null): boolean => this.departementService.compareDepartement(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ filiere }) => {
      this.filiere = filiere;
      if (filiere) {
        this.updateForm(filiere);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const filiere = this.filiereFormService.getFiliere(this.editForm);
    if (filiere.id !== null) {
      this.subscribeToSaveResponse(this.filiereService.update(filiere));
    } else {
      this.subscribeToSaveResponse(this.filiereService.create(filiere));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFiliere>>): void {
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

  protected updateForm(filiere: IFiliere): void {
    this.filiere = filiere;
    this.filiereFormService.resetForm(this.editForm, filiere);

    this.departementsSharedCollection = this.departementService.addDepartementToCollectionIfMissing<IDepartement>(
      this.departementsSharedCollection,
      filiere.departement
    );
  }

  protected loadRelationshipsOptions(): void {
    this.departementService
      .query()
      .pipe(map((res: HttpResponse<IDepartement[]>) => res.body ?? []))
      .pipe(
        map((departements: IDepartement[]) =>
          this.departementService.addDepartementToCollectionIfMissing<IDepartement>(departements, this.filiere?.departement)
        )
      )
      .subscribe((departements: IDepartement[]) => (this.departementsSharedCollection = departements));
  }
}
