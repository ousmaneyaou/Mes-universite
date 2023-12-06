import dayjs from 'dayjs/esm';
import { ISession } from 'app/entities/session/session.model';
import { EnumSexe } from 'app/entities/enumerations/enum-sexe.model';

export interface IDepot {

  // ... autres propriétés
  disabledChoix1?: boolean | undefined;
  disabledChoix2?: boolean | undefined;
  disabledChoix3?: boolean | null | undefined; 
  id: number;
  nom?: string | null;
  prenom?: string | null;
  dateNaissance?: dayjs.Dayjs | null;
  lieuNaissance?: string | null;
  email?: string | null;
  nationalite?: string | null;
  telephone?: string | null;
  sexe?: EnumSexe | null;
  dateDepot?: dayjs.Dayjs | null;
  numeroDeTable?: string | null;
  serie?: string | null;
  diplome?: string | null;
  diplomeContentType?: string | null;
  releveDeNote?: string | null;
  releveDeNoteContentType?: string | null;
  anneeObtention?: string | null;
  lieuObtention?: string | null;
  mention?: string | null;
  lettreDeMotivation?: string | null;
  lettreDeMotivationContentType?: string | null;
  choix1?: boolean | null; // Mettez à jour le type ici
  choix2?: boolean | null;
  choix3?: boolean | null; 
  resultat?: string | null;
  photo?: string | null;
  photoContentType?: string | null;
  session?: Pick<ISession, 'id'> | null;
}

export type NewDepot = Omit<IDepot, 'id'> & { id: null };
