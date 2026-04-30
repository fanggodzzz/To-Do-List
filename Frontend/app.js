const DEFAULT_API_BASE = "http://localhost:8080/api/todo";

const queryApiBase = new URLSearchParams(window.location.search).get("api");
if (queryApiBase) {
  localStorage.setItem("todoApiBase", queryApiBase);
}

const API =
  queryApiBase || localStorage.getItem("todoApiBase") || DEFAULT_API_BASE;

function apiUrl(path) {
  return `${API}${path}`;
}

const TAG_COLOR_PALETTE = [
  "#e74c3c",
  "#3498db",
  "#2ecc71",
  "#f39c12",
  "#9b59b6",
  "#e67e22",
  "#1abc9c",
  "#e91e63",
  "#7cb342",
  "#4338ca",
  "#f43f5e",
  "#00897b",
  "#d97706",
  "#7c3aed",
  "#059669",
  "#0284c7",
  "#d946ef",
  "#475569",
  "#ea580c",
  "#0d9488",
];

const state = {
  todos: [],
  tags: [],
  upcoming: [],
  overdue: [],
  currentMonth: new Date(new Date().getFullYear(), new Date().getMonth(), 1),
  selectedTodoId: null,
  selectedTagId: null,
  listDrawerContext: null,
};

const monthLabel = document.getElementById("monthLabel");
const calendarGrid = document.getElementById("calendarGrid");
const apiStatus = document.getElementById("apiStatus");
const tagLabelList = document.getElementById("tagLabelList");

const drawer = document.getElementById("detailDrawer");
const listDrawer = document.getElementById("listDrawer");
const tagDrawer = document.getElementById("tagDrawer");
const addTagDrawer = document.getElementById("addTagDrawer");
const drawerBackdrop = document.getElementById("drawerBackdrop");
const closeListDrawerBtn = document.getElementById("closeListDrawerBtn");
const closeDrawerBtn = document.getElementById("closeDrawerBtn");
const closeTagDrawerBtn = document.getElementById("closeTagDrawerBtn");
const closeAddTagDrawerBtn = document.getElementById("closeAddTagDrawerBtn");
const cancelAddTagBtn = document.getElementById("cancelAddTagBtn");
const saveTaskBtn = document.getElementById("saveTaskBtn");
const deleteTaskBtn = document.getElementById("deleteTaskBtn");
const saveTagBtn = document.getElementById("saveTagBtn");
const deleteTagBtn = document.getElementById("deleteTagBtn");
const confirmAddTagBtn = document.getElementById("confirmAddTagBtn");
const toggleDrawerAddBtn = document.getElementById("toggleDrawerAddBtn");
const drawerAddPanel = document.getElementById("drawerAddPanel");
const confirmDrawerAddBtn = document.getElementById("confirmDrawerAddBtn");

const detailDescription = document.getElementById("detailDescription");
const detailTag = document.getElementById("detailTag");
const detailDueDate = document.getElementById("detailDueDate");
const detailCompleted = document.getElementById("detailCompleted");
const listDrawerKicker = document.getElementById("listDrawerKicker");
const listDrawerTitle = document.getElementById("listDrawerTitle");
const listDrawerMeta = document.getElementById("listDrawerMeta");
const listDrawerItems = document.getElementById("listDrawerItems");
const upcomingSummaryCard = document.getElementById("upcomingSummaryCard");
const upcomingPreviewList = document.getElementById("upcomingPreviewList");
const overdueSummaryCard = document.getElementById("overdueSummaryCard");
const overduePreviewList = document.getElementById("overduePreviewList");
const tagEditName = document.getElementById("tagEditName");
const newTagDrawerName = document.getElementById("newTagDrawerName");

const addDescription = document.getElementById("addDescription");
const addTagSelect = document.getElementById("addTag");
const addDueDate = document.getElementById("addDueDate");

function formatDate(value) {
  if (!value) {
    return "";
  }

  return value.includes("T") ? value.split("T")[0] : value;
}

function getDateKey(dateValue) {
  return formatDate(dateValue);
}

function getMonthTitle(date) {
  return date.toLocaleDateString(undefined, { month: "long", year: "numeric" });
}

