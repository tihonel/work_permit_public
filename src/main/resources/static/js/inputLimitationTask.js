//Ограничение ввода в поручается
var coefficientCutting = 0.95;
var textarea = document.getElementById("task");
var maxFirstRowWidth = 610;
var maxRowWidth = 675; // Максимальная ширина строки в пикселях
var maxRows = 3;

textarea.addEventListener("input", function(event) {
    var lines = textarea.value.split("\n");

    lines.forEach(function(line, index) {
        var font = window.getComputedStyle(textarea).font;
        var lineWidth = measureTextWidth(line, font);

        if(index === 0 && lineWidth > maxFirstRowWidth) {
            lines[index] = line.substring(0, line.length * coefficientCutting);
        } else if (lineWidth > maxRowWidth) {
            // Если ширина строки превышает максимальную, обрезаем ее
            lines[index] = line.substring(0, line.length * coefficientCutting);
        }
    });
    // Проверяем количество строк и удаляем лишние строки
    if (lines.length > maxRows) {
        lines = lines.slice(0, maxRows);
    }
    // Обновляем текст в поле textarea
    textarea.value = lines.join("\n");
});