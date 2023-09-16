import {PasswordManager} from './PasswordManager.js'

$(document).ready(function() {
        var customSelect = $(".custom-select");

        customSelect.each(function() {
            var thisCustomSelect = $(this),
                options = thisCustomSelect.find("option"),
                firstOptionText = options.first().text();

            var selectedItem = $("<div></div>", {
                class: "selected-item"
            })
                .appendTo(thisCustomSelect)
                .text(firstOptionText);

            var allItems = $("<div></div>", {
                class: "all-items all-items-hide"
            }).appendTo(thisCustomSelect);

            options.each(function() {
                var that = $(this),
                    optionText = that.text();

                var item = $("<div></div>", {
                    class: "item",
                    on: {
                        click: function() {
                            var selectedOptionText = that.text();
                            selectedItem.text(selectedOptionText).removeClass("arrowanim");
                            allItems.addClass("all-items-hide");
                        }
                    }
                })
                    .appendTo(allItems)
                    .text(optionText);
            });
        });

        var selectedItem = $(".selected-item"),
            allItems = $(".all-items");

        selectedItem.on("click", function(e) {
            var currentSelectedItem = $(this),
                currentAllItems = currentSelectedItem.next(".all-items");

            allItems.not(currentAllItems).addClass("all-items-hide");
            selectedItem.not(currentSelectedItem).removeClass("arrowanim");
            currentSelectedItem.parent().removeClass("arrowanim");

            currentAllItems.toggleClass("all-items-hide");
            currentSelectedItem.toggleClass("arrowanim");
            currentSelectedItem.parent().toggleClass("arrowanim");

            e.stopPropagation();
        });

        $(document).on("click", function() {
            var opened = $(".all-items:not(.all-items-hide)"),
                index = opened.parent().index();

            customSelect
                .eq(index)
                .find(".all-items")
                .addClass("all-items-hide");
            customSelect
                .eq(index)
                .find(".selected-item")
                .removeClass("arrowanim");

            customSelect
                .removeClass("arrowanim");
        });

        $("progress")
            .val(currentStep+1/numOfSteps)
            .attr("max",numOfSteps);
});

let numOfSteps = $(".progress").find("li").length-1;
let currentStep = 0;
let signUpBoxes = $(".container-sign-up").find(".box-sign-up");
let stepLevels = $(".progress").find(".step-level")

function nextBox (operator = 1) {
    signUpBoxes
        .eq(currentStep)
        .toggleClass("hidden-box");
    currentStep = currentStep + operator;
    signUpBoxes
        .eq(currentStep)
        .removeClass("hidden-box");
}

function nextLevel() {
    stepLevels
        .eq(currentStep)
        .removeClass("step-level-hidden")
        .toggleClass("step-level-current");

    stepLevels
        .eq(currentStep-1)
        .removeClass("step-level-current");

    $("progress")
        .val(currentStep+1/numOfSteps);
}
function previousLevel() {
    stepLevels
        .eq(currentStep)
        .toggleClass("step-level-current");

    stepLevels
        .eq(currentStep+1)
        .toggleClass("step-level-hidden")
        .removeClass("step-level-current");

    $("progress")
        .val(currentStep+1/numOfSteps);
}
function getAmountOfEmptyRequiredInputs() {
    let n = 0;
    let requiredInputs = signUpBoxes
            .eq(currentStep)
            .find("input[required]");

    requiredInputs.each(function () {
        if(! $(this).val()) {
            $(this)
                .parent()
                .addClass("input-error");
            n++;
        }
    });

    return n;
}

$(".button-next").on("click",function (){
    let isNextBoxPermit = false;
    switch (currentStep) {
        case 0:
            if (getAmountOfEmptyRequiredInputs() == 0) {
                isNextBoxPermit = true;
            }
            break;
        case 1:
            $(".box-with-details").find("#email").keyup();
            $(".box-with-details").find("#password").keyup();
            $(".box-with-details").find("#reentry-password").keyup();

            if($(".button-next").find("button").attr("disabled") != 'disabled') {
                isNextBoxPermit = true;
            }
    }

    if(isNextBoxPermit) {
        if(currentStep != numOfSteps) {
            nextBox();
            nextLevel();
            $(".button-back").find("button").removeAttr("disabled");
        } else {
            $(".button-next").find("button").attr("disabled","true");
        }
    }
});

$(".button-back").on("click",function (){
    nextBox(-1);
    previousLevel();
    if (currentStep==0) {
        $(".button-back").find("button").attr("disabled","true");
    }
});

$(".box-with-details").find("input[type='text'], input[type='date']").on("focusout", function () {
    let currentInput = $(this);
    let currentDate = new Date();
    let  selectedDate = new Date(Date.parse(currentInput.val()));
    if (
        (!currentInput.val() && currentInput.attr("id") != "middle-name") ||
        (currentInput.attr("id")=="birth-date" && selectedDate > currentDate) ) {

        currentInput
            .parent()
            .addClass("input-error");

        $(".button-next").find("button").attr("disabled","true");

    } else {
        currentInput
            .parent()
            .removeClass("input-error");

        $(".button-next").find("button").removeAttr("disabled");
    }
});