function formatLongDate(value) {
  const normalized = formatDate(value);
  if (!normalized) {
    return "";
  }

  return new Date(`${normalized}T00:00:00`).toLocaleDateString(undefined, {
    weekday: "long",
    year: "numeric",
    month: "long",
    day: "numeric",
  });
}

function parseDateValue(value) {
  const normalized = formatDate(value);
  return normalized ? new Date(`${normalized}T00:00:00`) : null;
}

function isSameCalendarDate(left, right) {
  return getDateKey(left) === getDateKey(right);
}

function sortTasksAscending(tasks) {
  return [...tasks].sort((left, right) => {
    const leftDate = parseDateValue(left.ac_due_date)?.getTime() ?? 0;
    const rightDate = parseDateValue(right.ac_due_date)?.getTime() ?? 0;
    return leftDate - rightDate;
  });
}

function sortTasksDescending(tasks) {
  return [...tasks].sort((left, right) => {
    const leftDate = parseDateValue(left.ac_due_date)?.getTime() ?? 0;
    const rightDate = parseDateValue(right.ac_due_date)?.getTime() ?? 0;
    return rightDate - leftDate;
  });
}

function clipDescription(text) {
  if (!text) {
    return "";
  }

  return text.length > 22 ? `${text.slice(0, 22)}...` : text;
}

function clipCalendarDescription(text) {
  if (!text) {
    return "";
  }

  return text.length > 14 ? `${text.slice(0, 14)}...` : text;
}

function openPicker(input) {
  if (!input) {
    return;
  }

  if (typeof input.showPicker === "function") {
    input.showPicker();
  }
}

function setStatus(message, isError = false) {
  apiStatus.textContent = message;
  apiStatus.classList.toggle("api-status--error", isError);
}

function syncBackdropState() {
  const shouldShowBackdrop =
    drawer.classList.contains("detail-drawer--open") ||
    listDrawer.classList.contains("list-drawer--open") ||
    tagDrawer.classList.contains("detail-drawer--open") ||
    addTagDrawer.classList.contains("detail-drawer--open");

  drawerBackdrop.classList.toggle("drawer-backdrop--open", shouldShowBackdrop);
}

function hexToRgba(hex, alpha) {
  const clean = hex.replace("#", "");
  const bigint = parseInt(clean, 16);
  const r = (bigint >> 16) & 255;
  const g = (bigint >> 8) & 255;
  const b = bigint & 255;
  return `rgba(${r}, ${g}, ${b}, ${alpha})`;
}

function getTagColor(tagId) {
  if (tagId == null) {
    return TAG_COLOR_PALETTE[0];
  }

  const index = Math.abs(Number(tagId)) % TAG_COLOR_PALETTE.length;
  return TAG_COLOR_PALETTE[index];
}

function buildTagOptions(selectElement, selectedTagId) {
  if (!selectElement) {
    return;
  }

  selectElement.innerHTML = "";

  const placeholder = document.createElement("option");
  placeholder.value = "";
  placeholder.textContent = "Select tag";
  selectElement.appendChild(placeholder);

  state.tags.forEach((tag) => {
    const option = document.createElement("option");
    option.value = String(tag.tag_id);
    option.textContent = tag.tag_name;
    if (selectedTagId != null && Number(tag.tag_id) === Number(selectedTagId)) {
      option.selected = true;
    }
    selectElement.appendChild(option);
  });
}

function renderTagLabels() {
  if (!tagLabelList) {
    return;
  }

  tagLabelList.innerHTML = "";

  state.tags.forEach((tag) => {
    const pill = document.createElement("button");
    pill.type = "button";
    pill.className = "tag-pill";
    pill.textContent = tag.tag_name;
    const color = getTagColor(tag.tag_id);
    pill.style.background = hexToRgba(color, 0.28);
    pill.style.borderColor = hexToRgba(color, 0.7);
    pill.onclick = () => openTagDrawer(tag.tag_id);
    tagLabelList.appendChild(pill);
  });

  const addButton = document.createElement("button");
  addButton.type = "button";
  addButton.className = "icon-btn tag-plus-btn";
  addButton.textContent = "+";
  addButton.setAttribute("aria-label", "Add tag");
  addButton.onclick = openAddTagDrawer;
  tagLabelList.appendChild(addButton);

  if (!state.tags.length) {
    const empty = document.createElement("span");
    empty.className = "tag-label-empty";
    empty.textContent = "No tags yet";
    tagLabelList.appendChild(empty);
  }
}

