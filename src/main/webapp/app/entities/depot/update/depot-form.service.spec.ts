import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../depot.test-samples';

import { DepotFormService } from './depot-form.service';

describe('Depot Form Service', () => {
  let service: DepotFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DepotFormService);
  });

  describe('Service methods', () => {
    describe('createDepotFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDepotFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            prenom: expect.any(Object),
            dateNaissance: expect.any(Object),
            lieuNaissance: expect.any(Object),
            email: expect.any(Object),
            nationalite: expect.any(Object),
            telephone: expect.any(Object),
            sexe: expect.any(Object),
            dateDepot: expect.any(Object),
            numeroDeTable: expect.any(Object),
            serie: expect.any(Object),
            diplome: expect.any(Object),
            releveDeNote: expect.any(Object),
            anneeObtention: expect.any(Object),
            lieuObtention: expect.any(Object),
            mention: expect.any(Object),
            lettreDeMotivation: expect.any(Object),
            choix1: expect.any(Object),
            choix2: expect.any(Object),
            choix3: expect.any(Object),
            photo: expect.any(Object),
            session: expect.any(Object),
          })
        );
      });

      it('passing IDepot should create a new form with FormGroup', () => {
        const formGroup = service.createDepotFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            prenom: expect.any(Object),
            dateNaissance: expect.any(Object),
            lieuNaissance: expect.any(Object),
            email: expect.any(Object),
            nationalite: expect.any(Object),
            telephone: expect.any(Object),
            sexe: expect.any(Object),
            dateDepot: expect.any(Object),
            numeroDeTable: expect.any(Object),
            serie: expect.any(Object),
            diplome: expect.any(Object),
            releveDeNote: expect.any(Object),
            anneeObtention: expect.any(Object),
            lieuObtention: expect.any(Object),
            mention: expect.any(Object),
            lettreDeMotivation: expect.any(Object),
            choix1: expect.any(Object),
            choix2: expect.any(Object),
            choix3: expect.any(Object),
            photo: expect.any(Object),
            session: expect.any(Object),
          })
        );
      });
    });

    describe('getDepot', () => {
      it('should return NewDepot for default Depot initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createDepotFormGroup(sampleWithNewData);

        const depot = service.getDepot(formGroup) as any;

        expect(depot).toMatchObject(sampleWithNewData);
      });

      it('should return NewDepot for empty Depot initial value', () => {
        const formGroup = service.createDepotFormGroup();

        const depot = service.getDepot(formGroup) as any;

        expect(depot).toMatchObject({});
      });

      it('should return IDepot', () => {
        const formGroup = service.createDepotFormGroup(sampleWithRequiredData);

        const depot = service.getDepot(formGroup) as any;

        expect(depot).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDepot should not enable id FormControl', () => {
        const formGroup = service.createDepotFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDepot should disable id FormControl', () => {
        const formGroup = service.createDepotFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
