import { IDepartement } from 'app/entities/departement/departement.model';
import { ISession } from 'app/entities/session/session.model';

export interface INiveau {
  id: number;
  libelle?: string | null;
  departement?: Pick<IDepartement, 'id'> | null;
  session?: Pick<ISession, 'id'> | null;
}

export type NewNiveau = Omit<INiveau, 'id'> & { id: null };
