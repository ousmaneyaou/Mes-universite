import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { errorRoute } from './layouts/error/error.route';
import { navbarRoute } from './layouts/navbar/navbar.route';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';
import { Authority } from 'app/config/authority.constants';


import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { Depot1Component } from './depot1/depot1.component';
import { DepotComponent } from './entities/depot/list/depot.component';
import { FaculteComponent } from './entities/faculte/list/faculte.component';
import { UniversiteComponent } from './entities/universite/list/universite.component';
import { Inscription1Component } from './inscription1/inscription1.component';
import { FaculteRoutingResolveService } from './entities/faculte/route/faculte-routing-resolve.service';
import { FaculteUpdateComponent } from './entities/faculte/update/faculte-update.component';

@NgModule({
  imports: [
    RouterModule.forRoot(
      [
        {
          path: 'admin',
          data: {
            authorities: [Authority.ADMIN],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () => import('./admin/admin-routing.module').then(m => m.AdminRoutingModule),
        },
        {
          path: 'account',
          loadChildren: () => import('./account/account.module').then(m => m.AccountModule),
        },

        {
          path: 'login',
          loadChildren: () => import('./login/login.module').then(m => m.LoginModule),
        },

        {
              path: 'depot1',
              component: Depot1Component
        },
        {
          path: 'new',
          component: FaculteUpdateComponent,
          resolve: {
            faculte: FaculteRoutingResolveService,
          },
          canActivate: [UserRouteAccessService],
        },
        {
          path: 'faculte',
          component: FaculteComponent
        },
        {
          path: 'universite',
          component: UniversiteComponent
        },

        {
          path: 'inscription1',
          component: Inscription1Component
        },

        {
          path: '',
          loadChildren: () => import(`./entities/entity-routing.module`).then(m => m.EntityRoutingModule),
        },
        navbarRoute,
        ...errorRoute,
      ],
      { enableTracing: DEBUG_INFO_ENABLED }
    ),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
