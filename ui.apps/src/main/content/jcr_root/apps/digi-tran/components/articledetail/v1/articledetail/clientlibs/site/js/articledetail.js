(function () {
    "use strict";

    var formSelector = 'form.article-comments';

    function onDocumentReady() {

        updateCommentsSection();

        $(formSelector).submit(function (e) {
            e.preventDefault();
            $(".errorMsg").hide();
            $(".successMsg").hide();
            $.ajax({
                url: $(formSelector).attr('data-action'),
                type: 'POST',
                dataType: 'json',
                data: $(formSelector).serialize(),
                success: function (data) {
                    console.log(data);
                    $('.inp').val('');
                    $(".successMsg").show().delay(3000).fadeOut();
                    updateCommentsSection();
                },
                error: function (data) {
                    $(".errorMsg").show().delay(3000).fadeOut();
                }
            });
        });
    }

    function updateCommentsSection() {
        $.ajax({
            url: $(formSelector).attr('data-action'),
            type: 'GET',
            dataType: 'json',
            data: $(formSelector).serialize(),
            success: function (data) {
                updateCommentsElement(data);
            },
            error: function (error) {
                console.log(error);
            }
        });
    }

    function updateCommentsElement(data) {
        var image = $(".user-comments").attr("data-image");
        var comments = data;
        var commentHTML = "";
        comments.forEach(function (item) {
            commentHTML += "<div class='comment'>"
                + "<img class='user-image' src='" + image + "' />"
                + "<div class='content-section'>"
                + "     <div class='comment-item'>" + item.comment + "</div>"
                + "     <div class='author-name'>" + item.userName + "</div>"
                + "     <div class='created-date'>" + item.createdDate + "</div>"
                + " </div>"
                + " </div>";
        })
        if (comments.length > 0) {
            commentHTML = "<h3>All Comments </h3>" + commentHTML;
        }
        $(".user-comments").html(commentHTML);
    }

    if (document.readyState !== "loading") {
        onDocumentReady();
    } else {
        document.addEventListener("DOMContentLoaded", onDocumentReady);
    }
}());


