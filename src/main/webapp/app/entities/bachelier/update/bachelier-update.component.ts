import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { BachelierFormService, BachelierFormGroup } from './bachelier-form.service';
import { IBachelier } from '../bachelier.model';
import { BachelierService } from '../service/bachelier.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IDepot } from 'app/entities/depot/depot.model';
import { DepotService } from 'app/entities/depot/service/depot.service';
import { EnumSexe } from 'app/entities/enumerations/enum-sexe.model';

@Component({
  selector: 'jhi-bachelier-update',
  templateUrl: './bachelier-update.component.html',
})
export class BachelierUpdateComponent implements OnInit {
  isSaving = false;
  bachelier: IBachelier | null = null;
  enumSexeValues = Object.keys(EnumSexe);

  usersSharedCollection: IUser[] = [];
  depotsSharedCollection: IDepot[] = [];

  editForm: BachelierFormGroup = this.bachelierFormService.createBachelierFormGroup();

  constructor(
    protected bachelierService: BachelierService,
    protected bachelierFormService: BachelierFormService,
    protected userService: UserService,
    protected depotService: DepotService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareDepot = (o1: IDepot | null, o2: IDepot | null): boolean => this.depotService.compareDepot(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bachelier }) => {
      this.bachelier = bachelier;
      if (bachelier) {
        this.updateForm(bachelier);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bachelier = this.bachelierFormService.getBachelier(this.editForm);
    if (bachelier.id !== null) {
      this.subscribeToSaveResponse(this.bachelierService.update(bachelier));
    } else {
      this.subscribeToSaveResponse(this.bachelierService.create(bachelier));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBachelier>>): void {
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

  protected updateForm(bachelier: IBachelier): void {
    this.bachelier = bachelier;
    this.bachelierFormService.resetForm(this.editForm, bachelier);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, bachelier.utilisateur);
    this.depotsSharedCollection = this.depotService.addDepotToCollectionIfMissing<IDepot>(this.depotsSharedCollection, bachelier.depot);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.bachelier?.utilisateur)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.depotService
      .query()
      .pipe(map((res: HttpResponse<IDepot[]>) => res.body ?? []))
      .pipe(map((depots: IDepot[]) => this.depotService.addDepotToCollectionIfMissing<IDepot>(depots, this.bachelier?.depot)))
      .subscribe((depots: IDepot[]) => (this.depotsSharedCollection = depots));
  }
}
