package tigertalk.Tiger_Talks.backend.service._implementation.Group;

import tigertalk.model.Group.*;
import tigertalk.model.Notification.Notification;
import tigertalk.model.User.UserProfile;
import tigertalk.repository.Group.GroupMembershipRepository;
import tigertalk.repository.Group.GroupPostCommentRepository;
import tigertalk.repository.Group.GroupPostRepository;
import tigertalk.repository.User.UserProfileRepository;
import tigertalk.service.Notification.NotificationService;
import tigertalk.service._implementation.Group.GroupPostCommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
public class GroupPostCommentServiceImplTest {

    @InjectMocks
    private GroupPostCommentServiceImpl groupPostCommentService;

    @Mock
    private GroupMembershipRepository groupMembershipRepository;

    @Mock
    private GroupPostRepository groupPostRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private GroupPostCommentRepository groupPostCommentRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test case for createGroupPostComment
     */
    @Test
    public void createGroupPostComment_success() {
        int groupPostId = 1;
        Group group = new Group();
        GroupPost groupPost = new GroupPost(group, "Content", "a@dal.ca", "picture");
        groupPost.setGroupPostId(groupPostId);

        UserProfile userProfile = new UserProfile();
        userProfile.setEmail("a@dal.ca");
        GroupMembership groupMembership = new GroupMembership();
        groupMembership.setUserProfile(userProfile);

        GroupPostComment groupPostComment = new GroupPostComment();
        groupPostComment.setContent("Comment content");
        groupPostComment.setGroupMembership(groupMembership);

        when(groupPostRepository.findById(groupPostId)).thenReturn(Optional.of(groupPost));
        when(groupMembershipRepository.findByGroupAndUserProfileEmail(group, "a@dal.ca")).thenReturn(Optional.of(groupMembership));
        Optional<String> result = groupPostCommentService.createGroupPostComment(groupPostId, groupPostComment);
        assertFalse(result.isPresent());
    }

    @Test
    public void createGroupPostComment_groupPostNotFound() {
        int groupPostId = 1;
        GroupPostComment groupPostComment = new GroupPostComment();
        when(groupPostRepository.findById(groupPostId)).thenReturn(Optional.empty());
        Optional<String> result = groupPostCommentService.createGroupPostComment(groupPostId, groupPostComment);
        assertTrue(result.isPresent());
        assertEquals("Group post id not found, fail to create group post comment.", result.get());
    }

    @Test
    public void createGroupPostComment_userNotGroupMember() {
        int groupPostId = 1;
        Group group = new Group();
        GroupPost groupPost = new GroupPost(group, "Content", "a@dal.ca", "picture");
        groupPost.setGroupPostId(groupPostId);

        UserProfile userProfile = new UserProfile();
        userProfile.setEmail("a@dal.ca");
        GroupMembership groupMembership = new GroupMembership();
        groupMembership.setUserProfile(userProfile);

        GroupPostComment groupPostComment = new GroupPostComment();
        groupPostComment.setContent("Comment content");
        groupPostComment.setGroupMembership(groupMembership);

        when(groupPostRepository.findById(groupPostId)).thenReturn(Optional.of(groupPost));
        when(groupMembershipRepository.findByGroupAndUserProfileEmail(group, "a@dal.ca")).thenReturn(Optional.empty());
        Optional<String> result = groupPostCommentService.createGroupPostComment(groupPostId, groupPostComment);
        assertTrue(result.isPresent());
        assertEquals("User is not a member of the group, fail to create group post comment.", result.get());
    }

