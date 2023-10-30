import { ICampagne } from 'app/entities/campagne/campagne.model';

export interface IAnneeScolaire {
  id: number;
  libelle?: string | null;
  enCour?: boolean | null;
  campagne?: Pick<ICampagne, 'id'> | null;
}

export type NewAnneeScolaire = Omit<IAnneeScolaire, 'id'> & { id: null };
