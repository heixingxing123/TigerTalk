package tigertalk.model.Friend;

import com.fasterxml.jackson.annotation.JsonBackReference;
import tigertalk.model.User.UserProfile;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "friendship_request")
public class FriendshipRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer friendshipRequestId;

    @ManyToOne
    @JoinColumn(name = "sender_email", referencedColumnName = "email")
    @JsonBackReference("sender-friendship-request")
    private UserProfile sender;

    @ManyToOne
    @JoinColumn(name = "receiver_email", referencedColumnName = "email")
    @JsonBackReference("receiver-friendship-request")
    private UserProfile receiver;

    private String senderEmailTemp;
    private String receiverEmailTemp;

    private String senderProfilePictureUrlTemp;
    private String receiverProfilePictureUrlTemp;

    private String senderUserNameTemp;
    private String receiverUserNameTemp;

    private LocalDateTime createTime = LocalDateTime.now();


    public FriendshipRequest(UserProfile sender,
                             UserProfile receiver) {
        this.sender = sender;
        this.receiver = receiver;
        this.senderEmailTemp = sender.email();
        this.receiverEmailTemp = receiver.email();
        this.senderProfilePictureUrlTemp = sender.getProfilePictureUrl();
        this.receiverProfilePictureUrlTemp = receiver.getProfilePictureUrl();
        this.senderUserNameTemp = sender.userName();
        this.receiverUserNameTemp = receiver.userName();
    }

    public FriendshipRequest() {
    }


    public Integer getFriendshipRequestId() {
        return friendshipRequestId;
    }

    public void setFriendshipRequestId(Integer friendshipRequestId) {
        this.friendshipRequestId = friendshipRequestId;
    }

    public UserProfile getSender() {
        return sender;
    }

    public void setSender(UserProfile userFriendshipSender) {
        this.sender = userFriendshipSender;
    }

    public UserProfile getReceiver() {
        return receiver;
    }

    public void setReceiver(UserProfile userFriendshipReceiver) {
        this.receiver = userFriendshipReceiver;
    }


    public LocalDateTime getCreateTime() {
        return createTime;
    }
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }


    public String getSenderEmailTemp() {
        return senderEmailTemp;
    }

    public void setSenderEmailTemp(String senderEmail) {
        this.senderEmailTemp = senderEmail;
    }

    public String getReceiverEmailTemp() {
        return receiverEmailTemp;
    }

    public void setReceiverEmailTemp(String receiverEmail) {
        this.receiverEmailTemp = receiverEmail;
    }


    public String getSenderProfilePictureUrlTemp() {
        return senderProfilePictureUrlTemp;
    }

    public void setSenderProfilePictureUrlTemp(String senderProfilePictureUrlTemp) {
        this.senderProfilePictureUrlTemp = senderProfilePictureUrlTemp;
    }

    public String getReceiverProfilePictureUrlTemp() {
        return receiverProfilePictureUrlTemp;
    }

    public void setReceiverProfilePictureUrlTemp(String receiverProfilePictureUrlTemp) {
        this.receiverProfilePictureUrlTemp = receiverProfilePictureUrlTemp;
    }

    public String getSenderUserNameTemp() {
        return senderUserNameTemp;
    }

    public void setSenderUserNameTemp(String senderUserNameTemp) {
        this.senderUserNameTemp = senderUserNameTemp;
    }

    public String getReceiverUserNameTemp() {
        return receiverUserNameTemp;
    }

    public void setReceiverUserNameTemp(String receiverUserNameTemp) {
        this.receiverUserNameTemp = receiverUserNameTemp;
    }

    public FriendshipRequestDTO toDto() {
        return new FriendshipRequestDTO(
                this.friendshipRequestId,
                this.sender.email(),
                this.sender.userName(),
                this.receiver.email(),
                this.receiver.userName(),
                this.sender.getProfilePictureUrl(),
                this.receiver.getProfilePictureUrl(),
                this.createTime
        );
    }

}
