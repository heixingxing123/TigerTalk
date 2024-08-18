import { useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
// Icon:
import { FaLock, FaUnlock } from "react-icons/fa";
// CSS:
import "../../assets/styles/Components/Group/Group.css";


const Group = ({ group }) => {
	const email = useSelector((state) => state.user.user.email);
	const member = group.groupMemberList.find(
		(mem) => mem.userProfileDTO.email === email
	);
	const navigate = useNavigate();

	return (
		<div className="group">
			<div className="group-header">

				<div className="one-group-background-image" onClick={() => {navigate(`/group/viewgroup/${group.groupId}`);}}>
					<img src={group.groupImg} alt="Group cover" />
				</div>

				<div className="group-creator-details" onClick={() => {navigate(`/group/viewgroup/${group.groupId}`);}}>
					{group.groupName} {group.isPrivate ? <FaLock /> : <FaUnlock />}
					<p>{group.dateCreated}</p>
				</div>

			</div>
		</div>
	);
};

export default Group;
