package io.cmartinezs.authboot.infra.utils;

import io.cmartinezs.authboot.core.exception.persistence.NotFoundEntityException;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class InfraCollections {

  public static <T> Map<String, T> createMapFrom(
      Set<String> keysToFind,
      Function<Set<String>, Set<T>> functionToRetrieve,
      Function<T, String> functionToExtractKeyForMap) {
    return functionToRetrieve.apply(keysToFind).stream()
        .collect(Collectors.toMap(functionToExtractKeyForMap, Function.identity()));
  }

  public static Optional<NotFoundEntityException> validateMissingCodes(
      Map<String, ?> entitiesByCode, Set<String> codes, String entityType) {
    Set<String> missingCodes = new HashSet<>(codes);
    missingCodes.removeAll(entitiesByCode.keySet());

    if (!missingCodes.isEmpty()) {
      return Optional.of(
          new NotFoundEntityException(entityType, "code", String.join(",", missingCodes)));
    }

    return Optional.empty();
  }
}
