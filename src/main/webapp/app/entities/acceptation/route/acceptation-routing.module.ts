import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AcceptationComponent } from '../list/acceptation.component';
import { AcceptationDetailComponent } from '../detail/acceptation-detail.component';
import { AcceptationUpdateComponent } from '../update/acceptation-update.component';
import { AcceptationRoutingResolveService } from './acceptation-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const acceptationRoute: Routes = [
  {
    path: '',
    component: AcceptationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AcceptationDetailComponent,
    resolve: {
      acceptation: AcceptationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AcceptationUpdateComponent,
    resolve: {
      acceptation: AcceptationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AcceptationUpdateComponent,
    resolve: {
      acceptation: AcceptationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(acceptationRoute)],
  exports: [RouterModule],
})
export class AcceptationRoutingModule {}
