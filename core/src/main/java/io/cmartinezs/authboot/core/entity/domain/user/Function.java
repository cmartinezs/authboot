package io.cmartinezs.authboot.core.entity.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Function {
  private final String code;
  private final String name;
}
