package com.example.usermanagement.service;

import com.example.usermanagement.api.model.CreateUserAccountRequest;
import com.example.usermanagement.exception.UsernameExistsException;
import com.example.usermanagement.model.UserAccount;
import com.example.usermanagement.model.UserAccountRepository;
import com.example.usermanagement.test.util.InitData;
import com.example.usermanagement.verification.VerificationService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService underTest;
    @Mock
    UserAccountRepository repository;
    @Mock
    VerificationService verificationService;
    @Spy
    MockHttpServletRequest servletRequest;

    @Nested
    class GetUserAccount {
        @Test
        void returnsUserAccount_givenExistingUserId() {
            // Given
            final var user = InitData.createUser();
            final var id = user.getId();
            given(repository.findById(id)).willReturn(Optional.of(user));

            // When
            final var response = underTest.getUserAccount(id);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.id()).isEqualTo(user.getId());
            assertThat(response.username()).isEqualTo(user.getUserDetails().username());
            assertThat(response.email()).isEqualTo(user.getUserDetails().email());
            assertThat(response.age()).isEqualTo(user.getUserDetails().age());
            assertThat(response.gender()).isEqualTo(user.getUserDetails().gender());
            verify(repository).findById(id);
            verifyNoMoreInteractions(repository);
            verifyNoInteractions(verificationService);
        }

        @Test
        void throwsIllegalArgumentException_whenUserDoesNotExist() {
            // Given
            final var id = 10L;
            given(repository.findById(id)).willReturn(Optional.empty());

            // When, Then
            assertThatThrownBy(() -> underTest.getUserAccount(id))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format("User with ID: %s not found", id));

            verify(repository).findById(id);
            verifyNoMoreInteractions(repository);
            verifyNoInteractions(verificationService);
        }
    }

    @Nested
    class CreateUserAccount {
        @Test
        void createsUserAccount_andSendsVerificationLink_givenValidRequest() {
            // Given
            final var user = InitData.createUser();
            final var id = user.getId();
            final var username = user.getUserDetails().username();
            final var userRequest = CreateUserAccountRequest.builder()
                .username(username)
                .email(user.getUserDetails().email())
                .age(user.getUserDetails().age())
                .gender(user.getUserDetails().gender())
                .build();
            given(repository.findByUsername(username)).willReturn(Optional.empty());
            given(repository.save(any(UserAccount.class))).willReturn(user);

            // When
            final var response = underTest.createUserAccount(userRequest, servletRequest);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.id()).isEqualTo(user.getId());
            assertThat(response.username()).isEqualTo(user.getUserDetails().username());
            assertThat(response.email()).isEqualTo(user.getUserDetails().email());
            assertThat(response.age()).isEqualTo(user.getUserDetails().age());
            assertThat(response.gender()).isEqualTo(user.getUserDetails().gender());
            verify(repository).findByUsername(username);
            verify(repository).save(any(UserAccount.class));
            verifyNoMoreInteractions(repository);
            verify(verificationService).sendAccountVerificationEmail(any(), any());
        }

        @Test
        void throwsIllegalArgumentException_whenUsernameExists() {
            // Given
            final var user = InitData.createUser();
            final var username = user.getUserDetails().username();
            final var userRequest = CreateUserAccountRequest.builder()
                .username(username)
                .email(user.getUserDetails().email())
                .age(user.getUserDetails().age())
                .gender(user.getUserDetails().gender())
                .build();

            given(repository.findByUsername(username)).willReturn(Optional.of(user));

            // When, Then
            assertThatThrownBy(() -> underTest.createUserAccount(userRequest, servletRequest))
                .isExactlyInstanceOf(UsernameExistsException.class)
                .hasMessage("Username already exists: " + username);

            verify(repository).findByUsername(username);
            verifyNoMoreInteractions(repository);
            verifyNoInteractions(verificationService);
        }
    }

    @Nested
    class UpdateUserAccount {

        @Test
        void updatesUserAccount_givenExistingUserId() {
            // Given
            final var user = InitData.createUser();
            final var id = user.getId();
            final var userRequest = CreateUserAccountRequest.builder()
                .username("username")
                .age(11)
                .build();
            given(repository.findById(id)).willReturn(Optional.of(user));
            given(repository.save(user)).willReturn(user);

            // When
            final var response = underTest.updateUserAccount(id, userRequest);

            // Then
            verify(repository).findById(id);
            verify(repository).save(any(UserAccount.class));
            assertThat(response.id()).isEqualTo(user.getId());
            assertThat(response.username()).isEqualTo(userRequest.username());
            assertThat(response.email()).isEqualTo(user.getUserDetails().email());
            assertThat(response.age()).isEqualTo(userRequest.age());
            assertThat(response.gender()).isEqualTo(user.getUserDetails().gender());
        }

        @Test
        void throwsIllegalArgumentException_whenUserDoesNotExist() {
            // Given
            final var id = 10L;
            final var userRequest = CreateUserAccountRequest.builder().build();
            given(repository.findById(id)).willReturn(Optional.empty());

            // When, Then
            assertThatThrownBy(() -> underTest.updateUserAccount(id, userRequest))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format("User with ID: %s not found", id));

            verify(repository).findById(id);
            verifyNoMoreInteractions(repository);
            verifyNoInteractions(verificationService);
        }
    }

    @Nested
    class RemoveUserAccount {
        @Test
        void removesUserAccount() {
            // Given
            final var id = 10L;

            // When
            underTest.removeUserAccount(id);

            // Then
            verify(verificationService).removeAllVerificationTokensForUser(id);
            verify(repository).deleteById(id);
        }
    }
}