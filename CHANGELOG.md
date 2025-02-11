## Added
- `ConditionTemplate` and `GameCondition#CODEC` allow using pre-existing condition templates via `ResourceLocation`s.
- `or` field in `silicate:compound` allows checking if conditions all match (`or: false`) or if any match (`or: true`).

## Changed
- `GameCondition#CODEC` renamed `GameCondition#TYPED_CODEC`
- `data/<namespace>/silicate/condition` renamed `data/<namespace>/silicate/condition_template`

## Fixed
