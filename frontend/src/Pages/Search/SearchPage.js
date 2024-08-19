import React, { useState, useEffect } from "react";
import "../../assets/styles/Pages/Search/SearchPage.css";
import { ImFilter } from "react-icons/im";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import Header from "../../Components/Main/Header";
import Dropdown from "../../Components/Search/DropDown";
import Group from "../../Components/Group/Group";
import { filterUsers, filterGroups } from "../../utils/filterFunctions";


const SearchPage = () => {
	const { globalUsers } = useSelector((state) => state.globalUsers);
	const { globalGroups } = useSelector((state) => state.globalGroups);

	const [filteredUsers, setFilteredUsers] = useState([]);
	const [filteredGroups, setFilteredGroups] = useState([]);
	const [searchQuery, setSearchQuery] = useState("");
	const [filterOption, setFilterOption] = useState("all");
	const [showFilterDropdown, setShowFilterDropdown] = useState(false);

	const navigate = useNavigate();

	useEffect(() => {
		const filteredUsersResult = filterUsers(
			globalUsers,
			searchQuery,
			filterOption
		);
		const filteredGroupsResult = filterGroups(
			globalGroups,
			searchQuery,
			filterOption
		);

		setFilteredUsers(filteredUsersResult);
		setFilteredGroups(filteredGroupsResult);
	}, [searchQuery, globalUsers, globalGroups, filterOption]);

	const handleChoose = (email) => {
		navigate(`/profile/${email}`);
	};

	const handleFilterChange = (option) => {
		setFilterOption(option);
		setSearchQuery(""); // Clear the search query when filter changes
	};

	const getFilterHeader = () => {
		switch (filterOption) {
			case "online":
				return "Online Users:";
			case "public":
				return "Public Groups:";
			default:
				return "Filter by:";
		}
	};

	return (
		<div className="main-page">
			<Header />
			<div className="content">
				<div className="search-content-container">
					<h2>{getFilterHeader()}</h2>
					<div className="filter-section">
						<div className="filter-icon" onClick={() => setShowFilterDropdown(!showFilterDropdown)}>
							<ImFilter />
						</div>
						{showFilterDropdown && (
							<div className="filter-dropdown">
								<button className="filter-button" onClick={() => handleFilterChange("all")}>
									Keyword
								</button>

								<button className="filter-button" onClick={() => handleFilterChange("online")}>
									Online Users
								</button>

								<button className="filter-button" onClick={() => handleFilterChange("public")}>
									Public Groups
								</button>
							</div>
						)}
					</div>

					<h2>Users matched with keyword:</h2>
					<div className="search-content-section">
					{filteredUsers && filteredUsers.length > 0 ? (
						<Dropdown items={filteredUsers} dropdownClassName="global" handleChoose={handleChoose}/>
					) : (<p>No users found with this keyword</p>)}
					</div>

					<h2>Groups matched with keyword:</h2>
					<div className="search-content-section">
						{filteredGroups && filteredGroups.length > 0 ? (
							filteredGroups.map((group) => (
								<div className="global-search-group-list" key={group.groupId}>
									<Group group={group} />
								</div>
							))
						) : (<p>No groups found with this keyword</p>)}
					</div>

				</div>
			</div>
		</div>
	);
};

export default SearchPage;
