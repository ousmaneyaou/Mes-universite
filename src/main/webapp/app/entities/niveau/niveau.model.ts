import { IDepartement } from 'app/entities/departement/departement.model';

export interface INiveau {
  id: number;
  libelle?: string | null;
  departement?: Pick<IDepartement, 'id'> | null;
}

export type NewNiveau = Omit<INiveau, 'id'> & { id: null };
