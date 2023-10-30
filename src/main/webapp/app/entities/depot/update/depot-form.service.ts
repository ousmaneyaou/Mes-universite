import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDepot, NewDepot } from '../depot.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDepot for edit and NewDepotFormGroupInput for create.
 */
type DepotFormGroupInput = IDepot | PartialWithRequiredKeyOf<NewDepot>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDepot | NewDepot> = Omit<T, 'dateNaissance' | 'dateDepot'> & {
  dateNaissance?: string | null;
  dateDepot?: string | null;
};

type DepotFormRawValue = FormValueOf<IDepot>;

type NewDepotFormRawValue = FormValueOf<NewDepot>;

type DepotFormDefaults = Pick<NewDepot, 'id' | 'dateNaissance' | 'dateDepot'>;

type DepotFormGroupContent = {
  id: FormControl<DepotFormRawValue['id'] | NewDepot['id']>;
  nom: FormControl<DepotFormRawValue['nom']>;
  prenom: FormControl<DepotFormRawValue['prenom']>;
  dateNaissance: FormControl<DepotFormRawValue['dateNaissance']>;
  lieuDeNaissance: FormControl<DepotFormRawValue['lieuDeNaissance']>;
  email: FormControl<DepotFormRawValue['email']>;
  nationalite: FormControl<DepotFormRawValue['nationalite']>;
  telephone: FormControl<DepotFormRawValue['telephone']>;
  sexe: FormControl<DepotFormRawValue['sexe']>;
  dateDepot: FormControl<DepotFormRawValue['dateDepot']>;
  numeroDeTable: FormControl<DepotFormRawValue['numeroDeTable']>;
  serie: FormControl<DepotFormRawValue['serie']>;
  diplome: FormControl<DepotFormRawValue['diplome']>;
  diplomeContentType: FormControl<DepotFormRawValue['diplomeContentType']>;
  releveDeNote: FormControl<DepotFormRawValue['releveDeNote']>;
  releveDeNoteContentType: FormControl<DepotFormRawValue['releveDeNoteContentType']>;
  anneeObtention: FormControl<DepotFormRawValue['anneeObtention']>;
  lieuObtention: FormControl<DepotFormRawValue['lieuObtention']>;
  mention: FormControl<DepotFormRawValue['mention']>;
  lettreDeMotivation: FormControl<DepotFormRawValue['lettreDeMotivation']>;
  lettreDeMotivationContentType: FormControl<DepotFormRawValue['lettreDeMotivationContentType']>;
  choix1: FormControl<DepotFormRawValue['choix1']>;
  choix2: FormControl<DepotFormRawValue['choix2']>;
  choix3: FormControl<DepotFormRawValue['choix3']>;
  photo: FormControl<DepotFormRawValue['photo']>;
  photoContentType: FormControl<DepotFormRawValue['photoContentType']>;
  bachelier: FormControl<DepotFormRawValue['bachelier']>;
  dossier: FormControl<DepotFormRawValue['dossier']>;
};

export type DepotFormGroup = FormGroup<DepotFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DepotFormService {
  createDepotFormGroup(depot: DepotFormGroupInput = { id: null }): DepotFormGroup {
    const depotRawValue = this.convertDepotToDepotRawValue({
      ...this.getFormDefaults(),
      ...depot,
    });
    return new FormGroup<DepotFormGroupContent>({
      id: new FormControl(
        { value: depotRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nom: new FormControl(depotRawValue.nom),
      prenom: new FormControl(depotRawValue.prenom),
      dateNaissance: new FormControl(depotRawValue.dateNaissance),
      lieuDeNaissance: new FormControl(depotRawValue.lieuDeNaissance),
      email: new FormControl(depotRawValue.email),
      nationalite: new FormControl(depotRawValue.nationalite),
      telephone: new FormControl(depotRawValue.telephone),
      sexe: new FormControl(depotRawValue.sexe),
      dateDepot: new FormControl(depotRawValue.dateDepot),
      numeroDeTable: new FormControl(depotRawValue.numeroDeTable),
      serie: new FormControl(depotRawValue.serie),
      diplome: new FormControl(depotRawValue.diplome),
      diplomeContentType: new FormControl(depotRawValue.diplomeContentType),
      releveDeNote: new FormControl(depotRawValue.releveDeNote),
      releveDeNoteContentType: new FormControl(depotRawValue.releveDeNoteContentType),
      anneeObtention: new FormControl(depotRawValue.anneeObtention),
      lieuObtention: new FormControl(depotRawValue.lieuObtention),
      mention: new FormControl(depotRawValue.mention),
      lettreDeMotivation: new FormControl(depotRawValue.lettreDeMotivation),
      lettreDeMotivationContentType: new FormControl(depotRawValue.lettreDeMotivationContentType),
      choix1: new FormControl(depotRawValue.choix1),
      choix2: new FormControl(depotRawValue.choix2),
      choix3: new FormControl(depotRawValue.choix3),
      photo: new FormControl(depotRawValue.photo),
      photoContentType: new FormControl(depotRawValue.photoContentType),
      bachelier: new FormControl(depotRawValue.bachelier),
      dossier: new FormControl(depotRawValue.dossier),
    });
  }

  getDepot(form: DepotFormGroup): IDepot | NewDepot {
    return this.convertDepotRawValueToDepot(form.getRawValue() as DepotFormRawValue | NewDepotFormRawValue);
  }

  resetForm(form: DepotFormGroup, depot: DepotFormGroupInput): void {
    const depotRawValue = this.convertDepotToDepotRawValue({ ...this.getFormDefaults(), ...depot });
    form.reset(
      {
        ...depotRawValue,
        id: { value: depotRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): DepotFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateNaissance: currentTime,
      dateDepot: currentTime,
    };
  }

  private convertDepotRawValueToDepot(rawDepot: DepotFormRawValue | NewDepotFormRawValue): IDepot | NewDepot {
    return {
      ...rawDepot,
      dateNaissance: dayjs(rawDepot.dateNaissance, DATE_TIME_FORMAT),
      dateDepot: dayjs(rawDepot.dateDepot, DATE_TIME_FORMAT),
    };
  }

  private convertDepotToDepotRawValue(
    depot: IDepot | (Partial<NewDepot> & DepotFormDefaults)
  ): DepotFormRawValue | PartialWithRequiredKeyOf<NewDepotFormRawValue> {
    return {
      ...depot,
      dateNaissance: depot.dateNaissance ? depot.dateNaissance.format(DATE_TIME_FORMAT) : undefined,
      dateDepot: depot.dateDepot ? depot.dateDepot.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
