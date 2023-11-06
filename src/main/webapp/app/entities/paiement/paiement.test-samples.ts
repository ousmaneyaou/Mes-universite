import dayjs from 'dayjs/esm';

import { IPaiement, NewPaiement } from './paiement.model';

export const sampleWithRequiredData: IPaiement = {
  id: 69996,
};

export const sampleWithPartialData: IPaiement = {
  id: 80929,
  datePaie: dayjs('2023-09-05T08:59'),
  etat: true,
};

export const sampleWithFullData: IPaiement = {
  id: 4235,
  datePaie: dayjs('2023-09-05T03:03'),
  etat: false,
  montant: 70780,
};

export const sampleWithNewData: NewPaiement = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
