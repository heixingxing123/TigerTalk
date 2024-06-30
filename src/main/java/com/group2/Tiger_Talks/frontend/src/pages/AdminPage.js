import React, { useEffect, useState } from "react";
import NavBar from "../components/NavBar";
import Header from "../components/Header";
import UserList from "../components/UserList";
import axios from "axios";
import "../assets/styles/AdminPage.css";
// TODO :: {Raphael} Fix admins cyclic reference using new links to dto
const AdminPage = () => {
	const [selectedUsers, setSelectedUsers] = useState([]);
	const [data, setData] = useState([]);
	useEffect(() => {
		const fetchData = async () => {
			try {
				const response = await axios.get(
					"http://localhost:8085/api/user/getAllProfiles"
				);
				setData(response.data);
			} catch (error) {
				console.error("Error fetching user data:", error);
			}
		};
		fetchData();
	}, []);

	const handleEnableDisableUsers = async () => {
		try {
			const promises = selectedUsers.map((email) =>
				axios.put(`http://localhost:8085/api/user/update`, {
					...data.find((user) => user.email === email),
					validated: !data.find((user) => user.email === email).validated,
				})
			);
			await Promise.all(promises);

			setData((prevData) =>
				prevData.map((user) =>
					selectedUsers.includes(user.email)
						? { ...user, validated: !user.validated }
						: user
				)
			);
			console.log("Users enabled/disabled successfully");
			setSelectedUsers([]);
		} catch (error) {
			console.error("Error enabling/disabling users:", error);
		}
	};
	const handleDeleteUsers = async () => {
		try {
			const promises = selectedUsers.map((email) =>
				axios.delete(`http://localhost:8085/api/user/deleteByEmail/${email}`)
			);
			await Promise.all(promises);

			setData((prevData) =>
				prevData.filter((user) => !selectedUsers.includes(user.email))
			);

			console.log("Users deleted successfully");
			setSelectedUsers([]);
		} catch (error) {
			console.error("Error deleting users:", error);
		}
	};

	return (
		<div className="admin-page">
			<Header />
			<div className="content">
				<div className="admin-nav">
					<NavBar />
				</div>
				<div className="admin-content">
					<UserList
						selectedUsers={selectedUsers}
						setSelectedUsers={setSelectedUsers}
						setData={setData}
						data={data}
					/>
					<div className="admin-buttons">
						<button
							className="delete-user-button"
							disabled={selectedUsers.length === 0}
							onClick={handleDeleteUsers}
						>
							Delete Users
						</button>
						<button
							className="toggle-user-button"
							disabled={selectedUsers.length === 0}
							onClick={handleEnableDisableUsers}
						>
							Enable/Disable Users
						</button>
					</div>
				</div>
			</div>
		</div>
	);
};

export default AdminPage;
