## Breaking Changes
- `silicate:entity_vehicle` must now use the `silicate:vehicle_entity` param type to reference the vehicle entity, with other fields remaining the exact same.
- `silicate:entity_passenger` must now use the `silicate:passenger_entity` param type to reference the passenger entity, with other fields remaining the exact same.
- `silicate:entity_tame_owner` must now use the `silicate:owner_entity` param type to reference the owner entity, with other fields remaining the exact same.
- `silicate:entity_projectile_owner` must now use the `silicate:owner_entity` param type to reference the owner entity, with other fields remaining the exact same.
- The ContextParamType codec will now return a data error if not present within the registry, or if the class of the registered param type is unable to be casted to the specified class. On a success, this will return the original context param type if it is an exact match in class, otherwise, this will create a new param type if the resulting class can be cast to the specified class.

## Added
- Ported to 1.21.5.

## Fixed
- Fixed `entity_vehicle`, `entity_passenger`, `entity_tame_owner`, `entity_projectile_owner` conditions overriding the original field with the target entity within their inner conditions' context.
- Fixed issues with registering custom game conditions on NeoForge.
- Fixed ContextParamType codec creating entirely new param types without checking if a param type of the sort exists already.
- Fixed ContextParamType codec's entirely new param types not being equal to any param type in the case of casted classes.
