package io.cmartinezs.authboot.core.adapter.service;

import io.cmartinezs.authboot.core.command.user.*;
import io.cmartinezs.authboot.core.command.user.PasswordRecoveryRequestCmd;
import io.cmartinezs.authboot.core.entity.domain.user.User;
import io.cmartinezs.authboot.core.exception.persistence.ExistsEntityException;
import io.cmartinezs.authboot.core.exception.service.ExpiredCodeException;
import io.cmartinezs.authboot.core.exception.service.InvalidCodeException;
import io.cmartinezs.authboot.core.exception.service.MismatchedPasswordException;
import io.cmartinezs.authboot.core.exception.service.SendValidationEmailException;
import io.cmartinezs.authboot.core.port.persistence.UserPersistencePort;
import io.cmartinezs.authboot.core.port.service.EmailServicePort;
import io.cmartinezs.authboot.core.port.service.PasswordEncoderServicePort;
import io.cmartinezs.authboot.core.port.service.UserServicePort;
import io.cmartinezs.authboot.core.utils.property.UserServiceProperties;

import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static io.cmartinezs.authboot.core.utils.service.UserServiceUtils.newPersistence;
import static io.cmartinezs.authboot.core.utils.service.UserServiceUtils.toPersistence;

/** This class is an adapter for the UserServicePort interface. */
@RequiredArgsConstructor
@Slf4j
public class UserServiceAdapter implements UserServicePort {
  private final UserPersistencePort userPersistence;
  private final PasswordEncoderServicePort passwordEncoderService;
  private final EmailServicePort emailService;
  private final UserServiceProperties properties;

  public static final String USER_ENTITY_NAME = "user";
  public static final String USERNAME_FIELD_NAME = "username";
  public static final String EMAIL_FIELD_NAME = "email";

  @Override
  public User getUserByUsername(String username) {
    return new User(userPersistence.findByUsername(username));
  }

  @Override
  public User getUserByEmail(String email) {
    return new User(userPersistence.findByEmail(email));
  }

  @Override
  public void validateUser(ValidateUserCmd cmd) {
    final var username = cmd.getUsername();
    final var validationCode = cmd.getValidationCode();
    final var foundUser = userPersistence.findByUsername(username);
    if (foundUser.getValidationCodeExpiredAt().isBefore(LocalDateTime.now())) {
      throw new ExpiredCodeException("email validation");
    }
    if (!validationCode.equals(foundUser.getValidationCode())) {
      throw new InvalidCodeException("email validation");
    }
    foundUser.setEnabledAt(LocalDateTime.now());
    foundUser.setPasswordResetAt(
        LocalDateTime.now().plusDays(properties.getDaysPasswordExpiration()));
    foundUser.setValidationCode(null);
    foundUser.setValidationCodeExpiredAt(null);
    userPersistence.save(foundUser);
  }

  @Override
  public void recoverPassword(RecoverPasswordCmd cmd) {
    final var username = cmd.getUsername();
    final var recoveryCode = cmd.getRecoveryCode();
    final var newPassword = cmd.getPassword();
    final var foundUser = userPersistence.findByUsername(username);
    if (foundUser.getRecoveryCodeExpiredAt().isBefore(LocalDateTime.now())) {
      throw new ExpiredCodeException("password recovery");
    }
    if (!recoveryCode.equals(foundUser.getRecoveryCode())) {
      throw new InvalidCodeException("password recovery");
    }
    final var cryptPassword = passwordEncoderService.encrypt(newPassword);
    foundUser.setPassword(cryptPassword);
    userPersistence.updatePassword(username, cryptPassword);
    new User(foundUser);
  }

  @Override
  public Integer createUser(CreateUserCmd cmd) {
    final var username = cmd.getUsername();
    final var email = cmd.getEmail();

    verifyUserDoesntExists(username, email);

    final var persistence =
        newPersistence(
            cmd,
            passwordEncoderService.encrypt(cmd.getPassword()),
            properties.getDaysPasswordExpiration(),
            properties.isEnabledByDefault());

    if (persistence.getEnabledAt() == null) {
      final var validationCode = passwordEncoderService.encrypt(username);
      persistence.setValidationCode(validationCode);
      persistence.setValidationCodeExpiredAt(getValidationTokenExpiredAt());
    }

    final var savedId = userPersistence.save(persistence);

    if (persistence.getEnabledAt() == null) {
      try {
        emailService.sendValidation(
            EmailValidationCmd.builder()
                .email(email)
                .username(username)
                .validationCode(persistence.getValidationCode())
                .build());
      } catch (Exception e) {
        logger.error("An error occurred while sending validation to email", e);
        userPersistence.delete(persistence);
        throw new SendValidationEmailException(email, username);
      }
    }

    return savedId;
  }

  private LocalDateTime getValidationTokenExpiredAt() {
    return LocalDateTime.now().plusMinutes(properties.getMinutesValidationCreateUser());
  }

  private void verifyUserDoesntExists(String username, String email) {
    if (userPersistence.existsByUsername(username)) {
      throw new ExistsEntityException(USER_ENTITY_NAME, USERNAME_FIELD_NAME, username);
    }
    if (userPersistence.existsByEmail(email)) {
      throw new ExistsEntityException(USER_ENTITY_NAME, EMAIL_FIELD_NAME, email);
    }
  }

  @Override
  public User updateUser(UpdateUserCmd cmd) {
    var foundUser = userPersistence.findByUsername(cmd.getUsername());

    if (cmd.getOldPassword() != null
        && !passwordEncoderService.matches(cmd.getOldPassword(), foundUser.getPassword())) {
      throw new MismatchedPasswordException();
    }
    String cryptPassword = null;
    if (cmd.getNewPassword() != null) {
      cryptPassword = passwordEncoderService.encrypt(cmd.getNewPassword());
    }
    var editedUser = userPersistence.edit(toPersistence(cmd, cryptPassword), foundUser);
    return new User(editedUser);
  }

  @Override
  public User deleteUser(DeleteUserCmd cmd) {
    var foundUser = userPersistence.findByUsername(cmd.getUsername());
    userPersistence.delete(foundUser);
    return new User(foundUser);
  }

  @Override
  public User getUser(GetUserCmd cmd) {
    return getUserByUsername(cmd.getUsername());
  }

  @Override
  public void processPasswordRecoveryRequest(PasswordRecoveryRequestCmd cmd) {
    final var username = cmd.getUsername();
    final var foundUser = userPersistence.findByUsername(username);
    final var token = passwordEncoderService.encrypt(username);
    userPersistence.updatePasswordRecoveryToken(
        username, token, getPasswordRecoveryTokenExpiredAt());
    var sendPasswordRecoveryCmd =
        PasswordRecoveryEmailCmd.builder()
            .email(foundUser.getEmail())
            .username(username)
            .validationCode(token)
            .build();
    emailService.sendPasswordRecovery(sendPasswordRecoveryCmd);
  }

  private LocalDateTime getPasswordRecoveryTokenExpiredAt() {
    return LocalDateTime.now().plusMinutes(properties.getMinutesPasswordRecovery());
  }
}
