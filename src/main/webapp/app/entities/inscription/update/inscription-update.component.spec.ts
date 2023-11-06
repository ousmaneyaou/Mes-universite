import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { InscriptionFormService } from './inscription-form.service';
import { InscriptionService } from '../service/inscription.service';
import { IInscription } from '../inscription.model';
import { ISession } from 'app/entities/session/session.model';
import { SessionService } from 'app/entities/session/service/session.service';

import { InscriptionUpdateComponent } from './inscription-update.component';

describe('Inscription Management Update Component', () => {
  let comp: InscriptionUpdateComponent;
  let fixture: ComponentFixture<InscriptionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let inscriptionFormService: InscriptionFormService;
  let inscriptionService: InscriptionService;
  let sessionService: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [InscriptionUpdateComponent],
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
      .overrideTemplate(InscriptionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InscriptionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    inscriptionFormService = TestBed.inject(InscriptionFormService);
    inscriptionService = TestBed.inject(InscriptionService);
    sessionService = TestBed.inject(SessionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Session query and add missing value', () => {
      const inscription: IInscription = { id: 456 };
      const session: ISession = { id: 80403 };
      inscription.session = session;

      const sessionCollection: ISession[] = [{ id: 75091 }];
      jest.spyOn(sessionService, 'query').mockReturnValue(of(new HttpResponse({ body: sessionCollection })));
      const additionalSessions = [session];
      const expectedCollection: ISession[] = [...additionalSessions, ...sessionCollection];
      jest.spyOn(sessionService, 'addSessionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inscription });
      comp.ngOnInit();

      expect(sessionService.query).toHaveBeenCalled();
      expect(sessionService.addSessionToCollectionIfMissing).toHaveBeenCalledWith(
        sessionCollection,
        ...additionalSessions.map(expect.objectContaining)
      );
      expect(comp.sessionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const inscription: IInscription = { id: 456 };
      const session: ISession = { id: 80142 };
      inscription.session = session;

      activatedRoute.data = of({ inscription });
      comp.ngOnInit();

      expect(comp.sessionsSharedCollection).toContain(session);
      expect(comp.inscription).toEqual(inscription);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInscription>>();
      const inscription = { id: 123 };
      jest.spyOn(inscriptionFormService, 'getInscription').mockReturnValue(inscription);
      jest.spyOn(inscriptionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inscription });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inscription }));
      saveSubject.complete();

      // THEN
      expect(inscriptionFormService.getInscription).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(inscriptionService.update).toHaveBeenCalledWith(expect.objectContaining(inscription));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInscription>>();
      const inscription = { id: 123 };
      jest.spyOn(inscriptionFormService, 'getInscription').mockReturnValue({ id: null });
      jest.spyOn(inscriptionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inscription: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inscription }));
      saveSubject.complete();

      // THEN
      expect(inscriptionFormService.getInscription).toHaveBeenCalled();
      expect(inscriptionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInscription>>();
      const inscription = { id: 123 };
      jest.spyOn(inscriptionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inscription });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(inscriptionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
