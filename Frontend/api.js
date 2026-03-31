import { pickArray, unwrapPayload } from "./utils.js";

export function shouldUseLive(config) {
  return Boolean(config.liveMode && config.baseUrl && config.token);
}

export async function apiRequest(config, path, options = {}) {
  const url = `${config.baseUrl.replace(/\/$/, "")}${path}`;
  const response = await fetch(url, {
    ...options,
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${config.token}`,
      ...(options.headers || {})
    }
  });

  let payload = {};
  try {
    payload = await response.json();
  } catch (error) {
    payload = {};
  }

  if (!response.ok || payload.success === false) {
    throw new Error(payload.message || `HTTP ${response.status}`);
  }

  return payload;
}

export async function fetchProducts(config) {
  const payload = await apiRequest(config, "/admin/products");
  return pickArray(unwrapPayload(payload)).map(normalizeProduct);
}

export async function fetchCompanies(config) {
  const payload = await apiRequest(config, "/admin/companies");
  return pickArray(unwrapPayload(payload)).map(normalizeCompany);
}

export async function fetchReports(config) {
  const payload = await apiRequest(config, "/admin/reports");
  return pickArray(unwrapPayload(payload)).map(normalizeReport);
}

export async function fetchAccounts(config) {
  const payload = await apiRequest(config, "/admin/users");
  return pickArray(unwrapPayload(payload)).map(normalizeAccount);
}

export async function fetchOverviewBundle(config) {
  const [products, companies, reports, accounts] = await Promise.all([
    fetchProducts(config),
    fetchCompanies(config),
    fetchReports(config),
    fetchAccounts(config)
  ]);

  return { products, companies, reports, accounts };
}

export async function runEntityAction(config, scope, action, id, formData = {}) {
  const bodyMap = {
    product: {
      approve: null,
      reject: { reason_code: formData.reasonCode || "other", comment: formData.comment || "" },
      block: { reason: formData.reason || formData.comment || "" },
      promote: {
        promotion_type: formData.promotionType || "top",
        starts_at: formData.startsAt || null,
        ends_at: formData.endsAt || null
      }
    },
    company: {
      verify: null,
      reject: { reason_code: formData.reasonCode || "other", comment: formData.comment || "" },
      block: { reason: formData.reason || formData.comment || "" }
    },
    report: {
      resolve: { resolution_note: formData.resolutionNote || formData.comment || "" },
      reject: { resolution_note: formData.resolutionNote || formData.comment || "" }
    },
    account: {
      block: { reason: formData.reason || formData.comment || "" },
      unblock: null,
      sessions: null
    }
  };

  const methodMap = {
    account: { sessions: "DELETE", default: "PUT" },
    default: "PUT"
  };

  const path = resolveActionPath(scope, action, id);
  const body = bodyMap[scope]?.[action] ?? null;
  const method = methodMap[scope]?.[action] || methodMap[scope]?.default || methodMap.default;

  return apiRequest(config, path, {
    method,
    body: body ? JSON.stringify(body) : undefined
  });
}

function resolveActionPath(scope, action, id) {
  if (scope === "product") {
    const map = {
      approve: `/admin/products/${id}/approve`,
      reject: `/admin/products/${id}/reject`,
      block: `/admin/products/${id}/block`,
      promote: `/admin/products/${id}/promote`
    };
    return map[action];
  }

  if (scope === "company") {
    const map = {
      verify: `/admin/companies/${id}/verify`,
      reject: `/admin/companies/${id}/reject`,
      block: `/admin/companies/${id}/block`
    };
    return map[action];
  }

  if (scope === "report") {
    const map = {
      resolve: `/admin/reports/${id}/resolve`,
      reject: `/admin/reports/${id}/reject`
    };
    return map[action];
  }

  const map = {
    block: `/admin/users/${id}/block`,
    unblock: `/admin/users/${id}/unblock`,
    sessions: `/admin/users/${id}/sessions`
  };
  return map[action];
}

export function normalizeProduct(item) {
  return {
    id: item.id,
    name: item.name || item.title || "Без названия",
    company: item.company?.name || item.company_name || item.companyName || "Компания не указана",
    category: item.category?.name_ru || item.category?.name_uz || item.category_name || "Категория не указана",
    region: item.region?.name || item.region_name || "Регион не указан",
    price: item.price ? `${item.price} ${item.currency || ""}`.trim() : item.price_text || "договорная",
    moderation_status: item.moderationStatus || item.moderation_status || item.status || "pending",
    active_status: item.activeStatus || item.active_status || "active",
    createdAt: item.createdAt || item.created_at || "-",
    views: item.views ?? 0,
    favorites: item.favorites ?? item.favorites_count ?? 0,
    promotion: item.promotionType || item.promotion_type || (item.isPromoted ? "top" : "none"),
    description: item.shortDescription || item.short_description || item.description || "Описание не заполнено."
  };
}

export function normalizeCompany(item) {
  return {
    id: item.id,
    name: item.name || "Компания",
    owner: item.ownerName || item.owner_name || item.user?.full_name || "Не указан",
    category: item.category || item.short_description || "Компания",
    region: item.region?.name || item.region_name || "Не указан",
    submittedAt: item.updatedAt || item.createdAt || item.created_at || "-",
    verification_status: item.verificationStatus || item.verification_status || "pending_verification",
    docs: pickArray(item.docs || item.documents || []).length ? pickArray(item.docs || item.documents || []) : ["STIR"],
    description: item.description || item.shortDescription || "Описание компании недоступно.",
    phone: item.phone_primary || item.phone || "—",
    website: item.website || "—",
    products_count: item.productsCount || item.products_count || 0,
    is_blocked: Boolean(item.isBlocked || item.is_blocked)
  };
}

export function normalizeReport(item) {
  return {
    id: item.id || item.report_id,
    targetType: item.targetType || item.target_type || "product",
    targetName: item.targetName || item.target_name || `Target #${item.targetId || item.target_id || ""}`,
    company: item.company || "—",
    reasonCode: item.reasonCode || item.reason_code || "other",
    reporter: item.reporter || item.user?.username || "unknown",
    submittedAt: item.createdAt || item.created_at || "-",
    status: item.status || "new",
    comment: item.comment || "Комментарий не указан",
    preview: item.preview || item.comment || "Нет превью по жалобе."
  };
}

export function normalizeAccount(item) {
  return {
    id: item.id || item.userId || item.user_id,
    full_name: item.full_name || item.fullName || item.username || "Пользователь",
    phone: item.phone || item.username || "—",
    role: item.role || item.user_type || "BUYER",
    region: item.region || item.address || "—",
    createdAt: item.createdAt || item.created_at || "-",
    is_blocked: Boolean(item.blocked || item.is_blocked || item.status === "BLOCKED"),
    last_login: item.lastLogin || item.last_login || "—",
    companies: item.companies || item.companies_count || 0,
    leads: item.leads || item.leads_count || 0
  };
}