function setDrawerAddPanelOpen(isOpen) {
  if (!drawerAddPanel) {
    return;
  }

  drawerAddPanel.classList.toggle("drawer-add-panel--open", isOpen);
  if (toggleDrawerAddBtn) {
    toggleDrawerAddBtn.setAttribute("aria-expanded", String(isOpen));
  }
}

function resetDrawerAddForm() {
  addDescription.value = "";
  addTagSelect.value = "";
}

function refreshActiveListDrawer() {
  if (
    !state.listDrawerContext ||
    !listDrawer.classList.contains("list-drawer--open")
  ) {
    return;
  }

  if (state.listDrawerContext.allowAdd && state.listDrawerContext.date) {
    openDayDrawer(new Date(`${state.listDrawerContext.date}T00:00:00`));
    return;
  }

  if (state.listDrawerContext.kicker === "Upcoming") {
    openUpcomingDrawer();
    return;
  }

  if (state.listDrawerContext.kicker === "Overdue") {
    openOverdueDrawer();
  }
}

function refreshAfterMutation() {
  return Promise.all([
    loadTags(),
    loadTodos(),
    loadUpcomingTasks(),
    loadOverdueTasks(),
  ]);
}

function loadTags() {
  return fetch(apiUrl("/tags"))
    .then((res) => {
      if (!res.ok) {
        throw new Error(`Tags endpoint failed (${res.status}) at ${res.url}`);
      }
      return res.json();
    })
    .then((data) => {
      state.tags = Array.isArray(data) ? data : [];
      buildTagOptions(addTagSelect);
      buildTagOptions(detailTag);
      renderTagLabels();
    })
    .catch((error) => {
      console.error("Failed to load tags:", error);
      setStatus(`Unable to load tags. ${error.message}`, true);
    });
}

function createTag(name) {
  const trimmedName = (name || "").trim();
  if (!trimmedName) {
    setStatus("Tag name is required.", true);
    return Promise.reject(new Error("Tag name is required"));
  }

  return fetch(apiUrl("/tags/add"), {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      tag_name: trimmedName,
    }),
  }).then((res) => {
    if (!res.ok) {
      throw new Error("Unable to add tag");
    }
    return refreshAfterMutation();
  });
}

function updateTag(tagId, name) {
  const trimmedName = (name || "").trim();
  if (!trimmedName) {
    setStatus("Tag name is required.", true);
    return Promise.reject(new Error("Tag name is required"));
  }

  return fetch(apiUrl(`/tags/update/${tagId}`), {
    method: "PATCH",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      tag_name: trimmedName,
    }),
  }).then((res) => {
    if (!res.ok) {
      throw new Error("Unable to update tag");
    }
    return refreshAfterMutation();
  });
}

function deleteTag(tagId) {
  return fetch(apiUrl(`/tags/delete/${tagId}`), {
    method: "DELETE",
  }).then((res) => {
    if (!res.ok) {
      throw new Error("Unable to delete tag");
    }
    return refreshAfterMutation();
  });
}

