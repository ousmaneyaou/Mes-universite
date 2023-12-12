import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAcceptation, NewAcceptation } from '../acceptation.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAcceptation for edit and NewAcceptationFormGroupInput for create.
 */
type AcceptationFormGroupInput = IAcceptation | PartialWithRequiredKeyOf<NewAcceptation>;

type AcceptationFormDefaults = Pick<NewAcceptation, 'id'>;

type AcceptationFormGroupContent = {
  id: FormControl<IAcceptation['id'] | NewAcceptation['id']>;
  filiere: FormControl<IAcceptation['filiere']>;
  dateAcceptation: FormControl<IAcceptation['dateAcceptation']>;
  inscription: FormControl<IAcceptation['inscription']>;
  depot: FormControl<IAcceptation['depot']>;
  user: FormControl<IAcceptation['user']>;
};

export type AcceptationFormGroup = FormGroup<AcceptationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AcceptationFormService {
  createAcceptationFormGroup(acceptation: AcceptationFormGroupInput = { id: null }): AcceptationFormGroup {
    const acceptationRawValue = {
      ...this.getFormDefaults(),
      ...acceptation,
    };
    return new FormGroup<AcceptationFormGroupContent>({
      id: new FormControl(
        { value: acceptationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      filiere: new FormControl(acceptationRawValue.filiere),
      dateAcceptation: new FormControl(acceptationRawValue.dateAcceptation),
      inscription: new FormControl(acceptationRawValue.inscription),
      depot: new FormControl(acceptationRawValue.depot),
      user: new FormControl(acceptationRawValue.user),
    });
  }

  getAcceptation(form: AcceptationFormGroup): IAcceptation | NewAcceptation {
    return form.getRawValue() as IAcceptation | NewAcceptation;
  }

  resetForm(form: AcceptationFormGroup, acceptation: AcceptationFormGroupInput): void {
    const acceptationRawValue = { ...this.getFormDefaults(), ...acceptation };
    form.reset(
      {
        ...acceptationRawValue,
        id: { value: acceptationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AcceptationFormDefaults {
    return {
      id: null,
    };
  }
}
