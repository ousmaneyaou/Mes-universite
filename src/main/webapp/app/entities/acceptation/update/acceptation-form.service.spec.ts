import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../acceptation.test-samples';

import { AcceptationFormService } from './acceptation-form.service';

describe('Acceptation Form Service', () => {
  let service: AcceptationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AcceptationFormService);
  });

  describe('Service methods', () => {
    describe('createAcceptationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAcceptationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            filiere: expect.any(Object),
            dateAcceptation: expect.any(Object),
            inscription: expect.any(Object),
            depot: expect.any(Object),
            user: expect.any(Object),
          })
        );
      });

      it('passing IAcceptation should create a new form with FormGroup', () => {
        const formGroup = service.createAcceptationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            filiere: expect.any(Object),
            dateAcceptation: expect.any(Object),
            inscription: expect.any(Object),
            depot: expect.any(Object),
            user: expect.any(Object),
          })
        );
      });
    });

    describe('getAcceptation', () => {
      it('should return NewAcceptation for default Acceptation initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createAcceptationFormGroup(sampleWithNewData);

        const acceptation = service.getAcceptation(formGroup) as any;

        expect(acceptation).toMatchObject(sampleWithNewData);
      });

      it('should return NewAcceptation for empty Acceptation initial value', () => {
        const formGroup = service.createAcceptationFormGroup();

        const acceptation = service.getAcceptation(formGroup) as any;

        expect(acceptation).toMatchObject({});
      });

      it('should return IAcceptation', () => {
        const formGroup = service.createAcceptationFormGroup(sampleWithRequiredData);

        const acceptation = service.getAcceptation(formGroup) as any;

        expect(acceptation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAcceptation should not enable id FormControl', () => {
        const formGroup = service.createAcceptationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAcceptation should disable id FormControl', () => {
        const formGroup = service.createAcceptationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
