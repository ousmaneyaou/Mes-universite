import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAcceptation } from '../acceptation.model';
import { AcceptationService } from '../service/acceptation.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './acceptation-delete-dialog.component.html',
})
export class AcceptationDeleteDialogComponent {
  acceptation?: IAcceptation;

  constructor(protected acceptationService: AcceptationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.acceptationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
