## Added
- `ContextParamMap#has(ContextParameterType)`
- `ContextParamMap.Mutable` - A mutable variant of `ContextParamMap`
- `TypedGameCondition` - An interface for parameter-typed conditions to implement.
- `EntityPassengerCondition` - A condition that tests a condition on an entity's passenger.
- `EntityVehicleCondition` - A condition that tests a condition on an entity's vehicle.
- `InvertedCondition` - A condition that inverts another condition's test.

## Changed
- Implemented `TypedGameCondition` on all built-in conditions.
- Use registries for `ContextParamType`.

## Removed
- Removed `@ApiStatus.Experimental` from `GameConditionType`.