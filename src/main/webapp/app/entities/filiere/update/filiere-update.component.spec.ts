import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FiliereFormService } from './filiere-form.service';
import { FiliereService } from '../service/filiere.service';
import { IFiliere } from '../filiere.model';
import { IDepartement } from 'app/entities/departement/departement.model';
import { DepartementService } from 'app/entities/departement/service/departement.service';

import { FiliereUpdateComponent } from './filiere-update.component';

describe('Filiere Management Update Component', () => {
  let comp: FiliereUpdateComponent;
  let fixture: ComponentFixture<FiliereUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let filiereFormService: FiliereFormService;
  let filiereService: FiliereService;
  let departementService: DepartementService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FiliereUpdateComponent],
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
      .overrideTemplate(FiliereUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FiliereUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    filiereFormService = TestBed.inject(FiliereFormService);
    filiereService = TestBed.inject(FiliereService);
    departementService = TestBed.inject(DepartementService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Departement query and add missing value', () => {
      const filiere: IFiliere = { id: 456 };
      const departement: IDepartement = { id: 96218 };
      filiere.departement = departement;

      const departementCollection: IDepartement[] = [{ id: 39959 }];
      jest.spyOn(departementService, 'query').mockReturnValue(of(new HttpResponse({ body: departementCollection })));
      const additionalDepartements = [departement];
      const expectedCollection: IDepartement[] = [...additionalDepartements, ...departementCollection];
      jest.spyOn(departementService, 'addDepartementToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ filiere });
      comp.ngOnInit();

      expect(departementService.query).toHaveBeenCalled();
      expect(departementService.addDepartementToCollectionIfMissing).toHaveBeenCalledWith(
        departementCollection,
        ...additionalDepartements.map(expect.objectContaining)
      );
      expect(comp.departementsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const filiere: IFiliere = { id: 456 };
      const departement: IDepartement = { id: 5607 };
      filiere.departement = departement;

      activatedRoute.data = of({ filiere });
      comp.ngOnInit();

      expect(comp.departementsSharedCollection).toContain(departement);
      expect(comp.filiere).toEqual(filiere);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFiliere>>();
      const filiere = { id: 123 };
      jest.spyOn(filiereFormService, 'getFiliere').mockReturnValue(filiere);
      jest.spyOn(filiereService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ filiere });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: filiere }));
      saveSubject.complete();

      // THEN
      expect(filiereFormService.getFiliere).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(filiereService.update).toHaveBeenCalledWith(expect.objectContaining(filiere));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFiliere>>();
      const filiere = { id: 123 };
      jest.spyOn(filiereFormService, 'getFiliere').mockReturnValue({ id: null });
      jest.spyOn(filiereService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ filiere: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: filiere }));
      saveSubject.complete();

      // THEN
      expect(filiereFormService.getFiliere).toHaveBeenCalled();
      expect(filiereService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFiliere>>();
      const filiere = { id: 123 };
      jest.spyOn(filiereService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ filiere });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(filiereService.update).toHaveBeenCalled();
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
  });
});
