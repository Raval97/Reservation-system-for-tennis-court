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
            let size = $('.permissions').length;
            var i = 0;
            var namee = "" ;
            for (let i = 0; i < size; i++) {
                namee = "radio"+i;
                alert($('[name=namee]:checked').value)
                usersPermissionsList.push($('[name=namee]:checked').value);
            }
            // $('.permissions').each(function () {
            //     usersPermissionsList.push(this.innerHTML);
            //     usersPermissionsList.push($(this + label + input:checked ).value);
            //     alert(this.children[0].children.namedItem("radio0").attributes.checked.value +" ... "+
            //     this.children[1].children.namedItem("radio0").attributes.checked.value);
            //
            //     // usersPermissionsList.push(this.children[0].);
            // });
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