import dayjs from 'dayjs/esm';

export interface ICampagne {
  id: number;
  intitule?: string | null;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
}

export type NewCampagne = Omit<ICampagne, 'id'> & { id: null };
