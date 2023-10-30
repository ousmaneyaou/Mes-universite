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
import { IPaiement } from 'app/entities/paiement/paiement.model';
import { PaiementService } from 'app/entities/paiement/service/paiement.service';
import { IDossier } from 'app/entities/dossier/dossier.model';
import { DossierService } from 'app/entities/dossier/service/dossier.service';

import { InscriptionUpdateComponent } from './inscription-update.component';

describe('Inscription Management Update Component', () => {
  let comp: InscriptionUpdateComponent;
  let fixture: ComponentFixture<InscriptionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let inscriptionFormService: InscriptionFormService;
  let inscriptionService: InscriptionService;
  let paiementService: PaiementService;
  let dossierService: DossierService;

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
    paiementService = TestBed.inject(PaiementService);
    dossierService = TestBed.inject(DossierService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Paiement query and add missing value', () => {
      const inscription: IInscription = { id: 456 };
      const paiement: IPaiement = { id: 55621 };
      inscription.paiement = paiement;

      const paiementCollection: IPaiement[] = [{ id: 60866 }];
      jest.spyOn(paiementService, 'query').mockReturnValue(of(new HttpResponse({ body: paiementCollection })));
      const additionalPaiements = [paiement];
      const expectedCollection: IPaiement[] = [...additionalPaiements, ...paiementCollection];
      jest.spyOn(paiementService, 'addPaiementToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inscription });
      comp.ngOnInit();

      expect(paiementService.query).toHaveBeenCalled();
      expect(paiementService.addPaiementToCollectionIfMissing).toHaveBeenCalledWith(
        paiementCollection,
        ...additionalPaiements.map(expect.objectContaining)
      );
      expect(comp.paiementsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Dossier query and add missing value', () => {
      const inscription: IInscription = { id: 456 };
      const dossiers: IDossier[] = [{ id: 57218 }];
      inscription.dossiers = dossiers;

      const dossierCollection: IDossier[] = [{ id: 78935 }];
      jest.spyOn(dossierService, 'query').mockReturnValue(of(new HttpResponse({ body: dossierCollection })));
      const additionalDossiers = [...dossiers];
      const expectedCollection: IDossier[] = [...additionalDossiers, ...dossierCollection];
      jest.spyOn(dossierService, 'addDossierToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inscription });
      comp.ngOnInit();

      expect(dossierService.query).toHaveBeenCalled();
      expect(dossierService.addDossierToCollectionIfMissing).toHaveBeenCalledWith(
        dossierCollection,
        ...additionalDossiers.map(expect.objectContaining)
      );
      expect(comp.dossiersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const inscription: IInscription = { id: 456 };
      const paiement: IPaiement = { id: 94622 };
      inscription.paiement = paiement;
      const dossiers: IDossier = { id: 93469 };
      inscription.dossiers = [dossiers];

      activatedRoute.data = of({ inscription });
      comp.ngOnInit();

      expect(comp.paiementsSharedCollection).toContain(paiement);
      expect(comp.dossiersSharedCollection).toContain(dossiers);
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
    describe('comparePaiement', () => {
      it('Should forward to paiementService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(paiementService, 'comparePaiement');
        comp.comparePaiement(entity, entity2);
        expect(paiementService.comparePaiement).toHaveBeenCalledWith(entity, entity2);
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
