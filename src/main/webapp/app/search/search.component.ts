import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';

@Component({
  selector: 'jhi-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css'],
})
export class SearchComponent implements OnInit {
  personList: IPerson[] = [];
  predicate = 'id';
  ascending = true;
  isLoading = true;
  page = 1;
  itemsPerPage: number = ITEMS_PER_PAGE;
  totalItems = 0;
  ngbPaginationPage = 1;
  searchForm = this.fb.group({
    firstName: [],
    lastName: [],
  });

  constructor(private fb: FormBuilder, private personService: PersonService) {}

  ngOnInit(): void {
    this.search();
  }

  async search(page?: number): Promise<void> {
    this.page = page ?? this.page;
    this.isLoading = true;

    const query: any = {
      page: this.page - 1,
      size: this.itemsPerPage,
      sort: this.sort(),
    };

    if (this.searchForm.get('firstName')?.value) {
      query['firstName.contains'] = this.searchForm.get('firstName')?.value;
    }

    if (this.searchForm.get('lastName')?.value) {
      query['lastName.contains'] = this.searchForm.get('lastName')?.value;
    }

    // firstName: this.searchForm.get('firstName')?.value ?? undefined,
    // lastName: this.searchForm.get('lastName')?.value ?? undefined,
    const res = await this.personService.query(query).toPromise();
    this.personList = res.body ?? [];
    this.totalItems = Number(res.headers.get('X-Total-Count'));
    this.ngbPaginationPage = this.page;
    this.isLoading = false;
  }

  trackId(index: number, item: IPerson): number {
    return item.id!;
  }

  getHohId(person: IPerson | undefined): number | undefined {
    return person?.headOfHouse?.id ?? person?.id;
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }
}
