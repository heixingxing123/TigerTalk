import React from "react";
import "../../assets/styles/Components/Friend/FriendRequest.css";
import {acceptFriendRequest, rejectFriendRequest} from "../../axios/Friend/FriendshipRequestAxios";
import {formatDate} from "../../utils/formatDate";


const FriendRequest = ({request}) => {
    const handleAccept = async () => {
        try {
            const response = await acceptFriendRequest(request.id);
            if (response.status === 200) {
                window.alert("Friend request accepted!");
                window.location.reload();
            }
        } catch (error) {
            window.alert("Failed to accept friend request. Please try again.");
            console.error(error);
        }
    };

    const handleReject = async () => {
        try {
            const response = await rejectFriendRequest(request.id);
            if (response.status === 200) {
                window.alert("Friend request rejected!");
                window.location.reload();
            }
        } catch (error) {
            window.alert("Failed to reject friend request. Please try again.");
            console.error(error);
        }
    };

    return (
        <div className="friend-request">
            <div className="friend-request-header">
                <div className="friend-request-friend-picture">
                    <img src={request.senderProfilePictureUrl} alt="avatar"/>
                </div>
                <div className="friend-request-friend-details">
                    <a href={"/profile/" + request.senderEmail}>{request.senderName}</a>
                    <div>Email: {request.senderEmail}</div>
                    <div>{formatDate(request.createTime)}</div>
                </div>
            </div>
            <div className="friend-request-actions">
                <button className="accept-button" onClick={handleAccept}>Accept</button>
                <button className="reject-button" onClick={handleReject}>Reject</button>
            </div>
        </div>
    );
};

export default FriendRequest;
