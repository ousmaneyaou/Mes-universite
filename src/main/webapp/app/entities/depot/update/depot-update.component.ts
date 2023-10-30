import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { DepotFormService, DepotFormGroup } from './depot-form.service';
import { IDepot } from '../depot.model';
import { DepotService } from '../service/depot.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IBachelier } from 'app/entities/bachelier/bachelier.model';
import { BachelierService } from 'app/entities/bachelier/service/bachelier.service';
import { IDossier } from 'app/entities/dossier/dossier.model';
import { DossierService } from 'app/entities/dossier/service/dossier.service';
import { EnumSexe } from 'app/entities/enumerations/enum-sexe.model';

@Component({
  selector: 'jhi-depot-update',
  templateUrl: './depot-update.component.html',
})
export class DepotUpdateComponent implements OnInit {
  isSaving = false;
  depot: IDepot | null = null;
  enumSexeValues = Object.keys(EnumSexe);

  bacheliersSharedCollection: IBachelier[] = [];
  dossiersSharedCollection: IDossier[] = [];

  editForm: DepotFormGroup = this.depotFormService.createDepotFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected depotService: DepotService,
    protected depotFormService: DepotFormService,
    protected bachelierService: BachelierService,
    protected dossierService: DossierService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareBachelier = (o1: IBachelier | null, o2: IBachelier | null): boolean => this.bachelierService.compareBachelier(o1, o2);

  compareDossier = (o1: IDossier | null, o2: IDossier | null): boolean => this.dossierService.compareDossier(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ depot }) => {
      this.depot = depot;
      if (depot) {
        this.updateForm(depot);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('mesUniversiteApp.error', { message: err.message })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const depot = this.depotFormService.getDepot(this.editForm);
    if (depot.id !== null) {
      this.subscribeToSaveResponse(this.depotService.update(depot));
    } else {
      this.subscribeToSaveResponse(this.depotService.create(depot));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDepot>>): void {
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

  protected updateForm(depot: IDepot): void {
    this.depot = depot;
    this.depotFormService.resetForm(this.editForm, depot);

    this.bacheliersSharedCollection = this.bachelierService.addBachelierToCollectionIfMissing<IBachelier>(
      this.bacheliersSharedCollection,
      depot.bachelier
    );
    this.dossiersSharedCollection = this.dossierService.addDossierToCollectionIfMissing<IDossier>(
      this.dossiersSharedCollection,
      depot.dossier
    );
  }

  protected loadRelationshipsOptions(): void {
    this.bachelierService
      .query()
      .pipe(map((res: HttpResponse<IBachelier[]>) => res.body ?? []))
      .pipe(
        map((bacheliers: IBachelier[]) =>
          this.bachelierService.addBachelierToCollectionIfMissing<IBachelier>(bacheliers, this.depot?.bachelier)
        )
      )
      .subscribe((bacheliers: IBachelier[]) => (this.bacheliersSharedCollection = bacheliers));

    this.dossierService
      .query()
      .pipe(map((res: HttpResponse<IDossier[]>) => res.body ?? []))
      .pipe(map((dossiers: IDossier[]) => this.dossierService.addDossierToCollectionIfMissing<IDossier>(dossiers, this.depot?.dossier)))
      .subscribe((dossiers: IDossier[]) => (this.dossiersSharedCollection = dossiers));
  }
}
