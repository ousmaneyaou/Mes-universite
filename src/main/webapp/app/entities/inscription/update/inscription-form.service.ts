import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IInscription, NewInscription } from '../inscription.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInscription for edit and NewInscriptionFormGroupInput for create.
 */
type InscriptionFormGroupInput = IInscription | PartialWithRequiredKeyOf<NewInscription>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IInscription | NewInscription> = Omit<T, 'dateInscription'> & {
  dateInscription?: string | null;
};

type InscriptionFormRawValue = FormValueOf<IInscription>;

type NewInscriptionFormRawValue = FormValueOf<NewInscription>;

type InscriptionFormDefaults = Pick<NewInscription, 'id' | 'dateInscription' | 'regime'>;

type InscriptionFormGroupContent = {
  id: FormControl<InscriptionFormRawValue['id'] | NewInscription['id']>;
  dateInscription: FormControl<InscriptionFormRawValue['dateInscription']>;
  regime: FormControl<InscriptionFormRawValue['regime']>;
  session: FormControl<InscriptionFormRawValue['session']>;
};

export type InscriptionFormGroup = FormGroup<InscriptionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InscriptionFormService {
  createInscriptionFormGroup(inscription: InscriptionFormGroupInput = { id: null }): InscriptionFormGroup {
    const inscriptionRawValue = this.convertInscriptionToInscriptionRawValue({
      ...this.getFormDefaults(),
      ...inscription,
    });
    return new FormGroup<InscriptionFormGroupContent>({
      id: new FormControl(
        { value: inscriptionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      dateInscription: new FormControl(inscriptionRawValue.dateInscription),
      regime: new FormControl(inscriptionRawValue.regime),
      session: new FormControl(inscriptionRawValue.session),
    });
  }

  getInscription(form: InscriptionFormGroup): IInscription | NewInscription {
    return this.convertInscriptionRawValueToInscription(form.getRawValue() as InscriptionFormRawValue | NewInscriptionFormRawValue);
  }

  resetForm(form: InscriptionFormGroup, inscription: InscriptionFormGroupInput): void {
    const inscriptionRawValue = this.convertInscriptionToInscriptionRawValue({ ...this.getFormDefaults(), ...inscription });
    form.reset(
      {
        ...inscriptionRawValue,
        id: { value: inscriptionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): InscriptionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateInscription: currentTime,
      regime: false,
    };
  }

  private convertInscriptionRawValueToInscription(
    rawInscription: InscriptionFormRawValue | NewInscriptionFormRawValue
  ): IInscription | NewInscription {
    return {
      ...rawInscription,
      dateInscription: dayjs(rawInscription.dateInscription, DATE_TIME_FORMAT),
    };
  }

  private convertInscriptionToInscriptionRawValue(
    inscription: IInscription | (Partial<NewInscription> & InscriptionFormDefaults)
  ): InscriptionFormRawValue | PartialWithRequiredKeyOf<NewInscriptionFormRawValue> {
    return {
      ...inscription,
      dateInscription: inscription.dateInscription ? inscription.dateInscription.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
