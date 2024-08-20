import React, { useState, useEffect } from "react";
import "../../assets/styles/Pages/Group/GroupPage.css";
import { handleGetGroupUserIsMember } from "../../axios/Group/GroupAxios";
import { useSelector } from "react-redux";
import Header from "../../Components/Main/Header";
import Group from "../../Components/Group/Group";
import { filterGroups } from "../../utils/filterFunctions.js";


const GroupPage = () => {
	const user = useSelector((state) => state.user.user);
	const [groups, setGroups] = useState({ created: [], joined: [] });
	const [filteredGroups, setFilteredGroups] = useState({ created: [], joined: [] });
	const [searchGroupQuery, setSearchGroupQuery] = useState("");
	const [showAllGroups, setShowAllGroups] = useState({ created: false, joined: false });

	useEffect(() => {
		const fetchGroups = async () => {
			try {
				if (user) {
					const userGroups = await handleGetGroupUserIsMember(user.email);
					const created = userGroups.filter(group => group.groupCreatorEmail === user.email);
					const joined = userGroups.filter(group => group.groupCreatorEmail !== user.email);
					setGroups({ created, joined });
					setFilteredGroups({ created, joined });
				}
			} catch (err) {
				console.error(err);
			}
		};
		fetchGroups();
	}, [user]);

	useEffect(() => {
		const filteredCreated = filterGroups(groups.created, searchGroupQuery);
		const filteredJoined = filterGroups(groups.joined, searchGroupQuery);
		setFilteredGroups({ created: filteredCreated, joined: filteredJoined });
	}, [searchGroupQuery, groups]);

	const handleToggleShowGroups = (type) => {
		setShowAllGroups((prev) => ({ ...prev, [type]: !prev[type] }));
	};

	const renderGroupSection = (type) => {

		if (filteredGroups[type].length === 0) {
			return <p className="no-groups">There is no group available.</p>;
		}

		const groupsToShow = showAllGroups[type] ? filteredGroups[type] : filteredGroups[type].slice(0, 4);

		return (
			<>
				<div className="group-content">
					{groupsToShow.map((group) => (
						<Group key={group.groupId} group={group} />
					))}
				</div>
				{filteredGroups[type].length > 4 && (
					<p onClick={() => handleToggleShowGroups(type)} className="toggle-groups">
						{showAllGroups[type] ? "View Less" : "View More"}
					</p>
				)}
			</>
		);
	};

	const handleInputChange = (e) => setSearchGroupQuery(e.target.value);

	return (
		<div className="main-page">
			<Header />
			<div className="content">
				<div>
					<div className="group-page-search-and-create">
						<div className="group-page-search-bar">
							<input type="text" placeholder="Search Available Groups..." onChange={handleInputChange}/>
						</div>

						<a href="/group/creategroup/" className="create-group-button">
							<span className="text-hide">Create a New Group</span>
						</a>
					</div>

					<div className="group-list-container">
						<h2>Created Groups:</h2>
						{renderGroupSection("created")}

						<h2>Joined Groups:</h2>
						{renderGroupSection("joined")}
					</div>
				</div>
			</div>
		</div>
	);
};

export default GroupPage;