$(".box-with-details").find("#email").on("keyup", function () {
    let validRegex = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-]{2,3}/;
    let currentInput = $(this);

    if(!currentInput.val().match(validRegex)) {
        $("#error-email").removeClass("hidden");
        currentInput
            .parent()
            .addClass("input-error");
        $(".button-next").find("button").attr("disabled","true");
    } else {
        $("#error-email").addClass("hidden");
        currentInput
            .parent()
            .removeClass("input-error");
        $(".button-next").find("button").removeAttr("disabled");
    }
});

$(".box-with-details").find("#password").on("keyup", function () {
    let  currentInput = $(this);
    let  password = currentInput.val();
    let errorNumber = 0;
    const passwordManager = new PasswordManager(password);
    let strength = passwordManager.checkPasswordStrength();
    if (strength < 75) {

    }

    if (passwordManager.num.Excess < 0) {
        $("#error_length").removeClass("hidden");
        errorNumber++;

        currentInput
            .parent()
            .addClass("input-error");

        $(".button-next").find("button").attr("disabled","true");
    } else {
        $("#error_length").addClass("hidden");
        errorNumber--;

        currentInput
            .parent()
            .removeClass("input-error");
    }

    if (passwordManager.num.Numbers == 0  && passwordManager.num.Symbols == 0) {
        $("#error_symbols").removeClass("hidden");
        errorNumber++;

        currentInput
            .parent()
            .addClass("input-error");

        $(".button-next").find("button").attr("disabled","true");
    } else {
        $("#error_symbols").addClass("hidden");
        errorNumber--;

        currentInput
            .parent()
            .removeClass("input-error");
    }

    if (passwordManager.num.Upper == 0
        || password.replace(/[^a-zA-z]/gi, '').length == passwordManager.num.Upper) {
        $("#error_characters").removeClass("hidden");
        errorNumber++;

        currentInput
            .parent()
            .addClass("input-error");

        $(".button-next").find("button").attr("disabled","true");
    } else {
        $("#error_characters").addClass("hidden");
        errorNumber--;

        currentInput
            .parent()
            .removeClass("input-error");
    }

    if(errorNumber > 0) {
        $("#error-main").removeClass("hidden");
    } else {
        $("#error-main").addClass("hidden");
    }

    if (passwordManager.num.Excess >= 0 && (passwordManager.num.Numbers > 0 || passwordManager.num.Symbols > 0)
        && (passwordManager.num.Upper > 0
            && password.replace(/[^a-zA-z]/gi, '').length != passwordManager.num.Upper)) {

        if (strength > 75) {
            $(".button-next").find("button").removeAttr("disabled");
            $("#error-easy").addClass("hidden");
        } else {
            $("#error-easy").removeClass("hidden");
        }

    }
});

$(".box-with-details").find("#reentry-password").on("keyup", function () {
    let currentInput = $(this);
    let password = $(".box-with-details").find("#password");

    if (currentInput.val() == password.val() && currentInput.val().length > 0) {
        $("#error-password-match").addClass("hidden");
        currentInput
            .parent()
            .removeClass("input-error");
        $(".button-next").find("button").removeAttr("disabled");
    } else {
        $("#error-password-match").removeClass("hidden");
        currentInput
            .parent()
            .addClass("input-error");
        $(".button-next").find("button").attr("disabled","true");
    }
});

$("#personal-info").on("click", function () {
    if($(this).is(':checked')) {
        $("#sign-up-send-button").removeAttr("disabled");
    } else {
        $("#sign-up-send-button").attr("disabled","true");
    }
});

$("#sign-up-button").on("click", function (e){

    nextBox();
    $(".container-left").hide();

    grecaptcha.execute('6LcDWO0nAAAAAC3S0eg_eqBxBnevjj7O6EkyipUB', {action: 'submit'}).then(function(token){

        let firstName = $(".box-with-details").find("#firstname");
        let lastName = $(".box-with-details").find("#lastname");
        let middleName = $(".box-with-details").find("#middle-name");
        let dateOfBirth = $(".box-with-details").find("#birth-date");
        let sex = $(".box-with-details").find("#gender");
        let email = $(".box-with-details").find("#email");
        let password = $(".box-with-details").find("#password");

        let regForm = {
            "firstName": firstName.val(),
            "lastName": lastName.val(),
            "middleName": middleName.val(),
            "sex": sex.val(),
            "dateOfBirth": dateOfBirth.val(),
            "email": email.val(),
            "password": password.val()
        }

        $.ajax({
            url: '/api/user/add',
            type: 'POST',
            dataType : "json",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(regForm),
            async: false,
            success: function () {
                $(".container-loader").addClass("hidden");
                $("#end-message-container-left-success").removeClass("hidden");
                $("#end-message-container-right-success").removeClass("hidden");
            },
            error: function (jqXHR, textStatus, errorThrown) {
                $(".container-loader").addClass("hidden");
                $("#end-message-container-left-error").removeClass("hidden");
                $(".end-message").removeClass("hidden");
                switch (jqXHR.status) {
                    case 403:
                        $("#end-message-container-right-email-already-exist").removeClass("hidden");
                        break;
                    case 400:
                        $("#end-message-container-right-bad-request")
                            .removeClass("hidden")
                            .append("<p>"+jqXHR.responseText+"</p>");
                        break;
                    case 500:
                        $("#end-message-container-right-error")
                            .removeClass("hidden")
                            .append("<p>"+jqXHR.responseText+"</p>");
                }
            }
        });
    });
});
