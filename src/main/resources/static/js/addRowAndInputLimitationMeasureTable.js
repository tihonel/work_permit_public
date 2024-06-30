//Добавление строки в таблицу мероприятий
var newOrder = 14;

function addRow(){
    var root = document.getElementById('measuresInput').getElementsByTagName('tbody')[0];
    var newRow = createMeasureRow(newOrder);
    var inputs = newRow.querySelectorAll("input[type='text']");
    inputs.forEach(function(input) {
        input.addEventListener("blur", handleInputBlur);
    });
    console.log(newRow)
    root.append(newRow);
    newOrder++;
}

function createMeasureRow(newOrder){
    const newRowHtml = `<table>
                                <tr>
                                    <input type="hidden" id="measures${newOrder}.number" name="measures[${newOrder}].number" value="${newOrder}"/>
                                    <td><input type="text" id="measures${ newOrder }.firstColumn" name="measures[${ newOrder }].firstColumn" value="" style="width: 200px;" class="col-first"/></td>
                                    <td><input type="text" id=measures${newOrder}.secondColumn" name="measures[${ newOrder }].secondColumn" value="" style="width: 390px;" class="col-second"/></td>
                                    <td><input type="text" id="measures${ newOrder }.thirdColumn" name="measures[${ newOrder }].thirdColumn" value="" style="width: 114px;" class="col-third"/></td>
                                </tr>
                            </table>`;

    const doc = (new DOMParser()).parseFromString(newRowHtml, "text/html");
    return doc.getElementsByTagName('tr')[0];
}

//Проверка введённого текста в таблице с мероприятиями
function measureTextWidth(text, font) {
    var span = document.createElement("span");
    span.style.font = font;
    span.textContent = text;
    span.style.visibility = "hidden";
    document.body.appendChild(span);
    var width = span.offsetWidth;
    document.body.removeChild(span);
    return width;
}

function handleInputBlur(event) {
    var input = event.target;
    var text = input.value;
    var font = window.getComputedStyle(input).font;
    var textWidth = measureTextWidth(text, font);

    var inputClass = input.classList[0];
    var expectedWidth;
    switch (inputClass) {
        case "col-first":
            expectedWidth = 200;
            break;
        case "col-second":
            expectedWidth = 390;
            break;
        case "col-third":
            expectedWidth = 114;
            break;
        default:
            expectedWidth = 0;
    }

    if (textWidth > expectedWidth) {
        input.classList.add("input-error");
    } else {
        input.classList.remove("input-error");
    }

    checkFormValidity(); // Проверяем валидность формы после изменения поля ввода
}

function checkFormValidity() {
    var inputs = document.querySelectorAll("#measuresInput input[type='text']");
    var hasErrors = false;

    inputs.forEach(function(input) {
        var text = input.value;
        var font = window.getComputedStyle(input).font;
        var textWidth = measureTextWidth(text, font);

        var inputClass = input.classList[0];
        var expectedWidth;
        switch (inputClass) {
            case "col-first":
                expectedWidth = 200;
                break;
            case "col-second":
                expectedWidth = 390;
                break;
            case "col-third":
                expectedWidth = 114;
                break;
            default:
                expectedWidth = 0;
        }

        if (textWidth > expectedWidth) {
            input.classList.add("input-error");
            hasErrors = true;
        } else {
            input.classList.remove("input-error");
        }
    });

    // Если есть ошибки, блокируем кнопку отправки, иначе разблокируем
    var submitButton = document.getElementById("submitButton");
    submitButton.disabled = hasErrors; // Разблокируем кнопку, если нет ошибок
}

var form = document.getElementById("mainForm");
form.addEventListener("submit", function(event) {
    checkFormValidity();
    if (document.querySelectorAll(".input-error").length > 0) {
        event.preventDefault(); // Предотвращаем отправку формы, если есть ошибки
    }
});

// Добавляем обработчик события blur для каждого поля ввода
var inputs = document.querySelectorAll("#measuresInput input[type='text']");
inputs.forEach(function(input) {
    input.addEventListener("blur", handleInputBlur);
});