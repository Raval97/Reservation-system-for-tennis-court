var counter = 0;
var weekday = new Array(7);
weekday[0] = "Sunday";
weekday[1] = "Monday";
weekday[2] = "Tuesday";
weekday[3] = "Wednesday";
weekday[4] = "Thursday";
weekday[5] = "Friday";
weekday[6] = "Saturday";

$(document).ready(function () {

    //###################### calendar init ########################################
    var data = new Date()
    data.setDate(data.getDate() - 1);
    var div = null;
    var newEl = null;
    var nextWeek = document.querySelector("#nextWeek");

    for (let i = 0; i < 7; i++) {
        div = document.querySelector("#calendar");
        newEl = document.createElement("button");
        data.setDate(data.getDate() + 1);
        let month = data.getMonth();
        if (month < 10) month = "0" + month;
        newEl.innerText = weekday[data.getDay()] + " \n" + data.getUTCDate() + "." + month;
        newEl.className = "calendarDay";
        newEl.id = "day" + i;
        div.insertBefore(newEl, nextWeek)

    }

    //###################### schedule init ########################################
    let d = new Date();
    d.setHours(5, 30, 0);
    for (let i = 0; i < 34; i++) {
        d.setMinutes(d.getMinutes()+30);
        timeElement = d.getHours()+"."+d.getMinutes();
        if (d.getMinutes()<30) timeElement += "0";
        markup = "<tr>" +
            "<td class=\"tableHour\" align=\"center\">"+timeElement+"</td>" +
            "<td><button class=\"tableNode\" id=\"w1t"+timeElement+"c1\"></button></td>" +
            "<td><button class=\"tableNode\" id=\"w2t"+timeElement+"c2\"></button></td>" +
            "<td><button class=\"tableNode\" id=\"w3t"+timeElement+"c3\"></button></td>" +
            "<td><button class=\"tableNode\" id=\"w4t"+timeElement+"c4\"></button></td>" +
            "</tr>";
        tableBody = $("table tbody");
        tableBody.append(markup);
    }

    $(".tableNode").click(function () {
        this.className = "selectNode";
    });

    //###################### calendar click ########################################
    let clickedButtonID = "day0";
    let nrOfWeek = counter;
    $(".calendarDay").click(function () {
        if(nrOfWeek === counter) {
            document.querySelector(".clickedButton").className = "calendarDay";
            document.querySelector("#" + this.id).className = "clickedButton";
            clickedButtonID = this.id;
        }
        else
            document.querySelector("#" + this.id).className = "clickedButton";
        clickedButtonID = this.id;
        nrOfWeek = counter;
    });
    document.querySelector("#"+clickedButtonID).className = "clickedButton";

    $(".week").click(function () {
        let el;
        let text;
        let month;
        if (this.id === "nextWeek") {
            if (counter < 3) {
                for (let i = 0; i < 7; i++) {
                    el = document.querySelector("#day" + i);
                    text = el.textContent.split(" ")[1].split(".");
                    data.setFullYear(data.getFullYear(), text[1], text[0]);
                    data.setDate(data.getDate() + 7);
                    month = data.getMonth();
                    if (month < 10) month = "0" + month;
                    el.innerText = weekday[data.getDay()] + " \n" + data.getUTCDate() + "." + month;
                }
                counter++;
            }
        }
        if (this.id === "prevWeek") {
            if (counter > 0) {
                for (let i = 0; i < 7; i++) {
                    el = document.querySelector("#day" + i);
                    text = el.textContent.split(" ")[1].split(".");
                    data.setFullYear(data.getFullYear(), text[1], text[0]);
                    data.setDate(data.getDate() - 7);
                    month = data.getMonth();
                    if (month < 10) month = "0" + month;
                    el.innerText = weekday[data.getDay()] + " \n" + data.getUTCDate() + "." + month;
                }
                counter--;
            }
        }
        if (counter > 2) {
            $("#nextWeek")
                .css("background", "gray")
                .css("color", "black")
                .css("cursor", "default");
        }
        else {
            $("#nextWeek")
                .css("background", "#7b38d8")
                .css("color", "#eeeeee")
                .css("cursor", "pointer");
        }
        if (counter < 1) {
            $("#prevWeek")
                .css("background", "gray")
                .css("color", "black")
                .css("cursor", "default");
        }
        else {
            $("#prevWeek")
                .css("background", "#7b38d8")
                .css("color", "#eeeeee")
                .css("cursor", "pointer");
        }
        if(nrOfWeek!==counter)
            document.querySelector(".clickedButton").className = "calendarDay";
        else
            document.querySelector("#" +clickedButtonID).className = "clickedButton";
    });


});

function time() {
    var data = new Date();
    var hours = data.getHours();
    var minutes = data.getMinutes();
    var seconds = data.getSeconds();
    if (minutes < 10) minutes = "0" + minutes;
    if (seconds < 10) seconds = "0" + seconds;
    var footer = document.getElementById("footer");
    footer.innerHTML = "&copy;2020 RG | " + hours + ":" + minutes + ":" + seconds;
}