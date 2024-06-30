//Ограничение ввода в отдельных указаниях
var textareaInstr = document.getElementById("instructions");
var maxFirstRowWidthInstr = 590;
var maxRowWidthInstr = 675;// Максимальная ширина строки в пикселях
var maxRowsInstr = 4;

textareaInstr.addEventListener("input", function(event) {
    var lines = textareaInstr.value.split("\n");

    lines.forEach(function(line, index) {
        var font = window.getComputedStyle(textareaInstr).font;
        var lineWidth = measureTextWidth(line, font);
        if(index === 0 && lineWidth > maxFirstRowWidthInstr){
            lines[index] = line.substring(0, line.length * coefficientCutting);
        } else if (lineWidth > maxRowWidthInstr) {
            // Если ширина строки превышает максимальную, обрезаем ее
            lines[index] = line.substring(0, line.length * coefficientCutting);
        }
    });
    // Проверяем количество строк и удаляем лишние строки
    if (lines.length > maxRowsInstr) {
        lines = lines.slice(0, maxRowsInstr);
    }
    // Обновляем текст в поле textarea
    textareaInstr.value = lines.join("\n");
});