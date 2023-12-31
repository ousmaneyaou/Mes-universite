import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DepotComponent } from './list/depot.component';
import { DepotDetailComponent } from './detail/depot-detail.component';
import { DepotUpdateComponent } from './update/depot-update.component';
import { DepotDeleteDialogComponent } from './delete/depot-delete-dialog.component';
import { DepotRoutingModule } from './route/depot-routing.module';
import { AcceptationComponent } from './acceptation/acceptation.component';

@NgModule({
  imports: [SharedModule, DepotRoutingModule],
  declarations: [DepotComponent, DepotDetailComponent, DepotUpdateComponent, DepotDeleteDialogComponent, AcceptationComponent],
})
export class DepotModule {}
