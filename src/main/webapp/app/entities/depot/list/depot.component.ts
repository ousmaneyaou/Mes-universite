import { Component, OnInit } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, filter, Observable, switchMap, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
//import { IDepot } from 'app/entities/depot/depot.model';



import { IDepot } from '../depot.model';

import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { ASC, DESC, SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { EntityArrayResponseType, DepotService } from '../service/depot.service';
import { DepotDeleteDialogComponent } from '../delete/depot-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-depot',
  templateUrl: './depot.component.html',
  styleUrls: ['./depot.component.css']
})
export class DepotComponent implements OnInit {
  depots: IDepot[] | null | undefined; 
  isLoading = false;

  predicate = 'id';
  ascending = true;

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;
  searchResults: string= '';

  constructor(
    protected depotService: DepotService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected dataUtils: DataUtils,
    protected modalService: NgbModal
  ) {}

  trackId = (_index: number, item: IDepot): number => this.depotService.getDepotIdentifier(item);

  ngOnInit(): void {
    this.load();
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(depot: IDepot): void {
    const modalRef = this.modalService.open(DepotDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.depot = depot;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        switchMap(() => this.loadFromBackendWithRouteInformations())
      )
      .subscribe({
        next: (res: EntityArrayResponseType) => {
          this.onResponseSuccess(res);
        },
      });
  }

  load(): void {
    this.loadFromBackendWithRouteInformations().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  navigateToWithComponentValues(): void {
    this.handleNavigation(this.page, this.predicate, this.ascending);
  }

  navigateToPage(page = this.page): void {
    this.handleNavigation(page, this.predicate, this.ascending);
  }

  protected loadFromBackendWithRouteInformations(): Observable<EntityArrayResponseType> {
    return combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data]).pipe(
      tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
      switchMap(() => this.queryBackend(this.page, this.predicate, this.ascending))
    );
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const page = params.get(PAGE_HEADER);
    this.page = +(page ?? 1);
    const sort = (params.get(SORT) ?? data[DEFAULT_SORT_DATA]).split(',');
    this.predicate = sort[0];
    this.ascending = sort[1] === ASC;
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.depots = dataFromBody;
  }

  protected fillComponentAttributesFromResponseBody(data: IDepot[] | null): IDepot[] {
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
  }

  protected queryBackend(page?: number, predicate?: string, ascending?: boolean): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const pageToLoad: number = page ?? 1;
    const queryObject = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      sort: this.getSortQueryParam(predicate, ascending),
    };
    return this.depotService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(page = this.page, predicate?: string, ascending?: boolean): void {
    const queryParamsObj = {
      page,
      size: this.itemsPerPage,
      sort: this.getSortQueryParam(predicate, ascending),
    };

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }

  protected getSortQueryParam(predicate = this.predicate, ascending = this.ascending): string[] {
    const ascendingQueryParam = ascending ? ASC : DESC;
    if (predicate === '') {
      return [];
    } else {
      return [predicate + ',' + ascendingQueryParam];
    }
  }

  acceptation(acceptation: any) {
    // Implémentez la logique de votre méthode 'acceptation' ici
  }


  searchParNom(){
    
    this.depotService.findByNom(this.searchResults).subscribe({
     next: value => {
        this.depots=value.body;
        
      }
    })
  }

  // Supposons que depots est votre tableau de données
  

// Méthode pour gérer le changement de la checkbox
// handleCheckboxChange(depot: IDepot, choix: string) {
//   // Désactive les autres checkboxes
//   if (choix === 'choix1') {
//     depot.disabledChoix2 = true;
//     depot.disabledChoix3 = true;
//     depot.choix2=false;
//     depot.choix3=false;
//   } else if (choix === 'choix2') {
//     depot.disabledChoix1 = true;
//     depot.disabledChoix3 = true;
//     depot.choix1=false;
//     depot.choix3=false;
//   } else if (choix === 'choix3') {
//     depot.disabledChoix1 = true;
//     depot.disabledChoix2 = true;
//     depot.choix1=false;
//     depot.choix2=false;
//   }

  // Réinitialise les valeurs des autres checkboxes
  // if (choix !== 'choix1') {
  //   depot.choix1 = null;
  // }
  // if (choix !== 'choix2') {
  //   depot.choix2 = null;
  // }
  // if (choix !== 'choix3') {
  //   depot.choix3 = null;
  // }

  // Fait disparaître le résultat
//   depot.resultat = '';
// }


  

}
