package io.cmartinezs.authboot.infra.adapter.service;

import io.cmartinezs.authboot.core.command.user.SendPasswordRecoveryCmd;
import io.cmartinezs.authboot.core.port.service.EmailServicePort;
import io.cmartinezs.authboot.infra.properties.EmailTemplateProperties;
import io.cmartinezs.authboot.infra.utils.properties.EmailProperties;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.util.UriComponentsBuilder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.InternetAddress;

@RequiredArgsConstructor
public class EmailServiceAdapter implements EmailServicePort {
  private final JavaMailSender mailSender;
  private final TemplateEngine emailTemplateEngine;
  private final EmailProperties properties;

  @SneakyThrows
  @Override
  public void sendPasswordRecovery(SendPasswordRecoveryCmd cmd) {
    final var message = mailSender.createMimeMessage();
    final var template = getTemplate("password-recovery");
    final var html = getHtml(cmd, template);

    final var messageHelper =
        new MimeMessageHelper(
            message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
    messageHelper.setTo(cmd.getEmail());
    messageHelper.setFrom(new InternetAddress(template.getFrom(), template.getPersonal()));
    messageHelper.setSubject(String.format(template.getSubject(), cmd.getUsername()));
    messageHelper.setText(html, true);

    mailSender.send(message);
  }

  private String getHtml(SendPasswordRecoveryCmd cmd, EmailTemplateProperties template) {
    final var ctx = new Context(LocaleContextHolder.getLocale());
    final var username = cmd.getUsername();
    ctx.setVariable("username", username);
    ctx.setVariable("recoveryLink", generateRecoveryLink(username, cmd.getToken()));
    ctx.setVariable("email", cmd.getEmail());
    return emailTemplateEngine.process(template.getName(), ctx);
  }

  private String generateRecoveryLink(String username, String token) {
    final var uriProperties = properties.getUris().get("password-recovery");
    final var uriBuilder =
        UriComponentsBuilder.newInstance()
            .scheme(uriProperties.getScheme())
            .host(uriProperties.getHost())
            .port(uriProperties.getPort())
            .path(uriProperties.getPath());

    uriBuilder.queryParam("username", username);
    uriBuilder.queryParam("token", token);

    return uriBuilder.build().toUriString();
  }

  private EmailTemplateProperties getTemplate(String templateName) {
    return properties.getTemplates().getOrDefault(templateName, new EmailTemplateProperties());
  }
}
