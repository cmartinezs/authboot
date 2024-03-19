package io.cmartinezs.authboot.core.adapter.service;

import io.cmartinezs.authboot.core.command.user.PasswordRecoveryCmd;
import io.cmartinezs.authboot.core.command.user.*;
import io.cmartinezs.authboot.core.entity.domain.user.User;
import io.cmartinezs.authboot.core.entity.persistence.RolePersistence;
import io.cmartinezs.authboot.core.entity.persistence.UserPersistence;
import io.cmartinezs.authboot.core.exception.persistence.ExistsEntityException;
import io.cmartinezs.authboot.core.exception.persistence.NotFoundEntityException;
import io.cmartinezs.authboot.core.exception.service.MismatchedPasswordException;
import io.cmartinezs.authboot.core.port.persistence.UserPersistencePort;
import io.cmartinezs.authboot.core.port.service.EmailServicePort;
import io.cmartinezs.authboot.core.port.service.PasswordEncoderServicePort;
import io.cmartinezs.authboot.core.port.service.UserServicePort;
import io.cmartinezs.authboot.core.utils.property.UserServiceProperties;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

/** This class is an adapter for the UserServicePort interface. */
@RequiredArgsConstructor
public class UserServiceAdapter implements UserServicePort {
  private final UserPersistencePort userPersistence;
  private final PasswordEncoderServicePort passwordEncoderService;
  private final EmailServicePort emailService;
  private final UserServiceProperties properties;

  private static Set<RolePersistence> toPersistence(Set<String> roles) {
    return Optional.ofNullable(roles).stream()
        .flatMap(Set::stream)
        .map(role -> new RolePersistence(role, null, null, null))
        .collect(Collectors.toSet());
  }

  private UserPersistence toPersistence(CreateUserCmd cmd) {
    var cryptPassword = passwordEncoderService.encrypt(cmd.getPassword());
    var userPersistence =
        new UserPersistence(
            cmd.getUsername(), cmd.getEmail(), cryptPassword, toPersistence(cmd.getRoles()));
    if (properties.isEnabledByDefault()) {
      userPersistence.setEnabledAt(LocalDateTime.now());
    }
    return userPersistence;
  }

  private UserPersistence toPersistence(UpdateUserCmd cmd) {
    String cryptPassword = null;
    if (cmd.getNewPassword() != null) {
      cryptPassword = passwordEncoderService.encrypt(cmd.getNewPassword());
    }
    return new UserPersistence(
        cmd.getUsername(), cmd.getEmail(), cryptPassword, toPersistence(cmd.getRoles()));
  }

  @Override
  public Integer createUser(CreateUserCmd cmd) {
    final var username = cmd.getUsername();
    final var email = cmd.getEmail();

    verifyUserDoesntExists(username, email);

    final var persistence = toPersistence(cmd);

    if (persistence.getEnabledAt() == null) {
      final var token = generateToken(username);
      persistence.setValidationToken(token);
      persistence.setValidationTokenExpiredAt(getValidationTokenExpiredAt());
    }

    final var savedId = userPersistence.save(persistence);

    if (persistence.getEnabledAt() == null) {
      emailService.sendValidation(
          EmailValidationCmd.builder()
              .email(email)
              .username(username)
              .token(persistence.getValidationToken())
              .build());
    }

    return savedId;
  }

  private LocalDateTime getValidationTokenExpiredAt() {
    return LocalDateTime.now().plusMinutes(properties.getMinutesValidationCreateUser());
  }

  private void verifyUserDoesntExists(String username, String email) {
    if (userPersistence.existsByUsername(username)) {
      throw new ExistsEntityException("user", "username", username);
    }
    if (userPersistence.existsByEmail(email)) {
      throw new ExistsEntityException("user", "email", email);
    }
  }

  @Override
  public User updateUser(UpdateUserCmd cmd) {
    var foundUser =
        userPersistence
            .findByUsername(cmd.getUsername())
            .orElseThrow(() -> new NotFoundEntityException("user", "username", cmd.getUsername()));

    if (cmd.getOldPassword() != null
        && !passwordEncoderService.matches(cmd.getOldPassword(), foundUser.getPassword())) {
      throw new MismatchedPasswordException();
    }
    var editedUser = userPersistence.edit(toPersistence(cmd), foundUser);
    return new User(editedUser);
  }

  @Override
  public User deleteUser(DeleteUserCmd cmd) {
    var foundUser =
        userPersistence
            .findByUsername(cmd.getUsername())
            .orElseThrow(() -> new NotFoundEntityException("user", "username", cmd.getUsername()));
    userPersistence.delete(foundUser);
    return new User(foundUser);
  }

  @Override
  public User getUser(GetUserCmd cmd) {
    var username = cmd.getUsername();
    return userPersistence
        .findByUsername(username)
        .map(User::new)
        .orElseThrow(() -> new NotFoundEntityException("user", "username", username));
  }

  @Override
  public void requestPasswordRecovery(PasswordRecoveryCmd cmd) {
    final var email = cmd.getEmail();
    final var foundUser =
        userPersistence
            .findByEmail(email)
            .orElseThrow(() -> new NotFoundEntityException("user", "email", email));
    final var username = foundUser.getUsername();
    final var token = generateToken(username);
    userPersistence.updatePasswordRecoveryToken(
        username, token, getpasswordRecoveryTokenExpiredAt());
    var sendPasswordRecoveryCmd =
        PasswordRecoveryEmailCmd.builder().email(email).username(username).token(token).build();
    emailService.sendPasswordRecovery(sendPasswordRecoveryCmd);
  }

  private LocalDateTime getpasswordRecoveryTokenExpiredAt() {
    return LocalDateTime.now().plusMinutes(properties.getMinutesPasswordRecovery());
  }

  private String generateToken(String text) {
    return passwordEncoderService.encrypt(text);
  }
}