    @Test
    public void createGroupPostComment_success_check_notification() {
        int groupPostId = 1;
        Group group = new Group();
        group.setGroupName("Test Group");
        GroupPost groupPost = new GroupPost(group, "Group post content", "owner@dal.ca", "picture");
        groupPost.setGroupPostId(groupPostId);

        UserProfile commenterProfile = new UserProfile();
        commenterProfile.setEmail("commenter@dal.ca");
        GroupMembership commenterMembership = new GroupMembership();
        commenterMembership.setUserProfile(commenterProfile);

        GroupPostComment groupPostComment = new GroupPostComment();
        groupPostComment.setContent("Comment content");
        groupPostComment.setGroupMembership(commenterMembership);

        UserProfile ownerProfile = new UserProfile();
        ownerProfile.setEmail("owner@dal.ca");

        when(groupPostRepository.findById(groupPostId)).thenReturn(Optional.of(groupPost));
        when(groupMembershipRepository.findByGroupAndUserProfileEmail(group, "commenter@dal.ca")).thenReturn(Optional.of(commenterMembership));
        when(userProfileRepository.findById("owner@dal.ca")).thenReturn(Optional.of(ownerProfile));
        when(notificationService.createNotification(any(Notification.class))).thenReturn(Optional.empty());

        Optional<String> result = groupPostCommentService.createGroupPostComment(groupPostId, groupPostComment);
        assertFalse(result.isPresent());

        ArgumentCaptor<Notification> notificationCaptor = ArgumentCaptor.forClass(Notification.class);
        verify(notificationService, times(1)).createNotification(notificationCaptor.capture());
        Notification capturedNotification = notificationCaptor.getValue();

        assertEquals("User commenter@dal.ca commented on your post in group Test Group", capturedNotification.getContent());
        assertEquals("GroupPostComment", capturedNotification.getNotificationType());
        assertEquals("owner@dal.ca", capturedNotification.getUserProfile().email());
    }

    @Test
    public void createGroupPostComment_no_notification_for_self_comment() {
        int groupPostId = 1;
        Group group = new Group();
        group.setGroupName("Test Group");
        GroupPost groupPost = new GroupPost(group, "Group post content", "self@dal.ca", "picture");
        groupPost.setGroupPostId(groupPostId);

        UserProfile selfProfile = new UserProfile();
        selfProfile.setEmail("self@dal.ca");
        GroupMembership selfMembership = new GroupMembership();
        selfMembership.setUserProfile(selfProfile);

        GroupPostComment groupPostComment = new GroupPostComment();
        groupPostComment.setContent("Self comment content");
        groupPostComment.setGroupMembership(selfMembership);

        when(groupPostRepository.findById(groupPostId)).thenReturn(Optional.of(groupPost));
        when(groupMembershipRepository.findByGroupAndUserProfileEmail(group, "self@dal.ca")).thenReturn(Optional.of(selfMembership));

        Optional<String> result = groupPostCommentService.createGroupPostComment(groupPostId, groupPostComment);
        assertFalse(result.isPresent());

        verify(notificationService, never()).createNotification(any(Notification.class));
    }

    /**
     * Test case for deleteGroupPostCommentById
     */
    @Test
    public void deleteGroupPostCommentById_existOne_deleteOne() {
        int groupPostCommentId = 1;
        GroupPostComment groupPostComment = new GroupPostComment();
        groupPostComment.setGroupPostCommentId(groupPostCommentId);
        when(groupPostCommentRepository.findById(groupPostCommentId)).thenReturn(Optional.of(groupPostComment));
        Optional<String> result = groupPostCommentService.deleteGroupPostCommentById(groupPostCommentId);
        assertFalse(result.isPresent());
    }

    @Test
    public void deleteGroupPostCommentById_notFound() {
        int groupPostCommentId = 1;
        when(groupPostCommentRepository.findById(groupPostCommentId)).thenReturn(Optional.empty());
        Optional<String> result = groupPostCommentService.deleteGroupPostCommentById(groupPostCommentId);
        assertTrue(result.isPresent());
        assertEquals("Group post comment id not found, fail to delete group post comment.", result.get());
    }

