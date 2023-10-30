import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Inscription1Component } from './inscription1.component';

describe('Inscription1Component', () => {
  let component: Inscription1Component;
  let fixture: ComponentFixture<Inscription1Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ Inscription1Component ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Inscription1Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