function openListDrawer(context) {
  state.listDrawerContext = context;
  listDrawerKicker.textContent = context.kicker;
  listDrawerTitle.textContent = context.title;
  listDrawerMeta.textContent = context.meta || "";
  if (toggleDrawerAddBtn) {
    toggleDrawerAddBtn.hidden = !context.allowAdd;
  }

  listDrawerItems.innerHTML = "";
  const items = context.items || [];

  if (!items.length) {
    const empty = document.createElement("p");
    empty.className = "list-drawer-empty";
    empty.textContent = context.emptyText || "No tasks found.";
    listDrawerItems.appendChild(empty);
  } else {
    items.forEach((todo) => {
      const item = document.createElement("div");
      item.className = "list-task-item";
      item.tabIndex = 0;
      item.setAttribute("role", "button");

      const tagColor = getTagColor(todo.ac_tag_id?.tag_id);
      item.style.borderColor = hexToRgba(tagColor, 0.52);

      const header = document.createElement("div");
      header.className = "list-task-item__header";

      const tagBadge = document.createElement("span");
      tagBadge.className = "calendar-task-tag";
      tagBadge.textContent = todo.ac_tag_id?.tag_name || "No tag";
      tagBadge.style.background = hexToRgba(tagColor, 0.35);
      tagBadge.style.borderColor = hexToRgba(tagColor, 0.72);

      const taskActions = document.createElement("div");
      taskActions.className = "list-task-actions";

      const status = document.createElement("span");
      status.className = "list-task-status";
      status.textContent = todo.ac_completed ? "Done" : "Open";

      const quickDeleteBtn = document.createElement("button");
      quickDeleteBtn.type = "button";
      quickDeleteBtn.className = "list-task-delete-btn";
      quickDeleteBtn.textContent = "🗑";
      quickDeleteBtn.setAttribute("aria-label", "Delete task");
      quickDeleteBtn.onclick = (event) => {
        event.stopPropagation();
        deleteTaskById(todo.ac_id).catch(() =>
          setStatus("Unable to delete task.", true),
        );
      };

      taskActions.appendChild(status);
      taskActions.appendChild(quickDeleteBtn);

      header.appendChild(tagBadge);
      header.appendChild(taskActions);

      const description = document.createElement("p");
      description.className = "list-task-description";
      description.textContent = todo.ac_description || "Untitled task";

      const dueDate = document.createElement("p");
      dueDate.className = "list-task-date";
      dueDate.textContent = `Due ${formatLongDate(todo.ac_due_date)}`;

      item.appendChild(header);
      item.appendChild(description);
      item.appendChild(dueDate);
      item.onclick = () => openDrawer(todo.ac_id);
      item.onkeydown = (event) => {
        if (event.key === "Enter" || event.key === " ") {
          event.preventDefault();
          openDrawer(todo.ac_id);
        }
      };
      listDrawerItems.appendChild(item);
    });
  }

  if (context.allowAdd) {
    addDueDate.value = context.date || "";
    buildTagOptions(addTagSelect);
    resetDrawerAddForm();
    setDrawerAddPanelOpen(false);
  } else {
    addDueDate.value = "";
    resetDrawerAddForm();
    setDrawerAddPanelOpen(false);
  }

  listDrawer.classList.add("list-drawer--open");
  drawerBackdrop.classList.add("drawer-backdrop--open");
}

function closeListDrawer() {
  state.listDrawerContext = null;
  listDrawer.classList.remove("list-drawer--open");
  listDrawerItems.innerHTML = "";
  setDrawerAddPanelOpen(false);
  syncBackdropState();
}

function openDayDrawer(day) {
  const key = getDateKey(day.toISOString());
  const tasks = sortTasksAscending(
    state.todos.filter((todo) => isSameCalendarDate(todo.ac_due_date, key)),
  );

  openListDrawer({
    kicker: "Date",
    title: formatLongDate(day.toISOString()),
    meta: `${tasks.length} task${tasks.length === 1 ? "" : "s"}`,
    items: tasks,
    emptyText: "No tasks for this date.",
    allowAdd: true,
    date: key,
  });
}

function openUpcomingDrawer() {
  openListDrawer({
    kicker: "Upcoming",
    title: "All upcoming tasks",
    meta: `${state.upcoming.length} task${state.upcoming.length === 1 ? "" : "s"}`,
    items: state.upcoming,
    emptyText: "No upcoming tasks.",
    allowAdd: false,
  });
}

function openOverdueDrawer() {
  openListDrawer({
    kicker: "Overdue",
    title: "All overdue tasks",
    meta: `${state.overdue.length} task${state.overdue.length === 1 ? "" : "s"}`,
    items: state.overdue,
    emptyText: "No overdue tasks.",
    allowAdd: false,
  });
}

function renderSummaryPreview(container, items, emptyText) {
  if (!container) {
    return;
  }

  container.innerHTML = "";

  if (!items.length) {
    const empty = document.createElement("p");
    empty.className = "summary-empty";
    empty.textContent = emptyText;
    container.appendChild(empty);
    return;
  }

  // Keep full data but visually show a 5-item viewport via CSS scrolling.
  items.forEach((todo) => {
    const row = document.createElement("div");
    row.className = "summary-preview-item";

    const title = document.createElement("span");
    title.className = "summary-preview-item__title";
    title.textContent = clipDescription(todo.ac_description);

    const meta = document.createElement("span");
    meta.className = "summary-preview-item__meta";
    meta.textContent = `${formatDate(todo.ac_due_date)} · ${todo.ac_tag_id?.tag_name || "No tag"}`;

    row.appendChild(title);
    row.appendChild(meta);
    container.appendChild(row);
  });
}

