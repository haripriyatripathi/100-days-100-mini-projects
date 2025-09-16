#!/usr/bin/env node
// ASCII Cat Printer — zero deps
// Usage: catprint [--list] [--random] [--cat N] [--say "text"] [--animate] [-- action name]

const cats = [
` /\\_/\\
( o.o )
 > ^ <`,

String.raw`|\---/|
| o_o |
 \_^_/`,

String.raw`  /\_/\ 
 ( -.-)
  > ^ <`,

String.raw` /\     /\
{  '-._.-'  }
 >  =   =  <
  \  .-.  /
   | | | |
  (__|__)`
];

// --- Speech bubble ---
function bubble(text) {
  if (!text) return "";
  const lines = text.split(/\r?\n/);
  const width = Math.max(...lines.map(l => l.length));
  const top = " " + "_".repeat(width + 2);
  const bottom = " " + "-".repeat(width + 2);
  const body = lines.map(l => `| ${l.padEnd(width)} |`).join("\n");
  return `${top}\n${body}\n${bottom}\n`;
}

// --- Colors ---
function color(s, code){ return `\x1b[${code}m${s}\x1b[0m`; }
const cyan = s => color(s, 36);
const mag = s => color(s, 35);
const dim = s => color(s, "2");

// --- CLI Args ---
const args = process.argv.slice(2);
const has = flag => args.includes(flag);
const getVal = (flag, fallback=null) => {
  const i = args.indexOf(flag);
  if (i !== -1 && i + 1 < args.length) return args[i+1];
  return fallback;
};

// --- Actions (animations) ---
const actions = {
  eat: [
    `  /\\_/\\  🍣\n ( o.o )\n  > ^ <`,
    `  /\\_/\\\n ( o.o )  nom nom 🍣\n  > ^ <`
  ],
  sleep: [
    `  /\\_/\\\n ( -.-) zZ\n  > ^ <`,
    `  /\\_/\\\n ( -_-) zzZ\n  > ^ <`
  ],
  type: [
    `  /\\_/\\   ⌨️\n ( •.• )\n  > ^ <  tap tap`,
    `  /\\_/\\   ⌨️\n ( •o• )\n  > ^ <  tak tak`
  ],
  dance: [
    `~(=^‥^)/`,
    `\\(=^‥^=)~`
  ]
};

async function animateFrames(frames, loops=6, delay=300) {
  for (let i = 0; i < loops; i++) {
    const f = frames[i % frames.length];
    process.stdout.write("\x1Bc"); // clear screen
    console.log(f);
    await new Promise(r => setTimeout(r, delay));
  }
}

// --- CLI Flags ---
if (has("--list")) {
  console.log(cyan("Available cats:"));
  cats.forEach((c, i) => console.log(dim(`#${i}`)));
  console.log(cyan("\nAvailable actions:"));
  Object.keys(actions).forEach(a => console.log(dim(`- ${a}`)));
  process.exit(0);
}

let chosen;
if (has("--random")) {
  chosen = Math.floor(Math.random() * cats.length);
} else if (has("--cat")) {
  const n = parseInt(getVal("--cat", "0"), 10);
  chosen = isNaN(n) ? 0 : Math.max(0, Math.min(cats.length - 1, n));
} else {
  chosen = 0;
}

const text = getVal("--say", null);
const action = getVal("--action", null);

// --- Animation helper for text ---
async function printAnimated(s, delay=12) {
  for (const ch of s) {
    process.stdout.write(ch);
    await new Promise(r => setTimeout(r, delay));
  }
  process.stdout.write("\n");
}

// --- Main ---
(async () => {
  if (action && actions[action]) {
    await animateFrames(actions[action]);
    return;
  }

  const out = bubble(text) + cyan(cats[chosen]);

  if (has("--animate")) {
    await printAnimated(out + "\n");
  } else {
    console.log(out + "\n");
  }

  // tiny easter egg: press 'c' during animate to meow (noop if not TTY)
  if (process.stdin.isTTY && has("--animate")) {
    process.stdin.setRawMode(true);
    process.stdin.resume();
    process.stdin.on("data", buf => {
      const key = buf.toString().toLowerCase();
      if (key === "c") {
        process.stdout.write(mag("meow~ 😺\n"));
      } else if (key === "\u0003" || key === "q") { // Ctrl+C or q
        process.stdout.write(dim("bye!\n"));
        process.exit(0);
      }
    });
  }
})();
