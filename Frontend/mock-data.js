export const mockAdminData = {
  overview: {
    metrics: [
      { key: "pendingProducts", label: "Товары на модерации", value: 148, trend: "+12 за сегодня", tone: "accent" },
      { key: "pendingCompanies", label: "Компании на проверке", value: 36, trend: "6 требуют документов", tone: "warning" },
      { key: "openReports", label: "Новые жалобы", value: 18, trend: "4 срочных кейса", tone: "danger" },
      { key: "blockedUsers", label: "Заблокированные аккаунты", value: 27, trend: "2 за последние 24 часа", tone: "neutral" }
    ],
    moderationLoad: [
      { day: "Пн", value: 44, muted: false },
      { day: "Вт", value: 38, muted: true },
      { day: "Ср", value: 61, muted: false },
      { day: "Чт", value: 57, muted: false },
      { day: "Пт", value: 78, muted: false },
      { day: "Сб", value: 40, muted: true },
      { day: "Вс", value: 28, muted: true }
    ],
    timeline: [
      {
        time: "09:42",
        title: "Поступила новая компания на верификацию",
        text: "OOO Agro House загрузила обновленный STIR и лицензию, очередь проверки сместилась на 1 позицию."
      },
      {
        time: "10:05",
        title: "Жалоба escalated в ручную проверку",
        text: "На товар “Автоматическая линия фасовки” пришло 3 жалобы по причине scam за 30 минут."
      },
      {
        time: "10:18",
        title: "Премиум-размещение выставлено",
        text: "Товар “Промышленный миксер 500л” получил TOP placement до 20 апреля."
      }
    ]
  },
  companies: [
    {
      id: 301,
      name: "OOO Agro House",
      owner: "Илхом Рахматов",
      category: "Сельхоз оборудование",
      region: "Ташкент",
      submittedAt: "2026-04-01 09:35",
      verification_status: "pending_verification",
      docs: ["STIR", "Свидетельство", "Лицензия"],
      description: "Поставщик линий сортировки, фасовки и холодильных камер для агросектора.",
      phone: "+998 90 123 45 67",
      website: "https://agrohouse.uz",
      products_count: 18,
      is_blocked: false
    },
    {
      id: 302,
      name: "Samarqand Trade Plast",
      owner: "Зухра Мирзаева",
      category: "Упаковка и тара",
      region: "Самарканд",
      submittedAt: "2026-03-31 18:14",
      verification_status: "pending_verification",
      docs: ["STIR", "Свидетельство"],
      description: "Производство и оптовая поставка пластиковой тары для пищевой отрасли.",
      phone: "+998 91 874 22 11",
      website: "https://tradeplast.uz",
      products_count: 9,
      is_blocked: false
    },
    {
      id: 303,
      name: "Orient Cooling Systems",
      owner: "Шухрат Умаров",
      category: "Холодильное оборудование",
      region: "Фергана",
      submittedAt: "2026-03-29 13:05",
      verification_status: "verified",
      docs: ["STIR", "Свидетельство", "Лицензия"],
      description: "Инженерные системы охлаждения для складов и производственных площадок.",
      phone: "+998 99 445 10 20",
      website: "https://orientcool.uz",
      products_count: 27,
      is_blocked: false
    },
    {
      id: 304,
      name: "Mega Market Supply",
      owner: "Фаррух Абдурахмонов",
      category: "Торговое оборудование",
      region: "Бухара",
      submittedAt: "2026-03-28 11:22",
      verification_status: "rejected",
      docs: ["STIR"],
      description: "Оборудование для магазинов, полки, кассовые зоны и стойки.",
      phone: "+998 95 000 30 30",
      website: "https://megamarket.uz",
      products_count: 11,
      is_blocked: false
    }
  ],
  products: [
    {
      id: 701,
      name: "Автоматическая фасовочная линия A-120",
      company: "OOO Agro House",
      category: "Фасовка и упаковка",
      region: "Ташкент",
      price: "от 18 500 USD",
      moderation_status: "pending",
      active_status: "active",
      createdAt: "2026-04-01 08:15",
      views: 124,
      favorites: 17,
      promotion: "none",
      description: "Линия для фасовки сыпучих продуктов с автоматической подачей и запайкой."
    },
    {
      id: 702,
      name: "Холодильная камера 40 м3",
      company: "Orient Cooling Systems",
      category: "Холодильное оборудование",
      region: "Фергана",
      price: "62 000 000 UZS",
      moderation_status: "approved",
      active_status: "active",
      createdAt: "2026-03-29 16:02",
      views: 299,
      favorites: 41,
      promotion: "top",
      description: "Камера шокового охлаждения для складов и магазинов с удаленным контролем."
    },
    {
      id: 703,
      name: "Стеллаж островной MOD-5",
      company: "Mega Market Supply",
      category: "Торговое оборудование",
      region: "Бухара",
      price: "договорная",
      moderation_status: "rejected",
      active_status: "draft",
      createdAt: "2026-03-28 10:43",
      views: 38,
      favorites: 3,
      promotion: "none",
      description: "Компактный металлический стеллаж для магазинов и складских зон."
    },
    {
      id: 704,
      name: "Пневматический упаковщик P-90",
      company: "Samarqand Trade Plast",
      category: "Фасовка и упаковка",
      region: "Самарканд",
      price: "8 900 USD",
      moderation_status: "pending",
      active_status: "active",
      createdAt: "2026-03-31 19:11",
      views: 67,
      favorites: 8,
      promotion: "vip",
      description: "Пневматическая упаковочная машина для средних производственных линий."
    }
  ],
  reports: [
    {
      id: 901,
      targetType: "product",
      targetName: "Автоматическая фасовочная линия A-120",
      company: "OOO Agro House",
      reasonCode: "scam",
      reporter: "buyer_204",
      submittedAt: "2026-04-01 09:18",
      status: "new",
      comment: "Менеджер запросил предоплату вне платформы и удалил переписку.",
      preview: "Пользователь утверждает, что продавец перевел диалог в Telegram и требует аванс."
    },
    {
      id: 902,
      targetType: "company",
      targetName: "Mega Market Supply",
      company: "Mega Market Supply",
      reasonCode: "fake",
      reporter: "buyer_118",
      submittedAt: "2026-03-31 17:26",
      status: "investigating",
      comment: "Номер телефона не отвечает, адрес офиса не найден.",
      preview: "Компания публикует дубли товаров и не подтверждает фактическое наличие."
    },
    {
      id: 903,
      targetType: "chat",
      targetName: "Диалог #chat-3049",
      company: "Orient Cooling Systems",
      reasonCode: "offensive",
      reporter: "seller_76",
      submittedAt: "2026-03-31 15:01",
      status: "new",
      comment: "Пользователь отправлял оскорбительные сообщения и спамил в чате.",
      preview: "Жалоба на токсичное поведение в чате после отказа от скидки."
    }
  ],
  accounts: [
    {
      id: "usr-1001",
      full_name: "Шахзод Мадрахимов",
      phone: "+998 90 777 11 22",
      role: "SELLER",
      region: "Ташкент",
      createdAt: "2025-11-18",
      is_blocked: false,
      last_login: "2026-04-01 09:48",
      companies: 2,
      leads: 14
    },
    {
      id: "usr-1002",
      full_name: "Дилрабо Акбарова",
      phone: "+998 91 444 93 82",
      role: "BUYER",
      region: "Самарканд",
      createdAt: "2026-02-04",
      is_blocked: true,
      last_login: "2026-03-30 14:31",
      companies: 0,
      leads: 7
    },
    {
      id: "usr-1003",
      full_name: "Фарход Рузиев",
      phone: "+998 99 100 20 30",
      role: "MODERATOR",
      region: "Ташкент",
      createdAt: "2025-08-21",
      is_blocked: false,
      last_login: "2026-04-01 10:02",
      companies: 0,
      leads: 0
    },
    {
      id: "usr-1004",
      full_name: "Муниса Каримова",
      phone: "+998 95 211 08 17",
      role: "SELLER",
      region: "Андижан",
      createdAt: "2025-12-11",
      is_blocked: false,
      last_login: "2026-03-31 18:43",
      companies: 1,
      leads: 5
    }
  ]
};
