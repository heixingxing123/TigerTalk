import axios from "axios";

const API_URL = "http://localhost:8080/api";

const UserProfileService = {
    getHello: () => axios.get(`${API_URL}/hello`),
};

export default UserProfileService;
