const API = "http://localhost:8080/api/todo";

const state = {
  todos: [],
  currentMonth: new Date(new Date().getFullYear(), new Date().getMonth(), 1),
  selectedTodoId: null,
};

const monthLabel = document.getElementById("monthLabel");
const calendarGrid = document.getElementById("calendarGrid");
const apiStatus = document.getElementById("apiStatus");

const drawer = document.getElementById("detailDrawer");
const drawerBackdrop = document.getElementById("drawerBackdrop");
const closeDrawerBtn = document.getElementById("closeDrawerBtn");
const saveTaskBtn = document.getElementById("saveTaskBtn");
const deleteTaskBtn = document.getElementById("deleteTaskBtn");

const detailDescription = document.getElementById("detailDescription");
const detailDueDate = document.getElementById("detailDueDate");
const detailCompleted = document.getElementById("detailCompleted");

const addDescription = document.getElementById("addDescription");
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

function clipDescription(text) {
  if (!text) {
    return "";
  }

  return text.length > 22 ? `${text.slice(0, 22)}...` : text;
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
      task.textContent = clipDescription(todo.ac_description);
      task.onclick = () => openDrawer(todo.ac_id);
      dayCell.appendChild(task);
    });

    if (items.length > 3) {
      const more = document.createElement("span");
      more.className = "task-more";
      more.textContent = `+${items.length - 3} more`;
      dayCell.appendChild(more);
    }

    calendarGrid.appendChild(dayCell);
  });
}

function loadTodos() {
  setStatus("Loading tasks...");

  fetch(API + "/")
    .then((res) => {
      if (!res.ok) {
        throw new Error(`Request failed with status ${res.status}`);
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
      setStatus("Unable to load tasks. Check CORS and backend logs.", true);
    });
}

function addTodo() {
  const description = addDescription.value.trim();
  const dueDate = addDueDate.value;

  if (!description || !dueDate) {
    setStatus("Description and due date are required.", true);
    return;
  }

  fetch(API + "/add", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      ac_due_date: dueDate,
      ac_description: description,
      ac_completed: false,
    }),
  })
    .then(() => {
      addDescription.value = "";
      addDueDate.value = "";
      setStatus("Task added.");
      loadTodos();
    })
    .catch(() => setStatus("Unable to add task.", true));
}

function openDrawer(todoId) {
  const todo = state.todos.find((item) => item.ac_id === todoId);
  if (!todo) {
    return;
  }

  state.selectedTodoId = todo.ac_id;
  detailDescription.value = todo.ac_description || "";
  detailDueDate.value = formatDate(todo.ac_due_date);
  detailCompleted.checked = Boolean(todo.ac_completed);

  drawer.classList.add("detail-drawer--open");
  drawerBackdrop.classList.add("drawer-backdrop--open");
}

function closeDrawer() {
  state.selectedTodoId = null;
  drawer.classList.remove("detail-drawer--open");
  drawerBackdrop.classList.remove("drawer-backdrop--open");
}

function saveTask() {
  if (state.selectedTodoId == null) {
    return;
  }

  const payload = {
    ac_description: detailDescription.value.trim(),
    ac_due_date: detailDueDate.value,
    ac_completed: detailCompleted.checked,
  };

  if (!payload.ac_description || !payload.ac_due_date) {
    setStatus("Description and due date are required.", true);
    return;
  }

  fetch(`${API}/update/${state.selectedTodoId}`, {
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
      loadTodos();
    })
    .catch(() => setStatus("Unable to update task.", true));
}

function deleteTask() {
  if (state.selectedTodoId == null) {
    return;
  }

  fetch(`${API}/delete/${state.selectedTodoId}`, {
    method: "DELETE",
  })
    .then((res) => {
      if (!res.ok) {
        throw new Error("Unable to delete task");
      }
      closeDrawer();
      setStatus("Task deleted.");
      loadTodos();
    })
    .catch(() => setStatus("Unable to delete task.", true));
}

document.getElementById("prevMonthBtn").addEventListener("click", () => {
  state.currentMonth = new Date(
    state.currentMonth.getFullYear(),
    state.currentMonth.getMonth() - 1,
    1,
  );
  renderCalendar();
});

document.getElementById("nextMonthBtn").addEventListener("click", () => {
  state.currentMonth = new Date(
    state.currentMonth.getFullYear(),
    state.currentMonth.getMonth() + 1,
    1,
  );
  renderCalendar();
});

closeDrawerBtn.addEventListener("click", closeDrawer);
drawerBackdrop.addEventListener("click", closeDrawer);
saveTaskBtn.addEventListener("click", saveTask);
deleteTaskBtn.addEventListener("click", deleteTask);

addDueDate.addEventListener("focus", () => openPicker(addDueDate));
detailDueDate.addEventListener("focus", () => openPicker(detailDueDate));

loadTodos();
