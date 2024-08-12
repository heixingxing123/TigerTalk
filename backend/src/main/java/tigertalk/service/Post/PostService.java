package tigertalk.service.Post;

import tigertalk.model.Post.Post;
import tigertalk.model.Post.PostDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface PostService {

    /**
     * Retrieves posts for a user and their friends based on the user's email.
     *
     * @param email the email of the user
     * @return a list of posts sorted by timestamp in descending order
     */
    List<PostDTO> getPostsForUserAndFriends(String email);

    /**
     * Retrieves posts for a user's friends based on the user's email.
     *
     * @param email the email of the user
     * @return a list of posts sorted by timestamp in descending order
     */

    List<PostDTO> getPostsForUser(String email);

    /**
     * Creates a new post.
     *
     * @param post the post to be created
     * @return an optional containing an error message if the user does not exist,
     * or empty if the post was created successfully
     */
    Optional<String> createPost(Post post);

    /**
     * Deletes a post by its ID.
     *
     * @param postId the ID of the post to be deleted
     * @return an optional containing an error message if the post does not exist,
     * or empty if the post was deleted successfully
     */
    Optional<String> deletePostById(Integer postId);

    /**
     * Deletes a post.
     *
     * @param post the post to be deleted
     * @return an optional containing an error message if the post does not exist, or empty if the post was deleted successfully
     */
    Optional<String> deletePost(Post post);

    /**
     * Edits the content of an existing post.
     *
     * @param postId the ID of the post to be edited
     * @param newContent the new content for the post
     * @return the updated Post object
     * @throws RuntimeException if the post with the given ID is not found
     */
    Post editPost(Integer postId, String newContent);

    /**
     * Updates a post by its ID.
     *
     * @param postId the ID of the post to be updated
     * @param post   the updated post-data
     * @return an optional containing an error message if the post does not exist,
     * or empty if the post was updated successfully
     */
    Optional<String> updatePostById(Integer postId, Post post);


    /**
     * Likes or unlikes a post based on the provided postId and userEmail.
     * If the user has already liked the post, it unlikes it; otherwise, it likes it.
     * Updates the number of likes on the post accordingly and sends a notification
     * to the post-owner upon liking.
     *
     * @param postId    The ID of the post to like/unlike.
     * @param userEmail The email of the user performing the action.
     * @return The updated Post object after liking/unliking.
     * @throws RuntimeException If the post or user is not found.
     */
    Post likePost(Integer postId, String userEmail);

}
