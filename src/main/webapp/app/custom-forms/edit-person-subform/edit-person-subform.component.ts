import { Component, Input } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { EntityArrayResponseType } from 'app/entities/person/service/person.service';
import { IMembershipLevel, MembershipLevel } from 'app/entities/membership-level/membership-level.model';
import { MembershipLevelService } from 'app/entities/membership-level/service/membership-level.service';

@Component({
  selector: 'jhi-edit-person-subform',
  templateUrl: './edit-person-subform.component.html',
  styleUrls: ['./edit-person-subform.component.css'],
})
export class EditPersonSubformComponent {
  @Input() personFormGroup?: FormGroup;
  @Input() isHoh = false;
  membershipLevels: MembershipLevel[];

  constructor(private fb: FormBuilder, private membershipLevelService: MembershipLevelService) {
    this.membershipLevels = [];
    const initMembershipLevels = (res: EntityArrayResponseType): void => {
      this.membershipLevels = res.body ?? [];
    };
    membershipLevelService.query().subscribe(initMembershipLevels);
  }

  createPhoneFormGroup(): FormGroup {
    return this.fb.group({
      number: new FormControl(undefined, Validators.compose([Validators.pattern('.*[0-9]{3}-[0-9]{4}$')])),
      type: [],
    });
  }

  addPhoneToForm(phoneList: string): void {
    (<FormArray>this.personFormGroup?.get(phoneList)).push(this.createPhoneFormGroup());
  }

  createEmailFormGroup(): FormGroup {
    return this.fb.group({
      email: [],
      type: [],
    });
  }

  addEmailToForm(emailList: string): void {
    (<FormArray>this.personFormGroup?.get(emailList)).push(this.createEmailFormGroup());
  }

  getFormArray(formArrayName: string): FormArray {
    return <FormArray>this.personFormGroup?.get(formArrayName);
  }

  trackMembershipLevelById(index: number, item: IMembershipLevel): number {
    return item.id!;
  }
}
