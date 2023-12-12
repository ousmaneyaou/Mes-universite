import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAcceptation } from '../acceptation.model';
import { AcceptationService } from '../service/acceptation.service';

@Injectable({ providedIn: 'root' })
export class AcceptationRoutingResolveService implements Resolve<IAcceptation | null> {
  constructor(protected service: AcceptationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAcceptation | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((acceptation: HttpResponse<IAcceptation>) => {
          if (acceptation.body) {
            return of(acceptation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
