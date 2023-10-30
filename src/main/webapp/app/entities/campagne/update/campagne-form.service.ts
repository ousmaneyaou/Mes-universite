import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICampagne, NewCampagne } from '../campagne.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICampagne for edit and NewCampagneFormGroupInput for create.
 */
type CampagneFormGroupInput = ICampagne | PartialWithRequiredKeyOf<NewCampagne>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICampagne | NewCampagne> = Omit<T, 'dateDebut' | 'dateFin'> & {
  dateDebut?: string | null;
  dateFin?: string | null;
};

type CampagneFormRawValue = FormValueOf<ICampagne>;

type NewCampagneFormRawValue = FormValueOf<NewCampagne>;

type CampagneFormDefaults = Pick<NewCampagne, 'id' | 'dateDebut' | 'dateFin'>;

type CampagneFormGroupContent = {
  id: FormControl<CampagneFormRawValue['id'] | NewCampagne['id']>;
  intitule: FormControl<CampagneFormRawValue['intitule']>;
  dateDebut: FormControl<CampagneFormRawValue['dateDebut']>;
  dateFin: FormControl<CampagneFormRawValue['dateFin']>;
};

export type CampagneFormGroup = FormGroup<CampagneFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CampagneFormService {
  createCampagneFormGroup(campagne: CampagneFormGroupInput = { id: null }): CampagneFormGroup {
    const campagneRawValue = this.convertCampagneToCampagneRawValue({
      ...this.getFormDefaults(),
      ...campagne,
    });
    return new FormGroup<CampagneFormGroupContent>({
      id: new FormControl(
        { value: campagneRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      intitule: new FormControl(campagneRawValue.intitule),
      dateDebut: new FormControl(campagneRawValue.dateDebut),
      dateFin: new FormControl(campagneRawValue.dateFin),
    });
  }

  getCampagne(form: CampagneFormGroup): ICampagne | NewCampagne {
    return this.convertCampagneRawValueToCampagne(form.getRawValue() as CampagneFormRawValue | NewCampagneFormRawValue);
  }

  resetForm(form: CampagneFormGroup, campagne: CampagneFormGroupInput): void {
    const campagneRawValue = this.convertCampagneToCampagneRawValue({ ...this.getFormDefaults(), ...campagne });
    form.reset(
      {
        ...campagneRawValue,
        id: { value: campagneRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CampagneFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateDebut: currentTime,
      dateFin: currentTime,
    };
  }

  private convertCampagneRawValueToCampagne(rawCampagne: CampagneFormRawValue | NewCampagneFormRawValue): ICampagne | NewCampagne {
    return {
      ...rawCampagne,
      dateDebut: dayjs(rawCampagne.dateDebut, DATE_TIME_FORMAT),
      dateFin: dayjs(rawCampagne.dateFin, DATE_TIME_FORMAT),
    };
  }

  private convertCampagneToCampagneRawValue(
    campagne: ICampagne | (Partial<NewCampagne> & CampagneFormDefaults)
  ): CampagneFormRawValue | PartialWithRequiredKeyOf<NewCampagneFormRawValue> {
    return {
      ...campagne,
      dateDebut: campagne.dateDebut ? campagne.dateDebut.format(DATE_TIME_FORMAT) : undefined,
      dateFin: campagne.dateFin ? campagne.dateFin.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
