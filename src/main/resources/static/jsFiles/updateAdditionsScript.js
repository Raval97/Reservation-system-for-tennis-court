
function sendChangeIfRocketWithAjax(id, addition){
    $('.selectNode').each(function () {
        selectNodeArray.push(this.id);
    });
    $.ajax({
       type: "post",
       url: "/updateIfRocket/"+id,
       contentType: "application/json",
       dataType:"json",
       data: JSON.stringify(addition),
       success: function(result) {
            console.log("success: ", result);
            window.location='/OurTennis/makeReservation';
       },
       error: function(e){
            console.log("ERROR: ", e);
       }
    });
}

function sendChangeIfBallsWithAjax(id, addition){
    $('.selectNode').each(function () {
        selectNodeArray.push(this.id);
    });
    $.ajax({
       type: "post",
       url: "/updateIfBalls/"+id,
       contentType: "application/json",
       dataType:"json",
       data: JSON.stringify(addition),
       success: function(result) {
            console.log("success: ", result);
            window.location='/OurTennis/makeReservation';
       },
       error: function(e){
            console.log("ERROR: ", e);
       }
    });
}

function sendChangeIfShoesWithAjax(id, addition){
    $('.selectNode').each(function () {
        selectNodeArray.push(this.id);
    });
    $.ajax({
       type: "post",
       url: "/updateIfShoes/"+id,
       contentType: "application/json",
       dataType:"json",
       data: JSON.stringify(addition),
       success: function(result) {
            console.log("success: ", result);
            window.location='/OurTennis/makeReservation';
       },
       error: function(e){
            console.log("ERROR: ", e);
       }
    });
}

$(document).ready(function () {

    let id = null;
    let addition = null;
    $("select.changeAdditions").change(function () {
        var decision = $(this).children("option:selected").val();
        id = this.id.split("_")[1];
        addition = this.id.split("_")[0];
        if(addition === 'ifBalls'){
            sendChangeIfBallsWithAjax(id, decision);
        }
        if(addition === 'ifRocket'){
            sendChangeIfRocketWithAjax(id, decision);
        }
        if(addition === 'ifShoes'){
            sendChangeIfShoesWithAjax(id, decision);
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