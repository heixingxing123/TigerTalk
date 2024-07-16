import axios from "axios";
const URL = process.env.REACT_APP_API_URL;

export const handleCreateGroup = (form) => {
	console.log(form);
	return axios
		.post(
			`${URL}/api/groups/create/${form.groupName}/${form.userEmail}/${form.status}`
		)
		.then((response) => response.data)
		.catch((error) => {
			console.error("Error creating group");
			throw error;
		});
};

export const handleGetGroupById = (groupId) => {
	return axios
		.get(`${URL}/api/groups/get/group/${groupId}`)
		.then((response) => response.data)
		.catch((error) => {
			console.error("Error getting all groups");
			throw error;
		});
};

export const checkIsMember = (userEmail, groupId) => {
	return axios
		.get(`${URL}/api/groups/get/isMember/${userEmail}/${groupId}`)
		.then((response) => response.data)
		.catch((error) => {
			console.error("Error getting all groups");
			throw error;
		});
};
export const handleGetAllGroups = () => {
	return axios
		.get(`${URL}/api/groups/get/allGroups`)
		.then((response) => response.data)
		.catch((error) => {
			console.error("Error getting all groups");
			throw error;
		});
};

export const handleJoinGroup = (userEmail, groupId) => {
	return axios
		.post(`${URL}/api/groups/join/${userEmail}/${groupId}`)
		.then((response) => response.data)
		.catch((error) => {
			console.error("Error joining user to the group");
			throw error;
		});
};

export const handleLeaveGroup = (groupMembershipId) => {
	return axios
		.delete(`${URL}/api/groups/delete/groupMembership/${groupMembershipId}`)
		.then((response) => response.data)
		.catch((error) => {
			console.error("Error joining user to the group");
			throw error;
		});
};

//GROUP POST
export const handleCreatePost = (post) => {
	return axios
		.post(`${URL}/api/groups/post/create`, post)
		.then((response) => response.data)
		.catch((error) => {
			console.error("Error creating group post");
			throw error;
		});
};

export const handleGetAllPost = (groupId) => {
	return axios
		.get(`${URL}/api/groups/post/getAll/${groupId}`)
		.then((response) => response.data)
		.catch((error) => {
			console.error("Error creating group post");
			throw error;
		});
};
