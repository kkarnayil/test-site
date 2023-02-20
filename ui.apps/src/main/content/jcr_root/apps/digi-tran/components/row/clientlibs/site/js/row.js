(function ($) {
  "use strict";
  var selectors = {
    dialogContent: ".cmp-row__editor"
  };

  $(document).on("dialog-loaded", function (e) {

    var $dialog = e.dialog;
    var $dialogContent = $dialog.find(selectors.dialogContent);
    var dialogContent = $dialogContent.length > 0 ? $dialogContent[0] : undefined;

    if (dialogContent) {
      var row_endpoint = $("form.cq-dialog").attr("action") + ".json";
      getTableComponentData(row_endpoint, handleTableResponse)
    }

  });

  function getTableComponentData(row_endpoint, handleTableResponse) {

    $.ajax({
      type: "GET",
      url: row_endpoint,
      success: function (data, status, xhr) {
        if (data.columns) {
          handleTableResponse(data);
        } else {
          console.error(data);
          $(".coral-Well").append("<h2>" + data + "</h2>");
        }

      },
      error: function (XMLHttpRequest, textStatus, errorThrown) {
        console.log("Error: " + errorThrown);
      }
    });

  }

  function handleTableResponse(data) {
    var columns = data.columns;
    var valueMap = data.valueMap;

    $.get('/apps/digi-tran/components/row/common/text.html', function (data) {

      columns.forEach((element, index) => {

        var type = element.columntype;

        var value = valueMap["colVal" + index];
        if (!value) {
          value = '';
        }

        var field = '';
        if (type === 'text') {
          field = "<div class='coral-Form-fieldwrapper'>" +
            "  <label class='coral-Form-fieldlabel'> Enter Value for " + element.columnname + "</label>" +
            "  <input is='coral-textfield' class='coral-Form-field coral3-Textfield' placeholder='Enter your text' name='./colVal" + index + "' value='" + value + "'>" +
            "</div>";
          $(".coral-Well").append(field);
        } else if (type === 'number') {
          field = "<div class='coral-Form-fieldwrapper'>" +
            "  <label class='coral-Form-fieldlabel'>Enter Value for " + element.columnname + "</label>" +
            "<coral-numberinput class='coral-Form-field coral3-NumberInput coral-InputGroup is-focused'  name='./colVal" + index + "' value='" + value + "'></coral-numberinput>"
          "</div>";
          $(".coral-Well").append(field);
        } else if (type === 'rte') {
          var rte = data;
          var name = "./colVal" + index;

          field = rte.replaceAll("./colVal", name).replaceAll('value=""', 'value="' + value + '"');
          $(field).find("input[name='" + name + "']").attr("value", value);
          field = "<br><hr><div class='coral-Form-fieldwrapper'><br>" +
            "<label class='coral-Form-fieldlabel'>Enter Value for " + element.columnname + "</label>" +
            field
          "</div>";
          $(".coral-Well").append(field).trigger("foundation-contentloaded");

        }

      });
    });
  }

})(jQuery);