package tigertalk.service.Group;

import tigertalk.model.Group.GroupDTO;
import tigertalk.model.Group.GroupMembershipDTO;

import java.util.List;
import java.util.Optional;

public interface GroupService {

    /**
     * Creates a new group with the specified name, creator, and privacy setting.
     *
     * @param groupName    the name of the group to be created.
     * @param creatorEmail the email of the user creating the group.
     * @param isPrivate    whether the group is private or public.
     * @return an Optional containing a success message if the group is created,
     * or an error message if the creator email is not found.
     */
    Optional<String> createGroup(String groupName, String creatorEmail, boolean isPrivate, String interest);

    /**
     * Attempts to add a user as a regular member to a group.
     *
     * @param email   the email of the user to be added to the group
     * @param groupId the ID of the group to join
     * @return an {@link Optional} containing an error message if the user or group is not found,
     * if the group is private, or if the user is already a member of the group,
     * otherwise an empty {@link Optional} indicating the user was successfully added to the group
     */
    Optional<String> joinGroupUser(String email, int groupId);

    /**
     * Attempts to add a user as an admin to a group.
     *
     * @param userEmail the email of the user to be added to the group
     * @param groupId   the ID of the group to join
     * @return an {@link Optional} containing an error message if the user or group is not found,
     * if the user is already a member of the group, otherwise an empty {@link Optional}
     * indicating the user was successfully added as an admin to the group
     */
    Optional<String> joinGroupAdmin(String userEmail, int groupId);

    /**
     * Retrieves a list of all groups.
     *
     * @return a list of GroupDTO objects representing all groups.
     */
    List<GroupDTO> getAllGroups();

    /**
     * Retrieves details of a specific group by its ID.
     *
     * @param groupId the ID of the group to retrieve.
     * @return An optional of a GroupDTO object representing the group, or empty if the group is not found.
     */
    Optional<GroupDTO> getGroup(int groupId);

    /**
     * Retrieves a list of all groups that a user is a member of or is the creator of.
     *
     * @param userEmail the email of the user.
     * @return a list of GroupDTO objects representing the groups the user is associated with.
     */
    List<GroupDTO> getAllGroupsByUser(String userEmail);

    /**
     * Updates the information of an existing group.
     *
     * @param groupUpdate the updated group information.
     * @return an Optional containing a success message if the group is updated,
     * or an error message if the group ID is not found.
     */
    Optional<String> updateGroupInfo(GroupDTO groupUpdate);

    /**
     * Deletes a group by its ID.
     *
     * @param groupId the ID of the group to delete.
     * @return an Optional containing a success message if the group is deleted,
     * or an error message if the group ID is not found.
     */
    Optional<String> deleteGroup(int groupId);

    /**
     * Deletes a group membership by its ID.
     *
     * @param groupMembershipId the ID of the group membership to delete.
     * @return an Optional containing a success message if the group membership is deleted,
     * or an error message if the group membership ID is not found.
     */
    Optional<String> deleteGroupMembership(int groupMembershipId);


    /**
     * Retrieves a list of group members by the group ID.
     *
     * @param groupId the ID of the group.
     * @return a list of GroupMembershipDTO objects representing the members of the group.
     */
    List<GroupMembershipDTO> getGroupMembersByGroupId(int groupId);


    /**
     * Get the membership id of a specific group by their email and group ID.
     *
     * @param userEmail the email of the user.
     * @param groupId   the ID of the group.
     * @return The membership id if the user is a member of the group, Empty otherwise.
     */
    Optional<Integer> getMemberShipId(String userEmail, int groupId);


    Optional<String> transferGroupOwnership(int previousOwnerMembershipId, int newOwnerMembershipId);
}
