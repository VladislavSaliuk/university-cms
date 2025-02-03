function openCreateModal(id) {
    var modal = document.getElementById(id);
    if (modal) {
        modal.style.display = 'flex';
    }
}

function closeModal(id) {
    var modal = document.getElementById(id);
    if (modal) {
        modal.style.display = 'none';
    }
}