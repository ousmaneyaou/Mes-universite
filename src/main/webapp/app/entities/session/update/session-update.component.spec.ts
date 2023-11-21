import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SessionFormService } from './session-form.service';
import { SessionService } from '../service/session.service';
import { ISession } from '../session.model';
import { INiveau } from 'app/entities/niveau/niveau.model';
import { NiveauService } from 'app/entities/niveau/service/niveau.service';

import { SessionUpdateComponent } from './session-update.component';

describe('Session Management Update Component', () => {
  let comp: SessionUpdateComponent;
  let fixture: ComponentFixture<SessionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sessionFormService: SessionFormService;
  let sessionService: SessionService;
  let niveauService: NiveauService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SessionUpdateComponent],
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
      .overrideTemplate(SessionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SessionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sessionFormService = TestBed.inject(SessionFormService);
    sessionService = TestBed.inject(SessionService);
    niveauService = TestBed.inject(NiveauService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Niveau query and add missing value', () => {
      const session: ISession = { id: 456 };
      const niveau: INiveau = { id: 63440 };
      session.niveau = niveau;

      const niveauCollection: INiveau[] = [{ id: 81232 }];
      jest.spyOn(niveauService, 'query').mockReturnValue(of(new HttpResponse({ body: niveauCollection })));
      const additionalNiveaus = [niveau];
      const expectedCollection: INiveau[] = [...additionalNiveaus, ...niveauCollection];
      jest.spyOn(niveauService, 'addNiveauToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ session });
      comp.ngOnInit();

      expect(niveauService.query).toHaveBeenCalled();
      expect(niveauService.addNiveauToCollectionIfMissing).toHaveBeenCalledWith(
        niveauCollection,
        ...additionalNiveaus.map(expect.objectContaining)
      );
      expect(comp.niveausSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const session: ISession = { id: 456 };
      const niveau: INiveau = { id: 18488 };
      session.niveau = niveau;

      activatedRoute.data = of({ session });
      comp.ngOnInit();

      expect(comp.niveausSharedCollection).toContain(niveau);
      expect(comp.session).toEqual(session);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISession>>();
      const session = { id: 123 };
      jest.spyOn(sessionFormService, 'getSession').mockReturnValue(session);
      jest.spyOn(sessionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ session });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: session }));
      saveSubject.complete();

      // THEN
      expect(sessionFormService.getSession).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(sessionService.update).toHaveBeenCalledWith(expect.objectContaining(session));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISession>>();
      const session = { id: 123 };
      jest.spyOn(sessionFormService, 'getSession').mockReturnValue({ id: null });
      jest.spyOn(sessionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ session: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: session }));
      saveSubject.complete();

      // THEN
      expect(sessionFormService.getSession).toHaveBeenCalled();
      expect(sessionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISession>>();
      const session = { id: 123 };
      jest.spyOn(sessionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ session });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sessionService.update).toHaveBeenCalled();
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
  });
});
