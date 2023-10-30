import { INiveau } from 'app/entities/niveau/niveau.model';
import { ICampagne } from 'app/entities/campagne/campagne.model';
import { IInscription } from 'app/entities/inscription/inscription.model';

export interface IDossier {
  id: number;
  valider?: boolean | null;
  niveau?: Pick<INiveau, 'id'> | null;
  campagne?: Pick<ICampagne, 'id'> | null;
  inscriptions?: Pick<IInscription, 'id'>[] | null;
}

export type NewDossier = Omit<IDossier, 'id'> & { id: null };
