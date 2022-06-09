import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminAppConsoleComponent } from './admin-app-console.component';

describe('AdminAppConsoleComponent', () => {
  let component: AdminAppConsoleComponent;
  let fixture: ComponentFixture<AdminAppConsoleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminAppConsoleComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminAppConsoleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
