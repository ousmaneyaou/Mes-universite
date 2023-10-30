import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DossierFormService } from './dossier-form.service';
import { DossierService } from '../service/dossier.service';
import { IDossier } from '../dossier.model';
import { INiveau } from 'app/entities/niveau/niveau.model';
import { NiveauService } from 'app/entities/niveau/service/niveau.service';
import { ICampagne } from 'app/entities/campagne/campagne.model';
import { CampagneService } from 'app/entities/campagne/service/campagne.service';

import { DossierUpdateComponent } from './dossier-update.component';

describe('Dossier Management Update Component', () => {
  let comp: DossierUpdateComponent;
  let fixture: ComponentFixture<DossierUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let dossierFormService: DossierFormService;
  let dossierService: DossierService;
  let niveauService: NiveauService;
  let campagneService: CampagneService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DossierUpdateComponent],
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
      .overrideTemplate(DossierUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DossierUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    dossierFormService = TestBed.inject(DossierFormService);
    dossierService = TestBed.inject(DossierService);
    niveauService = TestBed.inject(NiveauService);
    campagneService = TestBed.inject(CampagneService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Niveau query and add missing value', () => {
      const dossier: IDossier = { id: 456 };
      const niveau: INiveau = { id: 87671 };
      dossier.niveau = niveau;

      const niveauCollection: INiveau[] = [{ id: 46705 }];
      jest.spyOn(niveauService, 'query').mockReturnValue(of(new HttpResponse({ body: niveauCollection })));
      const additionalNiveaus = [niveau];
      const expectedCollection: INiveau[] = [...additionalNiveaus, ...niveauCollection];
      jest.spyOn(niveauService, 'addNiveauToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ dossier });
      comp.ngOnInit();

      expect(niveauService.query).toHaveBeenCalled();
      expect(niveauService.addNiveauToCollectionIfMissing).toHaveBeenCalledWith(
        niveauCollection,
        ...additionalNiveaus.map(expect.objectContaining)
      );
      expect(comp.niveausSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Campagne query and add missing value', () => {
      const dossier: IDossier = { id: 456 };
      const campagne: ICampagne = { id: 93624 };
      dossier.campagne = campagne;

      const campagneCollection: ICampagne[] = [{ id: 68737 }];
      jest.spyOn(campagneService, 'query').mockReturnValue(of(new HttpResponse({ body: campagneCollection })));
      const additionalCampagnes = [campagne];
      const expectedCollection: ICampagne[] = [...additionalCampagnes, ...campagneCollection];
      jest.spyOn(campagneService, 'addCampagneToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ dossier });
      comp.ngOnInit();

      expect(campagneService.query).toHaveBeenCalled();
      expect(campagneService.addCampagneToCollectionIfMissing).toHaveBeenCalledWith(
        campagneCollection,
        ...additionalCampagnes.map(expect.objectContaining)
      );
      expect(comp.campagnesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const dossier: IDossier = { id: 456 };
      const niveau: INiveau = { id: 26621 };
      dossier.niveau = niveau;
      const campagne: ICampagne = { id: 5110 };
      dossier.campagne = campagne;

      activatedRoute.data = of({ dossier });
      comp.ngOnInit();

      expect(comp.niveausSharedCollection).toContain(niveau);
      expect(comp.campagnesSharedCollection).toContain(campagne);
      expect(comp.dossier).toEqual(dossier);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDossier>>();
      const dossier = { id: 123 };
      jest.spyOn(dossierFormService, 'getDossier').mockReturnValue(dossier);
      jest.spyOn(dossierService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dossier });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dossier }));
      saveSubject.complete();

      // THEN
      expect(dossierFormService.getDossier).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(dossierService.update).toHaveBeenCalledWith(expect.objectContaining(dossier));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDossier>>();
      const dossier = { id: 123 };
      jest.spyOn(dossierFormService, 'getDossier').mockReturnValue({ id: null });
      jest.spyOn(dossierService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dossier: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dossier }));
      saveSubject.complete();

      // THEN
      expect(dossierFormService.getDossier).toHaveBeenCalled();
      expect(dossierService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDossier>>();
      const dossier = { id: 123 };
      jest.spyOn(dossierService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dossier });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(dossierService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareNiveau', () => {
      it('Should forward to niveauService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(niveauService, 'compareNiveau');
        comp.compareNiveau(entity, entity2);
        expect(niveauService.compareNiveau).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCampagne', () => {
      it('Should forward to campagneService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(campagneService, 'compareCampagne');
        comp.compareCampagne(entity, entity2);
        expect(campagneService.compareCampagne).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
