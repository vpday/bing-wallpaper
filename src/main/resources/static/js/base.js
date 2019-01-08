$(function () {
    var click = "ontouchstart" in window ? "touchstart" : "click";
    var toke = document.querySelector('meta[name="csrf_token"]').getAttribute("content");

    $(document).off(click, ".ctrl.heart").on(click, ".ctrl.heart", function (e) {
        var _this = $(this);
        var code = _this.parent(".options").attr("code");

        if (_this.hasClass("active")) {
            return;
        }

        $.post("/like", {"code": code, "_token": toke}, function (data) {
            _this.addClass("active").children("em").html(data.payload);
        }, "json");
    });

    $(document).off(click, ".ctrl.download").on(click, ".ctrl.download", function (e) {
        var options = $(this).parent(".options").attr("code");
        var code = options.attr("code");
        var fileName = options.attr("fileName");
        var formData = "code=" + code + "&_token=" + toke;

        beforeDownloading(formData, fileName);
    });

    function beforeDownloading(formData, fileName) {
        var request = new XMLHttpRequest();
        request.open("POST", "/download", true);
        request.responseType = "blob";

        request.onload = function () {
            if (this.readyState === 4 && this.status === 200) {
                downloadImg(request, fileName);
            }
        };
        request.ontimeout = function () {
            console.warn("request timed out, please try again later.");
        };
        request.onerror = function () {
            console.error("request server failed.");
        };

        request.send(formData);
    }

    function downloadImg(request, fileName) {
        var blob = request.response;

        if (blob.type === "application/json") {
            var reader = new FileReader();

            reader.onloadend = function () {
                var error = JSON.parse(reader.result);
                var message = (typeof error.message !== "undefined") ? error.message : "unknown error";
                console.error("download failed," + message);
            };
            reader.readAsBinaryString(blob);
        } else if (window.navigator.msSaveOrOpenBlob) {
            window.navigator.msSaveBlob(blob, fileName);
        } else {
            var downloadLink = window.document.createElement("a");
            var contentTypeHeader = request.getResponseHeader("Content-Type");
            downloadLink.href = window.URL.createObjectURL(new Blob([blob], {type: contentTypeHeader}));
            downloadLink.download = fileName;
            document.body.appendChild(downloadLink);
            downloadLink.click();
            document.body.removeChild(downloadLink);
        }
    }
});