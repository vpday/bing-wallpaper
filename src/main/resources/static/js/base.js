$(function () {
    var click = "ontouchstart" in window ? "touchstart" : "click";
    var toke = document.querySelector('meta[name="csrf_token"]').getAttribute("content");

    $(document).off(click, ".ctrl.heart").on(click, ".ctrl.heart", function (e) {
        var _this = $(this);
        var code = _this.parent(".options").attr("code");
        var copyright = _this.parent(".options").attr("copyright");

        if (_this.hasClass("active")) {
            return;
        }

        ma.trackEvent('likes', 'click', copyright, 1);
        $.post("/like", {"code": code, "_token": toke}, function (data) {
            if (data.success) {
                updateShowNum(_this.addClass("active").children("em"));
            } else {
                var message = (typeof data.msg !== "undefined") ? data.msg : "unknown error";
                console.error("download failed, " + message);
            }
        }, "json");
    });

    $(document).off(click, ".ctrl.download").on(click, ".ctrl.download", function (e) {
        var options = $(this).parent(".options");
        var code = options.attr("code");
        var fileName = options.attr("fileName");
        var copyright = options.attr("copyright");
        var formData = "code=" + code + "&_token=" + toke;

        ma.trackEvent('download', 'click', copyright, 1);
        beforeDownloading(formData, fileName, $(this).children("em"));
    });

    function beforeDownloading(formData, fileName, emElement) {
        var request = new XMLHttpRequest();
        request.open("POST", "/download", true);
        request.responseType = "blob";

        request.onload = function () {
            if (this.status === 200) {
                downloadImg(request, fileName, emElement);
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

    function updateShowNum(emElement) {
        var number = emElement.text();
        if (!Number.isNaN(+number)) {
            emElement.text(++number);
        }
    }

    function downloadImg(request, fileName, emElement) {
        var blob = request.response;

        if (blob.type === "application/json") {
            var reader = new FileReader();

            reader.onloadend = function () {
                var error = JSON.parse(reader.result);
                if (!error.success) {
                    var message = (typeof error.msg !== "undefined") ? error.msg : "unknown error";
                    console.error("download failed, " + message);
                }
            };
            reader.readAsBinaryString(blob);
        } else if (window.navigator.msSaveOrOpenBlob) {
            window.navigator.msSaveBlob(blob, fileName);
        } else {
            var contentTypeHeader = request.getResponseHeader("Content-Type");
            createDownloadLink(fileName, blob, contentTypeHeader);
            updateShowNum(emElement);
        }
    }

    function createDownloadLink(fileName, blob, contentTypeHeader) {
        var downloadLink = window.document.createElement("a");
        downloadLink.href = window.URL.createObjectURL(new Blob([blob], {type: contentTypeHeader}));
        downloadLink.download = fileName;

        document.body.appendChild(downloadLink);
        downloadLink.click();
        document.body.removeChild(downloadLink);
    }
});