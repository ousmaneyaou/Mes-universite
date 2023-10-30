import dayjs from 'dayjs/esm';

import { EnumSexe } from 'app/entities/enumerations/enum-sexe.model';

import { IBachelier, NewBachelier } from './bachelier.model';

export const sampleWithRequiredData: IBachelier = {
  id: 93464,
};

export const sampleWithPartialData: IBachelier = {
  id: 71050,
  sexe: EnumSexe['FEMININ'],
  dateNaissance: dayjs('2023-09-05T14:22'),
  lieuNaissance: 'Operations',
  nationalite: 'Ferry',
};

export const sampleWithFullData: IBachelier = {
  id: 12807,
  sexe: EnumSexe['MASCULIN'],
  dateNaissance: dayjs('2023-09-05T20:03'),
  lieuNaissance: 'viral haptic invoice',
  nationalite: 'Wooden methodologies disintermediate',
  telephone: '483.363.6810',
};

export const sampleWithNewData: NewBachelier = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
