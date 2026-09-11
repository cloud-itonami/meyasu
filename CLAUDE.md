# com-etzhayyim-meyasu — CLAUDE.md

## Identity

- **Name**: meyasu (目安 — a guide / yardstick, NOT a trade)
- **DID**: `did:web:meyasu.etzhayyim.com`
- **Role**: the **統合 arbitrage アクター** — a thin orchestrator that FUSES three siblings'
  outputs into one public-good intel surface. It computes **no** price or forecast math
  itself.

## What it fuses

```
kakaku 価格    → cross-merchant/region price SPREAD + present supply/demand index   (now)
mitooshi 見通し → forecast DISTRIBUTION of that supply/demand index                 (next)
ossekai 御節介 → the aggregate-first publication discipline                          (how)
        ↓ meyasu
  one per-product arbitrage-intel card  →  aggregate-first post + planner handoff
```

- `handle_fuse` — `{kakaku, mitooshi}` per product → unified card (spread, supply/demand now,
  forecast band, trajectory, attention flag, route-to). A point-asserted or speculative
  forecast is **refused** (G2), never fused.
- `handle_publish` — cards → aggregate-first social post (G3) + planner handoff for attention
  cards (G4). Broadcast operator-gated, default `:draft` (no-server-key).

## Autonomous heartbeat (clj-native)

- `src/meyasu/methods/autorun.cljk` (+ `kotoba.clj`) — **Clojure-native SSoT** (ADR-2606142300 D1
  authored in Clojure, no Python twin). The autonomous fuse→persist loop: each cycle observes the
  OFFLINE fused-input snapshot (`data/seed.edn`) → `agent/handle-fuse` → **persists one
  content-addressed transaction** (the cards' Datoms) to the append-only **local** kotoba Datom log
  (`src/meyasu/methods/kotoba.cljk`), linking the previous CID into a verifiable commit-DAG. Deterministic /
  resume-safe (cycle drives tx-id + as-of; observed-at is a fixed snapshot stamp → same cycles →
  same commit-DAG); NO external I/O. **G1/G2 hold by construction**: a point-asserted/speculative
  forecast is refused at fuse and never persisted; a card's forecast is written as a BAND, never a
  point; `:trade`/`:speculation` are unrepresentable. Publication / live kakaku·mitooshi ingest /
  live-node push stay operator-gated (no-server-key). Invariants in `test/meyasu/methods/test_autorun.cljk` (persist,
  commit-DAG verify, determinism, tamper-detect, G2 refusal, G1 no-trade, frozen golden head-CID).

  ```sh
  kbb -M -m meyasu.methods.autorun --cycles 3 --fresh
  ```

## Gates (the union of its siblings' invariants — do NOT weaken)

- **G1 non-speculative** — intent is `buyer-transparency+supply-resilience`; meyasu never
  emits a trade / price target and settles no money. The name 目安 (a yardstick) is the point.
- **G2 distribution-respecting** — a consumed forecast MUST be a distribution
  (`pointAsserted` false) with a use in the resilience set; a point/speculative forecast is
  refused per-item.
- **G3 aggregate-first** — published intel is anonymized aggregate (`shape == "aggregate"`).
- **G4 non-adjudicating** — attention-flagged cards (notable spread AND tightening forecast)
  are ROUTED to a planner (`okaimono` buyers / `danjo` resilience); meyasu states, the
  planner decides.
- **G5 Murakumo-only** — any narration via the kotoba `llm` host binding (no external LLM).
- **no-server-key** — live publication is operator-gated.

## Boundaries

- meyasu is an **orchestrator**: it must not re-implement kakaku's pricing or mitooshi's
  forecasting. If a number is wrong, fix it in the source actor, not here.
- It is **not** a trading actor and must never become one. `:trade` / `:speculation` are
  unrepresentable in the fused card's intent.

## Build & test

```sh
kbb -M:test
kbb -M:audit
```

## Related

- `com-etzhayyim-kakaku` — price spread + supply/demand source
- `com-etzhayyim-mitooshi` — forecast distribution source
- `com-etzhayyim-ossekai` — aggregate-first publication discipline
