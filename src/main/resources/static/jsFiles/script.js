var counter = 0;
var weekday = new Array(7);
var selectDay = null;
var selectNodeArray = [];
weekday[0] = "Sunday";
weekday[1] = "Monday";
weekday[2] = "Tuesday";
weekday[3] = "Wednesday";
weekday[4] = "Thursday";
weekday[5] = "Friday";
weekday[6] = "Saturday";

$(document).ready(function () {

    //###################### calendar init ########################################
    var data = new Date();
    let today = data;
    const dateBackend = new Date(dateFromBackend.split("-")[0],dateFromBackend.split("-")[1]-1,dateFromBackend.split("-")[2]);
    let diffDays =  Math.ceil((dateBackend - today) / (1000 * 60 * 60 * 24));
    counter= counter + Math.floor(diffDays/7);
    let clickedButtonID = "day"+(diffDays%7);
    let nrOfWeek = counter;
    data.setDate(data.getDate() + (counter*7) - 1);
    for (let i = 0; i < 7; i++) {
        data.setDate(data.getDate() + 1);
        var month = data.getMonth()+1;
        if (month < 10) month = "0" + month;
        var day = data.getUTCDate();
        if (day < 10) day = "0" + day;
        newEle = $("<button class=\"calendarDay\" id=\"day" + i+"\">"+weekday[data.getDay()] + " " + day + "." + month+"</button>");
        nextWeekButton = $('#nextWeek')[0];
        newEle.insertBefore(nextWeekButton);
    }

    if (counter > 2)
        $("#nextWeek").css("background", "gray").css("color", "black").css("cursor", "default").attr("disabled", true);
    else
        $("#nextWeek").css("background", "#7b38d8").css("color", "#eeeeee").css("cursor", "pointer").attr("disabled", false);
    if (counter < 1)
        $("#prevWeek").css("background", "gray").css("color", "black").css("cursor", "default").attr("disabled", true);
    else
        $("#prevWeek").css("background", "#7b38d8") .css("color", "#eeeeee").css("cursor", "pointer").attr("disabled", false);

    //###################### calendar day click ########################################
    $(".calendarDay").click(function () {
        clickedButtonID = this.id;
        button = document.querySelector("#" + clickedButtonID);
        dateOfCalendar = button.innerText;
        dateOfCalendar = dateOfCalendar.split(" ")[1].split(".");
        dateOfCalendar = "2020-"+dateOfCalendar[1]+"-"+dateOfCalendar[0];
        window.location='/reservation?date='+dateOfCalendar;
    });
    document.querySelector("#"+clickedButtonID).className = "clickedButton";
    selectDay =$("#"+clickedButtonID).text().split(" ")[1].split(".");
    selectDay = "d"+selectDay[0]+"m"+selectDay[1];

    //###################### schedule init ########################################
    let d = new Date();
    d.setHours(5, 30, 0);
    for (let i = 0; i < 34; i++) {
        d.setMinutes(d.getMinutes()+30);
        var minute = d.getMinutes();
        if (d.getMinutes()<30) minute += "0";
        var hours =d.getHours()
        if (d.getHours()<10) hours = "0" + hours;
        timeElement = "h"+hours+"m"+minute;
        markup = "<tr>" +
            "<td class=\"tableHour\" align=\"center\">"+d.getHours()+"."+minute+"</td>" +
            "<td><button class=\"tableNode\" id=\""+selectDay+"_"+timeElement+"_c1\"></button></td>" +
            "<td><button class=\"tableNode\" id=\""+selectDay+"_"+timeElement+"_c2\"></button></td>" +
            "<td><button class=\"tableNode\" id=\""+selectDay+"_"+timeElement+"_c3\"></button></td>" +
            "<td><button class=\"tableNode\" id=\""+selectDay+"_"+timeElement+"_c4\"></button></td>" +
            "</tr>";
        tableBody = $("table tbody");
        tableBody.append(markup);
    }

    // set node elements to inaccessible where the reservation was made
    for (var i = 0; i < reservedTimeList.length; i++){
        let timeListIter = reservedTimeList[i].split(":");
        d.setHours(timeListIter[0], timeListIter[1], timeListIter[2]);
        for (var j = 0; j < reservedNumberOfHoursList[i]; j+=0.5){
           hours =d.getHours()
           if (d.getHours()<10) hours = "0" + hours;
           timeElement = "h"+hours+"m"+d.getMinutes();
           if (d.getMinutes()<30) timeElement += "0";
           var element = document.querySelector("#"+selectDay+"_"+timeElement+"_c"+reservedCourtIdList[i]);
           if(element !== null){
               element.className = "inaccessible";
               element.disabled = true;
           }
           d.setMinutes(d.getMinutes()+30);
        }
    }

    // set node elements to reserved where the client confirm selected node
        for (var i = 0; i < startedTimeList.length; i++){
            let timeListIter = startedTimeList[i].split(":");
            d.setHours(timeListIter[0], timeListIter[1], timeListIter[2]);
            for (var j = 0; j < startedNumberOfHoursList[i]; j+=0.5){
               hours =d.getHours()
               if (d.getHours()<10) hours = "0" + hours;
               timeElement = "h"+hours+"m"+d.getMinutes();
               if (d.getMinutes()<30) timeElement += "0";
               var element = document.querySelector("#"+selectDay+"_"+timeElement+"_c"+startedCourtIdList[i]);
               if(element !== null){
                   element.className = "reservedNode";
                   element.disabled = true;
               }
               d.setMinutes(d.getMinutes()+30);
            }
        }

    $(".tableNode").click(function () {
        this.className = "selectNode";
    });

    $("#makeReservation").click(function (){
        $('.selectNode').each(function () {
            selectNodeArray.push(this.id);
        });
        $.ajax({
               type: "post",
               url: "/saveSelectedDay",
               contentType: "application/json",
               dataType:"json",
               data: JSON.stringify(selectNodeArray),
               success: function(result) {
                    console.log("success: ", result);
                    window.location='/reservation?date='+dateFromBackend;
               },
               error: function(e){
                    console.log("ERROR: ", e);
               }
          });
//        $(".selectNode").addClass("reservedNode").removeClass("selectNode");
    });

    //###################### next or prev click ########################################
    let changedWeek = false;
    $(".week").click(function () {
        let el;
        let text;
        let month;
        if (this.id === "nextWeek") {
            if (counter < 3) {
                for (let i = 0; i < 7; i++) {
                    el = document.querySelector("#day" + i);
                    text = el.textContent.split(" ")[1].split(".");
                    data.setFullYear(data.getFullYear(), text[1]-1, text[0]);
                    data.setDate(data.getDate() + 7);
                    month = data.getMonth()+1;
                    if (month < 10) month = "0" + month;
                    var day = data.getUTCDate();
                    if (day < 10) day = "0" + day;
                    el.innerText = weekday[data.getDay()] + " " + day + "." + month;
                }
                counter++;
            }
        }
        if (this.id === "prevWeek") {
            if (counter > 0) {
                for (let i = 0; i < 7; i++) {
                    el = document.querySelector("#day" + i);
                    text = el.textContent.split(" ")[1].split(".");
                    data.setFullYear(data.getFullYear(), text[1]-1, text[0]);
                    data.setDate(data.getDate() - 7);
                    month = data.getMonth()+1;
                    if (month < 10) month = "0" + month;
                    var day = data.getUTCDate();
                    if (day < 10) day = "0" + day;
                    el.innerText = weekday[data.getDay()] + " " + day + "." + month;
                }
                counter--;
            }
        }
        if (counter > 2)
            $("#nextWeek").css("background", "gray").css("color", "black").css("cursor", "default").attr("disabled", true);
        else
            $("#nextWeek").css("background", "#7b38d8").css("color", "#eeeeee").css("cursor", "pointer").attr("disabled", false);
        if (counter < 1)
            $("#prevWeek").css("background", "gray").css("color", "black").css("cursor", "default").attr("disabled", true);
        else
            $("#prevWeek").css("background", "#7b38d8") .css("color", "#eeeeee").css("cursor", "pointer").attr("disabled", false);

        if(nrOfWeek!==counter && !changedWeek){
            document.querySelector(".clickedButton").className = "calendarDay";
            changedWeek = true;
         }
        if(nrOfWeek===counter && changedWeek){
            document.querySelector("#" +clickedButtonID).className = "clickedButton";
            changedWeek = false;
        }
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