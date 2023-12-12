import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AcceptationFormService } from './acceptation-form.service';
import { AcceptationService } from '../service/acceptation.service';
import { IAcceptation } from '../acceptation.model';
import { IInscription } from 'app/entities/inscription/inscription.model';
import { InscriptionService } from 'app/entities/inscription/service/inscription.service';
import { IDepot } from 'app/entities/depot/depot.model';
import { DepotService } from 'app/entities/depot/service/depot.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { AcceptationUpdateComponent } from './acceptation-update.component';

describe('Acceptation Management Update Component', () => {
  let comp: AcceptationUpdateComponent;
  let fixture: ComponentFixture<AcceptationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let acceptationFormService: AcceptationFormService;
  let acceptationService: AcceptationService;
  let inscriptionService: InscriptionService;
  let depotService: DepotService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AcceptationUpdateComponent],
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
      .overrideTemplate(AcceptationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AcceptationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    acceptationFormService = TestBed.inject(AcceptationFormService);
    acceptationService = TestBed.inject(AcceptationService);
    inscriptionService = TestBed.inject(InscriptionService);
    depotService = TestBed.inject(DepotService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Inscription query and add missing value', () => {
      const acceptation: IAcceptation = { id: 456 };
      const inscription: IInscription = { id: 82496 };
      acceptation.inscription = inscription;

      const inscriptionCollection: IInscription[] = [{ id: 43969 }];
      jest.spyOn(inscriptionService, 'query').mockReturnValue(of(new HttpResponse({ body: inscriptionCollection })));
      const additionalInscriptions = [inscription];
      const expectedCollection: IInscription[] = [...additionalInscriptions, ...inscriptionCollection];
      jest.spyOn(inscriptionService, 'addInscriptionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ acceptation });
      comp.ngOnInit();

      expect(inscriptionService.query).toHaveBeenCalled();
      expect(inscriptionService.addInscriptionToCollectionIfMissing).toHaveBeenCalledWith(
        inscriptionCollection,
        ...additionalInscriptions.map(expect.objectContaining)
      );
      expect(comp.inscriptionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Depot query and add missing value', () => {
      const acceptation: IAcceptation = { id: 456 };
      const depot: IDepot = { id: 69813 };
      acceptation.depot = depot;

      const depotCollection: IDepot[] = [{ id: 64804 }];
      jest.spyOn(depotService, 'query').mockReturnValue(of(new HttpResponse({ body: depotCollection })));
      const additionalDepots = [depot];
      const expectedCollection: IDepot[] = [...additionalDepots, ...depotCollection];
      jest.spyOn(depotService, 'addDepotToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ acceptation });
      comp.ngOnInit();

      expect(depotService.query).toHaveBeenCalled();
      expect(depotService.addDepotToCollectionIfMissing).toHaveBeenCalledWith(
        depotCollection,
        ...additionalDepots.map(expect.objectContaining)
      );
      expect(comp.depotsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const acceptation: IAcceptation = { id: 456 };
      const user: IUser = { id: 17286 };
      acceptation.user = user;

      const userCollection: IUser[] = [{ id: 74684 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ acceptation });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const acceptation: IAcceptation = { id: 456 };
      const inscription: IInscription = { id: 29482 };
      acceptation.inscription = inscription;
      const depot: IDepot = { id: 69475 };
      acceptation.depot = depot;
      const user: IUser = { id: 63835 };
      acceptation.user = user;

      activatedRoute.data = of({ acceptation });
      comp.ngOnInit();

      expect(comp.inscriptionsSharedCollection).toContain(inscription);
      expect(comp.depotsSharedCollection).toContain(depot);
      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.acceptation).toEqual(acceptation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAcceptation>>();
      const acceptation = { id: 123 };
      jest.spyOn(acceptationFormService, 'getAcceptation').mockReturnValue(acceptation);
      jest.spyOn(acceptationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ acceptation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: acceptation }));
      saveSubject.complete();

      // THEN
      expect(acceptationFormService.getAcceptation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(acceptationService.update).toHaveBeenCalledWith(expect.objectContaining(acceptation));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAcceptation>>();
      const acceptation = { id: 123 };
      jest.spyOn(acceptationFormService, 'getAcceptation').mockReturnValue({ id: null });
      jest.spyOn(acceptationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ acceptation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: acceptation }));
      saveSubject.complete();

      // THEN
      expect(acceptationFormService.getAcceptation).toHaveBeenCalled();
      expect(acceptationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAcceptation>>();
      const acceptation = { id: 123 };
      jest.spyOn(acceptationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ acceptation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(acceptationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareInscription', () => {
      it('Should forward to inscriptionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(inscriptionService, 'compareInscription');
        comp.compareInscription(entity, entity2);
        expect(inscriptionService.compareInscription).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDepot', () => {
      it('Should forward to depotService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(depotService, 'compareDepot');
        comp.compareDepot(entity, entity2);
        expect(depotService.compareDepot).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
