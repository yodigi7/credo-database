import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-create-person',
  templateUrl: './create-person.component.html',
  styleUrls: ['./create-person.component.css'],
})
export class CreatePersonComponent implements OnInit {
  message: string;

  constructor() {
    this.message = '';
  }

  ngOnInit(): void {
    this.message = 'CreatePersonComponent message';
  }
}
