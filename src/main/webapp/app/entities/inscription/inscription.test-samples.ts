import dayjs from 'dayjs/esm';

import { IInscription, NewInscription } from './inscription.model';

export const sampleWithRequiredData: IInscription = {
  id: 45715,
};

export const sampleWithPartialData: IInscription = {
  id: 4846,
  dateInscription: dayjs('2023-09-05T07:58'),
  regime: false,
  anneeAcademique: 'plum Dynamic architecture',
  niveau: 'Movies',
  observation: 'Iranian',
};

export const sampleWithFullData: IInscription = {
  id: 92320,
  dateInscription: dayjs('2023-09-05T18:27'),
  regime: true,
  anneeAcademique: 'invoice',
  montantInscription: 59336,
  niveau: 'Executive Grenadines',
  observation: 'Mobility Reverse-engineered',
};

export const sampleWithNewData: NewInscription = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
