package io.cmartinezs.authboot.infra.adapter.service.email;

import io.cmartinezs.authboot.core.command.user.PasswordRecoveryEmailCmd;
import io.cmartinezs.authboot.core.command.user.EmailValidationCmd;
import io.cmartinezs.authboot.core.port.service.EmailServicePort;
import io.cmartinezs.authboot.core.utils.property.EmailSenderInfo;
import io.cmartinezs.authboot.infra.properties.BaseEmailSenderInfo;
import io.cmartinezs.authboot.infra.properties.UriProperties;
import io.cmartinezs.authboot.infra.utils.properties.EmailProperties;

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

import jakarta.mail.internet.InternetAddress;

@RequiredArgsConstructor
public class EmailServiceAdapter implements EmailServicePort {
  private final JavaMailSender mailSender;
  private final TemplateEngine emailTemplateEngine;
  private final EmailProperties properties;

  @Override
  public void sendPasswordRecovery(PasswordRecoveryEmailCmd cmd) {
    sendEmail(new PasswordRecoveryEmailStrategy(cmd));
  }

  @Override
  public void sendValidation(EmailValidationCmd cmd) {
    sendEmail(new EmailValidationStrategy(cmd));
  }

  private void sendEmail(EmailStrategy strategy) {
    sendMail(
        getSenderInfo(strategy),
        getHtml(strategy),
        strategy.getVariables().get("email"),
        strategy.getVariables().get("username"));
  }

  public String getHtml(EmailStrategy strategy) {
    final var ctx = new Context(LocaleContextHolder.getLocale());
    for (var entry : strategy.getVariables().entrySet()) {
      ctx.setVariable(entry.getKey(), entry.getValue());
    }

    for (var entry : strategy.getUris().entrySet()) {
      var uriProperties = properties.getUris().get(entry.getKey());
      var uriParams = entry.getValue();
      var uri =
          generateUri(
              uriProperties,
              uriParams.get("path-variables"),
              uriParams.get("query-params"));
      ctx.setVariable(entry.getKey(), uri);
    }
    return emailTemplateEngine.process(strategy.getTemplateName(), ctx);
  }

  @SneakyThrows
  public void sendMail(EmailSenderInfo info, String html, String email, String username) {
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
      UriProperties uriProperties, Map<String, String> pathVariables, Map<String, String> queryParams) {
    final var uriBuilder =
        UriComponentsBuilder.newInstance()
            .scheme(uriProperties.getScheme())
            .host(uriProperties.getHost())
            .port(uriProperties.getPort())
            .path(uriProperties.getPath());

    queryParams.forEach(uriBuilder::queryParam);

    return uriBuilder.buildAndExpand(pathVariables).toUriString();
  }

  public EmailSenderInfo getSenderInfo(EmailStrategy templateName) {
    return properties
        .getSenderInfo()
        .getOrDefault(templateName.getTemplateName(), new BaseEmailSenderInfo());
  }
}
