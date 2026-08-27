# Source layout

This repository contains the **Srot** application source authored for the Grok App Builder (TanStack Start + React 19 + Tailwind v4).

## Included

| Path | Role |
|------|------|
| `src/lib/types.ts` | Domain types (options, queue, media, settings) |
| `src/lib/presets.ts` | Quality presets and default options |
| `src/lib/ytdlp-args.ts` | Maps every UI option → real yt-dlp argv |
| `src/lib/sanitize.ts` | Path/filename safety + shell-free tokenizer |
| `src/lib/utils.ts` | Formatting helpers |
| `public/favicon.svg` | App mark |
| `README.md` | Product overview |

## Hosted UI (Grok App Builder session)

The full interactive UI (routes, Material 3 shell, i18n, Zustand store, queue engine, oEmbed analyze server function, Settings/Advanced panels) was built and verified in the Grok live preview. Port that session’s `src/routes/*`, `src/components/*`, `src/styles.css`, and host `vite`/`router` contracts into this tree to run end-to-end outside the builder.

## Note on repository name

`mdtamimhasan545/yt-dlp` could not be created (name unavailable on the account). This project lives at **https://github.com/mdtamimhasan545/srot**.
