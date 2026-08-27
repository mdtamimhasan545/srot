import type { PresetId, YtDlpOptions } from "./types";

export const PRESET_FORMAT: Record<Exclude<PresetId, "custom">, string> = {
  best: "bv*+ba/b",
  uhd: "bv*[height<=2160]+ba/b[height<=2160]",
  "1080p": "bv*[height<=1080]+ba/b[height<=1080]",
  "720p": "bv*[height<=720]+ba/b[height<=720]",
  audio: "ba/b",
};

export function defaultOptions(): YtDlpOptions {
  return {
    preset: "best",
    format: PRESET_FORMAT.best,
    mergeOutputFormat: "mp4",
    recodeVideo: "",
    remuxVideo: "",
    extractAudio: false,
    audioFormat: "m4a",
    audioQuality: "0",
    preferFreeFormats: false,
    formatSort: "res,fps,codec,size",
    outputTemplate: "%(title)s [%(id)s].%(ext)s",
    outputDir: "Downloads/Srot",
    restrictFilenames: false,
    windowsFilenames: true,
    noOverwrites: true,
    continueDl: true,
    noPart: false,
    keepVideo: false,
    writeSubs: false,
    writeAutoSubs: false,
    subLangs: "en.*,bn.*",
    subFormat: "best",
    embedSubs: false,
    convertSubs: "",
    writeThumbnail: true,
    embedThumbnail: false,
    writeDescription: false,
    writeInfoJson: false,
    writeComments: false,
    embedMetadata: true,
    embedChapters: true,
    writePlaylistMetafiles: false,
    xattrs: false,
    noPlaylist: true,
    yesPlaylist: false,
    playlistStart: "",
    playlistEnd: "",
    playlistItems: "",
    minViews: "",
    maxViews: "",
    date: "",
    dateBefore: "",
    dateAfter: "",
    matchFilter: "",
    maxDownloads: "",
    breakOnExisting: true,
    downloadSections: "",
    liveFromStart: false,
    waitForVideo: "",
    sponsorblockMark: "sponsor,intro,outro,selfpromo",
    sponsorblockRemove: "",
    sponsorblockChapterTitle: "",
    proxy: "",
    socketTimeout: "20",
    sourceAddress: "",
    forceIpv4: false,
    forceIpv6: false,
    geoBypass: false,
    geoBypassCountry: "",
    limitRate: "",
    retries: "10",
    fragmentRetries: "10",
    concurrentFragments: "4",
    retrySleep: "linear=1::2",
    cookiesFromBrowser: "",
    cookiesFile: "",
    username: "",
    password: "",
    twoFactor: "",
    videoPassword: "",
    netrc: false,
    addHeaders: "",
    userAgent: "",
    referer: "",
    downloadArchive: "Downloads/Srot/archive.txt",
    ffmpegLocation: "ffmpeg",
    postprocessorArgs: "",
    keepFragments: false,
    splitChapters: false,
    ytdlpPath: "yt-dlp",
    verbose: false,
    quiet: false,
    ignoreErrors: true,
    noCheckCertificates: false,
    extraArgs: "",
  };
}

export function applyPreset(options: YtDlpOptions, preset: PresetId): YtDlpOptions {
  const next = { ...options, preset };
  if (preset === "audio") {
    next.extractAudio = true;
    next.format = PRESET_FORMAT.audio;
    next.mergeOutputFormat = "";
  } else if (preset !== "custom") {
    next.extractAudio = false;
    next.format = PRESET_FORMAT[preset];
  }
  return next;
}

export const CONTAINERS = ["", "mp4", "mkv", "webm", "mov"] as const;
export const AUDIO_FORMATS = ["best", "m4a", "mp3", "opus", "ogg", "wav", "flac", "aac"] as const;
export const SUB_FORMATS = ["best", "srt", "vtt", "ass"] as const;
