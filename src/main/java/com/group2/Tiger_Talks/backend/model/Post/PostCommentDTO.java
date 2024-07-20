package com.group2.Tiger_Talks.backend.model.Post;

import com.group2.Tiger_Talks.backend.model.User.UserProfileDTO;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for PostComment.
 * This record is used to transfer post-comment data between processes.
 *
 * @param commentId                   the unique identifier of the comment
 * @param content                     the content of the comment
 * @param timestamp                   the timestamp when the comment was created
 * @param commentSenderUserProfileDTO the profile of the user who sent the comment
 * @param postSenderUserProfileDTO    the profile of the user who sent the post
 * @param postId                      the unique identifier of the post
 */
public record PostCommentDTO(
        Integer commentId,
        String content,
        LocalDateTime timestamp,
        UserProfileDTO commentSenderUserProfileDTO,
        UserProfileDTO postSenderUserProfileDTO,
        Integer postId
) {
    // For tests only
    public PostCommentDTO(Integer id, UserProfileDTO commentSenderUserProfileDTO, UserProfileDTO postSenderUserProfileDTO) {
        this(
                id,
                null,
                null,
                commentSenderUserProfileDTO,
                postSenderUserProfileDTO,
                id
        );
    }
}
