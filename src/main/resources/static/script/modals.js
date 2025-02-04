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

function openClassroomUpdateModal(button) {
    const classroomId = button.getAttribute('data-classroom-id');
    const classroomNumber = button.getAttribute('data-classroom-number');
    const classroomDescription = button.getAttribute('data-classroom-description');

    document.getElementById('updatedClassroomId').value = classroomId;
    document.getElementById('updatedClassroomNumber').value = classroomNumber;
    document.getElementById('updatedClassroomDescription').value = classroomDescription;

    document.getElementById('update-modal').style.display = 'flex';

}

function openGroupUpdateModal(button) {
    const groupId = button.getAttribute('data-group-id');
    const groupName = button.getAttribute('data-group-number');

    document.getElementById('updatedGroupId').value = groupId;
    document.getElementById('updatedGroupName').value = groupName;

    document.getElementById('update-modal').style.display = 'flex';

}