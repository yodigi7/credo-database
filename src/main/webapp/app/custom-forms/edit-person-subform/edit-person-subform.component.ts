import { Component, OnDestroy, OnInit } from '@angular/core';
import { ControlValueAccessor, FormArray, FormBuilder, FormGroup, NG_VALUE_ACCESSOR } from '@angular/forms';
import { EntityArrayResponseType } from 'app/entities/person/service/person.service';
import { IMembershipLevel, MembershipLevel } from 'app/entities/membership-level/membership-level.model';
import { MembershipLevelService } from 'app/entities/membership-level/service/membership-level.service';
import { PersonPhone } from 'app/entities/person-phone/person-phone.model';
import { PersonEmail } from 'app/entities/person-email/person-email.model';
import { IPerson, Person } from 'app/entities/person/person.model';
import { Subscription } from 'rxjs';

@Component({
  selector: 'jhi-edit-person-subform',
  templateUrl: './edit-person-subform.component.html',
  styleUrls: ['./edit-person-subform.component.css'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      multi: true,
      useExisting: EditPersonSubformComponent,
    },
  ],
})
export class EditPersonSubformComponent implements ControlValueAccessor, OnDestroy {
  personFormGroup: FormGroup;
  isHoh = false;
  membershipLevels: MembershipLevel[];

  onChange: any;
  onTouched: any;
  disabled = false;
  subscribers: Subscription[] = [];

  constructor(private fb: FormBuilder, private membershipLevelService: MembershipLevelService) {
    this.membershipLevels = [];
    this.personFormGroup = this.fb.group(new Person());
    this.personFormGroup.addControl('phones', this.fb.array([]));
    this.personFormGroup.addControl('emails', this.fb.array([]));
    this.personFormGroup.setControl('phones', this.fb.array([]));
    this.personFormGroup.setControl('emails', this.fb.array([]));
    const initMembershipLevels = (res: EntityArrayResponseType): void => {
      this.membershipLevels = res.body ?? [];
    };
    membershipLevelService.query().subscribe(initMembershipLevels);
  }

  createPhoneFormGroup(): FormGroup {
    return this.fb.group(new PersonPhone());
  }

  createEmailFormGroup(): FormGroup {
    return this.fb.group(new PersonEmail());
  }

  addEmailToForm(): void {
    (<FormArray>this.personFormGroup.get('emails')).push(this.createEmailFormGroup());
  }

  addPhoneToForm(): void {
    (this.personFormGroup.get('phones') as FormArray).push(this.createPhoneFormGroup());
  }

  getFormArray(formArrayName: string): FormArray {
    return <FormArray>this.personFormGroup.get(formArrayName);
  }

  trackMembershipLevelById(index: number, item: IMembershipLevel): number {
    return item.id!;
  }

  updatePhoneFormat(index: number): void {
    let number: string = this.getFormArray('phones').controls[index].get('phoneNumber')?.value ?? '';
    if (number.length === 10) {
      number = '(' + number.substr(0, 3) + ') ' + number.substr(3, 3) + '-' + number.substr(6, 4);
      this.getFormArray('phones').controls[index].get('phoneNumber')?.setValue(number);
      this.onChange(this.personFormGroup);
    }
  }

  writeValue(form: IPerson | null): void {
    this.personFormGroup.reset();
    this.personFormGroup.controls.isDeceased.setValue(false);
    this.personFormGroup.controls.isHeadOfHouse.setValue(false);
    if (form) {
      this.personFormGroup.patchValue(form);
      if (typeof form.membershipExpirationDate !== 'string') {
        this.personFormGroup.patchValue({
          membershipStartDate: form.membershipStartDate?.format('YYYY-MM-DD'),
        });
      }
      if (typeof form.membershipExpirationDate !== 'string') {
        this.personFormGroup.patchValue({
          membershipExpirationDate: form.membershipExpirationDate?.format('YYYY-MM-DD'),
        });
      }
      this.personFormGroup.setControl('emails', this.fb.array(form.emails ?? []));
      this.personFormGroup.setControl('phones', this.fb.array(form.phones?.map(phone => this.fb.group(phone)) ?? []));
    }
  }
  registerOnChange(fn: any): void {
    this.onChange = fn;
    // eslint-disable-next-line
    this.subscribers.push(this.personFormGroup.valueChanges.subscribe(() => fn(this.personFormGroup)));
    fn(this.personFormGroup);
  }
  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }
  setDisabledState(disabled: boolean): void {
    this.disabled = disabled;
  }

  ngOnDestroy(): void {
    for (const sub of this.subscribers) {
      sub.unsubscribe();
    }
  }
}
