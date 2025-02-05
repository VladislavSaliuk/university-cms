document.getElementById('dayFilter').addEventListener('change', function () {
    let selectedDay = this.value;
    document.querySelectorAll('tbody tr').forEach(row => {
        row.style.display = selectedDay && row.querySelector('.day').textContent !== selectedDay ? 'none' : '';
    });
});

document.addEventListener("DOMContentLoaded", function () {
    const dayFilter = document.getElementById("dayFilter");
    const rows = document.querySelectorAll("tbody tr");

    const selectedDay = dayFilter.value;
    rows.forEach(row => {
        const dayCell = row.getAttribute("data-day");
        row.style.display = (selectedDay === "" || dayCell === selectedDay) ? "" : "none";
    });

    dayFilter.addEventListener("change", function () {
        const selectedDay = this.value;
        rows.forEach(row => {
            const dayCell = row.getAttribute("data-day");
            row.style.display = (selectedDay === "" || dayCell === selectedDay) ? "" : "none";
        });
    });
});