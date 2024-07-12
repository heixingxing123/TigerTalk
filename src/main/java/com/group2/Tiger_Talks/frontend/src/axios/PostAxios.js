import axios from "axios";
const URL = process.env.REACT_APP_API_URL;

export const handleLikeAxios = (postId, userEmail) => {
	return axios
		.put(`${URL}/posts/like/${postId}?userEmail=${userEmail}`)
		.then((response) => response.data)
		.catch((error) => {
			console.error("Error liking post:", error);
			throw error;
		});
};

export const handleAddCommentAxios = (commentObj) => {
	return axios
		.post(`${URL}/api/comments/createComment`, commentObj)
		.then((response) => response.data)
		.catch((error) => {
			console.error("Adding comment error", error);
			throw error;
		});
};

export const getCommentFromPostId = (postId) => {
	return axios
		.get(`${URL}/api/comments/getComments/${postId}`)
		.then((response) => response.data)
		.catch((error) => {
			console.error("Adding comment error", error);
			throw error;
		});
};
