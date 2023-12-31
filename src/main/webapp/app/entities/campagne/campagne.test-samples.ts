import dayjs from 'dayjs/esm';

import { ICampagne, NewCampagne } from './campagne.model';

export const sampleWithRequiredData: ICampagne = {
  id: 96270,
};

export const sampleWithPartialData: ICampagne = {
  id: 97091,
};

export const sampleWithFullData: ICampagne = {
  id: 1158,
  intitule: 'Tasty Steel set',
  dateDebut: dayjs('2023-09-05T12:30'),
  dateFin: dayjs('2023-09-05T21:24'),
};

export const sampleWithNewData: NewCampagne = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
