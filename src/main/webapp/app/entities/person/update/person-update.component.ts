import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPerson, Person } from '../person.model';
import { PersonService } from '../service/person.service';
import { IMembershipLevel } from 'app/entities/membership-level/membership-level.model';
import { MembershipLevelService } from 'app/entities/membership-level/service/membership-level.service';
import { IParish } from 'app/entities/parish/parish.model';
import { ParishService } from 'app/entities/parish/service/parish.service';
import { IOrganization } from 'app/entities/organization/organization.model';
import { OrganizationService } from 'app/entities/organization/service/organization.service';

@Component({
  selector: 'jhi-person-update',
  templateUrl: './person-update.component.html',
})
export class PersonUpdateComponent implements OnInit {
  isSaving = false;

  peopleSharedCollection: IPerson[] = [];
  spousesCollection: IPerson[] = [];
  membershipLevelsSharedCollection: IMembershipLevel[] = [];
  parishesSharedCollection: IParish[] = [];
  organizationsSharedCollection: IOrganization[] = [];

  editForm = this.fb.group({
    id: [],
    prefix: [],
    preferredName: [],
    firstName: [],
    middleName: [],
    lastName: [],
    suffix: [],
    nameTag: [],
    currentMember: [],
    membershipStartDate: [],
    membershipExpirationDate: [],
    isHeadOfHouse: [null, [Validators.required]],
    isDeceased: [null, [Validators.required]],
    spouse: [],
    membershipLevel: [],
    headOfHouse: [],
    parish: [],
    organizations: [],
  });

  constructor(
    protected personService: PersonService,
    protected membershipLevelService: MembershipLevelService,
    protected parishService: ParishService,
    protected organizationService: OrganizationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ person }) => {
      this.updateForm(person);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const person = this.createFromForm();
    if (person.id !== undefined) {
      this.subscribeToSaveResponse(this.personService.update(person));
    } else {
      this.subscribeToSaveResponse(this.personService.create(person));
    }
  }

  trackPersonById(index: number, item: IPerson): number {
    return item.id!;
  }

  trackMembershipLevelById(index: number, item: IMembershipLevel): number {
    return item.id!;
  }

  trackParishById(index: number, item: IParish): number {
    return item.id!;
  }

  trackOrganizationById(index: number, item: IOrganization): number {
    return item.id!;
  }

  getSelectedOrganization(option: IOrganization, selectedVals?: IOrganization[]): IOrganization {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPerson>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(person: IPerson): void {
    this.editForm.patchValue({
      id: person.id,
      prefix: person.prefix,
      preferredName: person.preferredName,
      firstName: person.firstName,
      middleName: person.middleName,
      lastName: person.lastName,
      suffix: person.suffix,
      nameTag: person.nameTag,
      currentMember: person.currentMember,
      membershipStartDate: person.membershipStartDate,
      membershipExpirationDate: person.membershipExpirationDate,
      isHeadOfHouse: person.isHeadOfHouse,
      isDeceased: person.isDeceased,
      spouse: person.spouse,
      membershipLevel: person.membershipLevel,
      headOfHouse: person.headOfHouse,
      parish: person.parish,
      organizations: person.organizations,
    });

    this.peopleSharedCollection = this.personService.addPersonToCollectionIfMissing(this.peopleSharedCollection, person.headOfHouse);
    this.spousesCollection = this.personService.addPersonToCollectionIfMissing(this.spousesCollection, person.spouse);
    this.membershipLevelsSharedCollection = this.membershipLevelService.addMembershipLevelToCollectionIfMissing(
      this.membershipLevelsSharedCollection,
      person.membershipLevel
    );
    this.parishesSharedCollection = this.parishService.addParishToCollectionIfMissing(this.parishesSharedCollection, person.parish);
    this.organizationsSharedCollection = this.organizationService.addOrganizationToCollectionIfMissing(
      this.organizationsSharedCollection,
      ...(person.organizations ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.personService
      .query()
      .pipe(map((res: HttpResponse<IPerson[]>) => res.body ?? []))
      .pipe(map((people: IPerson[]) => this.personService.addPersonToCollectionIfMissing(people, this.editForm.get('headOfHouse')!.value)))
      .subscribe((people: IPerson[]) => (this.peopleSharedCollection = people));

    this.personService
      .query({ 'personId.specified': 'false' })
      .pipe(map((res: HttpResponse<IPerson[]>) => res.body ?? []))
      .pipe(map((people: IPerson[]) => this.personService.addPersonToCollectionIfMissing(people, this.editForm.get('spouse')!.value)))
      .subscribe((people: IPerson[]) => (this.spousesCollection = people));

    this.membershipLevelService
      .query()
      .pipe(map((res: HttpResponse<IMembershipLevel[]>) => res.body ?? []))
      .pipe(
        map((membershipLevels: IMembershipLevel[]) =>
          this.membershipLevelService.addMembershipLevelToCollectionIfMissing(membershipLevels, this.editForm.get('membershipLevel')!.value)
        )
      )
      .subscribe((membershipLevels: IMembershipLevel[]) => (this.membershipLevelsSharedCollection = membershipLevels));

    this.parishService
      .query()
      .pipe(map((res: HttpResponse<IParish[]>) => res.body ?? []))
      .pipe(map((parishes: IParish[]) => this.parishService.addParishToCollectionIfMissing(parishes, this.editForm.get('parish')!.value)))
      .subscribe((parishes: IParish[]) => (this.parishesSharedCollection = parishes));

    this.organizationService
      .query()
      .pipe(map((res: HttpResponse<IOrganization[]>) => res.body ?? []))
      .pipe(
        map((organizations: IOrganization[]) =>
          this.organizationService.addOrganizationToCollectionIfMissing(organizations, ...(this.editForm.get('organizations')!.value ?? []))
        )
      )
      .subscribe((organizations: IOrganization[]) => (this.organizationsSharedCollection = organizations));
  }

  protected createFromForm(): IPerson {
    return {
      ...new Person(),
      id: this.editForm.get(['id'])!.value,
      prefix: this.editForm.get(['prefix'])!.value,
      preferredName: this.editForm.get(['preferredName'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      middleName: this.editForm.get(['middleName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      suffix: this.editForm.get(['suffix'])!.value,
      nameTag: this.editForm.get(['nameTag'])!.value,
      currentMember: this.editForm.get(['currentMember'])!.value,
      membershipStartDate: this.editForm.get(['membershipStartDate'])!.value,
      membershipExpirationDate: this.editForm.get(['membershipExpirationDate'])!.value,
      isHeadOfHouse: this.editForm.get(['isHeadOfHouse'])!.value,
      isDeceased: this.editForm.get(['isDeceased'])!.value,
      spouse: this.editForm.get(['spouse'])!.value,
      membershipLevel: this.editForm.get(['membershipLevel'])!.value,
      headOfHouse: this.editForm.get(['headOfHouse'])!.value,
      parish: this.editForm.get(['parish'])!.value,
      organizations: this.editForm.get(['organizations'])!.value,
    };
  }
}
