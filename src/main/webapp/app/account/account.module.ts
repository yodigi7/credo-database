import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { PasswordStrengthBarComponent } from './password/password-strength-bar/password-strength-bar.component';
import { PasswordComponent } from './password/password.component';
import { accountState } from './account.route';

@NgModule({
  imports: [SharedModule, RouterModule.forChild(accountState)],
  declarations: [PasswordComponent, PasswordStrengthBarComponent],
})
export class AccountModule {}
