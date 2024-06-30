//Ограничение флажков в членах бригады
// Обработчик события для каждого чекбокса
function handleCheckboxChange(event) {
    var checkbox = event.target;
    var row = checkbox.closest("tr");
    var maxChecked = 1; // Максимальное количество выбранных чекбоксов в строке

    // Если количество выбранных чекбоксов превышает максимальное, отключаем остальные чекбоксы в строке
    if (countCheckedCheckboxes(row) >= maxChecked) {
        var checkboxes = row.querySelectorAll("input[type='checkbox']");
        checkboxes.forEach(function(checkbox) {
            if (!checkbox.checked) {
                checkbox.disabled = true;
            }
        });
    } else {
        // Включаем все чекбоксы в строке
        var checkboxes = row.querySelectorAll("input[type='checkbox']");
        checkboxes.forEach(function(checkbox) {
            checkbox.disabled = false;
        });
    }
}

function countCheckedCheckboxes(row) {
    var checkboxes = row.querySelectorAll("input[type='checkbox']");
    var checkedCount = 0;
    checkboxes.forEach(function(checkbox) {
        if (checkbox.checked) {
            checkedCount++;
        }
    });
    return checkedCount;
}

// Добавляем обработчик события для каждого чекбокса в таблице
var checkboxes = document.querySelectorAll("#membersTable input[type='checkbox']");
checkboxes.forEach(function(checkbox) {
    checkbox.addEventListener("change", handleCheckboxChange);
});