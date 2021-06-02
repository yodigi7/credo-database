import { Pipe, PipeTransform } from '@angular/core';
import { FormArray } from '@angular/forms';

@Pipe({
  name: 'formArray',
})
export class FormArrayPipe implements PipeTransform {
  transform(value: any): FormArray {
    return value as FormArray;
  }
}
