import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAcceptation, NewAcceptation } from '../acceptation.model';

export type PartialUpdateAcceptation = Partial<IAcceptation> & Pick<IAcceptation, 'id'>;

type RestOf<T extends IAcceptation | NewAcceptation> = Omit<T, 'dateAcceptation'> & {
  dateAcceptation?: string | null;
};

export type RestAcceptation = RestOf<IAcceptation>;

export type NewRestAcceptation = RestOf<NewAcceptation>;

export type PartialUpdateRestAcceptation = RestOf<PartialUpdateAcceptation>;

export type EntityResponseType = HttpResponse<IAcceptation>;
export type EntityArrayResponseType = HttpResponse<IAcceptation[]>;

@Injectable({ providedIn: 'root' })
export class AcceptationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/acceptations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(acceptation: NewAcceptation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(acceptation);
    return this.http
      .post<RestAcceptation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(acceptation: IAcceptation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(acceptation);
    return this.http
      .put<RestAcceptation>(`${this.resourceUrl}/${this.getAcceptationIdentifier(acceptation)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(acceptation: PartialUpdateAcceptation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(acceptation);
    return this.http
      .patch<RestAcceptation>(`${this.resourceUrl}/${this.getAcceptationIdentifier(acceptation)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAcceptation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAcceptation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAcceptationIdentifier(acceptation: Pick<IAcceptation, 'id'>): number {
    return acceptation.id;
  }

  compareAcceptation(o1: Pick<IAcceptation, 'id'> | null, o2: Pick<IAcceptation, 'id'> | null): boolean {
    return o1 && o2 ? this.getAcceptationIdentifier(o1) === this.getAcceptationIdentifier(o2) : o1 === o2;
  }

  addAcceptationToCollectionIfMissing<Type extends Pick<IAcceptation, 'id'>>(
    acceptationCollection: Type[],
    ...acceptationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const acceptations: Type[] = acceptationsToCheck.filter(isPresent);
    if (acceptations.length > 0) {
      const acceptationCollectionIdentifiers = acceptationCollection.map(
        acceptationItem => this.getAcceptationIdentifier(acceptationItem)!
      );
      const acceptationsToAdd = acceptations.filter(acceptationItem => {
        const acceptationIdentifier = this.getAcceptationIdentifier(acceptationItem);
        if (acceptationCollectionIdentifiers.includes(acceptationIdentifier)) {
          return false;
        }
        acceptationCollectionIdentifiers.push(acceptationIdentifier);
        return true;
      });
      return [...acceptationsToAdd, ...acceptationCollection];
    }
    return acceptationCollection;
  }

  protected convertDateFromClient<T extends IAcceptation | NewAcceptation | PartialUpdateAcceptation>(acceptation: T): RestOf<T> {
    return {
      ...acceptation,
      dateAcceptation: acceptation.dateAcceptation?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restAcceptation: RestAcceptation): IAcceptation {
    return {
      ...restAcceptation,
      dateAcceptation: restAcceptation.dateAcceptation ? dayjs(restAcceptation.dateAcceptation) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAcceptation>): HttpResponse<IAcceptation> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAcceptation[]>): HttpResponse<IAcceptation[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
