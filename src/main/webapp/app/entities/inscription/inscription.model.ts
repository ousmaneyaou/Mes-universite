import dayjs from 'dayjs/esm';
import { IPaiement } from 'app/entities/paiement/paiement.model';
import { IDossier } from 'app/entities/dossier/dossier.model';

export interface IInscription {
  id: number;
  dateInscription?: dayjs.Dayjs | null;
  regime?: boolean | null;
  paiement?: Pick<IPaiement, 'id'> | null;
  dossiers?: Pick<IDossier, 'id'>[] | null;
}

export type NewInscription = Omit<IInscription, 'id'> & { id: null };
