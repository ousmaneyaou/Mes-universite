import { IFiliere, NewFiliere } from './filiere.model';

export const sampleWithRequiredData: IFiliere = {
  id: 65746,
};

export const sampleWithPartialData: IFiliere = {
  id: 18405,
  libelle: 'redundant Flat',
};

export const sampleWithFullData: IFiliere = {
  id: 58831,
  libelle: 'multi-byte',
};

export const sampleWithNewData: NewFiliere = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
