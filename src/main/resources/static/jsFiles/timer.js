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