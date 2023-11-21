import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISession, NewSession } from '../session.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISession for edit and NewSessionFormGroupInput for create.
 */
type SessionFormGroupInput = ISession | PartialWithRequiredKeyOf<NewSession>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISession | NewSession> = Omit<T, 'dateDebut' | 'dateFin'> & {
  dateDebut?: string | null;
  dateFin?: string | null;
};

type SessionFormRawValue = FormValueOf<ISession>;

type NewSessionFormRawValue = FormValueOf<NewSession>;

type SessionFormDefaults = Pick<NewSession, 'id' | 'dateDebut' | 'dateFin'>;

type SessionFormGroupContent = {
  id: FormControl<SessionFormRawValue['id'] | NewSession['id']>;
  intitule: FormControl<SessionFormRawValue['intitule']>;
  dateDebut: FormControl<SessionFormRawValue['dateDebut']>;
  dateFin: FormControl<SessionFormRawValue['dateFin']>;
  niveau: FormControl<SessionFormRawValue['niveau']>;
};

export type SessionFormGroup = FormGroup<SessionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SessionFormService {
  createSessionFormGroup(session: SessionFormGroupInput = { id: null }): SessionFormGroup {
    const sessionRawValue = this.convertSessionToSessionRawValue({
      ...this.getFormDefaults(),
      ...session,
    });
    return new FormGroup<SessionFormGroupContent>({
      id: new FormControl(
        { value: sessionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      intitule: new FormControl(sessionRawValue.intitule),
      dateDebut: new FormControl(sessionRawValue.dateDebut),
      dateFin: new FormControl(sessionRawValue.dateFin),
      niveau: new FormControl(sessionRawValue.niveau),
    });
  }

  getSession(form: SessionFormGroup): ISession | NewSession {
    return this.convertSessionRawValueToSession(form.getRawValue() as SessionFormRawValue | NewSessionFormRawValue);
  }

  resetForm(form: SessionFormGroup, session: SessionFormGroupInput): void {
    const sessionRawValue = this.convertSessionToSessionRawValue({ ...this.getFormDefaults(), ...session });
    form.reset(
      {
        ...sessionRawValue,
        id: { value: sessionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SessionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateDebut: currentTime,
      dateFin: currentTime,
    };
  }

  private convertSessionRawValueToSession(rawSession: SessionFormRawValue | NewSessionFormRawValue): ISession | NewSession {
    return {
      ...rawSession,
      dateDebut: dayjs(rawSession.dateDebut, DATE_TIME_FORMAT),
      dateFin: dayjs(rawSession.dateFin, DATE_TIME_FORMAT),
    };
  }

  private convertSessionToSessionRawValue(
    session: ISession | (Partial<NewSession> & SessionFormDefaults)
  ): SessionFormRawValue | PartialWithRequiredKeyOf<NewSessionFormRawValue> {
    return {
      ...session,
      dateDebut: session.dateDebut ? session.dateDebut.format(DATE_TIME_FORMAT) : undefined,
      dateFin: session.dateFin ? session.dateFin.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
