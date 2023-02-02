(function () {
    "use strict";

    function onDocumentReady() {
        console.log("Ready");
        $('form.article-form').submit(function (e) {
            e.preventDefault();
            $(".errorMsg").hide();
            $(".successMsg").hide();
            $.ajax({
                url: $('form.article-form').attr('data-action'),
                type: 'POST',
                dataType: 'json',
                data: $('form.article-form').serialize(),
                success: function (data) {
                    console.log(data);
                    $('.inp').val('');
                    $(".successMsg").show().delay(5000).fadeOut();
                },
                error: function (data) {
                    $(".errorMsg").show();
                }
            });
        });
    }

    if (document.readyState !== "loading") {
        onDocumentReady();
    } else {
        document.addEventListener("DOMContentLoaded", onDocumentReady);
    }
}());
