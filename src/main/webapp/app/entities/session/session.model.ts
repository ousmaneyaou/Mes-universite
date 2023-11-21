import dayjs from 'dayjs/esm';
import { INiveau } from 'app/entities/niveau/niveau.model';

export interface ISession {
  id: number;
  intitule?: string | null;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  niveau?: Pick<INiveau, 'id'> | null;
}

export type NewSession = Omit<ISession, 'id'> & { id: null };
