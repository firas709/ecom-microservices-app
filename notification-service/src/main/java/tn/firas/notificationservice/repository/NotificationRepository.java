package tn.firas.notificationservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.firas.notificationservice.entities.Notification;

public interface NotificationRepository extends MongoRepository<Notification, String> {
}
