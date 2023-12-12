import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IAcceptation } from '../acceptation.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../acceptation.test-samples';

import { AcceptationService, RestAcceptation } from './acceptation.service';

const requireRestSample: RestAcceptation = {
  ...sampleWithRequiredData,
  dateAcceptation: sampleWithRequiredData.dateAcceptation?.format(DATE_FORMAT),
};

describe('Acceptation Service', () => {
  let service: AcceptationService;
  let httpMock: HttpTestingController;
  let expectedResult: IAcceptation | IAcceptation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AcceptationService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Acceptation', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const acceptation = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(acceptation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Acceptation', () => {
      const acceptation = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(acceptation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Acceptation', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Acceptation', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Acceptation', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAcceptationToCollectionIfMissing', () => {
      it('should add a Acceptation to an empty array', () => {
        const acceptation: IAcceptation = sampleWithRequiredData;
        expectedResult = service.addAcceptationToCollectionIfMissing([], acceptation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(acceptation);
      });

      it('should not add a Acceptation to an array that contains it', () => {
        const acceptation: IAcceptation = sampleWithRequiredData;
        const acceptationCollection: IAcceptation[] = [
          {
            ...acceptation,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAcceptationToCollectionIfMissing(acceptationCollection, acceptation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Acceptation to an array that doesn't contain it", () => {
        const acceptation: IAcceptation = sampleWithRequiredData;
        const acceptationCollection: IAcceptation[] = [sampleWithPartialData];
        expectedResult = service.addAcceptationToCollectionIfMissing(acceptationCollection, acceptation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(acceptation);
      });

      it('should add only unique Acceptation to an array', () => {
        const acceptationArray: IAcceptation[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const acceptationCollection: IAcceptation[] = [sampleWithRequiredData];
        expectedResult = service.addAcceptationToCollectionIfMissing(acceptationCollection, ...acceptationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const acceptation: IAcceptation = sampleWithRequiredData;
        const acceptation2: IAcceptation = sampleWithPartialData;
        expectedResult = service.addAcceptationToCollectionIfMissing([], acceptation, acceptation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(acceptation);
        expect(expectedResult).toContain(acceptation2);
      });

      it('should accept null and undefined values', () => {
        const acceptation: IAcceptation = sampleWithRequiredData;
        expectedResult = service.addAcceptationToCollectionIfMissing([], null, acceptation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(acceptation);
      });

      it('should return initial array if no Acceptation is added', () => {
        const acceptationCollection: IAcceptation[] = [sampleWithRequiredData];
        expectedResult = service.addAcceptationToCollectionIfMissing(acceptationCollection, undefined, null);
        expect(expectedResult).toEqual(acceptationCollection);
      });
    });

    describe('compareAcceptation', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAcceptation(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAcceptation(entity1, entity2);
        const compareResult2 = service.compareAcceptation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAcceptation(entity1, entity2);
        const compareResult2 = service.compareAcceptation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAcceptation(entity1, entity2);
        const compareResult2 = service.compareAcceptation(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
