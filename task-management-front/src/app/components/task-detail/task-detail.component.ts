import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { catchError, finalize, switchMap } from 'rxjs/operators';
import { of } from 'rxjs';
import { TaskService } from '../../services/task.service';
import { Task } from '../../models/task';
import { LoadingSpinnerComponent } from '../../shared/loading-spinner/loading-spinner.component';
import { ConfirmDialogComponent } from '../../shared/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-task-detail',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatCheckboxModule,
    MatDialogModule,
    LoadingSpinnerComponent
  ],
  templateUrl: './task-detail.component.html',
  styleUrls: ['./task-detail.component.scss']
})
export class TaskDetailComponent implements OnInit {
  task?: Task;
  isLoading = false;
  error: string | null = null;

  constructor(
    private readonly taskService: TaskService,
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly snackBar: MatSnackBar,
    private readonly dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.loadTask();
  }

  private loadTask(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isLoading = true;
      this.error = null;
      this.taskService.getTaskById(+id).pipe(
        catchError(err => {
          this.error = 'Failed to load task details. Please try again.';
          return of(undefined);
        }),
        finalize(() => this.isLoading = false)
      ).subscribe(task => {
        if (task) {
          this.task = task;
        }
      });
    }
  }

  toggleStatus(): void {
    if (this.task) {
      const dialogRef = this.dialog.open(ConfirmDialogComponent, {
        data: {
          title: 'Confirm Status Change',
          message: `Are you sure you want to mark this task as ${this.task.completed ? 'incomplete' : 'complete'}?`,
          confirmText: 'Yes',
          cancelText: 'No'
        }
      });

      dialogRef.afterClosed().pipe(
        switchMap(result => {
          if (result) {
            return this.taskService.updateTaskStatus(this.task!.id, !this.task!.completed).pipe(
              catchError(err => {
                this.snackBar.open('Failed to update task status', 'Close', {
                  duration: 3000
                });
                return of(undefined);
              })
            );
          }
          return of(null);
        })
      ).subscribe(updatedTask => {
        if (updatedTask) {
          this.task = updatedTask;
          this.snackBar.open('Task status updated successfully', 'Close', {
            duration: 3000
          });
        }
      });
    }
  }

  goBack(): void {
    this.router.navigate(['/tasks']);
  }
}
