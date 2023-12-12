import dayjs from 'dayjs/esm';

import { IAcceptation, NewAcceptation } from './acceptation.model';

export const sampleWithRequiredData: IAcceptation = {
  id: 56387,
};

export const sampleWithPartialData: IAcceptation = {
  id: 42851,
  dateAcceptation: dayjs('2023-12-10'),
};

export const sampleWithFullData: IAcceptation = {
  id: 22559,
  filiere: 'tan',
  dateAcceptation: dayjs('2023-12-09'),
};

export const sampleWithNewData: NewAcceptation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