    @Test
    public void deleteGroupPostCommentById_existTwo_deleteOne() {
        int groupPostCommentId1 = 1;
        int groupPostCommentId2 = 2;
        GroupPostComment groupPostComment1 = new GroupPostComment();
        groupPostComment1.setGroupPostCommentId(groupPostCommentId1);
        GroupPostComment groupPostComment2 = new GroupPostComment();
        groupPostComment2.setGroupPostCommentId(groupPostCommentId2);

        when(groupPostCommentRepository.findById(groupPostCommentId1)).thenReturn(Optional.of(groupPostComment1));
        when(groupPostCommentRepository.findById(groupPostCommentId2)).thenReturn(Optional.of(groupPostComment2));

        // delete the first GroupPostComment
        Optional<String> result = groupPostCommentService.deleteGroupPostCommentById(groupPostCommentId1);
        assertFalse(result.isPresent());

        // ensure the second GroupPostComment is still exist
        Optional<GroupPostComment> remainingGroupPostComment = groupPostCommentRepository.findById(groupPostCommentId2);
        assertTrue(remainingGroupPostComment.isPresent());
        assertEquals(groupPostCommentId2, remainingGroupPostComment.get().getGroupPostCommentId().intValue());
    }

    /**
     * Test case for getCommentsByGroupPostId
     */
    @Test
    public void getCommentsByGroupPostId_success() {
        int groupPostId = 1;
        Group group = new Group();
        group.setGroupId(1);

        UserProfile userProfile = new UserProfile();
        userProfile.setEmail("a@dal.ca");
        userProfile.setUserName("User");
        userProfile.setProfilePictureUrl("profilePicUrl");

        GroupMembership groupMembership = new GroupMembership();
        groupMembership.setUserProfile(userProfile);

        GroupPost groupPost = new GroupPost(group, "Content", "a@dal.ca", "picture");
        groupPost.setGroupPostId(groupPostId);

        GroupPostComment comment1 = new GroupPostComment();
        comment1.setGroupPostCommentId(1);
        comment1.setContent("Comment 1");
        comment1.setGroupPost(groupPost);
        comment1.setGroupPostCommentCreateTime(LocalDateTime.now().minusHours(1));
        comment1.setGroupMembership(groupMembership);

        GroupPostComment comment2 = new GroupPostComment();
        comment2.setGroupPostCommentId(2);
        comment2.setContent("Comment 2");
        comment2.setGroupPost(groupPost);
        comment2.setGroupPostCommentCreateTime(LocalDateTime.now());
        comment2.setGroupMembership(groupMembership);

        groupPost.setGroupPostCommentList(Arrays.asList(comment1, comment2));

        when(groupPostRepository.findById(groupPostId)).thenReturn(Optional.of(groupPost));
        List<GroupPostCommentDTO> result = groupPostCommentService.getCommentsByGroupPostId(groupPostId);
        assertEquals(2, result.size());
        assertEquals("Comment 2", result.get(0).groupPostCommentContent());
        assertEquals("Comment 1", result.get(1).groupPostCommentContent());
    }

    @Test
    public void getCommentsByGroupPostId_noComments() {
        int groupPostId = 1;
        Group group = new Group();
        GroupPost groupPost = new GroupPost(group, "Content", "a@dal.ca", "picture");
        groupPost.setGroupPostId(groupPostId);
        groupPost.setGroupPostCommentList(Collections.emptyList());
        when(groupPostRepository.findById(groupPostId)).thenReturn(Optional.of(groupPost));
        List<GroupPostCommentDTO> result = groupPostCommentService.getCommentsByGroupPostId(groupPostId);
        assertTrue(result.isEmpty());
    }

    @Test
    public void getCommentsByGroupPostId_postNotFound() {
        int groupPostId = 1;
        when(groupPostRepository.findById(groupPostId)).thenReturn(Optional.empty());
        List<GroupPostCommentDTO> result = groupPostCommentService.getCommentsByGroupPostId(groupPostId);
        assertTrue(result.isEmpty());
    }
}