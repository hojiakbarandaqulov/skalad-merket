export const ROUTES = [
  {
    id: "overview",
    label: "Обзор",
    eyebrow: "Sklad Market admin",
    title: "Панель модератора",
    subtitle: "Отслеживайте поток модерации, проверяйте очереди компаний и товаров, закрывайте жалобы и держите под контролем пользовательские аккаунты."
  },
  {
    id: "products",
    label: "Товары",
    eyebrow: "Контент-контроль",
    title: "Модерация товаров",
    subtitle: "Проверяйте новые объявления, быстро принимайте решения и управляйте промо-размещением в одном потоке."
  },
  {
    id: "companies",
    label: "Компании",
    eyebrow: "Верификация",
    title: "Проверка компаний",
    subtitle: "Обрабатывайте документы, STIR и статусы верификации в аккуратной очереди без лишних переходов."
  },
  {
    id: "reports",
    label: "Жалобы",
    eyebrow: "Trust & Safety",
    title: "Центр жалоб",
    subtitle: "Приоритизируйте критические кейсы, открывайте детали контента и закрывайте обращения с историей решения."
  },
  {
    id: "accounts",
    label: "Аккаунты",
    eyebrow: "User control",
    title: "Управление аккаунтами",
    subtitle: "Ищите пользователей, блокируйте нарушителей и сбрасывайте активные сессии без перехода в другие панели."
  }
];

export const DEFAULT_CONFIG = {
  baseUrl: "http://localhost:8080/api/v1",
  token: "",
  liveMode: false
};

export const ICONS = {
  overview:
    '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><path d="M4 13h6V4H4zm10 7h6V4h-6zM4 20h6v-5H4zm10 0h6v-8h-6z"/></svg>',
  products:
    '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><path d="M4 7.5 12 3l8 4.5M4 7.5V16.5L12 21l8-4.5V7.5M12 12l8-4.5M12 12v9M12 12 4 7.5"/></svg>',
  companies:
    '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><path d="M4 20V7.5A1.5 1.5 0 0 1 5.5 6H10v14m0 0h10V4.5A1.5 1.5 0 0 0 18.5 3H11a1 1 0 0 0-1 1v16Zm-3-8h1m-1 4h1m4-8h1m-1 4h1m4-8h1m-1 4h1"/></svg>',
  reports:
    '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><path d="m12 9 .1 4m-.1 4h.01M10.7 3.4l-8 14a1.5 1.5 0 0 0 1.3 2.2h16a1.5 1.5 0 0 0 1.3-2.2l-8-14a1.5 1.5 0 0 0-2.6 0Z"/></svg>',
  accounts:
    '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><path d="M16 21v-2a4 4 0 0 0-4-4H7a4 4 0 0 0-4 4v2m18 0v-2a4 4 0 0 0-3-3.87M13 5.13a4 4 0 1 1 0 7.75M9.5 11a4 4 0 1 0 0-8 4 4 0 0 0 0 8Z"/></svg>',
  bell:
    '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><path d="M6 8a6 6 0 1 1 12 0c0 7 3 9 3 9H3s3-2 3-9m6 13a2.5 2.5 0 0 0 2.45-2h-4.9A2.5 2.5 0 0 0 12 21Z"/></svg>',
  spark:
    '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><path d="m12 3 1.9 5.1L19 10l-5.1 1.9L12 17l-1.9-5.1L5 10l5.1-1.9L12 3Z"/></svg>',
  settings:
    '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><path d="M12 15.5A3.5 3.5 0 1 0 12 8.5a3.5 3.5 0 0 0 0 7Zm8-3.5 1.5-2.6-2-3.4-3 .2a8.8 8.8 0 0 0-2.2-1.3L13 2h-4l-1.3 2.9a8.8 8.8 0 0 0-2.2 1.3l-3-.2-2 3.4L4 12l-1.5 2.6 2 3.4 3-.2a8.8 8.8 0 0 0 2.2 1.3L9 22h4l1.3-2.9a8.8 8.8 0 0 0 2.2-1.3l3 .2 2-3.4Z"/></svg>',
  refresh:
    '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><path d="M21 12a9 9 0 1 1-2.64-6.36M21 3v6h-6"/></svg>',
  shield:
    '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><path d="m12 3 7 3v5c0 5-3.4 9-7 10-3.6-1-7-5-7-10V6l7-3Z"/></svg>'
};

export function clone(value) {
  return JSON.parse(JSON.stringify(value));
}

export function normalizeRoute(hash) {
  const raw = hash.replace(/^#\/?/, "");
  return ROUTES.some((route) => route.id === raw) ? raw : "overview";
}

export function escapeHtml(value) {
  return String(value ?? "")
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#39;");
}

export function initials(text) {
  return String(text || "")
    .split(/\s+/)
    .filter(Boolean)
    .slice(0, 2)
    .map((part) => part[0]?.toUpperCase() || "")
    .join("");
}

export function renderOptions(options, selectedValue) {
  return options
    .map(([value, label]) => `<option value="${escapeHtml(value)}" ${value === selectedValue ? "selected" : ""}>${escapeHtml(label)}</option>`)
    .join("");
}

export function pickArray(value) {
  if (Array.isArray(value)) {
    return value;
  }
  if (value && Array.isArray(value.content)) {
    return value.content;
  }
  if (value && Array.isArray(value.items)) {
    return value.items;
  }
  return [];
}

export function unwrapPayload(payload) {
  if (payload && typeof payload === "object" && "data" in payload) {
    return payload.data;
  }
  return payload;
}

export function statusPresentation(value, scope) {
  const map = {
    product: {
      pending: { tone: "warning", label: "Pending" },
      approved: { tone: "success", label: "Approved" },
      rejected: { tone: "danger", label: "Rejected" },
      blocked: { tone: "danger", label: "Blocked" }
    },
    company: {
      pending_verification: { tone: "warning", label: "Pending verification" },
      verified: { tone: "success", label: "Verified" },
      rejected: { tone: "danger", label: "Rejected" },
      blocked: { tone: "danger", label: "Blocked" }
    },
    report: {
      new: { tone: "danger", label: "New" },
      investigating: { tone: "warning", label: "Investigating" },
      resolved: { tone: "success", label: "Resolved" },
      rejected: { tone: "neutral", label: "Rejected" }
    },
    account: {
      active: { tone: "success", label: "Active" },
      blocked: { tone: "danger", label: "Blocked" }
    }
  };

  return map[scope]?.[value] || { tone: "neutral", label: value };
}
