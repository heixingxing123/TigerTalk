package tigertalk.service._implementation.Group;

import tigertalk.model.Group.GroupPost;
import tigertalk.model.Group.GroupPostComment;
import tigertalk.model.Group.GroupPostCommentDTO;
import tigertalk.model.Notification.Notification;
import tigertalk.model.User.UserProfile;
import tigertalk.repository.Group.GroupPostCommentRepository;
import tigertalk.repository.Group.GroupPostRepository;
import tigertalk.repository.User.UserProfileRepository;
import tigertalk.service.Group.GroupPostCommentService;
import tigertalk.service.Notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroupPostCommentServiceImpl implements GroupPostCommentService {

    @Autowired
    private GroupPostRepository groupPostRepository;

    @Autowired
    private GroupPostCommentRepository groupPostCommentRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private NotificationService notificationService;


    @Override
    public Optional<String> createGroupPostComment(int groupPostId, GroupPostComment groupPostComment) {
        Optional<GroupPost> groupPostOpt = groupPostRepository.findById(groupPostId);
        if (groupPostOpt.isPresent()) {
            GroupPost groupPost = groupPostOpt.get();
            groupPostComment.setGroupPost(groupPost);

            Optional<UserProfile> userProfileOpt = userProfileRepository.findById(groupPostComment.getGroupPostCommentCreator().email());

            if (userProfileOpt.isPresent()) {
                groupPostComment.setGroupPostCommentCreator( userProfileOpt.get());
                groupPostCommentRepository.save(groupPostComment);

                // Create and send notification to GroupPost owner if it's not the same as the commenter
                String groupPostOwnerEmail = groupPost.getUserProfile().email();
                String commenterEmail = groupPostComment.getGroupPostCommentCreator().email();
                if (!groupPostOwnerEmail.equals(commenterEmail)) {
                    Optional<UserProfile> groupPostOwnerOpt = userProfileRepository.findById(groupPostOwnerEmail);
                    if (groupPostOwnerOpt.isPresent()) {
                        UserProfile groupPostOwner = groupPostOwnerOpt.get();
                        Notification notification = new Notification(
                                groupPostOwner,
                                "User " + commenterEmail +
                                        " commented on your post in group " + groupPost.getGroup().getGroupName(),
                                "GroupPostComment"
                        );
                        notificationService.createNotification(notification);
                    }
                }

                return Optional.empty();
            } else {
                return Optional.of("user email not found, fail to create group post comment.");
            }
        } else {
            return Optional.of("Group post id not found, fail to create group post comment.");
        }
    }

    @Override
    public Optional<String> deleteGroupPostCommentById(int groupPostCommentId) {
        Optional<GroupPostComment> groupPostCommentOpt = groupPostCommentRepository.findById(groupPostCommentId);
        if (groupPostCommentOpt.isPresent()) {
            groupPostCommentRepository.deleteById(groupPostCommentId);
            return Optional.empty();
        } else {
            return Optional.of("Group post comment id not found, fail to delete group post comment.");
        }
    }

    @Override
    public List<GroupPostCommentDTO> getCommentsByGroupPostId(Integer groupPostId) {
        return groupPostRepository.findById(groupPostId)
                .map(groupPost -> groupPost.getGroupPostCommentList().stream()
                        .map(GroupPostComment::toDto)
                        .sorted(Comparator.comparing(GroupPostCommentDTO::groupPostCommentCreateTime).reversed())
                        .collect(Collectors.toList())
                )
                .orElse(Collections.emptyList());
    }

}