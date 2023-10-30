import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DepotFormService } from './depot-form.service';
import { DepotService } from '../service/depot.service';
import { IDepot } from '../depot.model';
import { IBachelier } from 'app/entities/bachelier/bachelier.model';
import { BachelierService } from 'app/entities/bachelier/service/bachelier.service';
import { IDossier } from 'app/entities/dossier/dossier.model';
import { DossierService } from 'app/entities/dossier/service/dossier.service';

import { DepotUpdateComponent } from './depot-update.component';

describe('Depot Management Update Component', () => {
  let comp: DepotUpdateComponent;
  let fixture: ComponentFixture<DepotUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let depotFormService: DepotFormService;
  let depotService: DepotService;
  let bachelierService: BachelierService;
  let dossierService: DossierService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DepotUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(DepotUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DepotUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    depotFormService = TestBed.inject(DepotFormService);
    depotService = TestBed.inject(DepotService);
    bachelierService = TestBed.inject(BachelierService);
    dossierService = TestBed.inject(DossierService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Bachelier query and add missing value', () => {
      const depot: IDepot = { id: 456 };
      const bachelier: IBachelier = { id: 17856 };
      depot.bachelier = bachelier;

      const bachelierCollection: IBachelier[] = [{ id: 89481 }];
      jest.spyOn(bachelierService, 'query').mockReturnValue(of(new HttpResponse({ body: bachelierCollection })));
      const additionalBacheliers = [bachelier];
      const expectedCollection: IBachelier[] = [...additionalBacheliers, ...bachelierCollection];
      jest.spyOn(bachelierService, 'addBachelierToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ depot });
      comp.ngOnInit();

      expect(bachelierService.query).toHaveBeenCalled();
      expect(bachelierService.addBachelierToCollectionIfMissing).toHaveBeenCalledWith(
        bachelierCollection,
        ...additionalBacheliers.map(expect.objectContaining)
      );
      expect(comp.bacheliersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Dossier query and add missing value', () => {
      const depot: IDepot = { id: 456 };
      const dossier: IDossier = { id: 72623 };
      depot.dossier = dossier;

      const dossierCollection: IDossier[] = [{ id: 25089 }];
      jest.spyOn(dossierService, 'query').mockReturnValue(of(new HttpResponse({ body: dossierCollection })));
      const additionalDossiers = [dossier];
      const expectedCollection: IDossier[] = [...additionalDossiers, ...dossierCollection];
      jest.spyOn(dossierService, 'addDossierToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ depot });
      comp.ngOnInit();

      expect(dossierService.query).toHaveBeenCalled();
      expect(dossierService.addDossierToCollectionIfMissing).toHaveBeenCalledWith(
        dossierCollection,
        ...additionalDossiers.map(expect.objectContaining)
      );
      expect(comp.dossiersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const depot: IDepot = { id: 456 };
      const bachelier: IBachelier = { id: 77306 };
      depot.bachelier = bachelier;
      const dossier: IDossier = { id: 29248 };
      depot.dossier = dossier;

      activatedRoute.data = of({ depot });
      comp.ngOnInit();

      expect(comp.bacheliersSharedCollection).toContain(bachelier);
      expect(comp.dossiersSharedCollection).toContain(dossier);
      expect(comp.depot).toEqual(depot);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDepot>>();
      const depot = { id: 123 };
      jest.spyOn(depotFormService, 'getDepot').mockReturnValue(depot);
      jest.spyOn(depotService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ depot });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: depot }));
      saveSubject.complete();

      // THEN
      expect(depotFormService.getDepot).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(depotService.update).toHaveBeenCalledWith(expect.objectContaining(depot));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDepot>>();
      const depot = { id: 123 };
      jest.spyOn(depotFormService, 'getDepot').mockReturnValue({ id: null });
      jest.spyOn(depotService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ depot: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: depot }));
      saveSubject.complete();

      // THEN
      expect(depotFormService.getDepot).toHaveBeenCalled();
      expect(depotService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDepot>>();
      const depot = { id: 123 };
      jest.spyOn(depotService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ depot });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(depotService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareBachelier', () => {
      it('Should forward to bachelierService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(bachelierService, 'compareBachelier');
        comp.compareBachelier(entity, entity2);
        expect(bachelierService.compareBachelier).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDossier', () => {
      it('Should forward to dossierService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(dossierService, 'compareDossier');
        comp.compareDossier(entity, entity2);
        expect(dossierService.compareDossier).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
