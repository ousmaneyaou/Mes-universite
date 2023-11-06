import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { NiveauFormService } from './niveau-form.service';
import { NiveauService } from '../service/niveau.service';
import { INiveau } from '../niveau.model';
import { IDepartement } from 'app/entities/departement/departement.model';
import { DepartementService } from 'app/entities/departement/service/departement.service';
import { ISession } from 'app/entities/session/session.model';
import { SessionService } from 'app/entities/session/service/session.service';

import { NiveauUpdateComponent } from './niveau-update.component';

describe('Niveau Management Update Component', () => {
  let comp: NiveauUpdateComponent;
  let fixture: ComponentFixture<NiveauUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let niveauFormService: NiveauFormService;
  let niveauService: NiveauService;
  let departementService: DepartementService;
  let sessionService: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [NiveauUpdateComponent],
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
      .overrideTemplate(NiveauUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NiveauUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    niveauFormService = TestBed.inject(NiveauFormService);
    niveauService = TestBed.inject(NiveauService);
    departementService = TestBed.inject(DepartementService);
    sessionService = TestBed.inject(SessionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Departement query and add missing value', () => {
      const niveau: INiveau = { id: 456 };
      const departement: IDepartement = { id: 88324 };
      niveau.departement = departement;

      const departementCollection: IDepartement[] = [{ id: 65482 }];
      jest.spyOn(departementService, 'query').mockReturnValue(of(new HttpResponse({ body: departementCollection })));
      const additionalDepartements = [departement];
      const expectedCollection: IDepartement[] = [...additionalDepartements, ...departementCollection];
      jest.spyOn(departementService, 'addDepartementToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ niveau });
      comp.ngOnInit();

      expect(departementService.query).toHaveBeenCalled();
      expect(departementService.addDepartementToCollectionIfMissing).toHaveBeenCalledWith(
        departementCollection,
        ...additionalDepartements.map(expect.objectContaining)
      );
      expect(comp.departementsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Session query and add missing value', () => {
      const niveau: INiveau = { id: 456 };
      const session: ISession = { id: 9500 };
      niveau.session = session;

      const sessionCollection: ISession[] = [{ id: 36149 }];
      jest.spyOn(sessionService, 'query').mockReturnValue(of(new HttpResponse({ body: sessionCollection })));
      const additionalSessions = [session];
      const expectedCollection: ISession[] = [...additionalSessions, ...sessionCollection];
      jest.spyOn(sessionService, 'addSessionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ niveau });
      comp.ngOnInit();

      expect(sessionService.query).toHaveBeenCalled();
      expect(sessionService.addSessionToCollectionIfMissing).toHaveBeenCalledWith(
        sessionCollection,
        ...additionalSessions.map(expect.objectContaining)
      );
      expect(comp.sessionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const niveau: INiveau = { id: 456 };
      const departement: IDepartement = { id: 42549 };
      niveau.departement = departement;
      const session: ISession = { id: 96304 };
      niveau.session = session;

      activatedRoute.data = of({ niveau });
      comp.ngOnInit();

      expect(comp.departementsSharedCollection).toContain(departement);
      expect(comp.sessionsSharedCollection).toContain(session);
      expect(comp.niveau).toEqual(niveau);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INiveau>>();
      const niveau = { id: 123 };
      jest.spyOn(niveauFormService, 'getNiveau').mockReturnValue(niveau);
      jest.spyOn(niveauService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ niveau });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: niveau }));
      saveSubject.complete();

      // THEN
      expect(niveauFormService.getNiveau).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(niveauService.update).toHaveBeenCalledWith(expect.objectContaining(niveau));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INiveau>>();
      const niveau = { id: 123 };
      jest.spyOn(niveauFormService, 'getNiveau').mockReturnValue({ id: null });
      jest.spyOn(niveauService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ niveau: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: niveau }));
      saveSubject.complete();

      // THEN
      expect(niveauFormService.getNiveau).toHaveBeenCalled();
      expect(niveauService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INiveau>>();
      const niveau = { id: 123 };
      jest.spyOn(niveauService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ niveau });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(niveauService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDepartement', () => {
      it('Should forward to departementService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(departementService, 'compareDepartement');
        comp.compareDepartement(entity, entity2);
        expect(departementService.compareDepartement).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareSession', () => {
      it('Should forward to sessionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(sessionService, 'compareSession');
        comp.compareSession(entity, entity2);
        expect(sessionService.compareSession).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
