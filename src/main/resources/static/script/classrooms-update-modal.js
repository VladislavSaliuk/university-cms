function openUpdateModal(button) {
    const classroomId = button.getAttribute('data-classroom-id');
    const classroomNumber = button.getAttribute('data-classroom-number');
    const classroomDescription = button.getAttribute('data-classroom-description');

    document.getElementById('updatedClassroomId').value = classroomId;
    document.getElementById('updatedClassroomNumber').value = classroomNumber;
    document.getElementById('updatedClassroomDescription').value = classroomDescription;

    document.getElementById('update-modal').style.display = 'flex';

}