import { registerPlugin } from "@capacitor/core";

interface LauncherConfigPlugin {
  configure(options: { url: string }): Promise<void>;
}

const launcher = registerPlugin<LauncherConfigPlugin>("LauncherConfig");
const form = document.querySelector<HTMLFormElement>("#connection-form");
const input = document.querySelector<HTMLInputElement>("#server-url");
const button = document.querySelector<HTMLButtonElement>("#connect-button");
const error = document.querySelector<HTMLElement>("#connection-error");

if (!form || !input || !button || !error) {
  throw new Error("The Lamplit connection form is incomplete.");
}

function showError(message: string) {
  error.textContent = message;
  error.hidden = false;
}

function normalizeUrl(value: string) {
  const url = new URL(value.trim());
  if (!/^https?:$/.test(url.protocol) || !url.hostname || url.username || url.password) {
    throw new Error();
  }
  url.hash = "";
  return url.href;
}

form.addEventListener("submit", async (event) => {
  event.preventDefault();
  error.hidden = true;

  let url: string;
  try {
    url = normalizeUrl(input.value);
  } catch {
    showError("Enter a full http:// or https:// address with a hostname.");
    input.focus();
    return;
  }

  button.disabled = true;
  button.textContent = "Opening Lamplit…";
  try {
    await launcher.configure({ url });
  } catch (cause) {
    button.disabled = false;
    button.textContent = "Connect to Lamplit";
    showError(cause instanceof Error ? cause.message : "Lamplit Mobile could not save this address. Try again.");
  }
});
