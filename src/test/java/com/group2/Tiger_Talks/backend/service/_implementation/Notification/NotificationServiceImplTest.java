package com.group2.Tiger_Talks.backend.service._implementation.Notification;

import com.group2.Tiger_Talks.backend.model.Notification.Notification;
import com.group2.Tiger_Talks.backend.model.Notification.NotificationDTO;
import com.group2.Tiger_Talks.backend.model.User.UserProfile;
import com.group2.Tiger_Talks.backend.repository.Notification.NotificationRepository;
import com.group2.Tiger_Talks.backend.repository.User.UserProfileRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     *  Test case for createNotification
     */
    @Test
    public void createNotification_success() {
        Notification notification = new Notification();
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);
        Optional<String> result = notificationService.createNotification(notification);
        assertFalse(result.isPresent());
    }

    @Test
    public void createNotification_fail() {
        Notification notification = new Notification();
        when(notificationRepository.save(any(Notification.class))).thenThrow(new RuntimeException("Database error"));
        Optional<String> result = notificationService.createNotification(notification);
        assertTrue(result.isPresent());
        assertEquals("Failed to create notification: Database error", result.get());
    }

    /**
     *  Test case for getNotificationListByUserEmail
     */
    @Test
    public void getNotificationListByUserEmail_userFound_twoNotifications() {
        UserProfile userProfile = mock(UserProfile.class);
        Notification notification3 = mock(Notification.class);
        Notification notification4 = mock(Notification.class);
        LocalDateTime now = LocalDateTime.now();
        when(notification3.getCreateTime()).thenReturn(now.minusHours(1));
        when(notification4.getCreateTime()).thenReturn(now);
        when(notification3.getUserProfile()).thenReturn(userProfile);
        when(notification4.getUserProfile()).thenReturn(userProfile);
        when(userProfile.getNotificationList()).thenReturn(Arrays.asList(notification3, notification4));
        when(userProfileRepository.findById("user@example.com")).thenReturn(Optional.of(userProfile));

        NotificationDTO dto3 = mock(NotificationDTO.class);
        NotificationDTO dto4 = mock(NotificationDTO.class);
        when(dto3.getCreateTime()).thenReturn(now.minusHours(1));
        when(dto4.getCreateTime()).thenReturn(now);

        when(notification3.toDTO()).thenReturn(dto3);
        when(notification4.toDTO()).thenReturn(dto4);

        List<NotificationDTO> results = notificationService.getNotificationListByUserEmail("user@example.com");
        assertFalse(results.isEmpty());
        assertEquals(2, results.size());

        assertEquals(dto4.getCreateTime(), results.get(0).getCreateTime());
        assertEquals(dto3.getCreateTime(), results.get(1).getCreateTime());
    }

    @Test
    public void getNotificationListByUserEmail_userFound_oneNotification() {
        UserProfile userProfile = mock(UserProfile.class);
        NotificationDTO notification = mock(NotificationDTO.class);
        Notification notification1 = mock(Notification.class);
        LocalDateTime now = LocalDateTime.now();
        when(notification.getCreateTime()).thenReturn(now);
        when(userProfile.getNotificationList()).thenReturn(List.of(notification1));
        when(notification1.getUserProfile()).thenReturn(userProfile);
        when(userProfileRepository.findById("user@example.com")).thenReturn(Optional.of(userProfile));

        List<NotificationDTO> results = notificationService.getNotificationListByUserEmail("user@example.com");
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
    }

    @Test
    public void getNotificationListByUserEmail_userNotFound() {
        when(userProfileRepository.findById("user@example.com")).thenReturn(Optional.empty());
        List<NotificationDTO> results = notificationService.getNotificationListByUserEmail("user@example.com");
        assertTrue(results.isEmpty());
    }

    /**
     *  Test case for deleteNotificationById
     */
    @Test
    public void deleteNotificationById_Success() {
        Notification notification = new Notification();
        when(notificationRepository.findById(1)).thenReturn(Optional.of(notification));
        Optional<String> result = notificationService.deleteNotificationById(1);
        assertFalse(result.isPresent());
    }

    @Test
    public void deleteNotificationById_NotFound() {
        when(notificationRepository.findById(1)).thenReturn(Optional.empty());
        Optional<String> result = notificationService.deleteNotificationById(1);
        assertTrue(result.isPresent());
        assertEquals("Notification not found with id: 1", result.get());
    }
}