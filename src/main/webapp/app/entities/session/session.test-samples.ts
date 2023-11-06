import dayjs from 'dayjs/esm';

import { ISession, NewSession } from './session.model';

export const sampleWithRequiredData: ISession = {
  id: 24252,
};

export const sampleWithPartialData: ISession = {
  id: 75391,
  intitule: 'magenta Infrastructure',
};

export const sampleWithFullData: ISession = {
  id: 41516,
  intitule: 'gold Creative Islands',
  dateDebut: dayjs('2023-11-05T10:31'),
  dateFin: dayjs('2023-11-05T02:03'),
};

export const sampleWithNewData: NewSession = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
