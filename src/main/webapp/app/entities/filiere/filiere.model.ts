import { IDepartement } from 'app/entities/departement/departement.model';

export interface IFiliere {
  id: number;
  libelle?: string | null;
  departement?: Pick<IDepartement, 'id'> | null;
}

export type NewFiliere = Omit<IFiliere, 'id'> & { id: null };
