import dayjs from 'dayjs/esm';

export interface ISession {
  id: number;
  intitule?: string | null;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
}

export type NewSession = Omit<ISession, 'id'> & { id: null };