function renderSummaryBoxes() {
  renderSummaryPreview(
    upcomingPreviewList,
    sortTasksAscending(state.upcoming),
    "No upcoming tasks.",
  );
  renderSummaryPreview(
    overduePreviewList,
    sortTasksDescending(state.overdue),
    "No overdue tasks.",
  );
}

function openTagDrawer(tagId) {
  const tag = state.tags.find((item) => item.tag_id === tagId);
  if (!tag) {
    return;
  }

  state.selectedTagId = tag.tag_id;
  tagEditName.value = tag.tag_name || "";
  tagDrawer.classList.add("detail-drawer--open");
  drawerBackdrop.classList.add("drawer-backdrop--open");
}

function closeTagDrawer() {
  state.selectedTagId = null;
  tagEditName.value = "";
  tagDrawer.classList.remove("detail-drawer--open");
  syncBackdropState();
}

function openAddTagDrawer() {
  newTagDrawerName.value = "";
  addTagDrawer.classList.add("detail-drawer--open");
  drawerBackdrop.classList.add("drawer-backdrop--open");
}

function closeAddTagDrawer() {
  newTagDrawerName.value = "";
  addTagDrawer.classList.remove("detail-drawer--open");
  syncBackdropState();
}

function addTagFromDrawer() {
  createTag(newTagDrawerName.value)
    .then(() => {
      closeAddTagDrawer();
      setStatus("Tag added.");
    })
    .catch(() => setStatus("Unable to add tag.", true));
}

function saveTagChanges() {
  if (state.selectedTagId == null) {
    return;
  }

  updateTag(state.selectedTagId, tagEditName.value)
    .then(() => {
      closeTagDrawer();
      setStatus("Tag updated.");
    })
    .catch(() => setStatus("Unable to update tag.", true));
}

function deleteSelectedTag() {
  if (state.selectedTagId == null) {
    return;
  }

  deleteTag(state.selectedTagId)
    .then(() => {
      closeTagDrawer();
      setStatus("Tag deleted.");
    })
    .catch(() => setStatus("Unable to delete tag.", true));
}

function buildMonthGrid() {
  const year = state.currentMonth.getFullYear();
  const month = state.currentMonth.getMonth();

  const firstDay = new Date(year, month, 1);
  const startOffset = (firstDay.getDay() + 6) % 7;
  const gridStart = new Date(year, month, 1 - startOffset);

  const days = [];
  for (let i = 0; i < 42; i += 1) {
    const d = new Date(gridStart);
    d.setDate(gridStart.getDate() + i);
    days.push(d);
  }

  return days;
}

function getCurrentMonthQuery() {
  return `?year=${state.currentMonth.getFullYear()}&month=${state.currentMonth.getMonth() + 1}`;
}

function todosByDateMap() {
  const map = new Map();
  state.todos.forEach((todo) => {
    const key = getDateKey(todo.ac_due_date);
    if (!map.has(key)) {
      map.set(key, []);
    }
    map.get(key).push(todo);
  });
  return map;
}

