import dayjs from 'dayjs/esm';

import { EnumSexe } from 'app/entities/enumerations/enum-sexe.model';

import { IDepot, NewDepot } from './depot.model';

export const sampleWithRequiredData: IDepot = {
  id: 1234,
};

export const sampleWithPartialData: IDepot = {
  id: 64746,
  dateNaissance: dayjs('2023-09-05T15:18'),
  numeroDeTable: 'Designer',
  diplome: '../fake-data/blob/hipster.png',
  diplomeContentType: 'unknown',
  releveDeNote: '../fake-data/blob/hipster.png',
  releveDeNoteContentType: 'unknown',
  anneeObtention: 'orchestrate',
  lettreDeMotivation: '../fake-data/blob/hipster.png',
  lettreDeMotivationContentType: 'unknown',
  choix1: 'Solutions',
  choix2: 'extensible Islands applications',
  choix3: 'Games',
};

export const sampleWithFullData: IDepot = {
  id: 61872,
  nom: 'niches',
  prenom: 'Licensed Toys SDD',
  dateNaissance: dayjs('2023-09-05T09:10'),
  lieuDeNaissance: 'Islands',
  email: 'Tremayne89@gmail.com',
  nationalite: 'Stand-alone systems Gorgeous',
  telephone: '843.772.0465 x1906',
  sexe: EnumSexe['FEMININ'],
  dateDepot: dayjs('2023-09-06T00:39'),
  numeroDeTable: 'approach',
  serie: 'Switchable Tactics Persistent',
  diplome: '../fake-data/blob/hipster.png',
  diplomeContentType: 'unknown',
  releveDeNote: '../fake-data/blob/hipster.png',
  releveDeNoteContentType: 'unknown',
  anneeObtention: 'real-time Account redundant',
  lieuObtention: 'hacking turquoise',
  mention: 'Account North',
  lettreDeMotivation: '../fake-data/blob/hipster.png',
  lettreDeMotivationContentType: 'unknown',
  choix1: 'Awesome',
  choix2: 'Cross-platform',
  choix3: 'integrated',
  photo: '../fake-data/blob/hipster.png',
  photoContentType: 'unknown',
};

export const sampleWithNewData: NewDepot = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
