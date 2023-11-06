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
import { ISession } from 'app/entities/session/session.model';
import { SessionService } from 'app/entities/session/service/session.service';
import { EnumSexe } from 'app/entities/enumerations/enum-sexe.model';

@Component({
  selector: 'jhi-depot-update',
  templateUrl: './depot-update.component.html',
})
export class DepotUpdateComponent implements OnInit {
  isSaving = false;
  depot: IDepot | null = null;
  enumSexeValues = Object.keys(EnumSexe);

  sessionsSharedCollection: ISession[] = [];

  editForm: DepotFormGroup = this.depotFormService.createDepotFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected depotService: DepotService,
    protected depotFormService: DepotFormService,
    protected sessionService: SessionService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareSession = (o1: ISession | null, o2: ISession | null): boolean => this.sessionService.compareSession(o1, o2);

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

    this.sessionsSharedCollection = this.sessionService.addSessionToCollectionIfMissing<ISession>(
      this.sessionsSharedCollection,
      depot.session
    );
  }

  protected loadRelationshipsOptions(): void {
    this.sessionService
      .query()
      .pipe(map((res: HttpResponse<ISession[]>) => res.body ?? []))
      .pipe(map((sessions: ISession[]) => this.sessionService.addSessionToCollectionIfMissing<ISession>(sessions, this.depot?.session)))
      .subscribe((sessions: ISession[]) => (this.sessionsSharedCollection = sessions));
  }
}
