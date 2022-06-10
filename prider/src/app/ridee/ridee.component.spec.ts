import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RideeComponent } from './ridee.component';

describe('RideeComponent', () => {
  let component: RideeComponent;
  let fixture: ComponentFixture<RideeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RideeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RideeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
