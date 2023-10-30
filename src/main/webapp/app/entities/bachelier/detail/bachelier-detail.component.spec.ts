import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BachelierDetailComponent } from './bachelier-detail.component';

describe('Bachelier Management Detail Component', () => {
  let comp: BachelierDetailComponent;
  let fixture: ComponentFixture<BachelierDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BachelierDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ bachelier: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BachelierDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BachelierDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load bachelier on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.bachelier).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
