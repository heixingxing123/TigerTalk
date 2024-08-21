import React, { useEffect, useState } from "react";
import "../../assets/styles/Pages/Profile/ProfileSettingsPage.css";
import {updateUser} from "../../axios/UserAxios";
import { useDispatch, useSelector } from "react-redux";
import Header from "../../Components/Main/Header";
import {useNavigate} from "react-router-dom";


const ProfileSettingsPage = () => {
	const dispatch = useDispatch();
	const user = useSelector((state) => state.user.user);
	const navigate = useNavigate();

	const [form, setForm] = useState({
		id: "",
		email: "",
		firstName: "",
		lastName: "",
		userName: "",
		biography: "",
		age: "",
		gender: "",
		profilePictureUrl: "",
		onlineStatus: "",
	});
	const [errors, setErrors] = useState({});
	const [uploading, setUploading] = useState(false);

	useEffect(() => {
		if (user) {
			setForm({
				id: user.id || "",
				email: user.email || "",
				firstName: user.firstName || "",
				lastName: user.lastName || "",
				userName: user.userName || "",
				biography: user.biography || "",
				age: user.age || "",
				gender: user.gender || "",
				profilePictureUrl: user.profilePictureUrl || "",
				onlineStatus: user.onlineStatus || "",
			});
		}
	}, [user]);

	const handleChange = (e) => {
		const { name, value } = e.target;
		setForm({...form, [name]: value});
	};


	const handleSubmit = async (e) => {
		e.preventDefault();
		setErrors({});
		const updatedUser = { ...user, ...form };
		try {
			const responseData = await updateUser(updatedUser);
			alert("Profile updated successfully");
			dispatch({ type: "SET_USER", payload: responseData });
			navigate(`/profile/${user.email}`);
		} catch (error) {
			if (error.response && error.response.data) {
				setErrors(error.response.data);
			} else {
				alert(
					"An error occurred while updating the profile. Please try again later."
				);
			}
		}
	};

	if (!user) {
		return <div>Loading...</div>;
	}

	return (
		<div className="main-page">
			<Header />
			<div className="content">
				<form className="profile-settings-form" onSubmit={handleSubmit}>


					<div className="form-group">
						<label>First Name</label>
						<input type="text" name="firstName" placeholder="First name" value={form.firstName} onChange={handleChange}/>
						{errors.firstName && <p className="error">{errors.firstName}</p>}
					</div>


					<div className="form-group">
						<label>Last Name</label>
						<input type="text" name="lastName" placeholder="Last name" value={form.lastName} onChange={handleChange}/>
						{errors.lastName && <p className="error">{errors.lastName}</p>}
					</div>


					<div className="form-group">
						<label>User Name</label>
						<input type="text" name="userName" placeholder="User name" value={form.userName} onChange={handleChange}/>
						{errors.userName && <p className="error">{errors.userName}</p>}
					</div>


					<div className="form-group">
						<label>Personal Interest</label>
						<input type="text" name="biography" placeholder="Personal Interest" value={form.biography} onChange={handleChange}/>
						{errors.biography && <p className="error">{errors.biography}</p>}
					</div>


					<div className="form-group">
						<label>Age</label>
						<input type="number" name="age" placeholder="Age" value={form.age} onChange={handleChange}/>
						{errors.age && <p className="error">{errors.age}</p>}
					</div>


					<div className="form-group">
						<label>Gender</label>
						<select name="gender" value={form.gender} onChange={handleChange}>
							<option value="">Select Gender</option>
							<option value="male">Male</option>
							<option value="female">Female</option>
							<option value="other">Other</option>
						</select>
						{errors.gender && <p className="error">{errors.gender}</p>}
					</div>


					<button type="submit" disabled={uploading}>{uploading ? "Uploading..." : "Update Profile"}</button>


				</form>
			</div>
		</div>
	);
};

export default ProfileSettingsPage;
