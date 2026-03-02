export function showNotification(message, type = "info") {
  const container = document.getElementById("notificationContainer") || createNotificationContainer();
  const toast = document.createElement("div");
  toast.className = `toast toast-${type}`;
  toast.setAttribute("role", "alert");
  toast.textContent = message;
  container.appendChild(toast);
  setTimeout(() => {
    toast.remove();
  }, 5000);
}

function createNotificationContainer() {
  const container = document.createElement("div");
  container.id = "notificationContainer";
  document.body.appendChild(container);
  return container;
}
