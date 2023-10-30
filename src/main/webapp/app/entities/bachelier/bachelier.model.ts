import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { EnumSexe } from 'app/entities/enumerations/enum-sexe.model';

export interface IBachelier {
  id: number;
  sexe?: EnumSexe | null;
  dateNaissance?: dayjs.Dayjs | null;
  lieuNaissance?: string | null;
  nationalite?: string | null;
  telephone?: string | null;
  utilisateur?: Pick<IUser, 'id'> | null;
}

export type NewBachelier = Omit<IBachelier, 'id'> & { id: null };
