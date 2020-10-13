var usersPermissionsList = [];
var priceList = [];


$(document).ready(function () {

    try {
        $("#confirmChangesPrice").click(function () {
            $('.price').each(function () {
                priceList.push(this.childNodes[0].value);
            });
            $.ajax({
                type: "post",
                url: "/admin/edit_priceList",
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify(priceList),
                success: function (result) {
                    console.log("success: ", result);
                    window.location = '/admin/priceList';
                },
                error: function (e) {
                    console.log("ERROR: ", e);
                }
            });
        });
    } catch (e) {
        console.log(e);
    }

    try {
        $("#confirmChangesUsers").click(function () {
            $('.permissions label input:checked').each(function() {
                    usersPermissionsList.push(this.value);
            });
            $.ajax({
                type: "post",
                url: "/admin/edit_usersPermissions",
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify(usersPermissionsList),
                success: function (result) {
                    console.log("success: ", result);
                    window.location = '/admin/usersPermissions';
                },
                error: function (e) {
                    console.log("ERROR: ", e);
                }
            });
        });
    } catch (e) {
        console.log(e);
    }

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