import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IBachelier, NewBachelier } from '../bachelier.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBachelier for edit and NewBachelierFormGroupInput for create.
 */
type BachelierFormGroupInput = IBachelier | PartialWithRequiredKeyOf<NewBachelier>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IBachelier | NewBachelier> = Omit<T, 'dateNaissance'> & {
  dateNaissance?: string | null;
};

type BachelierFormRawValue = FormValueOf<IBachelier>;

type NewBachelierFormRawValue = FormValueOf<NewBachelier>;

type BachelierFormDefaults = Pick<NewBachelier, 'id' | 'dateNaissance'>;

type BachelierFormGroupContent = {
  id: FormControl<BachelierFormRawValue['id'] | NewBachelier['id']>;
  sexe: FormControl<BachelierFormRawValue['sexe']>;
  dateNaissance: FormControl<BachelierFormRawValue['dateNaissance']>;
  lieuNaissance: FormControl<BachelierFormRawValue['lieuNaissance']>;
  nationalite: FormControl<BachelierFormRawValue['nationalite']>;
  telephone: FormControl<BachelierFormRawValue['telephone']>;
  utilisateur: FormControl<BachelierFormRawValue['utilisateur']>;
};

export type BachelierFormGroup = FormGroup<BachelierFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BachelierFormService {
  createBachelierFormGroup(bachelier: BachelierFormGroupInput = { id: null }): BachelierFormGroup {
    const bachelierRawValue = this.convertBachelierToBachelierRawValue({
      ...this.getFormDefaults(),
      ...bachelier,
    });
    return new FormGroup<BachelierFormGroupContent>({
      id: new FormControl(
        { value: bachelierRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      sexe: new FormControl(bachelierRawValue.sexe),
      dateNaissance: new FormControl(bachelierRawValue.dateNaissance),
      lieuNaissance: new FormControl(bachelierRawValue.lieuNaissance),
      nationalite: new FormControl(bachelierRawValue.nationalite),
      telephone: new FormControl(bachelierRawValue.telephone),
      utilisateur: new FormControl(bachelierRawValue.utilisateur),
    });
  }

  getBachelier(form: BachelierFormGroup): IBachelier | NewBachelier {
    return this.convertBachelierRawValueToBachelier(form.getRawValue() as BachelierFormRawValue | NewBachelierFormRawValue);
  }

  resetForm(form: BachelierFormGroup, bachelier: BachelierFormGroupInput): void {
    const bachelierRawValue = this.convertBachelierToBachelierRawValue({ ...this.getFormDefaults(), ...bachelier });
    form.reset(
      {
        ...bachelierRawValue,
        id: { value: bachelierRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): BachelierFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateNaissance: currentTime,
    };
  }

  private convertBachelierRawValueToBachelier(rawBachelier: BachelierFormRawValue | NewBachelierFormRawValue): IBachelier | NewBachelier {
    return {
      ...rawBachelier,
      dateNaissance: dayjs(rawBachelier.dateNaissance, DATE_TIME_FORMAT),
    };
  }

  private convertBachelierToBachelierRawValue(
    bachelier: IBachelier | (Partial<NewBachelier> & BachelierFormDefaults)
  ): BachelierFormRawValue | PartialWithRequiredKeyOf<NewBachelierFormRawValue> {
    return {
      ...bachelier,
      dateNaissance: bachelier.dateNaissance ? bachelier.dateNaissance.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
