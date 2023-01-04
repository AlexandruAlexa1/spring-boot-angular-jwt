import { Injectable } from '@angular/core';
import { NotificationType } from '../enum/notification-type';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor() { }

  sendNotification(type: NotificationType, message: string) {
    const notification = $('.notification');

    switch(type) {
      case 'Info':
        notification.addClass('notification-info');
        break;
      case 'Warning':
        notification.addClass('notification-warning');
        break;
      case 'Error':
        notification.addClass('notification-error');
        break;
      case 'Success':
        notification.addClass('notification-success');
        break;
      default:
        notification.addClass('notification-success');
    }

    // Show notification
    notification.fadeIn();

    // Set message
    $('.notification-message').text(message);

    // Hide notification
    setTimeout(() => {
      notification.fadeOut();
    }, 5000);
  }
}
