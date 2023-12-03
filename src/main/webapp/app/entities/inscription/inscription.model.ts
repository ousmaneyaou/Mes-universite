import dayjs from 'dayjs/esm';
import { ISession } from 'app/entities/session/session.model';

export interface IInscription {
  id: number;
  dateInscription?: dayjs.Dayjs | null;
  regime?: boolean | null;
  anneeAcademique?: string | null;
  montantInscription?: number | null;
  niveau?: string | null;
  observation?: string | null;
  session?: Pick<ISession, 'id'> | null;
}

export type NewInscription = Omit<IInscription, 'id'> & { id: null };
