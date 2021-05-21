import { Component, OnDestroy, OnInit } from '@angular/core';
import { ControlValueAccessor, FormArray, FormBuilder, FormGroup, NG_VALUE_ACCESSOR } from '@angular/forms';
import { EntityArrayResponseType } from 'app/entities/person/service/person.service';
import { IMembershipLevel, MembershipLevel } from 'app/entities/membership-level/membership-level.model';
import { MembershipLevelService } from 'app/entities/membership-level/service/membership-level.service';
import { PersonPhone } from 'app/entities/person-phone/person-phone.model';
import { PersonEmail } from 'app/entities/person-email/person-email.model';
import { Person } from 'app/entities/person/person.model';
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
export class EditPersonSubformComponent implements ControlValueAccessor, OnInit, OnDestroy {
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
    // this.subscribers.push(this.personFormGroup.valueChanges.subscribe(console.log));
    const initMembershipLevels = (res: EntityArrayResponseType): void => {
      this.membershipLevels = res.body ?? [];
    };
    membershipLevelService.query().subscribe(initMembershipLevels);
  }

  ngOnInit(): void {
    console.log(this.personFormGroup.controls);
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
    console.log(this.personFormGroup.controls);
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
      console.log(number);
      console.log(this.personFormGroup);
      this.getFormArray('phones').controls[index].get('phoneNumber')?.setValue(number);
      this.onChange(this.personFormGroup);
      // console.log(this.personFormGroup);
    }
  }

  writeValue(form: any): void {
    if (form) {
      console.log(form);
      this.personFormGroup.reset();
      this.personFormGroup.patchValue(form);
    }
  }
  registerOnChange(fn: any): void {
    this.onChange = fn;
    this.subscribers.push(this.personFormGroup.valueChanges.subscribe(fn));
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
