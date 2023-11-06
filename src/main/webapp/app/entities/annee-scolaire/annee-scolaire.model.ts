import { ISession } from 'app/entities/session/session.model';

export interface IAnneeScolaire {
  id: number;
  libelle?: string | null;
  enCours?: boolean | null;
  session?: Pick<ISession, 'id'> | null;
}

export type NewAnneeScolaire = Omit<IAnneeScolaire, 'id'> & { id: null };
