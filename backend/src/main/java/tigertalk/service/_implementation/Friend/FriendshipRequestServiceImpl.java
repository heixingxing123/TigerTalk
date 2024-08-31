package tigertalk.service._implementation.Friend;

import tigertalk.model.Friend.Friendship;
import tigertalk.model.Friend.FriendshipRequest;
import tigertalk.model.Friend.FriendshipRequestDTO;
import tigertalk.model.Notification.Notification;
import tigertalk.model.User.UserProfile;
import tigertalk.repository.Friend.FriendshipRepository;
import tigertalk.repository.Friend.FriendshipRequestRepository;
import tigertalk.repository.User.UserProfileRepository;
import tigertalk.service.Friend.FriendshipRequestService;
import tigertalk.service.Notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FriendshipRequestServiceImpl implements FriendshipRequestService {

    @Autowired
    private FriendshipRequestRepository friendshipRequestRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private NotificationService notificationService;


    // We have E->A, D->A, get(A) will get E, D
    @Override
    public List<FriendshipRequestDTO> getAllFriendRequests(String email) {
        Optional<UserProfile> optionalUserProfile = userProfileRepository.findUserProfileByEmail(email);
        if (optionalUserProfile.isEmpty()) {
            throw new IllegalStateException("User not found");
        }
        UserProfile user = optionalUserProfile.get();

        List<FriendshipRequestDTO> list = new ArrayList<>();
        for (FriendshipRequest friendshipRequest : friendshipRequestRepository.findByReceiver(user)) {
            FriendshipRequestDTO dto = friendshipRequest.toDto();
            list.add(dto);
        }
        return list;
    }

    @Override
    public Optional<String> sendFriendshipRequest(String senderEmail, String receiverEmail) {
        UserProfile sender = userProfileRepository.findUserProfileByEmail(senderEmail)
                .orElseThrow(() -> new IllegalStateException("Sender not found"));
        UserProfile receiver = userProfileRepository.findUserProfileByEmail(receiverEmail)
                .orElseThrow(() -> new IllegalStateException("Receiver not found"));

        if (friendshipRepository.findBySenderAndReceiver(sender, receiver).isPresent()) {
            return Optional.of("Friendship has already existed between these users.");
        }
        if (friendshipRepository.findBySenderAndReceiver(receiver, sender).isPresent()) {
            return Optional.of("Friendship has already existed between these users.");
        }

        if (friendshipRequestRepository.findBySenderAndReceiver(sender, receiver).isPresent()) {
            return Optional.of("Friendship request has already existed between these users.");
        }
        if (friendshipRequestRepository.findBySenderAndReceiver(receiver, sender).isPresent()) {
            return Optional.of("Friendship request has already existed between these users.");
        }

        friendshipRequestRepository.save(new FriendshipRequest(sender, receiver));


        // send notification
        return notificationService.createNotification(new Notification(
                receiver,
                "You have a new friend request from " + senderEmail,
                "FriendshipRequestSend"));
    }

    @Override
    public Optional<String> acceptFriendshipRequest(Integer friendshipRequestId) {
        FriendshipRequest friendshipRequest = friendshipRequestRepository.findById(friendshipRequestId)
                .orElseThrow(() -> new IllegalStateException("friendship request ID does not exist!"));
        friendshipRepository.save(
                new Friendship(
                        friendshipRequest.getSender(),
                        friendshipRequest.getReceiver())
        );
        friendshipRequestRepository.delete(friendshipRequest);


        // send notification
        return notificationService.createNotification(new Notification(
                friendshipRequest.getSender(),
                "Your friend request to " + friendshipRequest.getReceiver().getEmail() + " has been accepted.",
                "FriendshipRequestAccept"
        ));
    }

    @Override
    public Optional<String> rejectFriendshipRequest(Integer friendshipRequestId) {
        Optional<FriendshipRequest> friendshipRequestOptional = friendshipRequestRepository.findById(friendshipRequestId);
        if (friendshipRequestOptional.isPresent()) {
            FriendshipRequest friendshipRequest = friendshipRequestOptional.get();
            friendshipRequestRepository.delete(friendshipRequest);
            Notification notification = new Notification(
                    friendshipRequest.getSender(),
                    "Your friend request to " + friendshipRequest.getReceiver().getEmail() + " has been rejected.",
                    "FriendshipRequestReject"
            );
            return notificationService.createNotification(notification);
        } else {
            throw new IllegalStateException("Friendship request ID does not exist!");
        }
    }

    @Override
    public int findNumOfTotalRequests() {
        return friendshipRequestRepository.findAll().size();
    }

    @Override
    public boolean areFriendshipRequestExist(String email1, String email2) {
        Optional<UserProfile> senderOptional = userProfileRepository.findUserProfileByEmail(email1);
        if (senderOptional.isEmpty()) {
            throw new IllegalStateException("Sender not found");
        }
        UserProfile sender = senderOptional.get();

        Optional<UserProfile> receiverOptional = userProfileRepository.findUserProfileByEmail(email2);
        if (receiverOptional.isEmpty()) {
            throw new IllegalStateException("Receiver not found");
        }
        UserProfile receiver = receiverOptional.get();

        return friendshipRequestRepository.findBySenderAndReceiver(sender, receiver).isPresent() ||
                friendshipRequestRepository.findBySenderAndReceiver(receiver, sender).isPresent();
    }


}
