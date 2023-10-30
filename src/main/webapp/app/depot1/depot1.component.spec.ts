import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Depot1Component } from './depot1.component';

describe('Depot1Component', () => {
  let component: Depot1Component;
  let fixture: ComponentFixture<Depot1Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ Depot1Component ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Depot1Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
