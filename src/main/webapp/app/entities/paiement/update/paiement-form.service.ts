import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPaiement, NewPaiement } from '../paiement.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPaiement for edit and NewPaiementFormGroupInput for create.
 */
type PaiementFormGroupInput = IPaiement | PartialWithRequiredKeyOf<NewPaiement>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPaiement | NewPaiement> = Omit<T, 'datePaie'> & {
  datePaie?: string | null;
};

type PaiementFormRawValue = FormValueOf<IPaiement>;

type NewPaiementFormRawValue = FormValueOf<NewPaiement>;

type PaiementFormDefaults = Pick<NewPaiement, 'id' | 'datePaie' | 'etat'>;

type PaiementFormGroupContent = {
  id: FormControl<PaiementFormRawValue['id'] | NewPaiement['id']>;
  datePaie: FormControl<PaiementFormRawValue['datePaie']>;
  etat: FormControl<PaiementFormRawValue['etat']>;
  montant: FormControl<PaiementFormRawValue['montant']>;
  inscription: FormControl<PaiementFormRawValue['inscription']>;
};

export type PaiementFormGroup = FormGroup<PaiementFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PaiementFormService {
  createPaiementFormGroup(paiement: PaiementFormGroupInput = { id: null }): PaiementFormGroup {
    const paiementRawValue = this.convertPaiementToPaiementRawValue({
      ...this.getFormDefaults(),
      ...paiement,
    });
    return new FormGroup<PaiementFormGroupContent>({
      id: new FormControl(
        { value: paiementRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      datePaie: new FormControl(paiementRawValue.datePaie),
      etat: new FormControl(paiementRawValue.etat),
      montant: new FormControl(paiementRawValue.montant),
      inscription: new FormControl(paiementRawValue.inscription),
    });
  }

  getPaiement(form: PaiementFormGroup): IPaiement | NewPaiement {
    return this.convertPaiementRawValueToPaiement(form.getRawValue() as PaiementFormRawValue | NewPaiementFormRawValue);
  }

  resetForm(form: PaiementFormGroup, paiement: PaiementFormGroupInput): void {
    const paiementRawValue = this.convertPaiementToPaiementRawValue({ ...this.getFormDefaults(), ...paiement });
    form.reset(
      {
        ...paiementRawValue,
        id: { value: paiementRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PaiementFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      datePaie: currentTime,
      etat: false,
    };
  }

  private convertPaiementRawValueToPaiement(rawPaiement: PaiementFormRawValue | NewPaiementFormRawValue): IPaiement | NewPaiement {
    return {
      ...rawPaiement,
      datePaie: dayjs(rawPaiement.datePaie, DATE_TIME_FORMAT),
    };
  }

  private convertPaiementToPaiementRawValue(
    paiement: IPaiement | (Partial<NewPaiement> & PaiementFormDefaults)
  ): PaiementFormRawValue | PartialWithRequiredKeyOf<NewPaiementFormRawValue> {
    return {
      ...paiement,
      datePaie: paiement.datePaie ? paiement.datePaie.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
