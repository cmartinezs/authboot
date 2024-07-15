package io.cmartinezs.authboot.core.entity.domain.user;

import io.cmartinezs.authboot.core.entity.domain.DomainBase;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Role extends DomainBase {
  private final String code;
  private final String name;
  private final String description;
  private final Set<Function> functions;
}
