package io.cmartinezs.authboot.infra.adapter.service.email;

import io.cmartinezs.authboot.core.command.user.EmailValidationCmd;
import io.cmartinezs.authboot.core.command.user.PasswordRecoveryEmailCmd;
import io.cmartinezs.authboot.core.port.service.EmailServicePort;
import io.cmartinezs.authboot.infra.properties.email.*;
import jakarta.mail.internet.InternetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.util.UriComponentsBuilder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RequiredArgsConstructor
public class EmailServiceAdapter implements EmailServicePort {
  private final JavaMailSender mailSender;
  private final TemplateEngine emailTemplateEngine;
  private final EmailServiceProperties properties;

  @Override
  public void sendPasswordRecovery(PasswordRecoveryEmailCmd cmd) {
    sendEmail(new PasswordRecoveryEmailStrategy(cmd));
  }

  @Override
  public void sendValidation(EmailValidationCmd cmd) {
    sendEmail(new EmailValidationStrategy(cmd));
  }

  private void sendEmail(EmailStrategy strategy) {
    final var emailDataData = properties.getUserEmails().get(strategy.getConfigName());
    final var variables = strategy.getVariables();
    sendMail(
        emailDataData.getSender(),
        getHtml(strategy),
        variables.get("email"),
        variables.get("username"));
  }

  private String getHtml(EmailStrategy strategy) {
    final var ctx = new Context(LocaleContextHolder.getLocale());
    final var emailTemplateData =
        properties.getUserEmails().get(strategy.getConfigName()).getTemplate();
    final var variables = strategy.getVariables();

    emailTemplateData
        .getVariables()
        .forEach(variableName -> ctx.setVariable(variableName, variables.get(variableName)));

    emailTemplateData
        .getLinks()
        .forEach(
            (link, data) -> {
              var uriParams = strategy.getUris().get(link);
              var uri =
                  generateUri(data, uriParams.get("path-variables"), uriParams.get("query-params"));
              ctx.setVariable(data.getVariableName(), uri);
            });

    return emailTemplateEngine.process(emailTemplateData.getName(), ctx);
  }

  @SneakyThrows
  public void sendMail(EmailSenderData info, String html, String email, String username) {
    final var message = mailSender.createMimeMessage();
    final var messageHelper =
        new MimeMessageHelper(
            message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
    messageHelper.setTo(email);
    messageHelper.setFrom(new InternetAddress(info.getFrom(), info.getPersonal()));
    messageHelper.setSubject(String.format(info.getSubject(), username));
    messageHelper.setText(html, true);
    mailSender.send(message);
  }

  private String generateUri(
      EmailLinkData uriProperties,
      Map<String, String> pathVariables,
      Map<String, String> queryParams) {
    final var uriBuilder =
        UriComponentsBuilder.newInstance()
            .scheme(uriProperties.getSchema())
            .host(uriProperties.getHost())
            .port(uriProperties.getPort())
            .path(uriProperties.getPath());

    if (queryParams != null && !queryParams.isEmpty()) {
      queryParams.forEach(uriBuilder::queryParam);
    }

    String uri;
    if (pathVariables != null && !pathVariables.isEmpty()) {
      uri = uriBuilder.buildAndExpand(pathVariables).toUriString();
    } else {
      uri = uriBuilder.build().toUriString();
    }

    return uri;
  }
}
