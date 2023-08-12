package io.cmartinezs.authboot.core.entity.domain.user;

import io.cmartinezs.authboot.core.entity.domain.DomainBase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Getter
@RequiredArgsConstructor
public class Role extends DomainBase {
    private final String code;
    private final String name;
    private final Set<Function> functions;
}
