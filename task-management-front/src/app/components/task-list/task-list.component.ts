import { Component, OnInit, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { RouterModule } from '@angular/router';
import { catchError, finalize, switchMap } from 'rxjs/operators';
import { of } from 'rxjs';
import { TaskService } from '../../services/task.service';
import { Task } from '../../models/task';
import { LoadingSpinnerComponent } from '../../shared/loading-spinner/loading-spinner.component';
import { ConfirmDialogComponent } from '../../shared/confirm-dialog/confirm-dialog.component';
import { BackToTopComponent } from '../../shared/back-to-top/back-to-top.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-task-list',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatPaginatorModule,
    MatButtonModule,
    MatButtonToggleModule,
    MatCheckboxModule,
    MatDialogModule,
    MatCardModule,
    MatIconModule,
    RouterModule,
    LoadingSpinnerComponent,
    ConfirmDialogComponent,
    BackToTopComponent
  ],
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.scss']
})
export class TaskListComponent implements OnInit {
  tasks: Task[] = [];
  displayedColumns: string[] = ['label', 'description', 'completed', 'actions'];
  totalItems = 0;
  pageSize = 10;
  currentPage = 0;
  filterValue: 'all' | 'completed' | 'pending' = 'all';
  isLoading = false;
  error: string | null = null;
  selectedRowIndex = -1;

  constructor(
    private readonly taskService: TaskService,
    private readonly snackBar: MatSnackBar,
    private readonly dialog: MatDialog,
    private readonly router: Router
  ) {}

  ngOnInit(): void {
    this.loadTasks();
  }

  loadTasks(): void {
    this.isLoading = true;
    this.error = null;

    const request$ = this.filterValue === 'all'
      ? this.taskService.getAllTasks(this.currentPage, this.pageSize)
      : this.taskService.getTasksByCompletion(
          this.filterValue === 'completed',
          this.currentPage,
          this.pageSize
        );

    request$.pipe(
      catchError(err => {
        this.error = 'Failed to load tasks. Please try again.';
        return of({ content: [], totalElements: 0 });
      }),
      finalize(() => this.isLoading = false)
    ).subscribe(response => {
      this.tasks = response.content;
      this.totalItems = response.totalElements;
    });
  }

  onPageChange(event: PageEvent): void {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadTasks();
  }

  onFilterChange(value: 'all' | 'completed' | 'pending'): void {
    this.filterValue = value;
    this.currentPage = 0;
    this.loadTasks();
  }

  toggleTaskStatus(task: Task): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: 'Confirm Status Change',
        message: `Are you sure you want to mark this task as ${task.completed ? 'incomplete' : 'complete'}?`,
        confirmText: 'Yes',
        cancelText: 'No'
      }
    });

    dialogRef.afterClosed().pipe(
      switchMap(result => {
        if (result) {
          return this.taskService.updateTaskStatus(task.id, !task.completed).pipe(
            catchError(err => {
              this.snackBar.open('Failed to update task status', 'Close', {
                duration: 3000
              });
              return of(task);
            })
          );
        }
        return of(null);
      })
    ).subscribe(updatedTask => {
      if (updatedTask) {
        task.completed = updatedTask.completed;
        this.snackBar.open('Task status updated successfully', 'Close', {
          duration: 3000
        });
      }
    });
  }

  @HostListener('window:keydown', ['$event'])
  handleKeyDown(event: KeyboardEvent): void {
    if (!this.tasks.length) return;

    switch(event.key) {
      case 'ArrowDown':
        event.preventDefault();
        this.selectedRowIndex = Math.min(this.selectedRowIndex + 1, this.tasks.length - 1);
        this.scrollToSelectedRow();
        break;
      case 'ArrowUp':
        event.preventDefault();
        this.selectedRowIndex = Math.max(this.selectedRowIndex - 1, 0);
        this.scrollToSelectedRow();
        break;
      case 'Enter':
        if (this.selectedRowIndex >= 0) {
          event.preventDefault();
          this.router.navigate(['/tasks', this.tasks[this.selectedRowIndex].id]);
        }
        break;
      case ' ':
        if (this.selectedRowIndex >= 0) {
          event.preventDefault();
          this.toggleTaskStatus(this.tasks[this.selectedRowIndex]);
        }
        break;
    }
  }

  private scrollToSelectedRow(): void {
    const row = document.querySelector(`tr[data-row-index="${this.selectedRowIndex}"]`);
    row?.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
  }

  onRowClick(index: number): void {
    this.selectedRowIndex = index;
  }

  getEmptyStateMessage(): string {
    switch (this.filterValue) {
      case 'completed':
        return 'No completed tasks found. Tasks will appear here when marked as complete.';
      case 'pending':
        return 'No pending tasks found. Create a new task to get started.';
      default:
        return 'No tasks found. Create your first task to get started.';
    }
  }
}
