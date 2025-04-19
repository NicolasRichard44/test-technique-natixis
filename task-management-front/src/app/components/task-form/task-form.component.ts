import { Component, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { finalize, catchError } from 'rxjs/operators';
import { of } from 'rxjs';
import { TaskService } from '../../services/task.service';
import { LoadingSpinnerComponent } from '../../shared/loading-spinner/loading-spinner.component';

@Component({
  selector: 'app-task-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule,
    LoadingSpinnerComponent
  ],
  templateUrl: './task-form.component.html',
  styleUrls: ['./task-form.component.scss']
})
export class TaskFormComponent {
  taskForm: FormGroup;
  isSubmitting = false;
  error: string | null = null;

  constructor(
    private readonly fb: FormBuilder,
    private readonly taskService: TaskService,
    private readonly router: Router,
    private readonly snackBar: MatSnackBar
  ) {
    this.taskForm = this.fb.group({
      label: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', [Validators.required, Validators.minLength(10)]]
    });
  }

  @HostListener('window:keydown', ['$event'])
  handleKeyDown(event: KeyboardEvent): void {
    if (event.key === 'Escape') {
      event.preventDefault();
      this.cancel();
    } else if (event.key === 'Enter' && event.ctrlKey) {
      event.preventDefault();
      if (this.taskForm.valid && !this.isSubmitting) {
        this.onSubmit();
      } else if (!this.taskForm.valid) {
        this.showValidationErrors();
      }
    }
  }

  private showValidationErrors(): void {
    if (this.taskForm.get('label')?.errors) {
      this.snackBar.open(
        'Label must be at least 3 characters long',
        'Close',
        { duration: 3000 }
      );
    } else if (this.taskForm.get('description')?.errors) {
      this.snackBar.open(
        'Description must be at least 10 characters long',
        'Close',
        { duration: 3000 }
      );
    }
  }

  onSubmit(): void {
    if (this.taskForm.valid) {
      this.isSubmitting = true;
      this.error = null;

      this.taskService.createTask({
        ...this.taskForm.value,
        completed: false
      }).pipe(
        catchError(err => {
          this.error = 'Failed to create task. Please try again.';
          this.snackBar.open('Failed to create task', 'Close', {
            duration: 3000
          });
          return of(null);
        }),
        finalize(() => this.isSubmitting = false)
      ).subscribe(task => {
        if (task) {
          this.snackBar.open('Task created successfully', 'Close', {
            duration: 3000
          });
          this.router.navigate(['/tasks']);
        }
      });
    } else {
      this.showValidationErrors();
    }
  }

  cancel(): void {
    this.router.navigate(['/tasks']);
  }
}
