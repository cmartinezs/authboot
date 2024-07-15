package io.cmartinezs.authboot.infra.properties;

import java.io.IOException;
import java.util.Objects;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.lang.Nullable;

public class YamlPropertySourceFactory implements PropertySourceFactory {
  @Override
  public PropertySource<?> createPropertySource(
      @Nullable String name, EncodedResource encodedResource) throws IOException {
    final var factory = new YamlPropertiesFactoryBean();
    factory.setResources(encodedResource.getResource());

    final var properties = factory.getObject();

    assert properties != null;
    return new PropertiesPropertySource(
        Objects.requireNonNull(encodedResource.getResource().getFilename()), properties);
  }
}
