document.addEventListener("DOMContentLoaded", function() {
    const alerts = document.querySelectorAll('.alert-custom');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.classList.add('fade-out');
        }, 3000);
    });
});