function renderCalendar() {
  monthLabel.textContent = getMonthTitle(state.currentMonth);
  calendarGrid.innerHTML = "";

  const weekdayLabels = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"];
  weekdayLabels.forEach((label) => {
    const head = document.createElement("div");
    head.className = "weekday-head";
    head.textContent = label;
    calendarGrid.appendChild(head);
  });

  const map = todosByDateMap();
  const todayKey = getDateKey(new Date().toISOString());

  buildMonthGrid().forEach((day) => {
    const key = getDateKey(day.toISOString());
    const isCurrentMonth = day.getMonth() === state.currentMonth.getMonth();
    const dayCell = document.createElement("div");
    dayCell.className = "day-cell";
    if (!isCurrentMonth) {
      dayCell.classList.add("day-cell--muted");
    }
    if (key === todayKey) {
      dayCell.classList.add("day-cell--today");
    }
    dayCell.classList.add("day-cell--clickable");

    const dayNumber = document.createElement("span");
    dayNumber.className = "day-number";
    dayNumber.textContent = String(day.getDate());
    dayCell.appendChild(dayNumber);

    const items = map.get(key) || [];
    const visible = items.slice(0, 3);
    visible.forEach((todo) => {
      const task = document.createElement("button");
      task.type = "button";
      task.className = "calendar-task";
      if (todo.ac_completed) {
        task.classList.add("calendar-task--done");
      }

      task.style.background = "rgba(73, 108, 255, 0.26)";
      task.style.borderColor = "rgba(135, 160, 255, 0.32)";

      const taskText = document.createElement("span");
      taskText.className = "calendar-task-text";
      taskText.textContent = clipCalendarDescription(todo.ac_description);

      task.appendChild(taskText);
      task.onclick = (event) => {
        event.stopPropagation();
        openDrawer(todo.ac_id);
      };
      dayCell.appendChild(task);
    });

    if (items.length > 3) {
      const more = document.createElement("span");
      more.className = "task-more";
      more.textContent = `+${items.length - 3} more`;
      dayCell.appendChild(more);
    }

    dayCell.onclick = () => openDayDrawer(day);

    calendarGrid.appendChild(dayCell);
  });
}

function loadUpcomingTasks() {
  return fetch(apiUrl("/upcoming"))
    .then((res) => {
      if (!res.ok) {
        throw new Error(
          `Upcoming endpoint failed (${res.status}) at ${res.url}`,
        );
      }

      return res.json();
    })
    .then((data) => {
      state.upcoming = Array.isArray(data) ? data : [];
      renderSummaryBoxes();
    })
    .catch((error) => {
      console.error("Failed to load upcoming tasks:", error);
      setStatus(`Unable to load upcoming tasks. ${error.message}`, true);
    });
}

function loadOverdueTasks() {
  return fetch(apiUrl("/overdue"))
    .then((res) => {
      if (!res.ok) {
        throw new Error(
          `Overdue endpoint failed (${res.status}) at ${res.url}`,
        );
      }

      return res.json();
    })
    .then((data) => {
      state.overdue = Array.isArray(data) ? data : [];
      renderSummaryBoxes();
    })
    .catch((error) => {
      console.error("Failed to load overdue tasks:", error);
      setStatus(`Unable to load overdue tasks. ${error.message}`, true);
    });
}

function loadTodos() {
  setStatus("Loading tasks...");

  return fetch(apiUrl(`/${getCurrentMonthQuery()}`))
    .then((res) => {
      if (!res.ok) {
        throw new Error(`Todos endpoint failed (${res.status}) at ${res.url}`);
      }

      return res.json();
    })
    .then((data) => {
      state.todos = Array.isArray(data) ? data : [];
      renderCalendar();
      setStatus("");
    })
    .catch((error) => {
      console.error("Failed to load todos:", error);
      setStatus(`Unable to load tasks. ${error.message}`, true);
    });
}

function addTodo() {
  const description = addDescription.value.trim();
  const tagId = addTagSelect.value;
  const dueDate = addDueDate.value || state.listDrawerContext?.date;

  if (!description || !tagId || !dueDate) {
    setStatus("Description and tag are required.", true);
    return;
  }

  fetch(apiUrl("/add"), {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      ac_due_date: dueDate,
      ac_description: description,
      ac_completed: false,
      ac_tag_id: {
        tag_id: Number(tagId),
      },
    }),
  })
    .then((res) => {
      if (!res.ok) {
        throw new Error("Unable to add task");
      }

      addDescription.value = "";
      addTagSelect.value = "";
      addDueDate.value = "";
      setStatus("Task added.");
      return refreshAfterMutation().then(() => {
        refreshActiveListDrawer();
      });
    })
    .catch(() => setStatus("Unable to add task.", true));
}

function deleteTaskById(todoId, options = {}) {
  const { closeDetailPanel = false } = options;

  return fetch(apiUrl(`/delete/${todoId}`), {
    method: "DELETE",
  }).then((res) => {
    if (!res.ok) {
      throw new Error("Unable to delete task");
    }

    if (closeDetailPanel) {
      closeDrawer();
    }

    setStatus("Task deleted.");
    return refreshAfterMutation().then(() => {
      refreshActiveListDrawer();
    });
  });
}

