package io.cmartinezs.authboot.core.entity.persistence;

import io.cmartinezs.authboot.core.entity.domain.user.Function;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/** This class represents a function persistence object. */
@Getter
@RequiredArgsConstructor
public class FunctionPersistence extends PersistenceBase {
  private final String code;
  private final String name;
  private final String type;
  private final String typeName;

  public Function toDomain() {
    var code = this.getCode() + "_" + this.getType();
    var name = this.getName() + " " + this.getTypeName();
    return new Function(code, name);
  }
}
