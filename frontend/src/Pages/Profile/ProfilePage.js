import React, { useEffect, useState } from "react";
import "../../assets/styles/Pages/Profile/ProfilePage.css";
import { sendFriendRequest, areFriendshipRequestExist } from "../../axios/Friend/FriendshipRequestAxios";
import { areFriends } from "../../axios/Friend/FriendshipAxios";
import {deleteFriendshipByEmail} from "../../axios/Friend/FriendshipAxios";
import { getCurrentUser } from "../../axios/UserAxios";
import { FetchPostsOfOneUser } from "../../axios/Post/PostAxios";
import { MdCheckCircle, MdRemoveCircle, MdAccessTimeFilled } from 'react-icons/md';
import { IoMdCloseCircle } from "react-icons/io";
import { useDispatch, useSelector } from "react-redux";
import { useParams, useNavigate } from "react-router-dom";
import Post from "../../Components/Post/Post";
import Header from "../../Components/Main/Header";
import { formatPost } from "../../utils/formatPost";


const getStatusClass = (status) => {
	switch (status) {
		case "available":
			return <MdCheckCircle style={{ color: '#4caf50' }} />;
		case "busy":
			return <MdRemoveCircle style={{ color: '#f44336' }} />;
		case "away":
			return <MdAccessTimeFilled style={{ color: '#ff9800' }} />;
		default:
			return <IoMdCloseCircle style={{ color: '#9e9e9e' }} />;
	}
};


const ProfilePage = () => {
	const user = useSelector((state) => state.user.user);
	const dispatch = useDispatch();
	const { userEmail } = useParams();
	const [profileUser, setProfileUser] = useState(null);
	const [isFriend, setIsFriend] = useState(false);
	const [requestPending, setRequestPending] = useState(false);
	const [posts, setPosts] = useState([]);
	const [friendButtonText, setFriendButtonText] = useState("Add Friend");
	const [message, setMessage] = useState("");
	const paramUserEmail = useParams().userEmail;
	const navigate = useNavigate();
	const [showSetting, setShowSetting] = useState(false);

	useEffect(() => {
		if (paramUserEmail === user.email) {
			setShowSetting(true);
		} else {
			setShowSetting(false);
		}
	}, [paramUserEmail, user.email]);

	useEffect(() => {
		if (user?.email && userEmail) {
			const fetchCurrentUser = async (email) => {
				try {
					const response = await getCurrentUser(email);
					dispatch({ type: "SET_USER", payload: response });
				} catch (error) {
					console.error("Error fetching current user data:", error);
				}
			};

			const fetchProfileUser = async (email) => {
				try {
					const response = await getCurrentUser(email);
					setProfileUser(response);
				} catch (error) {
					console.error("Error fetching profile user data:", error);
				}
			};
			fetchCurrentUser(user.email);
			fetchProfileUser(userEmail);
		}
	}, [user?.email, userEmail, dispatch]);

	useEffect(() => {
		if (profileUser && user) {
			const checkAreFriends = async () => {
				try {
					const response = await areFriends(userEmail, user.email);
					setIsFriend(response);
					if (response) {
						setFriendButtonText("Unfriend");
					}
				} catch (error) {
					console.error("Error checking friendship status:", error);
				}
			};
			const checkFriendShipRequestExist = async () => {
				const response = await areFriendshipRequestExist(userEmail, user.email);
				setRequestPending(response);
				if (response) {
					setFriendButtonText("Pending");
				}
			};
			checkAreFriends();
			checkFriendShipRequestExist();
		}
	}, [profileUser, user, userEmail, friendButtonText]);

	useEffect(() => {
		if (profileUser && user) {
			if (profileUser.email === user.email || isFriend) {
				fetchPosts(profileUser.email);
				setMessage("");
			} else {
				setMessage("You are not friends so you can't see this person's posts.");
			}
		}
	}, [profileUser, user, isFriend]);

	const fetchPosts = async (email) => {
		try {
			const response = await FetchPostsOfOneUser(email);
			const transformedPosts = formatPost(response);
			setPosts(transformedPosts);
		} catch (err) {
			console.error("There was an error fetching posts!", err);
		}
	};

	const handleFriendShip = async () => {
		try {
			if (isFriend) {
				if (window.confirm("Are you sure to unfriend?")) {
					deleteFriendshipByEmail(user.email, profileUser.email);
					setIsFriend(false);
				}
			} else if (!requestPending) {
				let params = {
					senderEmail: user.email,
					receiverEmail: profileUser.email,
				};
				sendFriendRequest(params);
				window.location.reload();
			}
		} catch (error) {
			console.log("Error sending friend request:", error);
		}
	};

	const handleDeletePost = (postId) => {
		setPosts(posts.filter((post) => post.id !== postId));
	};
	return (
		<div className="main-page">
			<Header />
			<div className="content">
				{profileUser && (
					<div className="profile-main-content">
						<div className="profile-page-user-info">
							<div className="profile-page-user-info-container">
								<div className="profile-page-user-info-picture-container">
									<div className="profile-page-user-info-picture">
										<img src={profileUser && profileUser.profilePictureUrl} alt="user profile"/>
									</div>
								</div>
								<div className="profile-page-user-info-text">
									<h2 className="profile-page-profile-name-status">
										{profileUser.userName}
										<span className="profile-page-status-icon">{getStatusClass(profileUser.onlineStatus)}</span>
									</h2>
									{showSetting ? (
										<button className="profile-button" onClick={() => navigate(`/profile/edit`)}>Edit profile</button>
									) : (
										profileUser && profileUser.email !== user.email && (
											<button className={`profile-button`} onClick={handleFriendShip}>{friendButtonText}</button>
										)
									)}

									<p className="profile-stats">
										<span><strong>Posts: </strong>{posts.length} posts</span>
										<span><strong>Age:</strong> {profileUser.age}</span>
										<span><strong>Gender:</strong> {profileUser.gender}</span>
										<span><strong>Role:</strong> {profileUser.role}</span>
									</p>
									<p><strong>Full Name:</strong> {profileUser.firstName}{" "}{profileUser.lastName}</p>
									<p><strong>Bio: </strong>{profileUser.biography}</p>
								</div>
							</div>
						</div>
						<div className="profile-content-post-list">
							<div className="profile-content-post">
								{message.length > 0 ? (
									<p>{message}</p>
								) : (
									posts.map((post) => (<Post key={post.id} post={post} user={user} removePost={handleDeletePost}/>))
								)}
							</div>
						</div>
					</div>
				)}
			</div>
		</div>
	);
};

export default ProfilePage;
