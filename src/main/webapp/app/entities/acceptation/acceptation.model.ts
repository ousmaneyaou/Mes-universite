import dayjs from 'dayjs/esm';
import { IInscription } from 'app/entities/inscription/inscription.model';
import { IDepot } from 'app/entities/depot/depot.model';
import { IUser } from 'app/entities/user/user.model';

export interface IAcceptation {
  id: number;
  filiere?: string | null;
  dateAcceptation?: dayjs.Dayjs | null;
  inscription?: Pick<IInscription, 'id'> | null;
  depot?: Pick<IDepot, 'id'> | null;
  user?: Pick<IUser, 'id'> | null;
}

export type NewAcceptation = Omit<IAcceptation, 'id'> & { id: null };
