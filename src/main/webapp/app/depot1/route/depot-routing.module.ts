import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';


import { DepotComponent } from 'app/entities/depot/list/depot.component';
import { Depot1Component } from '../depot1.component';

const depotRoute: Routes = [
  {
    path: 'Dépôt',
    component: DepotComponent

  },
  {
      path: 'Dépôt1',
      component: Depot1Component

    }
];

@NgModule({
  imports: [RouterModule.forChild(depotRoute)],
  exports: [RouterModule],
})
export class DepotRoutingModule {}