function openDrawer(todoId) {
  const todo = state.todos.find((item) => item.ac_id === todoId);
  if (!todo) {
    return;
  }

  state.selectedTodoId = todo.ac_id;
  detailDescription.value = todo.ac_description || "";
  buildTagOptions(detailTag, todo.ac_tag_id ? todo.ac_tag_id.tag_id : null);
  detailDueDate.value = formatDate(todo.ac_due_date);
  detailCompleted.checked = Boolean(todo.ac_completed);

  drawer.classList.add("detail-drawer--open");
  drawerBackdrop.classList.add("drawer-backdrop--open");
}

function closeDrawer() {
  state.selectedTodoId = null;
  drawer.classList.remove("detail-drawer--open");
  syncBackdropState();
}

function saveTask() {
  if (state.selectedTodoId == null) {
    return;
  }

  const payload = {
    ac_description: detailDescription.value.trim(),
    ac_due_date: detailDueDate.value,
    ac_completed: detailCompleted.checked,
    ac_tag_id: {
      tag_id: Number(detailTag.value),
    },
  };

  if (!payload.ac_description || !detailTag.value || !payload.ac_due_date) {
    setStatus("Description, tag, and due date are required.", true);
    return;
  }

  fetch(apiUrl(`/update/${state.selectedTodoId}`), {
    method: "PATCH",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(payload),
  })
    .then((res) => {
      if (!res.ok) {
        throw new Error("Unable to update task");
      }
      closeDrawer();
      setStatus("Task updated.");
      refreshAfterMutation();
    })
    .catch(() => setStatus("Unable to update task.", true));
}

function deleteTask() {
  if (state.selectedTodoId == null) {
    return;
  }

  deleteTaskById(state.selectedTodoId, { closeDetailPanel: true }).catch(() =>
    setStatus("Unable to delete task.", true),
  );
}

function closeAllDrawers() {
  closeListDrawer();
  closeDrawer();
  closeTagDrawer();
  closeAddTagDrawer();
  drawerBackdrop.classList.remove("drawer-backdrop--open");
}

document.getElementById("prevMonthBtn").addEventListener("click", () => {
  state.currentMonth = new Date(
    state.currentMonth.getFullYear(),
    state.currentMonth.getMonth() - 1,
    1,
  );
  loadTodos();
});

document.getElementById("nextMonthBtn").addEventListener("click", () => {
  state.currentMonth = new Date(
    state.currentMonth.getFullYear(),
    state.currentMonth.getMonth() + 1,
    1,
  );
  loadTodos();
});

closeListDrawerBtn.addEventListener("click", closeListDrawer);
toggleDrawerAddBtn.addEventListener("click", () => {
  if (!state.listDrawerContext?.allowAdd) {
    return;
  }

  setDrawerAddPanelOpen(
    !drawerAddPanel.classList.contains("drawer-add-panel--open"),
  );
});
closeDrawerBtn.addEventListener("click", closeDrawer);
closeTagDrawerBtn.addEventListener("click", closeTagDrawer);
closeAddTagDrawerBtn.addEventListener("click", closeAddTagDrawer);
cancelAddTagBtn.addEventListener("click", closeAddTagDrawer);
drawerBackdrop.addEventListener("click", closeAllDrawers);
saveTaskBtn.addEventListener("click", saveTask);
deleteTaskBtn.addEventListener("click", deleteTask);
saveTagBtn.addEventListener("click", saveTagChanges);
deleteTagBtn.addEventListener("click", deleteSelectedTag);
confirmAddTagBtn.addEventListener("click", addTagFromDrawer);
confirmDrawerAddBtn.addEventListener("click", addTodo);
upcomingSummaryCard.addEventListener("click", openUpcomingDrawer);
overdueSummaryCard.addEventListener("click", openOverdueDrawer);
newTagDrawerName.addEventListener("keydown", (event) => {
  if (event.key === "Enter") {
    event.preventDefault();
    addTagFromDrawer();
  }
});
tagEditName.addEventListener("keydown", (event) => {
  if (event.key === "Enter") {
    event.preventDefault();
    saveTagChanges();
  }
});

detailDueDate.addEventListener("focus", () => openPicker(detailDueDate));

Promise.all([loadTags(), loadTodos(), loadUpcomingTasks(), loadOverdueTasks()]);
