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

function openStudentUpdateModal(button) {
    const userId = button.getAttribute('data-user-id');
    const groupId = button.getAttribute('data-group-id');

    document.getElementById('updatedUserId').value = userId;

    const select = document.getElementById('updatedGroup');
    select.selectedIndex = -1;

    const groupOption = [...select.options].find(option => option.value === groupId);
    if (groupOption) {
        groupOption.selected = true;
    }


    document.getElementById('update-modal').style.display = 'flex';
}

function openCourseUpdateModal(button) {
    const courseId = button.getAttribute('data-course-id');
    const courseName = button.getAttribute('data-course-name');
    const courseDescription = button.getAttribute('data-course-description');
    const userId = button.getAttribute('data-user-id');

    document.getElementById('updatedCourseId').value = courseId;
    document.getElementById('updatedCourseName').value = courseName;
    document.getElementById('updatedCourseDescription').value = courseDescription;

    const select = document.getElementById('updatedTeacher');
    select.selectedIndex = -1;

    const teacherOption = [...select.options].find(option => option.value === userId);
    if (teacherOption) {
        teacherOption.selected = true;
    }


    document.getElementById('update-modal').style.display = 'flex';
}

function openLessonUpdateModal(button) {
    const lessonId = button.getAttribute('data-lesson-id');
    const courseId = button.getAttribute('data-course-id');
    const groupId = button.getAttribute('data-group-id');
    const startTime = button.getAttribute('data-start-time');
    const endTime = button.getAttribute('data-end-time');
    const dayOfWeek = button.getAttribute('data-day');
    const classroomId = button.getAttribute('data-classroom-id');

    document.getElementById('updatedLessonId').value = lessonId;
    document.getElementById('updatedStartTime').value = startTime;
    document.getElementById('updatedEndTime').value = endTime;
    document.getElementById('updatedDay').value = dayOfWeek;

    const courseSelect = document.getElementById('updatedCourse');
    courseSelect.selectedIndex = -1;
    const courseOption = [...courseSelect.options].find(option => option.value === courseId);
    if (courseOption) {
        courseOption.selected = true;
    }

    const groupSelect = document.getElementById('updatedGroup');
    groupSelect.selectedIndex = -1;
    const groupOption = [...groupSelect.options].find(option => option.value === groupId);
    if (groupOption) {
        groupOption.selected = true;
    }

    const classroomSelect = document.getElementById('updatedClassroom');
    classroomSelect.selectedIndex = -1;
    const classroomOption = [...classroomSelect.options].find(option => option.value === classroomId);
    if (classroomOption) {
        classroomOption.selected = true;
    }

    document.getElementById('update-modal').style.display = 'flex';
}

function openUpdateUserModal(button) {
    const userId = button.getAttribute('data-user-id');
    const status = button.getAttribute('data-status');

    document.getElementById('updatedUserId').value = userId;
    document.getElementById('updatedStatus').value = status;

    document.getElementById('update-modal').style.display = 'flex';
}
