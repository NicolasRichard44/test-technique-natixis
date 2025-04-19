import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Task } from '../models/task';

interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  private readonly apiUrl = 'http://localhost:8080/api/tasks';

  constructor(private readonly http: HttpClient) {}

  getAllTasks(page: number = 0, size: number = 10): Observable<PageResponse<Task>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Task>>(this.apiUrl, { params });
  }

  getTasksByCompletion(completed: boolean, page: number = 0, size: number = 10): Observable<PageResponse<Task>> {
    const params = new HttpParams()
      .set('completed', completed.toString())
      .set('page', page.toString())
      .set('size', size.toString());
    // Modification: utiliser le même endpoint que getAllTasks, mais avec le paramètre 'completed'
    return this.http.get<PageResponse<Task>>(this.apiUrl, { params });
  }

  getTaskById(id: number): Observable<Task> {
    return this.http.get<Task>(`${this.apiUrl}/${id}`);
  }

  createTask(task: Omit<Task, 'id'>): Observable<Task> {
    return this.http.post<Task>(this.apiUrl, task);
  }

  updateTaskStatus(id: number, completed: boolean): Observable<Task> {
    return this.http.put<Task>(`${this.apiUrl}/${id}/status`, { completed });
  }
}
