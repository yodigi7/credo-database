import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';

@Component({
  selector: 'jhi-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css'],
})
export class SearchComponent implements OnInit {
  searchForm = this.fb.group({
    firstName: [],
    lastName: [],
    phone: [],
    email: [],
    address: [],
  });

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    console.warn('here');
  }

  search(): void {
    return;
  }
}
