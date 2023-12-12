import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AcceptationComponent } from './list/acceptation.component';
import { AcceptationDetailComponent } from './detail/acceptation-detail.component';
import { AcceptationUpdateComponent } from './update/acceptation-update.component';
import { AcceptationDeleteDialogComponent } from './delete/acceptation-delete-dialog.component';
import { AcceptationRoutingModule } from './route/acceptation-routing.module';

@NgModule({
  imports: [SharedModule, AcceptationRoutingModule],
  declarations: [AcceptationComponent, AcceptationDetailComponent, AcceptationUpdateComponent, AcceptationDeleteDialogComponent],
})
export class AcceptationModule {}
