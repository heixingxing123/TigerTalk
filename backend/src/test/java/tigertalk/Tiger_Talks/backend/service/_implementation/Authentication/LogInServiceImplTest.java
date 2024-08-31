package tigertalk.Tiger_Talks.backend.service._implementation.Authentication;

import tigertalk.model.User.UserProfile;
import tigertalk.model.User.UserProfileDTO;
import tigertalk.repository.User.UserProfileRepository;
import tigertalk.service._implementation.Authentication.LogInServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static tigertalk.model.Utils.OnlineStatus.AVAILABLE;
import static tigertalk.model.Utils.OnlineStatus.OFFLINE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class LogInServiceImplTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private LogInServiceImpl logInServiceImpl;
    private UserProfile userA;
    private UserProfile userB;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userA = new UserProfile(
                "Along",
                "Aside",
                22,
                "Male",
                "userA",
                "a@dal.ca",
                "aaaa1A@a",
                new String[]{"1", "2", "3"},
                new String[]{
                        "What was your favourite book as a child?",
                        "In what city were you born?",
                        "What is the name of the hospital where you were born?"
                }
        );
        userB = new UserProfile(
                "Beach",
                "Boring",
                21,
                "Male",
                "userB",
                "b@dal.ca",
                "aaaa1A@a",
                new String[]{"1", "2", "3"},
                new String[]{
                        "What was your favourite book as a child?",
                        "In what city were you born?",
                        "What is the name of the hospital where you were born?"
                }
        );
    }

    /**
     * Test case for logInUser
     */
    @Test
    public void logInUser_normal_resultExist() {
        when(userProfileRepository.findById(userA.email())).thenReturn(Optional.of(userA));
        when(userProfileRepository.save(userA)).thenReturn(userA);
        Optional<UserProfileDTO> result = logInServiceImpl.loginUser(userA.email(), userA.getPassword());
        assertTrue(result.isPresent());
    }

    @Test
    public void logInUser_normal_resultCorrect() {
        when(userProfileRepository.findById(userA.email())).thenReturn(Optional.of(userA));
        when(userProfileRepository.save(userA)).thenReturn(userA);
        Optional<UserProfileDTO> result = logInServiceImpl.loginUser(userA.email(), userA.getPassword());
        assertTrue(result.isPresent());
        assertEquals(userA.email(), result.get().email());
    }

    @Test
    public void logInUser_normal_onlineCheck() {
        userA.setOnlineStatus(OFFLINE);
        when(userProfileRepository.findById(userA.email())).thenReturn(Optional.of(userA));
        when(userProfileRepository.save(userA)).thenReturn(userA);
        Optional<UserProfileDTO> result = logInServiceImpl.loginUser(userA.email(), userA.getPassword());
        assertEquals(AVAILABLE, userA.getOnlineStatus());
        assertTrue(result.isPresent());
    }

    @Test
    public void logInUser_wrongPassword() {
        userA.setPassword("aaaa1A@a");
        when(userProfileRepository.findById(userA.email())).thenReturn(Optional.of(userA));
        when(userProfileRepository.save(userA)).thenReturn(userA);
        Optional<UserProfileDTO> result = logInServiceImpl.loginUser(userA.email(), "bbbb2B@b");
        assertTrue(result.isEmpty());
    }

    @Test
    public void logInUser_userNotFound() {
        when(userProfileRepository.findById(userA.email())).thenReturn(Optional.of(userA));
        when(userProfileRepository.save(userA)).thenReturn(userA);
        Optional<UserProfileDTO> result = logInServiceImpl.loginUser(userB.email(), userB.getPassword());
        assertTrue(result.isEmpty());
    }

    /**
     * Test case for logOut
     */
    @Test
    public void logOut_normal_onlineCheck_online() {
        userA.setOnlineStatus(AVAILABLE);
        when(userProfileRepository.findById(userA.email())).thenReturn(Optional.of(userA));
        when(userProfileRepository.save(userA)).thenReturn(userA);
        logInServiceImpl.logOut(userA.email());
        assertEquals(OFFLINE, userA.getOnlineStatus());
    }

    @Test
    public void logOut_normal_onlineCheck_offline() {
        userA.setOnlineStatus(OFFLINE);
        when(userProfileRepository.findById(userA.email())).thenReturn(Optional.of(userA));
        when(userProfileRepository.save(userA)).thenReturn(userA);
        logInServiceImpl.logOut(userA.email());
        assertEquals(OFFLINE, userA.getOnlineStatus());
    }

    @Test
    public void logOut_userNotFound() {
        when(userProfileRepository.findById(userA.email())).thenReturn(Optional.empty());
        logInServiceImpl.logOut(userA.email());
        verify(userProfileRepository, never()).save(any(UserProfile.class));
    }
}