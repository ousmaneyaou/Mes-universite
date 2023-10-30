import dayjs from 'dayjs/esm';

export interface IPaiement {
  id: number;
  datePaie?: dayjs.Dayjs | null;
  etat?: boolean | null;
}

export type NewPaiement = Omit<IPaiement, 'id'> & { id: null };
