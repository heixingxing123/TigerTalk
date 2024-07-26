import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import axios from "axios";
import { useParams, useNavigate } from "react-router-dom";
// import ProfileNavBar from "../components/ProfileNavBar";
import NavBar from "../components/NavBar";
import Post from "../components/Post";
import Header from "../components/Header";
import "../assets/styles/ProfilePage.css";
import { formatPost } from "./../utils/formatPost";

const getStatusColor = (status) => {
	switch (status) {
		case "available":
			return "green";
		case "busy":
			return "#DC143C";
		case "away":
			return "#FDDA0D";
		default:
			return "gray";
	}
};

const ProfilePage = () => {
	const user = useSelector((state) => state.user.user);
	const dispatch = useDispatch();
	const { userEmail } = useParams();
	const [profileUser, setProfileUser] = useState(null);
	const [isFriend, setIsFriend] = useState(false);
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
					const response = await axios.get(
						`http://localhost:8085/api/user/getByEmail/${email}`
					);
					const data = response.data;
					dispatch({ type: "SET_USER", payload: data });
				} catch (error) {
					console.error("Error fetching current user data:", error);
				}
			};

			const fetchProfileUser = async (email) => {
				try {
					const response = await axios.get(
						`http://localhost:8085/api/user/getByEmail/${email}`
					);
					const data = response.data;
					setProfileUser(data);
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
					const response = await axios.get(
						`http://localhost:8085/friendships/areFriends/${userEmail}/${user.email}`
					);
					setIsFriend(response.data);
					if (response.data) {
						setFriendButtonText("You are friends");
					}
				} catch (error) {
					console.error("Error checking friendship status:", error);
				}
			};

			checkAreFriends();
		}
	}, [profileUser, user, userEmail]);

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
			const response = await axios.get(
				`http://localhost:8085/posts/getPostForUser/${email}`
			);
			const transformedPosts = formatPost(response.data);
			setPosts(transformedPosts);
		} catch (err) {
			console.error("There was an error fetching posts!", err);
		}
	};
	console.log(profileUser);
	const handleAddFriend = async () => {
		try {
			await axios.post("http://localhost:8085/friendshipRequests/send", null, {
				params: {
					senderEmail: user.email,
					receiverEmail: profileUser.email,
				},
			});

			setFriendButtonText("Friend request sent");
		} catch (error) {
			if (
				error.response.data ===
				"Friendship request has already existed between these users."
			) {
				setFriendButtonText("Friend request pending");
			}
			console.error("Error sending friend request:", error);
		}
	};

	const handleDeletePost = (postId) => {
		setPosts(posts.filter((post) => post.id !== postId));
	};
	return (
		<div className="profile-page">
			<Header />
			<div className="profile-content">
				<div className="profile-nav">
					{/* {profileUser && <ProfileNavBar profileUser={profileUser} />} */}
					{profileUser && <NavBar profileUser={profileUser} />}
				</div>
				{profileUser && (
					<div className="profile-main-content">
						<div className="profile-page-user-info">
							<div className="profile-page-user-info-container">
								<div className="profile-page-user-info-picture-container">
									<div className="profile-page-user-info-picture">
										<img
											src={profileUser && profileUser.profilePictureUrl}
											alt="user profile"
										/>
									</div>
								</div>
								<div className="profile-page-user-info-text">
									<h2 className="profile-page-profile-name-status">
										{profileUser.userName}
										<span
											className="profile-page-status-circle"
											style={{
												backgroundColor: getStatusColor(
													profileUser.onlineStatus
												),
											}}
										></span>
									</h2>
									{showSetting && (
										<button
											className="profile-button"
											onClick={() => navigate(`/profile/edit`)}
										>
											Edit profile
										</button>
									)}

									<p className="profile-stats">
										<span>
											<strong>Posts: </strong>
											{posts.length} posts
										</span>
										<span>
											<strong>Age:</strong> {profileUser.age}
										</span>
										<span>
											<strong>Gender:</strong> {profileUser.gender}
										</span>
										<span>
											<strong>Role:</strong> {profileUser.role}
										</span>
									</p>
									<p>
										<strong>
											{profileUser.firstName} {profileUser.lastName}
										</strong>
									</p>
									<p>
										<strong>Bio: {profileUser.biography}</strong>
									</p>
								</div>
							</div>
						</div>
						<div className="profile-content-post-list">
							<div className="profile-content-post">
								{profileUser && profileUser.email !== user.email && (
									<button
										className={`friend-button-${
											isFriend ? "friend" : "not-friend"
										}`}
										onClick={handleAddFriend}
									>
										{friendButtonText}
									</button>
								)}
								{message.length > 0 ? (
									<p>{message}</p>
								) : (
									posts.map((post) => (
										<Post
											key={post.id}
											post={post}
											user={user}
											removePost={handleDeletePost}
										/>
									))
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
