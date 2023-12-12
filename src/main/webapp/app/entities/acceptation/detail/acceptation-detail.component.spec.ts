import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AcceptationDetailComponent } from './acceptation-detail.component';

describe('Acceptation Management Detail Component', () => {
  let comp: AcceptationDetailComponent;
  let fixture: ComponentFixture<AcceptationDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AcceptationDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ acceptation: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AcceptationDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AcceptationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load acceptation on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.acceptation).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
