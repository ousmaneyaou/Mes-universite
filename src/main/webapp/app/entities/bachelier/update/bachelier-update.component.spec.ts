import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BachelierFormService } from './bachelier-form.service';
import { BachelierService } from '../service/bachelier.service';
import { IBachelier } from '../bachelier.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IDepot } from 'app/entities/depot/depot.model';
import { DepotService } from 'app/entities/depot/service/depot.service';

import { BachelierUpdateComponent } from './bachelier-update.component';

describe('Bachelier Management Update Component', () => {
  let comp: BachelierUpdateComponent;
  let fixture: ComponentFixture<BachelierUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bachelierFormService: BachelierFormService;
  let bachelierService: BachelierService;
  let userService: UserService;
  let depotService: DepotService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BachelierUpdateComponent],
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
      .overrideTemplate(BachelierUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BachelierUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bachelierFormService = TestBed.inject(BachelierFormService);
    bachelierService = TestBed.inject(BachelierService);
    userService = TestBed.inject(UserService);
    depotService = TestBed.inject(DepotService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const bachelier: IBachelier = { id: 456 };
      const utilisateur: IUser = { id: 234 };
      bachelier.utilisateur = utilisateur;

      const userCollection: IUser[] = [{ id: 63383 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [utilisateur];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bachelier });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Depot query and add missing value', () => {
      const bachelier: IBachelier = { id: 456 };
      const depot: IDepot = { id: 35122 };
      bachelier.depot = depot;

      const depotCollection: IDepot[] = [{ id: 82293 }];
      jest.spyOn(depotService, 'query').mockReturnValue(of(new HttpResponse({ body: depotCollection })));
      const additionalDepots = [depot];
      const expectedCollection: IDepot[] = [...additionalDepots, ...depotCollection];
      jest.spyOn(depotService, 'addDepotToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bachelier });
      comp.ngOnInit();

      expect(depotService.query).toHaveBeenCalled();
      expect(depotService.addDepotToCollectionIfMissing).toHaveBeenCalledWith(
        depotCollection,
        ...additionalDepots.map(expect.objectContaining)
      );
      expect(comp.depotsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const bachelier: IBachelier = { id: 456 };
      const utilisateur: IUser = { id: 19051 };
      bachelier.utilisateur = utilisateur;
      const depot: IDepot = { id: 71836 };
      bachelier.depot = depot;

      activatedRoute.data = of({ bachelier });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(utilisateur);
      expect(comp.depotsSharedCollection).toContain(depot);
      expect(comp.bachelier).toEqual(bachelier);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBachelier>>();
      const bachelier = { id: 123 };
      jest.spyOn(bachelierFormService, 'getBachelier').mockReturnValue(bachelier);
      jest.spyOn(bachelierService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bachelier });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bachelier }));
      saveSubject.complete();

      // THEN
      expect(bachelierFormService.getBachelier).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(bachelierService.update).toHaveBeenCalledWith(expect.objectContaining(bachelier));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBachelier>>();
      const bachelier = { id: 123 };
      jest.spyOn(bachelierFormService, 'getBachelier').mockReturnValue({ id: null });
      jest.spyOn(bachelierService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bachelier: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bachelier }));
      saveSubject.complete();

      // THEN
      expect(bachelierFormService.getBachelier).toHaveBeenCalled();
      expect(bachelierService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBachelier>>();
      const bachelier = { id: 123 };
      jest.spyOn(bachelierService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bachelier });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bachelierService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
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
  });
});
