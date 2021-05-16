import { Component, Input } from '@angular/core';
import { FormArray, FormBuilder, FormGroup } from '@angular/forms';
import { IPerson, Person } from 'app/entities/person/person.model';
import { HouseDetails } from 'app/entities/house-details/house-details.model';
import { EntityArrayResponseType } from 'app/entities/person/service/person.service';
import { HouseAddress } from 'app/entities/house-address/house-address.model';
import { PersonNotes } from 'app/entities/person-notes/person-notes.model';
import { IMembershipLevel, MembershipLevel } from 'app/entities/membership-level/membership-level.model';
import { YesNoEmpty } from 'app/entities/enumerations/yes-no-empty.model';
import { MembershipLevelService } from 'app/entities/membership-level/service/membership-level.service';
import { PersonPhone } from 'app/entities/person-phone/person-phone.model';
import { PersonEmail } from 'app/entities/person-email/person-email.model';

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
      number: [],
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
