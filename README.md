# Srot (স্রোত)

**Material 3 yt-dlp studio** — bilingual (English + বাংলা) companion UI for [yt-dlp](https://github.com/yt-dlp/yt-dlp).

Srot compiles **real yt-dlp arguments** from every control, analyzes **public media metadata** (oEmbed / Open Graph), and manages a **persistent download queue**. The native `yt-dlp` / FFmpeg binaries are **not executed in the browser** — copy the generated command to a machine where you have the right to download the media.

## Features

- **Home** — paste one or more URLs, analyze, presets (Best / 4K / 1080p / 720p / Audio / Custom)
- **Media info** — thumbnail, title, uploader, duration, full format & options panel
- **Queue** — pause / resume / cancel / retry, concurrent limit, progress / speed / ETA
- **History & Files** — finished jobs with exact `argv` and copyable commands
- **Settings** — language, theme (light / dark / system), Wi‑Fi only, retries, paths
- **Advanced** — full flag map (network, auth/cookies, playlist, subtitles, SponsorBlock, post-processing, custom args)
- **Logs** — structured activity stream
- **About** — licenses and copyright notice

## Stack

- React 19 + TanStack Start / Router
- Tailwind CSS v4 + Radix UI (Material 3–inspired tokens)
- Zustand (persisted settings, queue, history, logs)
- Server function for public metadata analysis (`createServerFn`)

## Security & policy

- Filenames and paths are sanitized; path traversal is rejected
- Custom arguments are **tokenized without a shell** (no `;`, `|`, backticks, `$()`)
- Credentials stay on-device; cookie contents are never embedded into copied commands as secrets beyond path flags
- Does **not** bypass DRM, paywalls, CAPTCHA, or access controls
- Only download media you have the right to save

## Language & theme

- **English** and **বাংলা** (Noto Sans Bengali) with an in-Settings switcher
- Light / Dark / System, remembered in `localStorage`

## Local development

This source was built against the Grok App Builder / TanStack Start workspace template (`0.0.0.0:8080`, Vite, Nitro).

```bash
npm install
npm run dev
```

Ensure the host template provides `src/router.tsx` (`getRouter`), `__root.tsx`, and the Vite/TanStack Start config used by the App Builder.

## License notes

- **yt-dlp** — Unlicense
- **FFmpeg** — LGPL / GPL (depending on build)
- **Srot UI** — companion interface; respect site terms and applicable copyright

## Repository note

Published at [github.com/mdtamimhasan545/srot](https://github.com/mdtamimhasan545/srot).
The name `yt-dlp` was not available for a new public repository on this account.
