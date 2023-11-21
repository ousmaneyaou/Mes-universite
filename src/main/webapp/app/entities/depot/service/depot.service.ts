import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDepot, NewDepot } from '../depot.model';

export type PartialUpdateDepot = Partial<IDepot> & Pick<IDepot, 'id'>;

type RestOf<T extends IDepot | NewDepot> = Omit<T, 'dateNaissance' | 'dateDepot'> & {
  dateNaissance?: string | null;
  dateDepot?: string | null;
};

export type RestDepot = RestOf<IDepot>;

export type NewRestDepot = RestOf<NewDepot>;

export type PartialUpdateRestDepot = RestOf<PartialUpdateDepot>;

export type EntityResponseType = HttpResponse<IDepot>;
export type EntityArrayResponseType = HttpResponse<IDepot[]>;

@Injectable({ providedIn: 'root' })
export class DepotService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/depots');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(depot: NewDepot): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(depot);
    return this.http.post<RestDepot>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(depot: IDepot): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(depot);
    return this.http
      .put<RestDepot>(`${this.resourceUrl}/${this.getDepotIdentifier(depot)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(depot: PartialUpdateDepot): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(depot);
    return this.http
      .patch<RestDepot>(`${this.resourceUrl}/${this.getDepotIdentifier(depot)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDepot>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDepot[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDepotIdentifier(depot: Pick<IDepot, 'id'>): number {
    return depot.id;
  }

  compareDepot(o1: Pick<IDepot, 'id'> | null, o2: Pick<IDepot, 'id'> | null): boolean {
    return o1 && o2 ? this.getDepotIdentifier(o1) === this.getDepotIdentifier(o2) : o1 === o2;
  }

  addDepotToCollectionIfMissing<Type extends Pick<IDepot, 'id'>>(
    depotCollection: Type[],
    ...depotsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const depots: Type[] = depotsToCheck.filter(isPresent);
    if (depots.length > 0) {
      const depotCollectionIdentifiers = depotCollection.map(depotItem => this.getDepotIdentifier(depotItem)!);
      const depotsToAdd = depots.filter(depotItem => {
        const depotIdentifier = this.getDepotIdentifier(depotItem);
        if (depotCollectionIdentifiers.includes(depotIdentifier)) {
          return false;
        }
        depotCollectionIdentifiers.push(depotIdentifier);
        return true;
      });
      return [...depotsToAdd, ...depotCollection];
    }
    return depotCollection;
  }

  protected convertDateFromClient<T extends IDepot | NewDepot | PartialUpdateDepot>(depot: T): RestOf<T> {
    return {
      ...depot,
      dateNaissance: depot.dateNaissance?.toJSON() ?? null,
      dateDepot: depot.dateDepot?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restDepot: RestDepot): IDepot {
    return {
      ...restDepot,
      dateNaissance: restDepot.dateNaissance ? dayjs(restDepot.dateNaissance) : undefined,
      dateDepot: restDepot.dateDepot ? dayjs(restDepot.dateDepot) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDepot>): HttpResponse<IDepot> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDepot[]>): HttpResponse<IDepot[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }


  findByNom(nom: string): Observable<HttpResponse<IDepot[]>> {
    return this.http.get<IDepot[]>(`${this.resourceUrl}/search/${nom}`, { observe: 'response' });
    
  }

  //la methode dans le service
  //FindByFaculte(depot: IDepot): Observable<EntityArrayResponseType> {
    //return this.http.post<IDepot[]>(this.resourceUrl, depot, { observe: 'response' });
  //}
}
