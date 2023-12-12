import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { AcceptationFormService, AcceptationFormGroup } from './acceptation-form.service';
import { IAcceptation } from '../acceptation.model';
import { AcceptationService } from '../service/acceptation.service';
import { IInscription } from 'app/entities/inscription/inscription.model';
import { InscriptionService } from 'app/entities/inscription/service/inscription.service';
import { IDepot } from 'app/entities/depot/depot.model';
import { DepotService } from 'app/entities/depot/service/depot.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-acceptation-update',
  templateUrl: './acceptation-update.component.html',
})
export class AcceptationUpdateComponent implements OnInit {
  isSaving = false;
  acceptation: IAcceptation | null = null;

  inscriptionsSharedCollection: IInscription[] = [];
  depotsSharedCollection: IDepot[] = [];
  usersSharedCollection: IUser[] = [];

  editForm: AcceptationFormGroup = this.acceptationFormService.createAcceptationFormGroup();

  constructor(
    protected acceptationService: AcceptationService,
    protected acceptationFormService: AcceptationFormService,
    protected inscriptionService: InscriptionService,
    protected depotService: DepotService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareInscription = (o1: IInscription | null, o2: IInscription | null): boolean => this.inscriptionService.compareInscription(o1, o2);

  compareDepot = (o1: IDepot | null, o2: IDepot | null): boolean => this.depotService.compareDepot(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ acceptation }) => {
      this.acceptation = acceptation;
      if (acceptation) {
        this.updateForm(acceptation);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const acceptation = this.acceptationFormService.getAcceptation(this.editForm);
    if (acceptation.id !== null) {
      this.subscribeToSaveResponse(this.acceptationService.update(acceptation));
    } else {
      this.subscribeToSaveResponse(this.acceptationService.create(acceptation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAcceptation>>): void {
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

  protected updateForm(acceptation: IAcceptation): void {
    this.acceptation = acceptation;
    this.acceptationFormService.resetForm(this.editForm, acceptation);

    this.inscriptionsSharedCollection = this.inscriptionService.addInscriptionToCollectionIfMissing<IInscription>(
      this.inscriptionsSharedCollection,
      acceptation.inscription
    );
    this.depotsSharedCollection = this.depotService.addDepotToCollectionIfMissing<IDepot>(this.depotsSharedCollection, acceptation.depot);
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, acceptation.user);
  }

  protected loadRelationshipsOptions(): void {
    this.inscriptionService
      .query()
      .pipe(map((res: HttpResponse<IInscription[]>) => res.body ?? []))
      .pipe(
        map((inscriptions: IInscription[]) =>
          this.inscriptionService.addInscriptionToCollectionIfMissing<IInscription>(inscriptions, this.acceptation?.inscription)
        )
      )
      .subscribe((inscriptions: IInscription[]) => (this.inscriptionsSharedCollection = inscriptions));

    this.depotService
      .query()
      .pipe(map((res: HttpResponse<IDepot[]>) => res.body ?? []))
      .pipe(map((depots: IDepot[]) => this.depotService.addDepotToCollectionIfMissing<IDepot>(depots, this.acceptation?.depot)))
      .subscribe((depots: IDepot[]) => (this.depotsSharedCollection = depots));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.acceptation?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
