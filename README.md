# meyasu (目安)

Public-good price-spread and supply-resilience synthesis. Meyasu fuses aggregate
Kakaku observations with Mitooshi forecast bands into buyer-transparency cards; it
never emits a trade recommendation or point forecast.

## Standalone layout

- `manifest.edn` — actor contract
- `src/meyasu/methods` — canonical Clojure/CLJC implementation
- `data/{cells,lex}` and `schema/` — canonical EDN definitions
- `data/seed.edn` — canonical deterministic input snapshot
- `resources/viz/template.html` — visualization template
- `wire/` — JSON and generated HTML interoperability artifacts
- `test/meyasu` — standalone tests
- `test/integration` — cross-repository Kakaku/Mitooshi cohort test

## Verify

```sh
kbb -M:test
kbb -M:audit
```

The cross-repository cohort suite requires the Kakaku and Mitooshi repositories on
the classpath and is intentionally separate from the standalone test gate. Python,
Go/TinyGo, and shell deployment/test wrappers are deprecated and forbidden by audit.
