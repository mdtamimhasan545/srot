const UNSAFE_FILENAME = /[<>:"|?*\u0000-\u001f]/g;
const TRAVERSAL = /(^|[\\/])\.\.([\\/]|$)/;

export function sanitizeFilename(name: string, fallback = "download"): string {
  const trimmed = name.replace(UNSAFE_FILENAME, "_").replace(/[. ]+$/g, "").trim();
  const base = trimmed.length > 0 ? trimmed : fallback;
  return base.slice(0, 180);
}

export function isSafePath(path: string): boolean {
  if (!path) return true;
  if (path.includes("\0")) return false;
  if (TRAVERSAL.test(path)) return false;
  return true;
}

export function sanitizeOutputTemplate(template: string): string {
  const t = template.trim() || "%(title)s [%(id)s].%(ext)s";
  if (TRAVERSAL.test(t) || t.includes("\0")) {
    return "%(title)s [%(id)s].%(ext)s";
  }
  return t.slice(0, 400);
}

export function sanitizePathInput(path: string): string {
  const t = path.trim();
  if (!isSafePath(t)) return "";
  return t.slice(0, 500);
}

/** Tokenize user extra args without a shell. Rejects interpolation. */
export function tokenizeExtraArgs(input: string): { args: string[]; error?: string } {
  const args: string[] = [];
  let cur = "";
  let quote: '"' | "'" | null = null;
  const src = input.replace(/\r/g, "");

  for (let i = 0; i < src.length; i += 1) {
    const ch = src[i];
    if (quote) {
      if (ch === "\\" && quote === '"' && i + 1 < src.length) {
        cur += src[i + 1];
        i += 1;
        continue;
      }
      if (ch === quote) {
        quote = null;
        continue;
      }
      cur += ch;
      continue;
    }
    if (ch === "'" || ch === '"') {
      quote = ch;
      continue;
    }
    if (/\s/.test(ch)) {
      if (cur) {
        args.push(cur);
        cur = "";
      }
      continue;
    }
    if (ch === ";" || ch === "|" || ch === "&" || ch === "`" || ch === "\n") {
      return { args: [], error: "Shell metacharacters are not allowed. Pass flags only." };
    }
    if (ch === "$" && src[i + 1] === "(") {
      return { args: [], error: "Command substitution is not allowed." };
    }
    cur += ch;
  }
  if (quote) return { args: [], error: "Unclosed quote in extra arguments." };
  if (cur) args.push(cur);

  for (const a of args) {
    if (a.includes("\0") || a.includes("..")) {
      return { args: [], error: "Unsafe token in extra arguments." };
    }
  }
  return { args };
}
